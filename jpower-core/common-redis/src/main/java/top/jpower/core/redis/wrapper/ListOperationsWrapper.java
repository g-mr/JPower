package top.jpower.core.redis.wrapper;

import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 转换类型值
 *
 * @author mr.g
 * @date 2024-10-10 23:00
 * @description
 */
@AllArgsConstructor
public class ListOperationsWrapper<V> {

    private final ListOperations<String, Object> delegate;
    private final Class<V> clz;

    /**
     * Get elements between {@code begin} and {@code end} from list at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param start
     * @param end
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
     */
    public List<V> range(String key, long start, long end) {
        return Convert.toList(clz, delegate.range(key, start, end));
    }

    /**
     * Trim list at {@code key} to elements between {@code start} and {@code end}.
     *
     * @param key must not be {@literal null}.
     * @param start
     * @param end
     * @see <a href="https://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     */
    public void trim(String key, long start, long end) {
        delegate.trim(key, start, end);
    }

    /**
     * Get the size of list stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/llen">Redis Documentation: LLEN</a>
     */
    public Long size(String key) {
        return delegate.size(key);
    }

    /**
     * Prepend {@code value} to {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     */
    public Long leftPush(String key, V value) {
        return delegate.leftPush(key, value);
    }

    /**
     * Prepend {@code values} to {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param values
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     */
    public Long leftPushAll(String key, V... values) {
        return delegate.leftPushAll(key, values);
    }

    /**
     * Prepend {@code values} to {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param values must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 1.5
     * @see <a href="https://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     */
    public Long leftPushAll(String key, Collection<V> values) {
        return delegate.leftPushAll(key, values);
    }

    /**
     * Prepend {@code values} to {@code key} only if the list exists.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/lpushx">Redis Documentation: LPUSHX</a>
     */
    @Nullable
    public Long leftPushIfPresent(String key, V value) {
        return delegate.leftPushIfPresent(key, value);
    }

    /**
     * Insert {@code value} to {@code key} before {@code pivot}.
     *
     * @param key must not be {@literal null}.
     * @param pivot must not be {@literal null}.
     * @param value
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/linsert">Redis Documentation: LINSERT</a>
     */
    @Nullable
    public Long leftPush(String key, V pivot, V value) {
        return delegate.leftPush(key, pivot, value);
    }

    /**
     * Append {@code value} to {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     */
    @Nullable
    public Long rightPush(String key, V value) {
        return delegate.rightPush(key, value);
    }

    /**
     * Append {@code values} to {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param values
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     */
    @Nullable
    public Long rightPushAll(String key, V... values) {
        return delegate.rightPushAll(key, values);
    }

    /**
     * Append {@code values} to {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param values
     * @return {@literal null} when used in pipeline / transaction.
     * @since 1.5
     * @see <a href="https://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     */
    @Nullable
    public Long rightPushAll(String key, Collection<V> values) {
        return delegate.rightPushAll(key, values);
    }

    /**
     * Append {@code values} to {@code key} only if the list exists.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/rpushx">Redis Documentation: RPUSHX</a>
     */
    @Nullable
    public Long rightPushIfPresent(String key, V value) {
        return delegate.rightPushIfPresent(key, value);
    }

    /**
     * Insert {@code value} to {@code key} after {@code pivot}.
     *
     * @param key must not be {@literal null}.
     * @param pivot must not be {@literal null}.
     * @param value
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/linsert">Redis Documentation: LINSERT</a>
     */
    @Nullable
    public Long rightPush(String key, V pivot, V value) {
        return delegate.rightPush(key, pivot, value);
    }

    /**
     * Atomically returns and removes the first/last element (head/tail depending on the {@code from} argument) of the
     * list stored at {@code sourceKey}, and pushes the element at the first/last element (head/tail depending on the
     * {@code to} argument) of the list stored at {@code destinationKey}.
     *
     * @param from must not be {@literal null}.
     * @param to must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/lmove">Redis Documentation: LMOVE</a>
     */
    @Nullable
    public V move(ListOperations.MoveFrom<String> from, ListOperations.MoveTo<String> to) {
        return Convert.convert(clz, delegate.move(from, to));
    }

