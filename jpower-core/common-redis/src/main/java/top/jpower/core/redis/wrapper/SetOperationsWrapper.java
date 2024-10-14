package top.jpower.core.redis.wrapper;

import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mr.g
 * @date 2024/10/12 18:11
 */

@AllArgsConstructor
public class SetOperationsWrapper<V> {

    private final SetOperations<String, Object> delegate;
    private final Class<V> clz;

    /**
     * Add given {@code values} to set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param values
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sadd">Redis Documentation: SADD</a>
     */
    public Long add(String key, V... values) {
        return delegate.add(key, values);
    }

    /**
     * Remove given {@code values} from set at {@code key} and return the number of removed elements.
     *
     * @param key must not be {@literal null}.
     * @param values
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/srem">Redis Documentation: SREM</a>
     */
    public Long remove(String key, Object... values) {
        return delegate.remove(key, values);
    }

    /**
     * Remove and return a random member from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/spop">Redis Documentation: SPOP</a>
     */
    public V pop(String key) {
        return Convert.convert(clz, delegate.pop(key));
    }

    /**
     * Remove and return {@code count} random members from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count number of random members to pop from the set.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/spop">Redis Documentation: SPOP</a>
     * @since 2.0
     */
    public List<V> pop(String key, long count) {
        return Convert.toList(clz, delegate.pop(key, count));
    }

    /**
     * Move {@code value} from {@code key} to {@code destKey}
     *
     * @param key must not be {@literal null}.
     * @param value
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/smove">Redis Documentation: SMOVE</a>
     */
    public Boolean move(String key, V value, String destKey) {
        return delegate.move(key, value, destKey);
    }

    /**
     * Get size of set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/scard">Redis Documentation: SCARD</a>
     */
    public Long size(String key) {
        return delegate.size(key);
    }

    /**
     * Check if set at {@code key} contains {@code value}.
     *
     * @param key must not be {@literal null}.
     * @param o
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sismember">Redis Documentation: SISMEMBER</a>
     */
    public Boolean isMember(String key, Object o) {
        return delegate.isMember(key, o);
    }

    /**
     * Check if set at {@code key} contains one or more {@code values}.
     *
     * @param key must not be {@literal null}.
     * @param objects
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/smismember">Redis Documentation: SMISMEMBER</a>
     */
    public Map<Object, Boolean> isMember(String key, Object... objects) {
        return delegate.isMember(key, objects);
    }

    /**
     * Returns the members intersecting all given sets at {@code key} and {@code otherKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sinter">Redis Documentation: SINTER</a>
     */
    public Set<V> intersect(String key, String otherKey) {
        return Convert.toSet(clz, delegate.intersect(key, otherKey));
    }

    /**
     * Returns the members intersecting all given sets at {@code key} and {@code otherKeys}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sinter">Redis Documentation: SINTER</a>
     */
    public Set<V> intersect(String key, Collection<String> otherKeys) {
        return Convert.toSet(clz, delegate.intersect(key, otherKeys));
    }

    /**
     * Returns the members intersecting all given sets at {@code keys}.
     *
     * @param keys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sinter">Redis Documentation: SINTER</a>
     * @since 2.2
     */
    public Set<V> intersect(Collection<String> keys) {
        return Convert.toSet(clz, delegate.intersect(keys));
    }

    /**
     * Intersect all given sets at {@code key} and {@code otherKey} and store result in {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
     */
    public Long intersectAndStore(String key, String otherKey, String destKey) {
        return delegate.intersectAndStore(key, otherKey, destKey);
    }

    /**
     * Intersect all given sets at {@code key} and {@code otherKeys} and store result in {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
     */
    public Long intersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return delegate.intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * Intersect all given sets at {@code keys} and store result in {@code destKey}.
     *
     * @param keys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
     * @since 2.2
     */
    public Long intersectAndStore(Collection<String> keys, String destKey) {
        return delegate.intersectAndStore(keys, destKey);
    }

    /**
     * Union all sets at given {@code keys} and {@code otherKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sunion">Redis Documentation: SUNION</a>
     */
    public Set<V> union(String key, String otherKey) {
        return Convert.toSet(clz, delegate.union(key, otherKey));
    }

    /**
     * Union all sets at given {@code keys} and {@code otherKeys}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sunion">Redis Documentation: SUNION</a>
     */
    
    Set<V> union(String key, Collection<String> otherKeys) {
        return Convert.toSet(clz, delegate.union(key, otherKeys));
    }

