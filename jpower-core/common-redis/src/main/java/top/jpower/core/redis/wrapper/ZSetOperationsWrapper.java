package top.jpower.core.redis.wrapper;

import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ZSet 操作
 *
 * @author mr.g
 * @date 2024/10/14 16:41
 */
@AllArgsConstructor
public class ZSetOperationsWrapper<V> {

    private final ZSetOperations<String, Object> delegate;
    private final Class<V> clz;

    /**
     * Add {@code value} to a sorted set at {@code key}, or update its {@code score} if it already exists.
     *
     * @param key must not be {@literal null}.
     * @param value the value.
     * @param score the score.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD</a>
     */
    public Boolean add(String key, V value, double score) {
        return delegate.add(key, value, score);
    }

    /**
     * Add {@code value} to a sorted set at {@code key} if it does not already exists.
     *
     * @param key must not be {@literal null}.
     * @param value the value.
     * @param score the score.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.5
     * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD NX</a>
     */
    public Boolean addIfAbsent(String key, V value, double score) {
        return delegate.addIfAbsent(key, value, score);
    }

    /**
     * Add {@code tuples} to a sorted set at {@code key}, or update its {@code score} if it already exists.
     *
     * @param key must not be {@literal null}.
     * @param tuples must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD</a>
     */
    public Long add(String key, Set<ZSetOperations.TypedTuple<V>> tuples) {
        return delegate.add(key, tuples.stream().map(tp-> ZSetOperations.TypedTuple.<Object>of(tp.getValue(), tp.getScore())).collect(Collectors.toSet()));
    }

    /**
     * Add {@code tuples} to a sorted set at {@code key} if it does not already exists.
     *
     * @param key must not be {@literal null}.
     * @param tuples must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.5
     * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD NX</a>
     */
    public Long addIfAbsent(String key, Set<ZSetOperations.TypedTuple<V>> tuples) {
        return delegate.addIfAbsent(key, tuples.stream().map(tp-> ZSetOperations.TypedTuple.<Object>of(tp.getValue(), tp.getScore())).collect(Collectors.toSet()));
    }

    /**
     * Remove {@code values} from sorted set. Return number of removed elements.
     *
     * @param key must not be {@literal null}.
     * @param values must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrem">Redis Documentation: ZREM</a>
     */
    public Long remove(String key, Object... values) {
        return delegate.remove(key, values);
    }

    /**
     * Increment the score of element with {@code value} in sorted set by {@code increment}.
     *
     * @param key must not be {@literal null}.
     * @param value the value.
     * @param delta the delta to add. Can be negative.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zincrby">Redis Documentation: ZINCRBY</a>
     */
    public Double incrementScore(String key, V value, double delta) {
        return delegate.incrementScore(key, value, delta);
    }

    /**
     * Get random element from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
     */
    public V randomMember(String key) {
        return Convert.convert(clz, delegate.randomMember(key));
    }

    /**
     * Get {@code count} distinct random elements from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count number of members to return.
     * @return empty {@link Set} if {@code key} does not exist.
     * @throws IllegalArgumentException if count is negative.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
     */
    public Set<V> distinctRandomMembers(String key, long count) {
        return Convert.toSet(clz, delegate.distinctRandomMembers(key, count));
    }

    /**
     * Get {@code count} random elements from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count number of members to return.
     * @return empty {@link List} if {@code key} does not exist or {@literal null} when used in pipeline / transaction.
     * @throws IllegalArgumentException if count is negative.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
     */

    public List<V> randomMembers(String key, long count) {
        return Convert.toList(clz, delegate.randomMembers(key, count));
    }

    /**
     * Get random element with its score from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
     */
    public ZSetOperations.TypedTuple<V> randomMemberWithScore(String key) {
        ZSetOperations.TypedTuple<Object> typedTuple = delegate.randomMemberWithScore(key);
        return ZSetOperations.TypedTuple.of(Convert.convert(clz, typedTuple.getValue()), typedTuple.getScore());
    }

    /**
     * Get {@code count} distinct random elements with their score from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count number of members to return.
     * @return empty {@link Set} if {@code key} does not exist.
     * @throws IllegalArgumentException if count is negative.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
     */

