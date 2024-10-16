package top.jpower.core.redis.service;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import top.jpower.core.redis.wrapper.*;
import top.jpower.core.util.utils.ExceptionUtil;
import top.jpower.core.util.utils.Fc;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * redis服务
 *
 * @author mr.g
 **/
@Slf4j
@AllArgsConstructor
public class RedisService {

    /**
     * RedisTemplate
     **/
    @Getter
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 分布式锁实现工具类
     **/
    private final LockOperations lock;
    /**
     * 分布式锁实现工具类
     **/
    private final QueueOperations<Object> queue;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        queue = new QueueOperations<>(this.redisTemplate, Object.class);
        lock = new LockOperations(this.redisTemplate);
    }

    /**
     * 分布式锁
     *
     * @author mr.g
     * @return 分布式锁
     **/
    public LockOperations lockOps(){
        return lock;
    }

    /**
     *
     *
     * @author mr.g
     * @return top.jpower.core.redis.wrapper.QueueOperations<java.lang.Object>
     **/
    public QueueOperations<Object> queueOps() {
        return queue;
    }

    /**
     * 消息队列
     *
     * @author mr.g
     * @param clz 消息类型
     * @return top.jpower.core.redis.wrapper.QueueOperations<T>
     **/
    public <T> QueueOperations<T> queueOps(Class<T> clz) {
        return new QueueOperations<>(redisTemplate, clz);
    }

    /**
     * Value操作
     *
     * @author mr.g
     * @return org.springframework.data.redis.core.ValueOperations<java.lang.String,java.lang.Object>
     **/
    public ValueOperations<String, Object> valueOps(){
        return redisTemplate.opsForValue();
    }

    /**
     * Value操作
     *
     * @author mr.g
     * @param clz 值类型
     * @return top.jpower.core.redis.wrapper.ValueOperationsWrapper<T>
     **/
    public <T> ValueOperationsWrapper<T> valueOps(Class<T> clz){
        return new ValueOperationsWrapper<>(redisTemplate.opsForValue(), clz);
    }

    /**
     * List操作
     *
     * @author mr.g
     * @return org.springframework.data.redis.core.ListOperations<java.lang.String,java.lang.Object>
     **/
    public ListOperations<String, Object> listOps(){
        return redisTemplate.opsForList();
    }

    /**
     * List操作
     *
     * @author mr.g
     * @return org.springframework.data.redis.core.ListOperations<java.lang.String,java.lang.Object>
     **/
    public <T> ListOperationsWrapper<T> listOps(Class<T> clz){
        return new ListOperationsWrapper<>(redisTemplate.opsForList(), clz);
    }

    /**
     * Set操作
     *
     * @author mr.g
     * @return Set操作
     **/
    public SetOperations<String, Object> setOps(){
        return redisTemplate.opsForSet();
    }

    /**
     * Set操作
     *
     * @author mr.g
     * @return Set操作
     **/
    public <T> SetOperationsWrapper<T> setOps(Class<T> clz){
        return new SetOperationsWrapper<>(redisTemplate.opsForSet(), clz);
    }

    /**
     * ZSet操作
     *
     * @author mr.g
     * @return ZSet操作
     **/
    public ZSetOperations<String, Object> zSetOps(){
        return redisTemplate.opsForZSet();
    }

    /**
     * ZSet操作
     *
     * @author mr.g
     * @return ZSet操作
     **/
    public <T> ZSetOperationsWrapper<T> zSetOps(Class<T> clz){
        return new ZSetOperationsWrapper<>(redisTemplate.opsForZSet(), clz);
    }

    /**
     * geo 命令的 Redis 操作。
     *
     * @author mr.g
     * @param
     * @return org.springframework.data.redis.core.GeoOperations<java.lang.String,java.lang.Object>
     **/
    public GeoOperations<String, Object> geoOps(){
        return redisTemplate.opsForGeo();
    }

    /**
     * geo 命令的 Redis 操作。
     *
     * @author mr.g
     * @param
     * @return org.springframework.data.redis.core.GeoOperations<java.lang.String,java.lang.Object>
     **/
    public <T> GeoOperationsWrapper<T> geoOps(Class<T> clz){
        return new GeoOperationsWrapper<>(redisTemplate.opsForGeo(), clz);
    }

    /**
     * hll操作
     *
     * @author mr.g
     * @return HyperLogLogOperations
     **/
    public HyperLogLogOperations<String, Object> hllOps(){
        return redisTemplate.opsForHyperLogLog();
    }

    /**
     * hll操作
     *
     * @author mr.g
     * @return HyperLogLogOperationsWrapper
     **/
    public <T> HyperLogLogOperationsWrapper<T> hllOps(Class<T> clz){
        return new HyperLogLogOperationsWrapper<>(redisTemplate.opsForHyperLogLog(), clz);
    }

    /**
     * cluster操作
     *
     * @author mr.g
     * @return ClusterOperations
     **/
    public ClusterOperations<String, Object> clusterOps(){
        return redisTemplate.opsForCluster();
    }

    /**
     * stream操作
     *
     * @author mr.g
     * @return ClusterOperations
     **/
    public <HV> StreamOperations<String, String, HV> streamOps(){
        return redisTemplate.opsForStream();
    }

    /**
     * hash操作
     *
     * @author mr.g
     * @return HashOperations
     **/
    public HashOperations<String, String, Object> hashOps(){
        return redisTemplate.opsForHash();
    }

    /**
     * hash操作
     *
     * @author mr.g
     * @return HashOperations
     **/
    public <T> HashOperationsWrapper<T> hashOps(Class<T> clz){
        return new HashOperationsWrapper<>(redisTemplate.opsForHash(), clz);
    }

    /**
     * 拷贝 Key
     *
     * @author mr.g
     * @param source 源KEY
     * @param target 目标KEY
     * @param replace 是否覆盖
     * @return 是否必须
     **/
    public Boolean copy(String source, String target, boolean replace) {
        return redisTemplate.copy(source, target, replace);
    }

    /**
     * 删除
     *
     * @author mr.g
     * @param key key
     * @return 是否成功
     **/
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除
     *
     * @author mr.g
     * @param keys KEY
     * @return 是否成功
     **/
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 删除
     *
     * @param keys KEYS
     */
    public Long delete(final String... keys) {
        return delete(ListUtil.toList(keys));
    }

    /**
     * 批量删除key
     *
     * @param pattern 匹配条件
     */
    public Long deletePattern(final String pattern) {
        Set<String> keys = keys(pattern);
        if (Fc.isNotEmpty(keys)) {
            return redisTemplate.delete(keys);
        }
        return 0L;
    }

    /**
     * 取消 与 keys keyspace 的链接。 del(byte[]...) 与实际内存回收不同，此处的回收是异步进行的
     *
     * @author mr.g
     * @param key KEY
     * @return 在 pipeline / transaction 中使用时为 null。
     **/
    public Boolean unlink(String key) {
        return redisTemplate.unlink(key);
    }

    /**
     * 取消 与 keys keyspace 的链接。 del(byte[]...) 与实际内存回收不同，此处的回收是异步进行的
     *
     * @author mr.g
     * @param keys KEYS
     * @return 在 pipeline / transaction 中使用时为 null。
     **/
    public Long unlink(Collection<String> keys) {
        return redisTemplate.unlink(keys);
    }

    /**
     * 是否存在KEY
     *
     * @author mr.g
     * @param key KEY
     * @return 是否存在
     **/
    public Boolean exist(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 是否存在KEY
     *
     * @author mr.g
     * @param keys KEYS
     * @return 存在的数量
     **/
    public Long countExistingKeys(Collection<String> keys) {
        return redisTemplate.countExistingKeys(keys);
    }

    /**
     * 设置KEY过期时间
     *
     * @author mr.g
     * @param key KEY
     * @param timeout 时长
     * @param unit 单位
     * @return 是否成功
     **/
    public Boolean expire(String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置KEY过期时间
     *
     * @author mr.g
     * @param key KEY
     * @param date 到期时间
     * @return 是否成功
     **/
    public Boolean expire(String key, final Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 获取过期时间
     *
     * @author mr.g
     * @param key KEY
     * @return 秒数
     **/
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 获取过期时间
     *
     * @author 郭丁志
     * @param key KEY
     * @param timeUnit 单位
     * @return 时长
     **/
    public Long getExpire(final String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 匹配KEY
     *
     * @author mr.g
     * @param pattern 匹配规则
     * @return KEYS
     **/
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 分页扫描KEY
     *
     * @author mr.g
     * @param options 匹配模式
     * @return KEYS
     **/
    public Cursor<String> scan(ScanOptions options) {
        return redisTemplate.scan(options);
    }

    /**
     * 分页扫描KEY
     *
     * @param pattern  表达式
     * @param consumer 对迭代到的key进行操作
     */
    public void scan(String pattern, Consumer<byte[]> consumer) {
        scan(pattern, 1000, consumer);
    }

    /**
     * 分页扫描KEY
     *
     * @param pattern  表达式
     * @param consumer 对迭代到的key进行操作
     */
    public void scan(String pattern, int count, Consumer<byte[]> consumer) {
        redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(count).match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return true;
            } catch (Exception e) {
                log.error("分页操作失败===>{}", ExceptionUtil.getStackTraceAsString(e));
                return false;
            }
        });
    }

    /**
     * 删除KEY的过期时间
     * @author mr.g
     * @param key KEY
     * @return 是否成功
     **/
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 移动KEY到其他数据库
     *
     * @author mr.g
     * @param key KEY
     * @param dbIndex 数据库
     * @return 是否成功
     **/
    public Boolean move(String key, final int dbIndex) {
        return redisTemplate.move(key, dbIndex);
    }

    /**
     * 随机获取一个KEY
     * @author mr.g
     * @return KEY
     **/
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    /**
     * 重命名KEY
     *
     * @author mr.g
     * @param oldKey 旧KEY
     * @param newKey 新KEY
     **/
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 重命名KEY
     *
     * @author mr.g
     * @param oldKey 旧KEY
     * @param newKey 新KEY
     * @return 是否成功
     **/
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

}
