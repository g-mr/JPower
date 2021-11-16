package com.wlcb.jpower.module.dictbind.handler;

import com.wlcb.jpower.module.base.annotation.Dict;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @Author mr.g
 * @Date 2021/11/16 0016 0:56
 */
public interface IDictBindHandler {

    /**
     * 绑定字典
     * @author mr.g
     * @param dict 字典注解
     * @param fieldValue 字段值
     * @param metaObject MetaObject对象
     */
    void setMetaObject(Dict dict, Object fieldValue, MetaObject metaObject);
}
