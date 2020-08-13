package com.wlcb.jpower.module.common.swagger;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.wlcb.jpower.module.common.swagger.support.ArrayRefProperty;
import io.swagger.models.Model;
import io.swagger.models.properties.*;
import org.springframework.beans.BeanUtils;
import springfox.documentation.RequestHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SwaggerConfigUtil
 * @Description TODO swagger工具类
 * @Author 郭丁志
 * @Date 2020-08-12 16:47
 * @Version 1.0
 */
public class SwaggerConfigUtil {

    public static Predicate<RequestHandler> basePackage(final List<String> basePackages) {
        return input -> declaringClass(input).transform(handlerPackage(basePackages)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final List<String> basePackages)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackages) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }


    /**
     * 判断是否Swagger基本类型
     * @param type
     * @return
     */
    public static boolean isBaseType(String type) {
        return SwaggerConfigUtil.getSwaggerProperty(type) != null;
    }

    /**
     * 获取Swagger支持的类型
     * @return
     */
    public static Map<String, AbstractProperty> getPropMap() {
        Map<String, AbstractProperty> map = new HashMap<String, AbstractProperty>();
        map.put("integer", new IntegerProperty());
        map.put("int", new IntegerProperty());
        map.put("long", new LongProperty());
        map.put("string", new StringProperty());
        map.put("object", new ObjectProperty());
        map.put("array", new ArrayProperty());
        map.put("boolean", new BooleanProperty());
        map.put("date", new DateTimeProperty());
        return map;
    }

    /**
     * 通过java类型获取Swagger类型
     * @param type javaType
     * @return swaggerType
     */
    public static AbstractProperty getSwaggerProperty(String type) {
        type = type.toLowerCase();
        return SwaggerConfigUtil.getPropMap().get(type);
    }

    public static boolean isMap(String type) {
        type = type.toLowerCase();
        return type.startsWith("map");
    }

    public static boolean isIterable(String type) {
        type = type.toLowerCase();
        return type.startsWith("list") || type.startsWith("set");
    }

    /**
     * 获取非基本类型的T<br>
     * new String[] { "A<List<C1>>", "A<C2>", "A<B<String,<String,List<C4>>>>" }
     * @param type
     * @return C1,C2,C3,C4
     */
    public static String getRef(String type) {
        try {
            String m = type.substring(type.lastIndexOf("«") + 1, type.indexOf("»"));
            String[] cc = m.split(",");
            for (String c : cc) {
                if (!SwaggerConfigUtil.isBaseType(c)) {
                    return c;
                }
            }
            return type;
        } catch (Exception e) {

        }
        return "!!Unknown T!!";
    }

    /**
     * 获取对象类型，主要是剥离第一层<>
     * @param type JsonResult<Map<Operator, List<Map<String, Customer>>>>
     * @return Map<Operator, List<Map<String, Customer>>>
     */
    public static String getRealType(String type) {
        try {
            String m = type.substring(type.indexOf("«") + 1, type.lastIndexOf("»"));
            return m;
        } catch (Exception e) {

        }
        return type;
    }

    /**
     * 判断是否存在非基本类型<参照类型>
     * @param type
     * @return
     */
    public static boolean hasRef(String type) {
        if (type.indexOf("»") > 0) {
            try {
                String m = type.substring(type.lastIndexOf("«") + 1, type.indexOf("»"));
                String[] cc = m.split(",");
                for (String c : cc) {
                    if (!SwaggerConfigUtil.isBaseType(c)) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        } else {
            return !SwaggerConfigUtil.isBaseType(type);
        }
    }

    /**
     * 递归处理泛型类型 <br>
     * JsonResult<Map<Map<Long, Operator>, List<Map<String, Customer>>>>
     * @param dataProp
     * @param type
     * @param definitions
     * @return
     */
    public static Property getNewProp(Property dataProp, String type, Map<String, Model> definitions) {
        Property newProp = null;
        Model model = definitions.get(type);
        Map<String, Property> props = null;
        if (null != model) {
            props = model.getProperties();
        }
        if (null == props) {
            props = new HashMap<String, Property>();
        }
        String realType = SwaggerConfigUtil.getRealType(type);
        if (SwaggerConfigUtil.isMap(type)) {
            String[] realTypes = SwaggerConfigUtil.splitByComma(realType);

            Map<PropertyBuilder.PropertyId, Object> argsK = new HashMap<PropertyBuilder.PropertyId, Object>();
            argsK.put(PropertyBuilder.PropertyId.DESCRIPTION, "Map的键");
            argsK.put(PropertyBuilder.PropertyId.TYPE, realTypes[0].toLowerCase());
            AbstractProperty _prop0 = SwaggerConfigUtil.getSwaggerProperty(realTypes[0]);
            Property propK = PropertyBuilder.build(null == _prop0 ? "object" : _prop0.getType(),
                    null == _prop0 ? null : _prop0.getFormat(), argsK);
            propK.setName("key");

            Map<PropertyBuilder.PropertyId, Object> argsV = new HashMap<PropertyBuilder.PropertyId, Object>();
            argsV.put(PropertyBuilder.PropertyId.DESCRIPTION, "Map的值");
            argsV.put(PropertyBuilder.PropertyId.TYPE, realTypes[1].toLowerCase());
            AbstractProperty _prop1 = SwaggerConfigUtil.getSwaggerProperty(realTypes[1]);
            Property propV = PropertyBuilder.build(null == _prop1 ? "object" : _prop1.getType(),
                    null == _prop1 ? null : _prop1.getFormat(), argsV);
            propV.setName("value");

            if (!realType.equals(type)) {
                propK = SwaggerConfigUtil.getNewProp(propK, realTypes[0], definitions);
                propV = SwaggerConfigUtil.getNewProp(propV, realTypes[1], definitions);
            }

            props.put(propK.getName(), propK);
            props.put(propV.getName(), propV);

            newProp = new RefProperty();
            BeanUtils.copyProperties(dataProp, newProp);
            ((RefProperty) newProp).set$ref(type);
        } else if (SwaggerConfigUtil.isIterable(type)) {
            String ref = SwaggerConfigUtil.getRealType(type);
            newProp = new ArrayRefProperty();
            BeanUtils.copyProperties(dataProp, newProp);
            ((ArrayRefProperty) newProp).set$ref(ref);
            ((ArrayRefProperty) newProp).setType(ArrayRefProperty.TYPE);
            if (!realType.equals(type)) {
                SwaggerConfigUtil.getNewProp(dataProp, realType, definitions);
            }
        } else if (SwaggerConfigUtil.isBaseType(type)) {
            Map<PropertyBuilder.PropertyId, Object> args = new HashMap<PropertyBuilder.PropertyId, Object>();
            args.put(PropertyBuilder.PropertyId.DESCRIPTION, dataProp.getDescription());
            args.put(PropertyBuilder.PropertyId.TYPE, type.toLowerCase());
            AbstractProperty _prop = SwaggerConfigUtil.getSwaggerProperty(type);
            newProp = PropertyBuilder.build(_prop.getType(), _prop.getFormat(), args);
            newProp.setName(dataProp.getName());
        } else if (SwaggerConfigUtil.hasRef(type)) {
            newProp = new RefProperty();
            BeanUtils.copyProperties(dataProp, newProp);
            ((RefProperty) newProp).set$ref(type);
        } else {

        }
        if (null != model) {
            model.setProperties(props);
        }

        return newProp;
    }

    public static String[] splitByComma(String str) {
        int index = 0;
        int has = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ("«".equals(c + "")) {
                has++;
            }
            if ("»".equals(c + "")) {
                has--;
            }
            if (",".equals(c + "") && has == 0) {
                index = i;
            }
        }

        String[] arr = new String[2];
        arr[0] = str.substring(0, index);
        arr[1] = str.substring(index + 1);
        return arr;
    }

    public static void main(String[] args) {
        String[] ss = new String[] { "A«List«C1»»", "A«C2»", "A«B«String,«String,List«C4»»»»" };
        for (String s : ss) {
            String c = SwaggerConfigUtil.getRealType(s);
            System.out.println(c);
        }

        String[] s2 = new String[] { "A,B«List«C1»»", "Map«A,B»,C«List«D»»",
                "Map«Map«A,B»,C«List«D»»,Map«A,B»,C«List«D»»»,C«List«D»»" };
        for (String s : s2) {
            String[] arr = SwaggerConfigUtil.splitByComma(s);
            System.out.println(arr[0]);
            System.out.println(arr[1]);
        }
    }
}
