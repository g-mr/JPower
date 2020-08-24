package com.wlcb.jpower.module.mp;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.*;
import com.wlcb.jpower.module.mp.methods.DeleteReal;
import com.wlcb.jpower.module.mp.methods.DeleteRealBatchByIds;
import com.wlcb.jpower.module.mp.methods.DeleteRealById;
import com.wlcb.jpower.module.mp.methods.DeleteRealByMap;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @ClassName CustomSqlInjector
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-08-11 15:13
 * @Version 1.0
 */
public class CustomSqlInjector extends AbstractSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        return Stream.of(
                new Insert(),
                new DeleteReal(),
                new DeleteRealBatchByIds(),
                new DeleteRealById(),
                new DeleteRealByMap(),
                new Delete(),
                new DeleteByMap(),
                new DeleteById(),
                new DeleteBatchByIds(),
                new Update(),
                new UpdateById(),
                new SelectById(),
                new SelectBatchByIds(),
                new SelectByMap(),
                new SelectOne(),
                new SelectCount(),
                new SelectMaps(),
                new SelectMapsPage(),
                new SelectObjs(),
                new SelectList(),
                new SelectPage()
        ).collect(toList());
    }
}
