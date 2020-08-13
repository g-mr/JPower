package com.wlcb.jpower.module.common.swagger.support;

import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefType;

/**
 * @ClassName ArrayRefProperty
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/13 0013 23:47
 * @Version 1.0
 */
public class ArrayRefProperty extends ArrayProperty {

    private GenericRef genericRef;

    public String get$ref() {
        return genericRef.getRef();
    }

    public void set$ref(String ref) {
        this.genericRef = new GenericRef(RefType.DEFINITION, ref);

        // $ref
        RefProperty items = new RefProperty();
        items.setType(ref);
        items.set$ref(ref);
        this.items(items);
    }

}
