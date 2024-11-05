package top.jpower.core.util.support;

/**
 * @author mr.g
 * @date 2024/11/5 18:06
 */
@FunctionalInterface
public interface ThrowableSupplier<T> {

    /**
     *
     * Gets a result.
     *
     * @return a result
     */
    T get() throws Throwable;

}