    /**
     * Atomically returns and removes the first/last element (head/tail depending on the {@code from} argument) of the
     * list stored at {@code sourceKey}, and pushes the element at the first/last element (head/tail depending on the
     * {@code to} argument) of the list stored at {@code destinationKey}.
     *
     * @param sourceKey must not be {@literal null}.
     * @param from must not be {@literal null}.
     * @param destinationKey must not be {@literal null}.
     * @param to must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/lmove">Redis Documentation: LMOVE</a>
     */
    @Nullable
    public V move(String sourceKey, RedisListCommands.Direction from, String destinationKey, RedisListCommands.Direction to) {
        return Convert.convert(clz, delegate.move(sourceKey, from, destinationKey, to));
    }

    /**
     * Atomically returns and removes the first/last element (head/tail depending on the {@code from} argument) of the
     * list stored at {@code sourceKey}, and pushes the element at the first/last element (head/tail depending on the
     * {@code to} argument) of the list stored at {@code destinationKey}.
     * <p>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param from must not be {@literal null}.
     * @param to must not be {@literal null}.
     * @param timeout must not be {@literal null} or negative.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/blmove">Redis Documentation: BLMOVE</a>
     */
    @Nullable
    public V move(ListOperations.MoveFrom<String> from, ListOperations.MoveTo<String> to, Duration timeout) {
        return Convert.convert(clz, delegate.move(from, to, timeout));
    }

    /**
     * Atomically returns and removes the first/last element (head/tail depending on the {@code from} argument) of the
     * list stored at {@code sourceKey}, and pushes the element at the first/last element (head/tail depending on the
     * {@code to} argument) of the list stored at {@code destinationKey}.
     * <p>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param sourceKey must not be {@literal null}.
     * @param from must not be {@literal null}.
     * @param destinationKey must not be {@literal null}.
     * @param to must not be {@literal null}.
     * @param timeout must not be {@literal null} or negative.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/blmove">Redis Documentation: BLMOVE</a>
     */
    @Nullable
    public V move(String sourceKey, RedisListCommands.Direction from, String destinationKey, RedisListCommands.Direction to, Duration timeout) {
        return Convert.convert(clz, delegate.move(sourceKey, from, destinationKey, to, timeout));
    }

    /**
     * Atomically returns and removes the first/last element (head/tail depending on the {@code from} argument) of the
     * list stored at {@code sourceKey}, and pushes the element at the first/last element (head/tail depending on the
     * {@code to} argument) of the list stored at {@code destinationKey}.
     * <p>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param sourceKey must not be {@literal null}.
     * @param from must not be {@literal null}.
     * @param destinationKey must not be {@literal null}.
     * @param to must not be {@literal null}.
     * @param timeout
     * @param unit
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/blmove">Redis Documentation: BLMOVE</a>
     */
    @Nullable
    public V move(String sourceKey, RedisListCommands.Direction from, String destinationKey, RedisListCommands.Direction to, long timeout, TimeUnit unit) {
        return Convert.convert(clz, delegate.move(sourceKey, from, destinationKey, to, timeout, unit));
    }

    /**
     * Set the {@code value} list element at {@code index}.
     *
     * @param key must not be {@literal null}.
     * @param index
     * @param value
     * @see <a href="https://redis.io/commands/lset">Redis Documentation: LSET</a>
     */
    public void set(String key, long index, V value) {
        delegate.set(key, index, value);
    }

    /**
     * Removes the first {@code count} occurrences of {@code value} from the list stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count
     * @param value
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/lrem">Redis Documentation: LREM</a>
     */
    @Nullable
    public Long remove(String key, long count, Object value) {
        return delegate.remove(key, count, value);
    }

    /**
     * Get element at {@code index} form list at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param index
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/lindex">Redis Documentation: LINDEX</a>
     */
    @Nullable
    public V index(String key, long index) {
        return Convert.convert(clz, delegate.index(key, index));
    }

    /**
     * Returns the index of the first occurrence of the specified value in the list at at {@code key}. <br />
     * Requires Redis 6.0.6 or newer.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction or when not contained in list.
     * @since 2.4
     * @see <a href="https://redis.io/commands/lpos">Redis Documentation: LPOS</a>
     */
    @Nullable
    public Long indexOf(String key, V value) {
        return delegate.indexOf(key, value);
    }

    /**
     * Returns the index of the last occurrence of the specified value in the list at at {@code key}. <br />
     * Requires Redis 6.0.6 or newer.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction or when not contained in list.
     * @since 2.4
     * @see <a href="https://redis.io/commands/lpos">Redis Documentation: LPOS</a>
     */
    @Nullable
    public Long lastIndexOf(String key, V value) {
        return delegate.lastIndexOf(key, value);
    }

