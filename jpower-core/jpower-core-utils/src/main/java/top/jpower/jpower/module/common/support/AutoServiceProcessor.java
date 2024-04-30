package top.jpower.jpower.module.common.support;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import top.jpower.core.utils.utils.BufferUtil;
import top.jpower.core.utils.utils.CollectionUtil;
import top.jpower.core.utils.utils.ExceptionUtil;
import top.jpower.jpower.module.base.annotation.LoaderService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author mr.g
 *
 * 参考google的实现
 */
@SupportedOptions("debug")
public class AutoServiceProcessor extends AbstractProcessor {

    private static final String SERVICES_PATH = "META-INF/services/";

    /**
     * 接口和实现类
     **/
    private final Multimap<String, String> providers = HashMultimap.create();

    @Override
    public ImmutableSet<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(LoaderService.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 实现
     *
     * @author mr.g
     * @param annotations
     * @param roundEnv
     * @return boolean
     **/
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            processImpl(roundEnv);
        } catch (RuntimeException e) {
            fatalError(ExceptionUtil.getStackTraceAsString(e));
        }
        return false;
    }

    private void processImpl(RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            generateConfigFiles();
        } else {
            processAnnotations(roundEnv);
        }
    }

    private void processAnnotations(RoundEnvironment roundEnv) {

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(LoaderService.class);

        for (Element e : elements) {
            if (e.getKind() == ElementKind.CLASS){
                TypeElement providerImplementer = (TypeElement) e;
                AnnotationMirror annotationMirror = providerImplementer.getAnnotationMirrors().stream().filter(am -> am.getAnnotationType().toString().contentEquals(LoaderService.class.getCanonicalName())).findFirst().get();
                Set<TypeMirror> providerInterfaces = getValueFieldOfClasses(annotationMirror);
                if (providerInterfaces.isEmpty()) {
                    continue;
                }
                for (TypeMirror providerType : providerInterfaces) {

                    if (checkImplementer(providerImplementer, providerType)) {
                        providers.put(providerType.toString(), getBinaryName(providerImplementer));
                    } else {
                        String message = "ServiceProviders must implement their service provider interface. "
                                        + providerImplementer.getQualifiedName()
                                        + " does not implement "
                                        + providerType.toString();
                        error(message, e, annotationMirror);
                    }
                }
            }
        }
    }

    private void generateConfigFiles() {
        Filer filer = processingEnv.getFiler();

        for (String providerInterface : providers.keySet()) {
            String resourceFile = SERVICES_PATH + providerInterface;
            try {
                HashSet<String> allServices = CollectionUtil.newHashSet(true,providers.get(providerInterface));

                try {
                    FileObject existingFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
                    IoUtil.readUtf8Lines(existingFile.openInputStream(), (LineHandler) line->{
                        int commentStart = line.indexOf('#');
                        if (commentStart >= 0) {
                            line = line.substring(0, commentStart);
                        }
                        line = line.trim();
                        if (!line.isEmpty()) {
                            allServices.add(line);
                        }
                    });
                } catch (IOException e){
                    log("未找到资源文件，去创建...");
                }

                FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
                try (OutputStream out = fileObject.openOutputStream()) {
                    BufferUtil.writeLines(out,allServices);
                }
            } catch (IOException e) {
                fatalError("Unable to create " + resourceFile + ", " + e);
                return;
            }
        }
    }

    private boolean checkImplementer(TypeElement providerImplementer, TypeMirror providerType) {
        Types types = processingEnv.getTypeUtils();
        if (types.isSubtype(providerImplementer.asType(), providerType)) {
            return true;
        }

        // 解决泛型继承
        return types.isSubtype(providerImplementer.asType(), types.erasure(providerType));
    }

    /**
     * Returns the binary name of a reference type. For example,
     * {@code com.google.Foo$Bar}, instead of {@code com.google.Foo.Bar}.
     *
     */
    private String getBinaryName(TypeElement element) {
        return getBinaryNameImpl(element, element.getSimpleName().toString());
    }

    private String getBinaryNameImpl(TypeElement element, String className) {
        Element enclosingElement = element.getEnclosingElement();

        if (enclosingElement instanceof PackageElement) {
            PackageElement pkg = (PackageElement) enclosingElement;
            if (pkg.isUnnamed()) {
                return className;
            }
            return pkg.getQualifiedName() + "." + className;
        }
        //内部类的情况
        TypeElement typeElement = (TypeElement) enclosingElement;
        return getBinaryNameImpl(typeElement, typeElement.getSimpleName() + "$" + className);
    }

    private AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror){
        // 如果注解里没有写属性值，这里会报空，因为我的@AutoService注解强制写值，所以这里可以获取到
        return annotationMirror.getElementValues().entrySet().stream()
                .filter(entry -> entry.getKey().getSimpleName().contentEquals("value"))
                .findFirst()
                .map(Map.Entry::getValue).get();
    }

    /**
     * Returns the contents of a {@code Class[]}-typed "value" field in a given {@code
     * annotationMirror}.
     */
    private ImmutableSet<TypeMirror> getValueFieldOfClasses(AnnotationMirror annotationMirror) {
        return getAnnotationValue(annotationMirror)
                .accept(
                        new SimpleAnnotationValueVisitor8<ImmutableSet<TypeMirror>, Void>(ImmutableSet.of()) {
                            @Override
                            public ImmutableSet<TypeMirror> visitType(TypeMirror typeMirror, Void v) {
                                // TODO(ronshapiro): class literals may not always be declared types, i.e.
                                // int.class, int[].class
                                return ImmutableSet.of(typeMirror);
                            }

                            @Override
                            public ImmutableSet<TypeMirror> visitArray(List<? extends AnnotationValue> values, Void v) {
                                return values.stream()
                                        .flatMap(value -> value.accept(this, null).stream())
                                        .collect(Collectors.collectingAndThen(toList(), ImmutableSet::copyOf));
                            }
                        },
                        null);
    }

    private void log(String msg) {
        if (processingEnv.getOptions().containsKey("debug")) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
        }
    }

    private void warning(String msg, Element element, AnnotationMirror annotation) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, msg, element, annotation);
    }

    private void error(String msg, Element element, AnnotationMirror annotation) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element, annotation);
    }

    private void fatalError(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "FATAL ERROR: " + msg);
    }

}
