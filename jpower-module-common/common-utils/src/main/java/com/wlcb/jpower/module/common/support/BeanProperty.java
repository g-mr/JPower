package com.wlcb.jpower.module.common.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName BeanProperty
 * @Description TODO Bean属性
 * @Author 郭丁志
 * @Date 2020-07-23 16:51
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public class BeanProperty {
    private final String name;
    private final Class<?> type;
}
