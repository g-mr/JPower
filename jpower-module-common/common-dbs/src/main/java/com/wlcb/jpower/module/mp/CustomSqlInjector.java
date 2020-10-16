package com.wlcb.jpower.module.mp;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.mp.methods.*;

import java.util.List;

/**
 * @ClassName CustomSqlInjector
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-08-11 15:13
 * @Version 1.0
 */
public class CustomSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new InsertBatchSomeColumn(i -> i.getFieldFill() != FieldFill.UPDATE));
        methodList.add(new UpdateAllById(field -> !Fc.contains(new String[]{
                "create_time", "create_user"
        }, field.getColumn())));
        methodList.add(new DeleteReal());
        methodList.add(new DeleteRealBatchByIds());
        methodList.add(new DeleteRealById());
        methodList.add(new DeleteRealByMap());
        return methodList;
    }
}
