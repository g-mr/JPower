package com.wlcb.jpower.module.base.annotation;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author mr.g
 */
@FunctionalInterface
public interface Fn<T, R> extends Function<T, R>, Serializable {
}