    /**
     * Union all sets at given {@code keys}.
     *
     * @param keys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sunion">Redis Documentation: SUNION</a>
     * @since 2.2
     */
    public Set<V> union(Collection<String> keys) {
        return Convert.toSet(clz, delegate.union(keys));
    }

    /**
     * Union all sets at given {@code key} and {@code otherKey} and store result in {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sunionstore">Redis Documentation: SUNIONSTORE</a>
     */
    public Long unionAndStore(String key, String otherKey, String destKey) {
        return delegate.unionAndStore(key, otherKey, destKey);
    }

    /**
     * Union all sets at given {@code key} and {@code otherKeys} and store result in {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sunionstore">Redis Documentation: SUNIONSTORE</a>
     */
    public Long unionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return delegate.unionAndStore(key, otherKeys, destKey);
    }

    /**
     * Union all sets at given {@code keys} and store result in {@code destKey}.
     *
     * @param keys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sunionstore">Redis Documentation: SUNIONSTORE</a>
     * @since 2.2
     */
    public Long unionAndStore(Collection<String> keys, String destKey) {
        return delegate.unionAndStore(keys, destKey);
    }

    /**
     * Diff all sets for given {@code key} and {@code otherKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sdiff">Redis Documentation: SDIFF</a>
     */
    public Set<V> difference(String key, String otherKey) {
        return Convert.toSet(clz, delegate.difference(key, otherKey));
    }

    /**
     * Diff all sets for given {@code key} and {@code otherKeys}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sdiff">Redis Documentation: SDIFF</a>
     */
    public Set<V> difference(String key, Collection<String> otherKeys) {
        return Convert.toSet(clz, delegate.difference(key, otherKeys));
    }

    /**
     * Diff all sets for given {@code keys}.
     *
     * @param keys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sdiff">Redis Documentation: SDIFF</a>
     * @since 2.2
     */
    public Set<V> difference(Collection<String> keys) {
        return Convert.toSet(clz, delegate.difference(keys));
    }

    /**
     * Diff all sets for given {@code key} and {@code otherKey} and store result in {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sdiffstore">Redis Documentation: SDIFFSTORE</a>
     */
    public Long differenceAndStore(String key, String otherKey, String destKey) {
        return delegate.differenceAndStore(key, otherKey, destKey);
    }

    /**
     * Diff all sets for given {@code key} and {@code otherKeys} and store result in {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sdiffstore">Redis Documentation: SDIFFSTORE</a>
     */
    public Long differenceAndStore(String key, Collection<String> otherKeys, String destKey) {
        return delegate.differenceAndStore(key, otherKeys, destKey);
    }

    /**
     * Diff all sets for given {@code keys} and store result in {@code destKey}.
     *
     * @param keys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/sdiffstore">Redis Documentation: SDIFFSTORE</a>
     * @since 2.2
     */
    public Long differenceAndStore(Collection<String> keys, String destKey) {
        return delegate.differenceAndStore(keys, destKey);
    }

    /**
     * Get all elements of set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/smembers">Redis Documentation: SMEMBERS</a>
     */
    public Set<V> members(String key) {
        return Convert.toSet(clz, delegate.members(key));
    }

    /**
     * Get random element from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     */
    public V randomMember(String key) {
        return Convert.convert(clz, delegate.randomMember(key));
    }

    /**
     * Get {@code count} distinct random elements from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count nr of members to return
     * @return empty {@link Set} if {@code key} does not exist.
     * @throws IllegalArgumentException if count is negative.
     * @see <a href="https://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     */

    public Set<V> distinctRandomMembers(String key, long count) {
        return Convert.toSet(clz, delegate.distinctRandomMembers(key, count));
    }

    /**
     * Get {@code count} random elements from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count nr of members to return.
     * @return empty {@link List} if {@code key} does not exist or {@literal null} when used in pipeline / transaction.
     * @throws IllegalArgumentException if count is negative.
     * @see <a href="https://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     */

    public List<V> randomMembers(String key, long count) {
        return Convert.toList(clz, delegate.randomMembers(key, count));
    }

    /**
     * Use a {@link Cursor} to iterate over entries set at {@code key}. <br />
     * <strong>Important:</strong> Call {@link Cursor#close()} when done to avoid resource leaks.
     *
     * @param key
     * @param options must not be {@literal null}.
     * @return the result cursor providing access to the scan result. Must be closed once fully processed (e.g. through a
     *         try-with-resources clause).
     * @since 1.4
     */
    public Cursor<V> scan(String key, ScanOptions options) {
        return new ConvertingCursor<>(delegate.scan(key, options), val -> Convert.convert(clz, val));
    }
}
