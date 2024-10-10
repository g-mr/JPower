package top.jpower.core.redis.wrapper;

import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 转换类型值
 *
 * @author mr.g
 * @date 2024-10-10 23:00
 * @description
 */
@AllArgsConstructor
public class ValueOperationsWrapper<V> {

    private final ValueOperations<String, Object> delegate;
    private final Class<V> clz;

    /**
     * Set {@code value} for {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
     */
    public void set(String key, V value){
        delegate.set(key, value);
    }

    /**
     * Set the {@code value} and expiration {@code timeout} for {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @param timeout the key expiration timeout.
     * @param unit must not be {@literal null}.
     * @see <a href="https://redis.io/commands/setex">Redis Documentation: SETEX</a>
     */
    public void set(String key, V value, long timeout, TimeUnit unit){
        delegate.set(key, value, timeout, unit);
    }

    /**
     * Set the {@code value} and expiration {@code timeout} for {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @param timeout must not be {@literal null}.
     * @throws IllegalArgumentException if either {@code key}, {@code value} or {@code timeout} is not present.
     * @see <a href="https://redis.io/commands/setex">Redis Documentation: SETEX</a>
     * @since 2.1
     */
    public void set(String key, V value, Duration timeout) {
        delegate.set(key, value, timeout);
    }

    /**
     * Set {@code key} to hold the string {@code value} if {@code key} is absent.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/setnx">Redis Documentation: SETNX</a>
     */
    @Nullable
    public Boolean setIfAbsent(String key, V value){
        return delegate.setIfAbsent(key, value);
    }

    /**
     * Set {@code key} to hold the string {@code value} and expiration {@code timeout} if {@code key} is absent.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @param timeout the key expiration timeout.
     * @param unit must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.1
     * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
     */
    @Nullable
    public Boolean setIfAbsent(String key, V value, long timeout, TimeUnit unit){
        return delegate.setIfAbsent(key, value, timeout, unit);
    }

    /**
     * Set {@code key} to hold the string {@code value} and expiration {@code timeout} if {@code key} is absent.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @param timeout must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @throws IllegalArgumentException if either {@code key}, {@code value} or {@code timeout} is not present.
     * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
     * @since 2.1
     */
    @Nullable
    public Boolean setIfAbsent(String key, V value, Duration timeout) {

        return delegate.setIfAbsent(key, value, timeout);
    }

    /**
     * Set {@code key} to hold the string {@code value} if {@code key} is present.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @return command result indicating if the key has been set.
     * @throws IllegalArgumentException if either {@code key} or {@code value} is not present.
     * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
     * @since 2.1
     */
    @Nullable
    public Boolean setIfPresent(String key, V value){
        return delegate.setIfPresent(key, value);
    }

    /**
     * Set {@code key} to hold the string {@code value} and expiration {@code timeout} if {@code key} is present.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @param timeout the key expiration timeout.
     * @param unit must not be {@literal null}.
     * @return command result indicating if the key has been set.
     * @throws IllegalArgumentException if either {@code key}, {@code value} or {@code timeout} is not present.
     * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
     * @since 2.1
     */
    @Nullable
    public Boolean setIfPresent(String key, V value, long timeout, TimeUnit unit){
        return delegate.setIfPresent(key, value, timeout, unit);
    }

    /**
     * Set {@code key} to hold the string {@code value} and expiration {@code timeout} if {@code key} is present.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @param timeout must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @throws IllegalArgumentException if either {@code key}, {@code value} or {@code timeout} is not present.
     * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
     * @since 2.1
     */
    @Nullable
    public Boolean setIfPresent(String key, V value, Duration timeout) {
        return delegate.setIfAbsent(key, value, timeout);
    }

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {@code tuple}.
     *
     * @param map must not be {@literal null}.
     * @see <a href="https://redis.io/commands/mset">Redis Documentation: MSET</a>
     */
    public void multiSet(Map<String, ? extends V> map){
        delegate.multiSet(map);
    }

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {@code tuple} only if the provided key does
     * not exist.
     *
     * @param map must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/msetnx">Redis Documentation: MSETNX</a>
     */
    @Nullable
    public Boolean multiSetIfAbsent(Map<String, ? extends V> map){
        return delegate.multiSetIfAbsent(map);
    }

    /**
     * Get the value of {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when key does not exist or used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/get">Redis Documentation: GET</a>
     */
    @Nullable
    public V get(String key){
        return Convert.convert(clz, delegate.get(key));
    }

    /**
     * Return the value at {@code key} and delete the key.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when key does not exist or used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/getdel">Redis Documentation: GETDEL</a>
     * @since 2.6
     */
    @Nullable
    public V getAndDelete(String key){
        return Convert.convert(clz, delegate.getAndDelete(key));
    }

    /**
     * Return the value at {@code key} and expire the key by applying {@code timeout}.
     *
     * @param key must not be {@literal null}.
     * @param timeout
     * @param unit must not be {@literal null}.
     * @return {@literal null} when key does not exist or used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/getex">Redis Documentation: GETEX</a>
     * @since 2.6
     */
    @Nullable
    public V getAndExpire(String key, long timeout, TimeUnit unit) {
        return Convert.convert(clz, delegate.getAndExpire(key, timeout, unit));
    }