    public Set<ZSetOperations.TypedTuple<V>> distinctRandomMembersWithScore(String key, long count) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.distinctRandomMembersWithScore(key, count);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
            ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Get {@code count} random elements with their score from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count number of members to return.
     * @return empty {@link List} if {@code key} does not exist or {@literal null} when used in pipeline / transaction.
     * @throws IllegalArgumentException if count is negative.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zrandmember">Redis Documentation: ZRANDMEMBER</a>
     */
    public List<ZSetOperations.TypedTuple<V>> randomMembersWithScore(String key, long count) {
        List<ZSetOperations.TypedTuple<Object>> tuples = delegate.randomMembersWithScore(key, count);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toList());
    }

    /**
     * Determine the index of element with {@code value} in a sorted set.
     *
     * @param key must not be {@literal null}.
     * @param o the value.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrank">Redis Documentation: ZRANK</a>
     */
    public Long rank(String key, Object o) {
        return delegate.rank(key, o);
    }

    /**
     * Determine the index of element with {@code value} in a sorted set when scored high to low.
     *
     * @param key must not be {@literal null}.
     * @param o the value.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrevrank">Redis Documentation: ZREVRANK</a>
     */
    public Long reverseRank(String key, Object o) {
        return delegate.reverseRank(key, o);
    }

    /**
     * Get elements between {@code start} and {@code end} from sorted set.
     *
     * @param key must not be {@literal null}.
     * @param start
     * @param end
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     */
    public Set<V> range(String key, long start, long end) {
        return Convert.toSet(clz, delegate.range(key, start, end));
    }

    /**
     * Get set of {@link RedisZSetCommands.Tuple}s between {@code start} and {@code end} from sorted set.
     *
     * @param key must not be {@literal null}.
     * @param start
     * @param end
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> rangeWithScores(String key, long start, long end) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.rangeWithScores(key, start, end);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Get elements where score is between {@code min} and {@code max} from sorted set.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     */
    public Set<V> rangeByScore(String key, double min, double max) {
        return Convert.toSet(clz, delegate.rangeByScore(key, min, max));
    }

    /**
     * Get set of {@link RedisZSetCommands.Tuple}s where score is between {@code min} and {@code max} from sorted set.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(String key, double min, double max) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.rangeByScoreWithScores(key, min, max);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Get elements in range from {@code start} to {@code end} where score is between {@code min} and {@code max} from
     * sorted set.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     */
    public Set<V> rangeByScore(String key, double min, double max, long offset, long count) {
        return Convert.toSet(clz, delegate.rangeByScore(key, min, max, offset, count));
    }

    /**
     * Get set of {@link RedisZSetCommands.Tuple}s in range from {@code start} to {@code end} where score is between {@code min} and
     * {@code max} from sorted set.
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.rangeByScoreWithScores(key, min, max, offset, count);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Get elements in range from {@code start} to {@code end} from sorted set ordered from high to low.
     *
     * @param key must not be {@literal null}.
     * @param start
     * @param end
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
     */
    public Set<V> reverseRange(String key, long start, long end) {
        return Convert.toSet(clz, delegate.reverseRange(key, start, end));
    }

    /**
     * Get set of {@link RedisZSetCommands.Tuple}s in range from {@code start} to {@code end} from sorted set ordered from high to low.
     *
     * @param key must not be {@literal null}.
     * @param start
     * @param end
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> reverseRangeWithScores(String key, long start, long end) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.reverseRangeWithScores(key, start, end);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Get elements where score is between {@code min} and {@code max} from sorted set ordered from high to low.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     */
    public Set<V> reverseRangeByScore(String key, double min, double max) {
        return Convert.toSet(clz, delegate.reverseRangeByScore(key, min, max));
    }

    /**
     * Get set of {@link RedisZSetCommands.Tuple} where score is between {@code min} and {@code max} from sorted set ordered from high to
     * low.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(String key, double min, double max) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.reverseRangeByScoreWithScores(key, min, max);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Get elements in range from {@code start} to {@code end} where score is between {@code min} and {@code max} from
     * sorted set ordered high -> low.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     */
    public Set<V> reverseRangeByScore(String key, double min, double max, long offset, long count) {
        return Convert.toSet(clz, delegate.reverseRangeByScore(key, min, max, offset, count));
    }

    /**
     * Get set of {@link RedisZSetCommands.Tuple} in range from {@code start} to {@code end} where score is between {@code min} and
     * {@code max} from sorted set ordered high -> low.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.reverseRangeByScoreWithScores(key, min, max, offset, count);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Count number of elements within sorted set with scores between {@code min} and {@code max}.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zcount">Redis Documentation: ZCOUNT</a>
     */
    public Long count(String key, double min, double max) {
        return delegate.count(key, min, max);
    }

    /**
     * Count number of elements within sorted set with value between {@code Range#min} and {@code Range#max} applying
     * lexicographical ordering.
     *
     * @param key must not be {@literal null}.
     * @param range must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.4
     * @see <a href="https://redis.io/commands/zlexcount">Redis Documentation: ZLEXCOUNT</a>
     */
    public Long lexCount(String key, RedisZSetCommands.Range range) {
        return delegate.lexCount(key, range);
    }

    /**
     * Remove and return the value with its score having the lowest score from sorted set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when the sorted set is empty or used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zpopmin">Redis Documentation: ZPOPMIN</a>
     * @since 2.6
     */
    public ZSetOperations.TypedTuple<V> popMin(String key) {
        ZSetOperations.TypedTuple<Object> tuple = delegate.popMin(key);
        return ZSetOperations.TypedTuple.of(Convert.convert(clz, tuple.getValue()), tuple.getScore());
    }

    /**
     * Remove and return {@code count} values with their score having the lowest score from sorted set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count number of elements to pop.
     * @return {@literal null} when the sorted set is empty or used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zpopmin">Redis Documentation: ZPOPMIN</a>
     * @since 2.6
     */
    public Set<ZSetOperations.TypedTuple<V>> popMin(String key, long count) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.popMin(key, count);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Remove and return the value with its score having the lowest score from sorted set at {@code key}. <br />
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param key must not be {@literal null}.
     * @param timeout
     * @param unit must not be {@literal null}.
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/bzpopmin">Redis Documentation: BZPOPMIN</a>
     * @since 2.6
     */
    public ZSetOperations.TypedTuple<V> popMin(String key, long timeout, TimeUnit unit) {
        ZSetOperations.TypedTuple<Object> tuple = delegate.popMin(key, timeout, unit);
        return ZSetOperations.TypedTuple.of(Convert.convert(clz, tuple.getValue()), tuple.getScore());
    }

    /**
     * Remove and return the value with its score having the lowest score from sorted set at {@code key}. <br />
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param key must not be {@literal null}.
     * @param timeout must not be {@literal null}.
     * @return can be {@literal null}.
     * @throws IllegalArgumentException if the timeout is {@literal null} or negative.
     * @see <a href="https://redis.io/commands/bzpopmin">Redis Documentation: BZPOPMIN</a>
     * @since 2.6
     */
    public ZSetOperations.TypedTuple<V> popMin(String key, Duration timeout) {
        ZSetOperations.TypedTuple<Object> tuple = delegate.popMin(key, timeout);
        return ZSetOperations.TypedTuple.of(Convert.convert(clz, tuple.getValue()), tuple.getScore());
    }

    /**
     * Remove and return the value with its score having the highest score from sorted set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when the sorted set is empty or used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zpopmax">Redis Documentation: ZPOPMAX</a>
     * @since 2.6
     */
    public ZSetOperations.TypedTuple<V> popMax(String key) {
        ZSetOperations.TypedTuple<Object> tuple = delegate.popMax(key);
        return ZSetOperations.TypedTuple.of(Convert.convert(clz, tuple.getValue()), tuple.getScore());
    }

    /**
     * Remove and return {@code count} values with their score having the highest score from sorted set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param count number of elements to pop.
     * @return {@literal null} when the sorted set is empty or used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zpopmax">Redis Documentation: ZPOPMAX</a>
     * @since 2.6
     */
    public Set<ZSetOperations.TypedTuple<V>> popMax(String key, long count) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.popMax(key, count);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Remove and return the value with its score having the highest score from sorted set at {@code key}. <br />
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param key must not be {@literal null}.
     * @param timeout
     * @param unit must not be {@literal null}.
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/bzpopmax">Redis Documentation: BZPOPMAX</a>
     * @since 2.6
     */
    public ZSetOperations.TypedTuple<V> popMax(String key, long timeout, TimeUnit unit) {
        ZSetOperations.TypedTuple<Object> tuple = delegate.popMax(key, timeout, unit);
        return ZSetOperations.TypedTuple.of(Convert.convert(clz, tuple.getValue()), tuple.getScore());
    }

    /**
     * Remove and return the value with its score having the highest score from sorted set at {@code key}. <br />
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param key must not be {@literal null}.
     * @param timeout must not be {@literal null}.
     * @return can be {@literal null}.
     * @throws IllegalArgumentException if the timeout is {@literal null} or negative.
     * @see <a href="https://redis.io/commands/bzpopmax">Redis Documentation: BZPOPMAX</a>
     * @since 2.6
     */
    public ZSetOperations.TypedTuple<V> popMax(String key, Duration timeout) {
        ZSetOperations.TypedTuple<Object> tuple = delegate.popMax(key, timeout);
        return ZSetOperations.TypedTuple.of(Convert.convert(clz, tuple.getValue()), tuple.getScore());
    }

    /**
     * Returns the number of elements of the sorted set stored with given {@code key}.
     *
     * @see #zCard(String)
     * @param key
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zcard">Redis Documentation: ZCARD</a>
     */
    public Long size(String key) {
        return delegate.size(key);
    }

    /**
     * Get the size of sorted set with {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 1.3
     * @see <a href="https://redis.io/commands/zcard">Redis Documentation: ZCARD</a>
     */

    public Long zCard(String key) {
        return delegate.zCard(key);
    }

    /**
     * Get the score of element with {@code value} from sorted set with key {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param o the value.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zscore">Redis Documentation: ZSCORE</a>
     */
    public Double score(String key, Object o) {
        return delegate.score(key, o);
    }

    /**
     * Get the scores of elements with {@code values} from sorted set with key {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param o the values.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zmscore">Redis Documentation: ZMSCORE</a>
     * @since 2.6
     */
    public List<Double> score(String key, Object... o) {
        return delegate.score(key, o);
    }

    /**
     * Remove elements in range between {@code start} and {@code end} from sorted set with {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param start
     * @param end
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zremrangebyrank">Redis Documentation: ZREMRANGEBYRANK</a>
     */
    public Long removeRange(String key, long start, long end) {
        return delegate.removeRange(key, start, end);
    }

    /**
     * Remove elements with scores between {@code min} and {@code max} from sorted set with {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zremrangebyscore">Redis Documentation: ZREMRANGEBYSCORE</a>
     */
    public Long removeRangeByScore(String key, double min, double max) {
        return delegate.removeRangeByScore(key, min, max);
    }

    /**
     * Diff sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zdiff">Redis Documentation: ZDIFF</a>
     */
    public Set<V> difference(String key, String otherKey) {
        return Convert.toSet(clz, delegate.difference(key, otherKey));
    }

    /**
     * Diff sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zdiff">Redis Documentation: ZDIFF</a>
     */
    public Set<V> difference(String key, Collection<String> otherKeys) {
        return Convert.toSet(clz, delegate.difference(key, otherKeys));
    }

    /**
     * Diff sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zdiff">Redis Documentation: ZDIFF</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> differenceWithScores(String key, String otherKey) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.differenceWithScores(key, otherKey);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Diff sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zdiff">Redis Documentation: ZDIFF</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> differenceWithScores(String key, Collection<String> otherKeys) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.differenceWithScores(key, otherKeys);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Diff sorted {@code sets} and store result in destination {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zdiffstore">Redis Documentation: ZDIFFSTORE</a>
     */
    public Long differenceAndStore(String key, Collection<String> otherKeys, String destKey) {
        return delegate.differenceAndStore(key, otherKeys, destKey);
    }

    /**
     * Intersect sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
     */
    public Set<V> intersect(String key, String otherKey) {
        return Convert.toSet(clz, delegate.intersect(key, otherKey));
    }

    /**
     * Intersect sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
     */
    public Set<V> intersect(String key, Collection<String> otherKeys) {
        return Convert.toSet(clz, delegate.intersect(key, otherKeys));
    }

    /**
     * Intersect sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> intersectWithScores(String key, String otherKey) {
        return intersectWithScores(key, Collections.singleton(otherKey));
    }

    /**
     * Intersect sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> intersectWithScores(String key, Collection<String> otherKeys) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.intersectWithScores(key, otherKeys);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Intersect sorted sets at {@code key} and {@code otherKeys} .
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param aggregate must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> intersectWithScores(String key, Collection<String> otherKeys, RedisZSetCommands.Aggregate aggregate) {
        return intersectWithScores(key, otherKeys, aggregate, RedisZSetCommands.Weights.fromSetCount(1 + otherKeys.size()));
    }

    /**
     * Intersect sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param aggregate must not be {@literal null}.
     * @param weights must not be {@literal null}.
     * @return
     * @since 2.6
     * @see <a href="https://redis.io/commands/zinter">Redis Documentation: ZINTER</a>
     */

    public Set<ZSetOperations.TypedTuple<V>> intersectWithScores(String key, Collection<String> otherKeys, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.intersectWithScores(key, otherKeys, aggregate, weights);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Intersect sorted sets at {@code key} and {@code otherKey} and store result in destination {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     */
    public Long intersectAndStore(String key, String otherKey, String destKey) {
        return delegate.intersectAndStore(key, otherKey, destKey);
    }

    /**
     * Intersect sorted sets at {@code key} and {@code otherKeys} and store result in destination {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     */
    public Long intersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return delegate.intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * Intersect sorted sets at {@code key} and {@code otherKeys} and store result in destination {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @param aggregate must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.1
     * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     */
    public Long intersectAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate) {
        return intersectAndStore(key, otherKeys, destKey, aggregate, RedisZSetCommands.Weights.fromSetCount(1 + otherKeys.size()));
    }

    /**
     * Intersect sorted sets at {@code key} and {@code otherKeys} and store result in destination {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @param aggregate must not be {@literal null}.
     * @param weights must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.1
     * @see <a href="https://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     */
    public Long intersectAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return delegate.intersectAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    /**
     * Union sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
     */
    public Set<V> union(String key, String otherKey) {
        return union(key, Collections.singleton(otherKey));
    }

    /**
     * Union sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
     */
    public Set<V> union(String key, Collection<String> otherKeys) {
        return Convert.toSet(clz, delegate.union(key, otherKeys));
    }

    /**
     * Union sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> unionWithScores(String key, String otherKey) {
        return unionWithScores(key, Collections.singleton(otherKey));
    }

    /**
     * Union sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> unionWithScores(String key, Collection<String> otherKeys) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.unionWithScores(key, otherKeys);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Union sorted sets at {@code key} and {@code otherKeys} .
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param aggregate must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
     */
    public Set<ZSetOperations.TypedTuple<V>> unionWithScores(String key, Collection<String> otherKeys, RedisZSetCommands.Aggregate aggregate) {
        return unionWithScores(key, otherKeys, aggregate, RedisZSetCommands.Weights.fromSetCount(1 + otherKeys.size()));
    }

    /**
     * Union sorted {@code sets}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param aggregate must not be {@literal null}.
     * @param weights must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/zunion">Redis Documentation: ZUNION</a>
     */

    public Set<ZSetOperations.TypedTuple<V>> unionWithScores(String key, Collection<String> otherKeys, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = delegate.unionWithScores(key, otherKeys, aggregate, weights);
        if (tuples == null){
            return null;
        }
        return tuples.stream().map(tp ->
                ZSetOperations.TypedTuple.of(Convert.convert(clz, tp.getValue()), tp.getScore())
        ).collect(Collectors.toSet());
    }

    /**
     * Union sorted sets at {@code key} and {@code otherKeys} and store result in destination {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKey must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     */
    public Long unionAndStore(String key, String otherKey, String destKey) {
        return delegate.unionAndStore(key, otherKey, destKey);
    }

    /**
     * Union sorted sets at {@code key} and {@code otherKeys} and store result in destination {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     */
    public Long unionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return delegate.unionAndStore(key, otherKeys, destKey);
    }

    /**
     * Union sorted sets at {@code key} and {@code otherKeys} and store result in destination {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @param aggregate must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.1
     * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     */
    public Long unionAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate) {
        return unionAndStore(key, otherKeys, destKey, aggregate, RedisZSetCommands.Weights.fromSetCount(1 + otherKeys.size()));
    }

    /**
     * Union sorted sets at {@code key} and {@code otherKeys} and store result in destination {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey must not be {@literal null}.
     * @param aggregate must not be {@literal null}.
     * @param weights must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.1
     * @see <a href="https://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     */
    public Long unionAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return delegate.unionAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    /**
     * Use a {@link Cursor} to iterate over entries zset at {@code key}. <br />
     * <strong>Important:</strong> Call {@link Cursor#close()} when done to avoid resource leaks.
     *
     * @param key
     * @param options must not be {@literal null}.
     * @return the result cursor providing access to the scan result. Must be closed once fully processed (e.g. through a
     *         try-with-resources clause).
     * @see <a href="https://redis.io/commands/zscan">Redis Documentation: ZSCAN</a>
     * @since 1.4
     */
    public Cursor<ZSetOperations.TypedTuple<V>> scan(String key, ScanOptions options) {
        return new ConvertingCursor<>(delegate.scan(key, options), tuple ->
            ZSetOperations.TypedTuple.of(Convert.convert(clz, tuple.getValue()), tuple.getScore())
        );
    }

    /**
     * Get all elements with lexicographical ordering from {@literal ZSET} at {@code key} with a value between
     * {@link RedisZSetCommands.Range#getMin()} and {@link RedisZSetCommands.Range#getMax()}.
     *
     * @param key must not be {@literal null}.
     * @param range must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 1.7
     * @see <a href="https://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
     */
    public Set<V> rangeByLex(String key, RedisZSetCommands.Range range) {
        return rangeByLex(key, range, RedisZSetCommands.Limit.unlimited());
    }

    /**
     * Get all elements {@literal n} elements, where {@literal n = } {@link RedisZSetCommands.Limit#getCount()}, starting at
     * {@link RedisZSetCommands.Limit#getOffset()} with lexicographical ordering from {@literal ZSET} at {@code key} with a value between
     * {@link RedisZSetCommands.Range#getMin()} and {@link RedisZSetCommands.Range#getMax()}.
     *
     * @param key must not be {@literal null}
     * @param range must not be {@literal null}.
     * @param limit can be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 1.7
     * @see <a href="https://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
     */
    public Set<V> rangeByLex(String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return Convert.toSet(clz, delegate.rangeByLex(key, range, limit));
    }

    /**
     * Get all elements with reverse lexicographical ordering from {@literal ZSET} at {@code key} with a value between
     * {@link RedisZSetCommands.Range#getMin()} and {@link RedisZSetCommands.Range#getMax()}.
     *
     * @param key must not be {@literal null}.
     * @param range must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.4
     * @see <a href="https://redis.io/commands/zrevrangebylex">Redis Documentation: ZREVRANGEBYLEX</a>
     */
    public Set<V> reverseRangeByLex(String key, RedisZSetCommands.Range range) {
        return reverseRangeByLex(key, range, RedisZSetCommands.Limit.unlimited());
    }

    /**
     * Get all elements {@literal n} elements, where {@literal n = } {@link RedisZSetCommands.Limit#getCount()}, starting at
     * {@link RedisZSetCommands.Limit#getOffset()} with reverse lexicographical ordering from {@literal ZSET} at {@code key} with a value
     * between {@link RedisZSetCommands.Range#getMin()} and {@link RedisZSetCommands.Range#getMax()}.
     *
     * @param key must not be {@literal null}
     * @param range must not be {@literal null}.
     * @param limit can be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @since 2.4
     * @see <a href="https://redis.io/commands/zrevrangebylex">Redis Documentation: ZREVRANGEBYLEX</a>
     */
    public Set<V> reverseRangeByLex(String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return Convert.toSet(clz, delegate.reverseRangeByLex(key, range, limit));
    }

}
