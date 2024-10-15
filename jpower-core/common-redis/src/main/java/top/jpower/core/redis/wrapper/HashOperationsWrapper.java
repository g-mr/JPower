package top.jpower.core.redis.wrapper;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ScanOptions;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * hash 操作
 *
 * @author mr.g
 * @date 2024/10/15 15:05
 */
@AllArgsConstructor
public class HashOperationsWrapper<HV> {

    private final HashOperations<String, String, Object> delegate;
    private final Class<HV> clz;

    /**
     * Delete given hash {@code hashKeys}.
     *
     * @param key must not be {@literal null}.
     * @param hashKeys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Long delete(String key, String... hashKeys) {
        return delegate.delete(key, hashKeys);
    }

    /**
     * Determine if given hash {@code hashKey} exists.
     *
     * @param key must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Boolean hasKey(String key, String hashKey) {
        return delegate.hasKey(key, hashKey);
    }

    /**
     * Get value for given {@code hashKey} from hash at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @return {@literal null} when key or hashKey does not exist or used in pipeline / transaction.
     */
    public HV get(String key, String hashKey) {
        return Convert.convert(clz, delegate.get(key, hashKey));
    }

    /**
     * Get values for given {@code hashKeys} from hash at {@code key}. Values are in the order of the requested keys
     * Absent field values are represented using {@code null} in the resulting {@link List}.
     *
     * @param key must not be {@literal null}.
     * @param hashKeys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     */
    public List<HV> multiGet(String key, Collection<String> hashKeys) {
        return Convert.toList(clz, delegate.multiGet(key, hashKeys));
    }

    /**
     * Increment {@code value} of a hash {@code hashKey} by the given {@code delta}.
     *
     * @param key must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @param delta
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Long increment(String key, String hashKey, long delta) {
        return delegate.increment(key, hashKey, delta);
    }

    /**
     * Increment {@code value} of a hash {@code hashKey} by the given {@code delta}.
     *
     * @param key must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @param delta
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Double increment(String key, String hashKey, double delta) {
        return delegate.increment(key, hashKey, delta);
    }
    /**
     * Return a random hash key (aka field) from the hash stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} if key does not exist or when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/hrandfield">Redis Documentation: HRANDFIELD</a>
     */
    public String randomKey(String key) {
        return delegate.randomKey(key);
    }

    /**
     * Return a random entry from the hash stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} if key does not exist or when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/hrandfield">Redis Documentation: HRANDFIELD</a>
     */
    public Map.Entry<String, HV> randomEntry(String key) {
        Map.Entry<String, Object> entry = delegate.randomEntry(key);
        if (entry == null){
            return null;
        }
        return Converters.entryOf(entry.getKey(), Convert.convert(clz, entry.getValue()));
    }

    /**
     * Return random hash keys (aka fields) from the hash stored at {@code key}. If the provided {@code count} argument is
     * positive, return a list of distinct hash keys, capped either at {@code count} or the hash size. If {@code count} is
     * negative, the behavior changes and the command is allowed to return the same hash key multiple times. In this case,
     * the number of returned fields is the absolute value of the specified count.
     *
     * @param key must not be {@literal null}.
     * @param count number of fields to return.
     * @return {@literal null} if key does not exist or when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/hrandfield">Redis Documentation: HRANDFIELD</a>
     */
    public List<String> randomKeys(String key, long count) {
        return delegate.randomKeys(key, count);
    }

    /**
     * Return a random entries from the hash stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count number of fields to return. Must be positive.
     * @return {@literal null} if key does not exist or when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/hrandfield">Redis Documentation: HRANDFIELD</a>
     */
    public Map<String, HV> randomEntries(String key, long count) {
        return Convert.toMap(String.class, clz, delegate.randomEntries(key, count));
    }

    /**
     * Get key set (fields) of hash at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Set<String> keys(String key) {
        return delegate.keys(key);
    }

    /**
     * Returns the length of the value associated with {@code hashKey}. If either the {@code key} or the {@code hashKey}
     * do not exist, {@code 0} is returned.
     *
     * @param key must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.1
     */
    public Long lengthOfValue(String key, String hashKey) {
        return delegate.lengthOfValue(key, hashKey);
    }

    /**
     * Get size of hash at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Long size(String key) {
        return delegate.size(key);
    }

    /**
     * Set multiple hash fields to multiple values using data provided in {@code m}.
     *
     * @param key must not be {@literal null}.
     * @param m must not be {@literal null}.
     */
    public void putAll(String key, Map<? extends String, ? extends HV> m) {
        delegate.putAll(key, m);
    }

    /**
     * Set the {@code value} of a hash {@code hashKey}.
     *
     * @param key must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @param value
     */
    public void put(String key, String hashKey, HV value) {
        delegate.put(key, hashKey, value);
    }

    /**
     * Set the {@code value} of a hash {@code hashKey} only if {@code hashKey} does not exist.
     *
     * @param key must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @param value
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Boolean putIfAbsent(String key, String hashKey, HV value) {
        return delegate.putIfAbsent(key, hashKey, value);
    }

    /**
     * Get entry set (values) of hash at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     */
    public List<HV> values(String key) {
        return Convert.toList(clz, delegate.values(key));
    }

    /**
     * Get entire hash stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Map<String, HV> entries(String key) {
        return Convert.toMap(String.class, clz, delegate.entries(key));
    }

    /**
     * Use a {@link Cursor} to iterate over entries in hash at {@code key}. <br />
     * <strong>Important:</strong> Call {@link Cursor#close()} when done to avoid resource leaks.
     *
     * @param key must not be {@literal null}.
     * @param options must not be {@literal null}.
     * @return the result cursor providing access to the scan result. Must be closed once fully processed (e.g. through a
     *         try-with-resources clause).
     * @since 1.4
     */
    public Cursor<Map.Entry<String, HV>> scan(String key, ScanOptions options) {
        return new ConvertingCursor<>(delegate.scan(key, options), entry ->
                Converters.entryOf(entry.getKey(), Convert.convert(clz, entry.getValue()))
        );
    }

    /**
     * 删除
     *
     * @param keys KEYS
     */
    public Boolean delete(final String keys) {
        return delegate.getOperations().delete(keys);
    }

    /**
     * 删除
     *
     * @param keys KEYS
     */
    public Long delete(final String... keys) {
        return delegate.getOperations().delete(ListUtil.toList(keys));
    }

}
