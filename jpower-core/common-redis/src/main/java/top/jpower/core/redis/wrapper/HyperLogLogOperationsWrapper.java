package top.jpower.core.redis.wrapper;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.HyperLogLogOperations;

/**
 * hll操作
 *
 * @author mr.g
 * @date 2024/10/15 10:35
 */
@AllArgsConstructor
public class HyperLogLogOperationsWrapper<V> {

    private final HyperLogLogOperations<String, Object> delegate;
    private final Class<V> clz;

    /**
     * Adds the given {@literal values} to the {@literal key}.
     *
     * @param key must not be {@literal null}.
     * @param values must not be {@literal null}.
     * @return 1 of at least one of the values was added to the key; 0 otherwise. {@literal null} when used in pipeline /
     *         transaction.
     */
    public Long add(String key, V... values) {
        return delegate.add(key, values);
    }

    /**
     * Gets the current number of elements within the {@literal key}.
     *
     * @param keys must not be {@literal null} or {@literal empty}.
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Long size(String... keys) {
        return delegate.size(keys);
    }

    /**
     * Merges all values of given {@literal sourceKeys} into {@literal destination} key.
     *
     * @param destination key of HyperLogLog to move source keys into.
     * @param sourceKeys must not be {@literal null} or {@literal empty}.
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Long union(String destination, String... sourceKeys) {
        return delegate.union(destination, sourceKeys);
    }

    /**
     * Removes the given {@literal key}.
     *
     * @param key must not be {@literal null}.
     */
    public void delete(String key) {
        delegate.delete(key);
    }

}