    /**
     * Return the value at {@code key} and expire the key by applying {@code timeout}.
     *
     * @param key must not be {@literal null}.
     * @param timeout must not be {@literal null}.
     * @return {@literal null} when key does not exist or used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/getex">Redis Documentation: GETEX</a>
     * @since 2.6
     */
    @Nullable
    public V getAndExpire(String key, Duration timeout){
        return Convert.convert(clz, delegate.getAndExpire(key, timeout));
    }

    /**
     * Return the value at {@code key} and persist the key. This operation removes any TTL that is associated with
     * {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when key does not exist or used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/getex">Redis Documentation: GETEX</a>
     * @since 2.6
     */
    @Nullable
    public V getAndPersist(String key){
        return Convert.convert(clz, delegate.getAndPersist(key));
    }

    /**
     * Set {@code value} of {@code key} and return its old value.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when key does not exist or used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/getset">Redis Documentation: GETSET</a>
     */
    @Nullable
    public V getAndSet(String key, V value){
        return Convert.convert(clz, delegate.getAndSet(key, value));
    }

    /**
     * Get multiple {@code keys}. Values are in the order of the requested keys Absent field values are represented using
     * {@code null} in the resulting {@link List}.
     *
     * @param keys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/mget">Redis Documentation: MGET</a>
     */
    @Nullable
    public List<V> multiGet(Collection<String> keys){
        return Convert.toList(clz, delegate.multiGet(keys));
    }

    /**
     * Increment an integer value stored as string value under {@code key} by one.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.1
     * @see <a href="https://redis.io/commands/incr">Redis Documentation: INCR</a>
     */
    @Nullable
    public Long increment(String key){
        return delegate.increment(key);
    }

    /**
     * Increment an integer value stored as string value under {@code key} by {@code delta}.
     *
     * @param key must not be {@literal null}.
     * @param delta
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/incrby">Redis Documentation: INCRBY</a>
     */
    @Nullable
    public Long increment(String key, long delta){
        return delegate.increment(key, delta);
    }

    /**
     * Increment a floating point number value stored as string value under {@code key} by {@code delta}.
     *
     * @param key must not be {@literal null}.
     * @param delta
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/incrbyfloat">Redis Documentation: INCRBYFLOAT</a>
     */
    @Nullable
    public Double increment(String key, double delta){
        return delegate.increment(key, delta);
    }

    /**
     * Decrement an integer value stored as string value under {@code key} by one.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.1
     * @see <a href="https://redis.io/commands/decr">Redis Documentation: DECR</a>
     */
    @Nullable
    public Long decrement(String key){
        return delegate.decrement(key);
    }

    /**
     * Decrement an integer value stored as string value under {@code key} by {@code delta}.
     *
     * @param key must not be {@literal null}.
     * @param delta
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.1
     * @see <a href="https://redis.io/commands/decrby">Redis Documentation: DECRBY</a>
     */
    @Nullable
    public Long decrement(String key, long delta){
        return delegate.decrement(key, delta);
    }

    /**
     * Append a {@code value} to {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/append">Redis Documentation: APPEND</a>
     */
    @Nullable
    public Integer append(String key, String value){
        return delegate.append(key, value);
    }

    /**
     * Get a substring of value of {@code key} between {@code begin} and {@code end}.
     *
     * @param key must not be {@literal null}.
     * @param start
     * @param end
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/getrange">Redis Documentation: GETRANGE</a>
     */
    @Nullable
    public String get(String key, long start, long end){
        return delegate.get(key, start, end);
    }

    /**
     * Overwrite parts of {@code key} starting at the specified {@code offset} with given {@code value}.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @param offset
     * @see <a href="https://redis.io/commands/setrange">Redis Documentation: SETRANGE</a>
     */
    public void set(String key, V value, long offset){
        delegate.set(key, value, offset);
    }

    /**
     * Get the length of the value stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/strlen">Redis Documentation: STRLEN</a>
     */
    @Nullable
    public Long size(String key){
        return delegate.size(key);
    }

    /**
     * Sets the bit at {@code offset} in value stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param offset
     * @param value
     * @return {@literal null} when used in pipeline / transaction.
     * @since 1.5
     * @see <a href="https://redis.io/commands/setbit">Redis Documentation: SETBIT</a>
     */
    @Nullable
    public Boolean setBit(String key, long offset, boolean value){
        return delegate.setBit(key, offset, value);
    }

    /**
     * Get the bit value at {@code offset} of value at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param offset
     * @return {@literal null} when used in pipeline / transaction.
     * @since 1.5
     * @see <a href="https://redis.io/commands/getbit">Redis Documentation: GETBIT</a>
     */
    @Nullable
    public Boolean getBit(String key, long offset) {
        return delegate.getBit(key, offset);
    }

    /**
     * Get / Manipulate specific integer fields of varying bit widths and arbitrary non (necessary) aligned offset stored
     * at a given {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param subCommands must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.1
     * @see <a href="https://redis.io/commands/bitfield">Redis Documentation: BITFIELD</a>
     */
    @Nullable
    public List<Long> bitField(String key, BitFieldSubCommands subCommands){
        return delegate.bitField(key, subCommands);
    }

}
