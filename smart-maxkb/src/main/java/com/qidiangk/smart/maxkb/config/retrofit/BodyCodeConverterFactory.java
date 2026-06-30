package com.qidiangk.smart.maxkb.config.retrofit;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.*;
import com.alibaba.fastjson2.JSON;
import com.qidiangk.smart.maxkb.config.annotation.Return;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.util.utils.Fc;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class BodyCodeConverterFactory extends Converter.Factory{

    @Override
    public @Nullable Converter<ResponseBody, ?> responseBodyConverter(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Optional<Annotation> optional = Arrays.stream(annotations).filter(Return.class::isInstance).findFirst();
        if (optional.isEmpty()){
            return null;
        }

        Return rt =  optional.map(Return.class::cast).get();
        // 如果返回类型和包裹类型一致则交给JSON处理
        if (rt.clz().isAssignableFrom(getRawType(returnType))){
            return null;
        }

        return new BodyCodeConverter<>(returnType, annotations, retrofit, rt, this);
    }

    @RequiredArgsConstructor
    static class BodyCodeConverter<T> implements Converter<ResponseBody, T> {
        private final Type returnType;

        private final Annotation[] annotations;

        private final Retrofit retrofit;

        private final Return rt;

        private final Factory factory;

        @Nullable
        @Override
        public T convert(ResponseBody value) throws IOException {
//            Type type = TypeUtil.getActualType(rt.clz(), returnType);

            Converter<ResponseBody, ?> converter = retrofit.nextResponseBodyConverter(factory, rt.clz(), annotations);
            Object result = converter.convert(value);

            int code = getCode(rt, result);
            if (Arrays.stream(rt.successCode()).anyMatch(val -> Fc.equalsValue(code, val))){

//                if (Boolean.TYPE == returnType){
//                    return Convert.convert(returnType, true);
//                }

                Object object = BeanUtil.getFieldValue(result, rt.data());
                if (Fc.isNull(object)){
                    return null;
                }

                if (getRawType(returnType).isAssignableFrom(object.getClass())) {
                    return Convert.convert(returnType, object);
                }

                if (object instanceof Map){
                    return Convert.convert(returnType, object);
                }

                log.warn("未找到数据字段，可能返回数据是空，请检测泛型类型[{}]，返回body={}", returnType.getTypeName(), JSON.toJSONString(result));
                return null;
            }

            log.warn("HTTP返回的数据不符合预期，原始数据=={}", JSON.toJSONString(result));

            String msg = getMessage(rt, result);
            throw new JpowerException(509, msg);
        }

        private int getCode(Return rt, Object result){
            if (Fc.isNotBlank(rt.code())){
                return Fc.toInt(BeanUtil.getFieldValue(result, rt.code()), 999);
            } else {
                Field[] fields = ReflectUtil.getFields(rt.clz(), f -> {
                    if (Integer.class.equals(f.getType()) || Integer.TYPE.equals(f.getType()) || String.class.equals(f.getType())){
                        String code = Fc.toStr(ReflectUtil.getFieldValue(result, f));
                        return Fc.isNotBlank(code) && NumberUtil.isNumber(code);
                    }
                    return false;
                });

                if (Fc.isEmpty(fields)){
                    return 999;
                }

                Optional<Integer> code = Arrays.stream(fields).filter(f -> StrUtil.equalsAnyIgnoreCase(f.getName(), "code")).map(f->Fc.toInt(ReflectUtil.getFieldValue(result, f), 999)).findAny();
                return code.orElse(Fc.toInt(ReflectUtil.getFieldValue(result, fields[0]), 999));
            }
        }

        private String getMessage(Return rt, Object result){
            if (Fc.isNotBlank(rt.message())){
                return Fc.toStr(BeanUtil.getFieldValue(result, rt.message()));
            } else {
                Field[] fields = ReflectUtil.getFields(rt.clz(), f -> String.class.equals(f.getType()) && Fc.notNull(ReflectUtil.getFieldValue(result, f)));

                if (Fc.isEmpty(fields)){
                    return "接口返回业务异常";
                }

                Optional<String> msg = Arrays.stream(fields).filter(f -> StrUtil.equalsAnyIgnoreCase(f.getName(), "msg", "message")).map(f->(String)ReflectUtil.getFieldValue(result, f)).findAny();
                return msg.orElse((String)ReflectUtil.getFieldValue(result, fields[0]));
            }
        }
    }
}
