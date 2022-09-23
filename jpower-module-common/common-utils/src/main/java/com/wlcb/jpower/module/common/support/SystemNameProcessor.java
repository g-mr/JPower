package com.wlcb.jpower.module.common.support;

import com.wlcb.jpower.module.base.annotation.SystemName;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author mr.g
 * @date 2022-09-20 18:00
 */
@SupportedAnnotationTypes(value = {"com.wlcb.jpower.module.base.annotation.SystemName"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@AutoService(Processor.class)
public class SystemNameProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        final Set<? extends Element> dataAnnotations = roundEnv.getElementsAnnotatedWith(SystemName.class);


        return true;
    }

}
