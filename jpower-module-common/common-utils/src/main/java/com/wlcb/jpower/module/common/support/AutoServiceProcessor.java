package com.wlcb.jpower.module.common.support;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.wlcb.jpower.module.base.annotation.AutoService;
import com.wlcb.jpower.module.common.utils.ExceptionUtil;

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
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author mr.g
 *
 * 参考google的实现
 */
@SupportedOptions("debug")
public class AutoServiceProcessor extends AbstractProcessor {


    /**
     * 报错接口和实现类
     **/
    private final Multimap<String, String> providers = HashMultimap.create();

    @Override
    public ImmutableSet<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(AutoService.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * <ol>
     *  <li> For each class annotated with {@link AutoService}<ul>
     *      <li> Verify the {@link AutoService} interface value is correct
     *      <li> Categorize the class by its service interface
     *      </ul>
     *
     *  <li> For each {@link AutoService} interface <ul>
     *       <li> Create a file named {@code META-INF/services/<interface>}
     *       <li> For each {@link AutoService} annotated class for this interface <ul>
     *           <li> Create an entry in the file
     *           </ul>
     *       </ul>
     * </ol>
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            processImpl(roundEnv);
        } catch (RuntimeException e) {
            fatalError(ExceptionUtil.getStackTraceAsString(e));
        }
        return false;
    }

//    ImmutableList<String> exceptionStacks() {
//        return ImmutableList.copyOf(exceptionStacks);
//    }

    private void processImpl(RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            generateConfigFiles();
        } else {
            processAnnotations(roundEnv);
        }
    }

    private void processAnnotations(RoundEnvironment roundEnv) {

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(AutoService.class);

        for (Element e : elements) {
            if (e.getKind() == ElementKind.CLASS){
                TypeElement providerImplementer = (TypeElement) e;
                AnnotationMirror annotationMirror = providerImplementer.getAnnotationMirrors().stream().filter(am -> am.getAnnotationType().toString().contentEquals(AutoService.class.getCanonicalName())).findFirst().get();
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
            String resourceFile = "META-INF/services/" + providerInterface;
            log("Working on resource file: " + resourceFile);
            try {
                SortedSet<String> allServices = Sets.newTreeSet();
                try {
                    // would like to be able to print the full path
                    // before we attempt to get the resource in case the behavior
                    // of filer.getResource does change to match the spec, but there's
                    // no good way to resolve CLASS_OUTPUT without first getting a resource.
                    FileObject existingFile =
                            filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
                    log("Looking for existing resource file at " + existingFile.toUri());
                    Set<String> oldServices = ServicesFiles.readServiceFile(existingFile.openInputStream());
                    log("Existing service entries: " + oldServices);
                    allServices.addAll(oldServices);
                } catch (IOException e) {
                    // According to the javadoc, Filer.getResource throws an exception
                    // if the file doesn't already exist.  In practice this doesn't
                    // appear to be the case.  Filer.getResource will happily return a
                    // FileObject that refers to a non-existent file but will throw
                    // IOException if you try to open an input stream for it.
                    log("Resource file did not already exist.");
                }

                Set<String> newServices = new HashSet<>(providers.get(providerInterface));
                if (!allServices.addAll(newServices)) {
                    log("No new service entries being added.");
                    continue;
                }

                log("New service file contents: " + allServices);
                FileObject fileObject =
                        filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
                try (OutputStream out = fileObject.openOutputStream()) {
                    ServicesFiles.writeServiceFile(allServices, out);
                }
                log("Wrote to: " + fileObject.toUri());
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