    /**
     * Removes and returns first element in list stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/lpop">Redis Documentation: LPOP</a>
     */
    @Nullable
    public V leftPop(String key) {
        return Convert.convert(clz, delegate.leftPop(key));
    }

    /**
     * Removes and returns first {@code} elements in list stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/lpop">Redis Documentation: LPOP</a>
     * @since 2.6
     */
    @Nullable
    public List<V> leftPop(String key, long count) {
        return Convert.toList(clz, delegate.leftPop(key, count));
    }

    /**
     * Removes and returns first element from lists stored at {@code key} . <br>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param key must not be {@literal null}.
     * @param timeout
     * @param unit must not be {@literal null}.
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/blpop">Redis Documentation: BLPOP</a>
     */
    @Nullable
    public V leftPop(String key, long timeout, TimeUnit unit) {
        return Convert.convert(clz, delegate.leftPop(key, timeout, unit));
    }

    /**
     * Removes and returns first element from lists stored at {@code key} . <br>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param key must not be {@literal null}.
     * @param timeout must not be {@literal null}.
     * @return can be {@literal null}.
     * @throws IllegalArgumentException if the timeout is {@literal null} or negative.
     * @since 2.3
     * @see <a href="https://redis.io/commands/blpop">Redis Documentation: BLPOP</a>
     */
    @Nullable
    public V leftPop(String key, Duration timeout) {
        return Convert.convert(clz, delegate.leftPop(key, timeout));
    }

    /**
     * Removes and returns last element in list stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/rpop">Redis Documentation: RPOP</a>
     */
    @Nullable
    public V rightPop(String key) {
        return Convert.convert(clz, delegate.rightPop(key));
    }

    /**
     * Removes and returns last {@code} elements in list stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/rpop">Redis Documentation: RPOP</a>
     * @since 2.6
     */
    @Nullable
    public List<V> rightPop(String key, long count) {
        return Convert.toList(clz, delegate.rightPop(key, count));
    }

    /**
     * Removes and returns last element from lists stored at {@code key}. <br>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param key must not be {@literal null}.
     * @param timeout
     * @param unit must not be {@literal null}.
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/brpop">Redis Documentation: BRPOP</a>
     */
    @Nullable
    public V rightPop(String key, long timeout, TimeUnit unit) {
        return Convert.convert(clz, delegate.rightPop(key, timeout, unit));
    }

    /**
     * Removes and returns last element from lists stored at {@code key}. <br>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param key must not be {@literal null}.
     * @param timeout must not be {@literal null}.
     * @return can be {@literal null}.
     * @since 2.3
     * @see <a href="https://redis.io/commands/brpop">Redis Documentation: BRPOP</a>
     */
    @Nullable
    public V rightPop(String key, Duration timeout) {
        return Convert.convert(clz, delegate.rightPop(key, timeout));
    }

    /**
     * Remove the last element from list at {@code sourceKey}, append it to {@code destinationKey} and return its value.
     *
     * @param sourceKey must not be {@literal null}.
     * @param destinationKey must not be {@literal null}.
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/rpoplpush">Redis Documentation: RPOPLPUSH</a>
     */
    @Nullable
    public V rightPopAndLeftPush(String sourceKey, String destinationKey) {
        return Convert.convert(clz, delegate.rightPopAndLeftPush(sourceKey, destinationKey));
    }

    /**
     * Remove the last element from list at {@code sourceKey}, append it to {@code destinationKey} and return its value.<br>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param sourceKey must not be {@literal null}.
     * @param destinationKey must not be {@literal null}.
     * @param timeout
     * @param unit must not be {@literal null}.
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/brpoplpush">Redis Documentation: BRPOPLPUSH</a>
     */
    @Nullable
    public V rightPopAndLeftPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit) {
        return Convert.convert(clz, delegate.rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit));
    }

    /**
     * Remove the last element from list at {@code sourceKey}, append it to {@code destinationKey} and return its value.<br>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param sourceKey must not be {@literal null}.
     * @param destinationKey must not be {@literal null}.
     * @param timeout must not be {@literal null}.
     * @return can be {@literal null}.
     * @throws IllegalArgumentException if the timeout is {@literal null} or negative.
     * @since 2.3
     * @see <a href="https://redis.io/commands/brpoplpush">Redis Documentation: BRPOPLPUSH</a>
     */
    @Nullable
    public  V rightPopAndLeftPush(String sourceKey, String destinationKey, Duration timeout) {
        return Convert.convert(clz, delegate.rightPopAndLeftPush(sourceKey, destinationKey, timeout));
    }

}
