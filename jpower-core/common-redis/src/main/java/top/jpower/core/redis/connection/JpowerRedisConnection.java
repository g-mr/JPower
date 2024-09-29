package top.jpower.core.redis.connection;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.*;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.connection.convert.ListConverter;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.KeyScanOptions;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.data.redis.domain.geo.GeoShape;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import top.jpower.core.redis.config.RedisPrefixHandler;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * redis 连接器
 *
 * @author mr.g
 */
@Slf4j
public class JpowerRedisConnection implements RedisConnection {

    private final RedisConnection delegate;

    private final RedisProperties redisProperties;
    private final RedisPrefixHandler redisPrefixHandler;
    private final RedisSerializer<String> serializer;

    @SuppressWarnings("rawtypes") private final Queue<Converter> pipelineConverters = new LinkedList<>();
    @SuppressWarnings("rawtypes") private final Queue<Converter> txConverters = new LinkedList<>();

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Setter
    private boolean deserializePipelineAndTxResults = false;

    public JpowerRedisConnection(RedisConnection connection, RedisProperties redisProperties, RedisPrefixHandler redisPrefixHandler) {
        this(connection, redisProperties, redisPrefixHandler, RedisSerializer.string());
    }

    public JpowerRedisConnection(RedisConnection connection, RedisProperties redisProperties, RedisPrefixHandler redisPrefixHandler, RedisSerializer<String> redisSerializer) {
        this.delegate = connection;
        this.redisProperties = redisProperties;
        this.redisPrefixHandler = redisPrefixHandler;
        this.serializer = redisSerializer;
    }

    /**
     * 是否开启前缀
     *
     * @author mr.g
     * @return 是否开启
     **/
    private boolean enabledPrefix(){
        return redisProperties.getPrefix().getEnabled() && Fc.notNull(redisPrefixHandler);
    }

    /**
     * 是否是扫描前缀
     *
     * @author mr.g
     * @param isAppend 是否拼接
     * @param key 缓存KEY
     * @return 是否扫描前缀
     **/
    private boolean prefixForScan(boolean isAppend, String key){
        if (isAppend && redisPrefixHandler.ignorePrefixForScan(key)){
            return StringUtil.isNotBlank(redisPrefixHandler.getPrefix(key));
        } else {
            return false;
        }
    }

    /**
     * 添加前缀
     * @author mr.g
     * @param isAppend 是否拼接
     * @param keys 缓存KEY
     * @return 缓存KEY
     **/
    private byte[][] addPrefix(boolean isAppend, byte[]... keys){
        if (enabledPrefix()){
            for (int i = 0; i < keys.length; i++) {
                byte[] key = keys[i]; // 获取当前键
                if (redisProperties.getPrefix().getIgnore().stream().noneMatch(pattern -> antPathMatcher.match(pattern, Objects.requireNonNull(serializer.deserialize(key), "non null key required")))){
                    keys[i] = appendPrefix(isAppend, key);
                }
            }
        }

        return keys;
    }

    /**
     * 拼接前缀
     * @author mr.g
     * @param isAppend 是否拼接
     * @param key 缓存KEY
     * @return 缓存KEY
     **/
    private byte[] appendPrefix(boolean isAppend,byte[] key){
        String keyStr = serializer.deserialize(key);
        if (prefixForScan(isAppend, keyStr)){
            return serializer.serialize(StringPool.ASTERISK+keyStr);
        } else {
            return serializer.serialize(getPrefix(redisPrefixHandler.getPrefix(keyStr))+keyStr);
        }
    }

    /**
     * 获取前缀
     *
     * @author mr.g
     * @param prefix 前缀
     * @return 前缀
     **/
    private String getPrefix(String prefix){
        if (StringUtil.isBlank(prefix)){
            return StringPool.EMPTY;
        }
        return prefix+StringPool.COLON;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#append(byte[], byte[])
     */
    @Override
    public Long append(byte[] key, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.append(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#bgSave()
     */
    @Override
    public void bgSave() {
        delegate.bgSave();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#bgReWriteAof()
     */
    @Override
    public void bgReWriteAof() {
        delegate.bgReWriteAof();
    }

    /**
     * @deprecated As of 1.3, use {@link #bgReWriteAof}.
     */
    @Deprecated
    @Override
    public void bgWriteAof() {
        bgReWriteAof();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#bLPop(int, byte[][])
     */
    @Override
    public List<byte[]> bLPop(int timeout, byte[]... keys) {
        keys = addPrefix(Boolean.FALSE, keys);
        return convertAndReturn(delegate.bLPop(timeout, keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#bRPop(int, byte[][])
     */
    @Override
    public List<byte[]> bRPop(int timeout, byte[]... keys) {
        keys = addPrefix(Boolean.FALSE, keys);
        return convertAndReturn(delegate.bRPop(timeout, keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#bRPopLPush(int, byte[], byte[])
     */
    @Override
    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        srcKey = addPrefix(Boolean.FALSE, srcKey)[0];
        dstKey = addPrefix(Boolean.FALSE, dstKey)[0];
        return convertAndReturn(delegate.bRPopLPush(timeout, srcKey, dstKey), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnection#close()
     */
    @Override
    public void close() throws RedisSystemException {
        delegate.close();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#copy(byte[], byte[], boolean)
     */
    @Override
    public Boolean copy(byte[] sourceKey, byte[] targetKey, boolean replace) {
        sourceKey = addPrefix(Boolean.FALSE, sourceKey)[0];
        targetKey = addPrefix(Boolean.FALSE, targetKey)[0];
        return convertAndReturn(delegate.copy(sourceKey, targetKey, replace), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#dbSize()
     */
    @Override
    public Long dbSize() {
        return convertAndReturn(delegate.dbSize(), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#decr(byte[])
     */
    @Override
    public Long decr(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.decr(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#decrBy(byte[], long)
     */
    @Override
    public Long decrBy(byte[] key, long value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.decrBy(key, value), Converters.identityConverter());
    }

    /**
     * 扫描所有的KEY
     *
     * @author mr.g
     * @param keys 键
     * @return 键
     **/
    private byte[][] scanAllForKey(byte[]... keys){
        keys = addPrefix(Boolean.TRUE, keys);

        Set<byte[]> keyList = new HashSet<>();
        for (byte[] key : keys){
            String keyForStr = serializer.deserialize(key);
            if (StringUtil.startWith(keyForStr, StringPool.ASTERISK)){
                Set<byte[]> set = convertAndReturn(delegate.keys(key), Converters.identityConverter());
                if (Fc.isNotEmpty(set)){
                    keyList.addAll(set);
                }
            } else {
                keyList.add(key);
            }
        }

        return keyList.toArray(new byte[0][]);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#del(byte[][])
     */
    @Override
    public Long del(byte[]... keys) {
        keys = scanAllForKey(keys);
        return convertAndReturn(delegate.del(keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#unlink(byte[][])
     */
    @Override
    public Long unlink(byte[]... keys) {
        keys = scanAllForKey(keys);
        return convertAndReturn(delegate.unlink(keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisTxCommands#discard()
     */
    @Override
    public void discard() {
        try {
            delegate.discard();
        } finally {
            txConverters.clear();
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnectionCommands#echo(byte[])
     */
    @Override
    public byte[] echo(byte[] message) {
        return convertAndReturn(delegate.echo(message), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisTxCommands#exec()
     */
    @Override
    @SuppressWarnings("rawtypes")
    public List<Object> exec() {

        try {
            List<Object> results = delegate.exec();
            if (isPipelined()) {
                pipelineConverters.add(new TransactionResultConverter(new LinkedList<>(txConverters)));
                return results;
            }
            return convertResults(results, txConverters);
        } finally {
            txConverters.clear();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<Object> convertResults(@Nullable List<Object> results, Queue<Converter> converters) {
        if (!deserializePipelineAndTxResults || results == null) {
            return results;
        }
        if (results.size() != converters.size()) {
            // Some of the commands were done directly on the delegate, don't attempt to convert
            log.warn("Delegate returned an unexpected number of results. Abandoning type conversion.");
            return results;
        }
        List<Object> convertedResults = new ArrayList<>(results.size());
        for (Object result : results) {

            Converter converter = converters.remove();
            convertedResults.add(result == null ? null : converter.convert(result));
        }
        return convertedResults;
    }

    @SuppressWarnings("rawtypes")
    private class TransactionResultConverter implements Converter<List<Object>, List<Object>> {
        private Queue<Converter> txConverters;

        public TransactionResultConverter(Queue<Converter> txConverters) {
            this.txConverters = txConverters;
        }

        @Override
        public List<Object> convert(List<Object> execResults) {
            return convertResults(execResults, txConverters);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#exists(byte[])
     */
    @Override
    public Boolean exists(byte[] key) {
        if (enabledPrefix()){
            byte[] ks = addPrefix(Boolean.TRUE, key)[0];
            if (StringUtil.startWith(serializer.deserialize(ks), StringPool.ASTERISK)){
                Set<byte[]> set = convertAndReturn(delegate.keys(ks), Converters.identityConverter());
                return set != null ? set.size() > 0 : null;
            } else {
                return convertAndReturn(delegate.exists(ks), Converters.identityConverter());
            }
        }

        return convertAndReturn(delegate.exists(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#exists(byte[][])
     */
    @Override
    public Long exists(byte[]... keys) {

        if (enabledPrefix()){
            byte[][] kss = addPrefix(Boolean.TRUE, keys);
            List<byte[]> keysForNo = new ArrayList<>();
            long count = 0L;
            for (byte[] ks: kss){
                if (StringUtil.startWith(serializer.deserialize(ks), StringPool.ASTERISK)){
                    Set<byte[]> set = convertAndReturn(delegate.keys(ks), Converters.identityConverter());
                    count = count + (set != null ? set.size() : 0);
                } else {
                    keysForNo.add(ks);
                }
            }

            if (Fc.isNotEmpty(keysForNo)){
                Long c = convertAndReturn(delegate.exists(keysForNo.toArray(new byte[0][])), Converters.identityConverter());
                count = count + Fc.toLong(c, 0);
            }

            return count;
        }

        return convertAndReturn(delegate.exists(keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#expire(byte[], long)
     */
    @Override
    public Boolean expire(byte[] key, long seconds) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.expire(key, seconds), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#expireAt(byte[], long)
     */
    @Override
    public Boolean expireAt(byte[] key, long unixTime) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.expireAt(key, unixTime), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#flushAll()
     */
    @Override
    public void flushAll() {
        delegate.flushAll();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#flushAll(org.springframework.data.redis.connection.RedisServerCommands.FlushOption)
     */
    @Override
    public void flushAll(FlushOption option) {
        delegate.flushAll(option);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#flushDb()
     */
    @Override
    public void flushDb() {
        delegate.flushDb();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#flushDb(org.springframework.data.redis.connection.RedisServerCommands.FlushOption)
     */
    @Override
    public void flushDb(FlushOption option) {
        delegate.flushDb(option);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#get(byte[])
     */
    @Override
    public byte[] get(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.get(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#getDel(byte[])
     */
    @Nullable
    @Override
    public byte[] getDel(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.getDel(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#get(byte[], org.springframework.data.redis.core.types.Expiration)
     */
    @Nullable
    @Override
    public byte[] getEx(byte[] key, Expiration expiration) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.getEx(key, expiration), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#getBit(byte[], long)
     */
    @Override
    public Boolean getBit(byte[] key, long offset) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.getBit(key, offset), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#getConfig(java.lang.String)
     */
    @Override
    public Properties getConfig(String pattern) {
        return convertAndReturn(delegate.getConfig(pattern), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnection#getNativeConnection()
     */
    @Override
    public Object getNativeConnection() {
        return convertAndReturn(delegate.getNativeConnection(), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#getRange(byte[], long, long)
     */
    @Override
    public byte[] getRange(byte[] key, long start, long end) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.getRange(key, start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#getSet(byte[], byte[])
     */
    @Override
    public byte[] getSet(byte[] key, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.getSet(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisPubSubCommands#getSubscription()
     */
    @Override
    public Subscription getSubscription() {
        return delegate.getSubscription();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hDel(byte[], byte[][])
     */
    @Override
    public Long hDel(byte[] key, byte[]... fields) {
        byte[][] keys = scanAllForKey(key);
        long count = 0;
        for (byte[] k : keys){
            count = count + Fc.toLong(convertAndReturn(delegate.hDel(k, fields), Converters.identityConverter()), 0);
        }
        return count;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hExists(byte[], byte[])
     */
    @Override
    public Boolean hExists(byte[] key, byte[] field) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.hExists(key, field), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hGet(byte[], byte[])
     */
    @Override
    public byte[] hGet(byte[] key, byte[] field) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.hGet(key, field), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hGetAll(byte[])
     */
    @Override
    public Map<byte[], byte[]> hGetAll(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.hGetAll(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hIncrBy(byte[], byte[], long)
     */
    @Override
    public Long hIncrBy(byte[] key, byte[] field, long delta) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.hIncrBy(key, field, delta), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hIncrBy(byte[], byte[], double)
     */
    @Override
    public Double hIncrBy(byte[] key, byte[] field, double delta) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.hIncrBy(key, field, delta), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hKeys(byte[])
     */
    @Override
    public Set<byte[]> hKeys(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.hKeys(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hLen(byte[])
     */
    @Override
    public Long hLen(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.hLen(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hMGet(byte[], byte[][])
     */
    @Override
    public List<byte[]> hMGet(byte[] key, byte[]... fields) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.hMGet(key, fields), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hMSet(byte[], java.util.Map)
     */
    @Override
    public void hMSet(byte[] key, Map<byte[], byte[]> hashes) {
        key = addPrefix(Boolean.FALSE, key)[0];
        delegate.hMSet(key, hashes);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hSet(byte[], byte[], byte[])
     */
    @Override
    public Boolean hSet(byte[] key, byte[] field, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.hSet(key, field, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hSetNX(byte[], byte[], byte[])
     */
    @Override
    public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.hSetNX(key, field, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hVals(byte[])
     */
    @Override
    public List<byte[]> hVals(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.hVals(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#incr(byte[])
     */
    @Override
    public Long incr(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.incr(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#incrBy(byte[], long)
     */
    @Override
    public Long incrBy(byte[] key, long value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.incrBy(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#incrBy(byte[], double)
     */
    @Override
    public Double incrBy(byte[] key, double value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.incrBy(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#info()
     */
    @Override
    public Properties info() {
        return convertAndReturn(delegate.info(), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#info(java.lang.String)
     */
    @Override
    public Properties info(String section) {
        return convertAndReturn(delegate.info(section), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnection#isClosed()
     */
    @Override
    public boolean isClosed() {
        return delegate.isClosed();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnection#isQueueing()
     */
    @Override
    public boolean isQueueing() {
        return delegate.isQueueing();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisPubSubCommands#isSubscribed()
     */
    @Override
    public boolean isSubscribed() {
        return delegate.isSubscribed();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#keys(byte[])
     */
    @Override
    public Set<byte[]> keys(byte[] pattern) {
        if (enabledPrefix()){
            pattern = appendPrefix(Boolean.TRUE, pattern);
            Set<byte[]> set = convertAndReturn(delegate.keys(pattern), Converters.identityConverter());
            if (set != null) {
                return set.stream()
                        .map(serializer::deserialize) // 先反序列化
                        .filter(Objects::nonNull)
                        .map(keyStr -> {
                            if (redisProperties.getPrefix().getIgnore().stream().noneMatch(ant -> antPathMatcher.match(ant, keyStr))) {
                                if (StringUtil.contains(keyStr, StringPool.COLON)){
                                    return serializer.serialize(StringUtil.subAfter(keyStr, StringPool.COLON, false));
                                }
                            }
                            return serializer.serialize(keyStr); // 如果不需要处理前缀，直接序列化
                        })
                        .collect(Collectors.toSet());
            }
        }

        return convertAndReturn(delegate.keys(pattern), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#lastSave()
     */
    @Override
    public Long lastSave() {
        return convertAndReturn(delegate.lastSave(), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lIndex(byte[], long)
     */
    @Override
    public byte[] lIndex(byte[] key, long index) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.lIndex(key, index), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lInsert(byte[], org.springframework.data.redis.connection.RedisListCommands.Position, byte[], byte[])
     */
    @Override
    public Long lInsert(byte[] key, Position where, byte[] pivot, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.lInsert(key, where, pivot, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lMove(byte[], byte[], org.springframework.data.redis.connection.RedisListCommands.Direction, org.springframework.data.redis.connection.RedisListCommands.Direction)
     */
    @Override
    public byte[] lMove(byte[] sourceKey, byte[] destinationKey, Direction from, Direction to) {
        sourceKey = addPrefix(Boolean.FALSE, sourceKey)[0];
        destinationKey = addPrefix(Boolean.FALSE, destinationKey)[0];
        return convertAndReturn(delegate.lMove(sourceKey, destinationKey, from, to), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#bLMove(byte[], byte[], org.springframework.data.redis.connection.RedisListCommands.Direction, org.springframework.data.redis.connection.RedisListCommands.Direction, double)
     */
    @Override
    public byte[] bLMove(byte[] sourceKey, byte[] destinationKey, Direction from, Direction to, double timeout) {
        sourceKey = addPrefix(Boolean.FALSE, sourceKey)[0];
        destinationKey = addPrefix(Boolean.FALSE, destinationKey)[0];
        return convertAndReturn(delegate.bLMove(sourceKey, destinationKey, from, to, timeout),
                Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lLen(byte[])
     */
    @Override
    public Long lLen(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.lLen(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lPop(byte[])
     */
    @Override
    public byte[] lPop(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.lPop(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lPop(byte[], long)
     */
    @Override
    public List<byte[]> lPop(byte[] key, long count) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.lPop(key, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lPos(byte[], byte[], java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<Long> lPos(byte[] key, byte[] element, @Nullable Integer rank, @Nullable Integer count) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.lPos(key, element, rank, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lPush(byte[], byte[][])
     */
    @Override
    public Long lPush(byte[] key, byte[]... values) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.lPush(key, values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lPushX(byte[], byte[])
     */
    @Override
    public Long lPushX(byte[] key, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.lPushX(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lRange(byte[], long, long)
     */
    @Override
    public List<byte[]> lRange(byte[] key, long start, long end) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.lRange(key, start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lRem(byte[], long, byte[])
     */
    @Override
    public Long lRem(byte[] key, long count, byte[] value) {
        byte[][] keys = scanAllForKey(key);
        long remCount = 0;
        for (byte[] k : keys){
            remCount = remCount + Fc.toLong(convertAndReturn(delegate.lRem(k, count, value), Converters.identityConverter()), 0);
        }
        return remCount;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lSet(byte[], long, byte[])
     */
    @Override
    public void lSet(byte[] key, long index, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        delegate.lSet(key, index, value);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lTrim(byte[], long, long)
     */
    @Override
    public void lTrim(byte[] key, long start, long end) {
        byte[][] keys = scanAllForKey(key);
        for (byte[] k : keys){
            delegate.lTrim(k, start, end);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#mGet(byte[][])
     */
    @Override
    public List<byte[]> mGet(byte[]... keys) {
        keys = addPrefix(Boolean.FALSE, keys);
        return convertAndReturn(delegate.mGet(keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#mSet(java.util.Map)
     */
    @Override
    public Boolean mSet(Map<byte[], byte[]> tuple) {
        tuple = tuple.entrySet().stream()
                .collect(Collectors.toMap(
                        // 修改键
                        entry -> addPrefix(Boolean.FALSE, entry.getKey())[0],
                        // 保持原值
                        Map.Entry::getValue,
                        // 选择保留现有值
                        (existing, replacement) -> replacement
                ));
        return convertAndReturn(delegate.mSet(tuple), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#mSetNX(java.util.Map)
     */
    @Override
    public Boolean mSetNX(Map<byte[], byte[]> tuple) {
        tuple = tuple.entrySet().stream()
                .collect(Collectors.toMap(
                        // 修改键
                        entry -> addPrefix(Boolean.FALSE, entry.getKey())[0],
                        // 保持原值
                        Map.Entry::getValue,
                        // 选择保留现有值
                        (existing, replacement) -> replacement
                ));
        return convertAndReturn(delegate.mSetNX(tuple), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisTxCommands#multi()
     */
    @Override
    public void multi() {
        delegate.multi();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#persist(byte[])
     */
    @Override
    public Boolean persist(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.persist(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#move(byte[], int)
     */
    @Override
    public Boolean move(byte[] key, int dbIndex) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.move(key, dbIndex), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnectionCommands#ping()
     */
    @Override
    public String ping() {
        return convertAndReturn(delegate.ping(), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisPubSubCommands#pSubscribe(org.springframework.data.redis.connection.MessageListener, byte[][])
     */
    @Override
    public void pSubscribe(MessageListener listener, byte[]... patterns) {
        delegate.pSubscribe(listener, patterns);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisPubSubCommands#publish(byte[], byte[])
     */
    @Override
    public Long publish(byte[] channel, byte[] message) {
        return convertAndReturn(delegate.publish(channel, message), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#randomKey()
     */
    @Override
    public byte[] randomKey() {

        byte[] key = convertAndReturn(delegate.randomKey(), Converters.identityConverter());

        if (enabledPrefix()){
            String keyStr = serializer.deserialize(key);
            if (Fc.isNotBlank(keyStr) && redisProperties.getPrefix().getIgnore().stream().noneMatch(ant -> antPathMatcher.match(ant, keyStr))) {
                if (StringUtil.contains(keyStr, StringPool.COLON)){
                    return serializer.serialize(StringUtil.subAfter(keyStr, StringPool.COLON, false));
                }
            }
            return serializer.serialize(keyStr); // 如果不需要处理前缀，直接序列化
        }

        return key;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#rename(byte[], byte[])
     */
    @Override
    public void rename(byte[] oldKey, byte[] newKey) {
        oldKey = addPrefix(Boolean.FALSE, oldKey)[0];
        newKey = addPrefix(Boolean.FALSE, newKey)[0];
        delegate.rename(oldKey, newKey);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#renameNX(byte[], byte[])
     */
    @Override
    public Boolean renameNX(byte[] oldKey, byte[] newKey) {
        oldKey = addPrefix(Boolean.FALSE, oldKey)[0];
        newKey = addPrefix(Boolean.FALSE, newKey)[0];
        return convertAndReturn(delegate.renameNX(oldKey, newKey), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#resetConfigStats()
     */
    @Override
    public void resetConfigStats() {
        delegate.resetConfigStats();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#rewriteConfig()
     */
    @Override
    public void rewriteConfig() {
        delegate.rewriteConfig();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#rPop(byte[])
     */
    @Override
    public byte[] rPop(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.rPop(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#rPop(byte[], long)
     */
    @Override
    public List<byte[]> rPop(byte[] key, long count) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.rPop(key, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#rPopLPush(byte[], byte[])
     */
    @Override
    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        srcKey = addPrefix(Boolean.FALSE, srcKey)[0];
        dstKey = addPrefix(Boolean.FALSE, dstKey)[0];
        return convertAndReturn(delegate.rPopLPush(srcKey, dstKey), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#rPush(byte[], byte[][])
     */
    @Override
    public Long rPush(byte[] key, byte[]... values) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.rPush(key, values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#rPushX(byte[], byte[])
     */
    @Override
    public Long rPushX(byte[] key, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.rPushX(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sAdd(byte[], byte[][])
     */
    @Override
    public Long sAdd(byte[] key, byte[]... values) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.sAdd(key, values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#save()
     */
    @Override
    public void save() {
        delegate.save();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sCard(byte[])
     */
    @Override
    public Long sCard(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.sCard(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sDiff(byte[][])
     */
    @Override
    public Set<byte[]> sDiff(byte[]... keys) {
        keys = addPrefix(Boolean.FALSE, keys);
        return convertAndReturn(delegate.sDiff(keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sDiffStore(byte[], byte[][])
     */
    @Override
    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        destKey = addPrefix(Boolean.FALSE, destKey)[0];
        keys = addPrefix(Boolean.FALSE, keys);
        return convertAndReturn(delegate.sDiffStore(destKey, keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnectionCommands#select(int)
     */
    @Override
    public void select(int dbIndex) {
        delegate.select(dbIndex);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#set(byte[], byte[])
     */
    @Override
    public Boolean set(byte[] key, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.set(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#set(byte[], byte[], org.springframework.data.redis.core.types.Expiration, org.springframework.data.redis.connection.RedisStringCommands.SetOptions)
     */
    @Override
    public Boolean set(byte[] key, byte[] value, Expiration expiration, SetOption option) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.set(key, value, expiration, option), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#setBit(byte[], long, boolean)
     */
    @Override
    public Boolean setBit(byte[] key, long offset, boolean value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.setBit(key, offset, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#setConfig(java.lang.String, java.lang.String)
     */
    @Override
    public void setConfig(String param, String value) {
        delegate.setConfig(param, value);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#setEx(byte[], long, byte[])
     */
    @Override
    public Boolean setEx(byte[] key, long seconds, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.setEx(key, seconds, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#pSetEx(byte[], long, byte[])
     */
    @Override
    public Boolean pSetEx(byte[] key, long milliseconds, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.pSetEx(key, milliseconds, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#setNX(byte[], byte[])
     */
    @Override
    public Boolean setNX(byte[] key, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.setNX(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#setRange(byte[], byte[], long)
     */
    @Override
    public void setRange(byte[] key, byte[] value, long start) {
        key = addPrefix(Boolean.FALSE, key)[0];
        delegate.setRange(key, value, start);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#shutdown()
     */
    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#shutdown(org.springframework.data.redis.connection.RedisServerCommands.ShutdownOption)
     */
    @Override
    public void shutdown(ShutdownOption option) {
        delegate.shutdown(option);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sInter(byte[][])
     */
    @Override
    public Set<byte[]> sInter(byte[]... keys) {
        keys = addPrefix(Boolean.FALSE, keys);
        return convertAndReturn(delegate.sInter(keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sInterStore(byte[], byte[][])
     */
    @Override
    public Long sInterStore(byte[] destKey, byte[]... keys) {
        destKey = addPrefix(Boolean.FALSE, destKey)[0];
        keys = addPrefix(Boolean.FALSE, keys);
        return convertAndReturn(delegate.sInterStore(destKey, keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sIsMember(byte[], byte[])
     */
    @Override
    public Boolean sIsMember(byte[] key, byte[] value) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.sIsMember(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sIsMember(byte[], byte[]...)
     */
    @Override
    public List<Boolean> sMIsMember(byte[] key, byte[]... values) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.sMIsMember(key, values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sMembers(byte[])
     */
    @Override
    public Set<byte[]> sMembers(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.sMembers(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sMove(byte[], byte[], byte[])
     */
    @Override
    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        srcKey = addPrefix(Boolean.FALSE, srcKey)[0];
        destKey = addPrefix(Boolean.FALSE, destKey)[0];
        return convertAndReturn(delegate.sMove(srcKey, destKey, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#sort(byte[], org.springframework.data.redis.connection.SortParameters, byte[])
     */
    @Override
    public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
        key = addPrefix(Boolean.FALSE, key)[0];
        storeKey = addPrefix(Boolean.FALSE, storeKey)[0];
        return convertAndReturn(delegate.sort(key, params, storeKey), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#sort(byte[], org.springframework.data.redis.connection.SortParameters)
     */
    @Override
    public List<byte[]> sort(byte[] key, SortParameters params) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.sort(key, params), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.StringRedisConnection#encoding(byte[])
     */
    @Override
    public ValueEncoding encodingOf(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.encodingOf(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.StringRedisConnection#idletime(byte[])
     */
    @Override
    public Duration idletime(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.idletime(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.StringRedisConnection#refcount(byte[])
     */
    @Override
    public Long refcount(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.refcount(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sPop(byte[])
     */
    @Override
    public byte[] sPop(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.sPop(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sPop(byte[], long)
     */
    @Override
    public List<byte[]> sPop(byte[] key, long count) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.sPop(key, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sRandMember(byte[])
     */
    @Override
    public byte[] sRandMember(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.sRandMember(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sRandMember(byte[], long)
     */
    @Override
    public List<byte[]> sRandMember(byte[] key, long count) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.sRandMember(key, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sRem(byte[], byte[][])
     */
    @Override
    public Long sRem(byte[] key, byte[]... values) {
        byte[][] keys = scanAllForKey(key);
        long remCount = 0;
        for (byte[] k : keys){
            remCount = remCount + Fc.toLong(convertAndReturn(delegate.sRem(key, values), Converters.identityConverter()), 0);
        }
        return remCount;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#strLen(byte[])
     */
    @Override
    public Long strLen(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.strLen(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#bitCount(byte[])
     */
    @Override
    public Long bitCount(byte[] key) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.bitCount(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#bitCount(byte[], long, long)
     */
    @Override
    public Long bitCount(byte[] key, long start, long end) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.bitCount(key, start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#bitOp(org.springframework.data.redis.connection.RedisStringCommands.BitOperation, byte[], byte[][])
     */
    @Override
    public Long bitOp(BitOperation op, byte[] destination, byte[]... keys) {
        destination = addPrefix(Boolean.FALSE, destination)[0];
        keys = addPrefix(Boolean.FALSE, keys);
        return convertAndReturn(delegate.bitOp(op, destination, keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.StringRedisConnection#bitPos(byte[], boolean, org.springframework.data.domain.Range)
     */
    @Nullable
    @Override
    public Long bitPos(byte[] key, boolean bit, org.springframework.data.domain.Range<Long> range) {
        key = addPrefix(Boolean.FALSE, key)[0];
        return convertAndReturn(delegate.bitPos(key, bit, range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisPubSubCommands#subscribe(org.springframework.data.redis.connection.MessageListener, byte[][])
     */
    @Override
    public void subscribe(MessageListener listener, byte[]... channels) {
        delegate.subscribe(listener, channels);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sUnion(byte[][])
     */
    @Override
    public Set<byte[]> sUnion(byte[]... keys) {










        return convertAndReturn(delegate.sUnion(keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sUnionStore(byte[], byte[][])
     */
    @Override
    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        return convertAndReturn(delegate.sUnionStore(destKey, keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#ttl(byte[])
     */
    @Override
    public Long ttl(byte[] key) {
        return convertAndReturn(delegate.ttl(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#ttl(byte[], java.util.concurrent.TimeUnit)
     */
    @Override
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        return convertAndReturn(delegate.ttl(key, timeUnit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#type(byte[])
     */
    @Override
    public DataType type(byte[] key) {
        return convertAndReturn(delegate.type(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#touch(byte[][])
     */
    @Override
    public Long touch(byte[]... keys) {
        return convertAndReturn(delegate.touch(keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisTxCommands#unwatch()
     */
    @Override
    public void unwatch() {
        delegate.unwatch();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisTxCommands#watch(byte[][])
     */
    @Override
    public void watch(byte[]... keys) {
        delegate.watch(keys);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zAdd(byte[], double, byte[], org.springframework.data.redis.connection.RedisZSetCommands.ZAddArgs)
     */
    @Override
    public Boolean zAdd(byte[] key, double score, byte[] value, ZAddArgs args) {
        return convertAndReturn(delegate.zAdd(key, score, value, args), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zAdd(byte[], java.util.Set, org.springframework.data.redis.connection.RedisZSetCommands.ZAddArgs)
     */
    @Override
    public Long zAdd(byte[] key, Set<Tuple> tuples, ZAddArgs args) {
        return convertAndReturn(delegate.zAdd(key, tuples, args), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zCard(byte[])
     */
    @Override
    public Long zCard(byte[] key) {
        return convertAndReturn(delegate.zCard(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zCount(byte[], double, double)
     */
    @Override
    public Long zCount(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zCount(key, min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zCount(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Long zCount(byte[] key, Range range) {
        return convertAndReturn(delegate.zCount(key, range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zIncrBy(byte[], double, byte[])
     */
    @Override
    public Double zIncrBy(byte[] key, double increment, byte[] value) {
        return convertAndReturn(delegate.zIncrBy(key, increment, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zDiff(byte[][])
     */
    @Nullable
    @Override
    public Set<byte[]> zDiff(byte[]... sets) {
        return convertAndReturn(delegate.zDiff(sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zDiffWithScores(byte[][])
     */
    @Nullable
    @Override
    public Set<Tuple> zDiffWithScores(byte[]... sets) {
        return convertAndReturn(delegate.zDiffWithScores(sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zDiffStore(byte[], byte[][])
     */
    @Nullable
    @Override
    public Long zDiffStore(byte[] destKey, byte[]... sets) {
        return convertAndReturn(delegate.zDiffStore(destKey, sets), Converters.identityConverter());
    }


    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zInter(byte[][])
     */
    @Nullable
    @Override
    public Set<byte[]> zInter(byte[]... sets) {
        return convertAndReturn(delegate.zInter(sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zInterWithScores(byte[][])
     */
    @Nullable
    @Override
    public Set<Tuple> zInterWithScores(byte[]... sets) {
        return convertAndReturn(delegate.zInterWithScores(sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zInterWithScores(org.springframework.data.redis.connection.RedisZSetCommands.Aggregate, org.springframework.data.redis.connection.RedisZSetCommands.Weights, byte[][])
     */
    @Nullable
    @Override
    public Set<Tuple> zInterWithScores(Aggregate aggregate, Weights weights, byte[]... sets) {
        return convertAndReturn(delegate.zInterWithScores(aggregate, weights, sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zInterStore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Aggregate, org.springframework.data.redis.connection.RedisZSetCommands.Weights, byte[][])
     */
    @Override
    public Long zInterStore(byte[] destKey, Aggregate aggregate, Weights weights, byte[]... sets) {
        return convertAndReturn(delegate.zInterStore(destKey, aggregate, weights, sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zInterStore(byte[], byte[][])
     */
    @Override
    public Long zInterStore(byte[] destKey, byte[]... sets) {
        return convertAndReturn(delegate.zInterStore(destKey, sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRange(byte[], long, long)
     */
    @Override
    public Set<byte[]> zRange(byte[] key, long start, long end) {
        return convertAndReturn(delegate.zRange(key, start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScore(byte[], double, double, long, long)
     */
    @Override
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return convertAndReturn(delegate.zRangeByScore(key, min, max, offset, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Set<byte[]> zRangeByScore(byte[] key, Range range) {
        return convertAndReturn(delegate.zRangeByScore(key, range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<byte[]> zRangeByScore(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRangeByScore(key, range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScoreWithScores(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, Range range) {
        return convertAndReturn(delegate.zRangeByScoreWithScores(key, range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScore(byte[], double, double)
     */
    @Override
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zRangeByScore(key, min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScoreWithScores(byte[], double, double, long, long)
     */
    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return convertAndReturn(delegate.zRangeByScoreWithScores(key, min, max, offset, count),
                Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScoreWithScores(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRangeByScoreWithScores(key, range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScoreWithScores(byte[], double, double)
     */
    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zRangeByScoreWithScores(key, min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeWithScores(byte[], long, long)
     */
    @Override
    public Set<Tuple> zRangeWithScores(byte[] key, long start, long end) {
        return convertAndReturn(delegate.zRangeWithScores(key, start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScore(byte[], double, double, long, long)
     */
    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return convertAndReturn(delegate.zRevRangeByScore(key, min, max, offset, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, Range range) {
        return convertAndReturn(delegate.zRevRangeByScore(key, range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScore(byte[], double, double)
     */
    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zRevRangeByScore(key, min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRevRangeByScore(key, range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScoreWithScores(byte[], double, double, long, long)
     */
    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return convertAndReturn(delegate.zRevRangeByScoreWithScores(key, min, max, offset, count),
                Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScoreWithScores(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, Range range) {
        return convertAndReturn(delegate.zRevRangeByScoreWithScores(key, range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScoreWithScores(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRevRangeByScoreWithScores(key, range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScoreWithScores(byte[], double, double)
     */
    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zRevRangeByScoreWithScores(key, min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRank(byte[], byte[])
     */
    @Override
    public Long zRank(byte[] key, byte[] value) {
        return convertAndReturn(delegate.zRank(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRem(byte[], byte[][])
     */
    @Override
    public Long zRem(byte[] key, byte[]... values) {
        return convertAndReturn(delegate.zRem(key, values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRemRange(byte[], long, long)
     */
    @Override
    public Long zRemRange(byte[] key, long start, long end) {
        return convertAndReturn(delegate.zRemRange(key, start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRemRangeByLex(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Long zRemRangeByLex(byte[] key, Range range) {
        return convertAndReturn(delegate.zRemRangeByLex(key, range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRemRangeByScore(byte[], double, double)
     */
    @Override
    public Long zRemRangeByScore(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zRemRangeByScore(key, min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRemRangeByScore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Long zRemRangeByScore(byte[] key, Range range) {
        return convertAndReturn(delegate.zRemRangeByScore(key, range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRange(byte[], long, long)
     */
    @Override
    public Set<byte[]> zRevRange(byte[] key, long start, long end) {
        return convertAndReturn(delegate.zRevRange(key, start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeWithScores(byte[], long, long)
     */
    @Override
    public Set<Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
        return convertAndReturn(delegate.zRevRangeWithScores(key, start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRank(byte[], byte[])
     */
    @Override
    public Long zRevRank(byte[] key, byte[] value) {
        return convertAndReturn(delegate.zRevRank(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zScore(byte[], byte[])
     */
    @Override
    public Double zScore(byte[] key, byte[] value) {
        return convertAndReturn(delegate.zScore(key, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zMScore(byte[], byte[][])
     */
    @Override
    public List<Double> zMScore(byte[] key, byte[]... values) {
        return convertAndReturn(delegate.zMScore(key, values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zUnion(byte[][])
     */
    @Nullable
    @Override
    public Set<byte[]> zUnion(byte[]... sets) {
        return convertAndReturn(delegate.zUnion(sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zUnionWithScores(byte[][])
     */
    @Nullable
    @Override
    public Set<Tuple> zUnionWithScores(byte[]... sets) {
        return convertAndReturn(delegate.zUnionWithScores(sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zUnionWithScores(org.springframework.data.redis.connection.RedisZSetCommands.Aggregate, org.springframework.data.redis.connection.RedisZSetCommands.Weights, byte[][])
     */
    @Nullable
    @Override
    public Set<Tuple> zUnionWithScores(Aggregate aggregate, Weights weights, byte[]... sets) {
        return convertAndReturn(delegate.zUnionWithScores(aggregate, weights, sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zUnionStore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Aggregate, org.springframework.data.redis.connection.RedisZSetCommands.Weights, byte[][])
     */
    @Override
    public Long zUnionStore(byte[] destKey, Aggregate aggregate, Weights weights, byte[]... sets) {
        return convertAndReturn(delegate.zUnionStore(destKey, aggregate, weights, sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zUnionStore(byte[], byte[][])
     */
    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        return convertAndReturn(delegate.zUnionStore(destKey, sets), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#pExpire(byte[], long)
     */
    @Override
    public Boolean pExpire(byte[] key, long millis) {
        return convertAndReturn(delegate.pExpire(key, millis), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#pExpireAt(byte[], long)
     */
    @Override
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        return convertAndReturn(delegate.pExpireAt(key, unixTimeInMillis), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#pTtl(byte[])
     */
    @Override
    public Long pTtl(byte[] key) {
        return convertAndReturn(delegate.pTtl(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#pTtl(byte[], java.util.concurrent.TimeUnit)
     */
    @Override
    public Long pTtl(byte[] key, TimeUnit timeUnit) {
        return convertAndReturn(delegate.pTtl(key, timeUnit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#dump(byte[])
     */
    @Override
    public byte[] dump(byte[] key) {
        return convertAndReturn(delegate.dump(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#restore(byte[], long, byte[], boolean)
     */
    @Override
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue, boolean replace) {
        delegate.restore(key, ttlInMillis, serializedValue, replace);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisScriptingCommands#scriptFlush()
     */
    @Override
    public void scriptFlush() {
        delegate.scriptFlush();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisScriptingCommands#scriptKill()
     */
    @Override
    public void scriptKill() {
        delegate.scriptKill();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisScriptingCommands#scriptLoad(byte[])
     */
    @Override
    public String scriptLoad(byte[] script) {
        return convertAndReturn(delegate.scriptLoad(script), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisScriptingCommands#scriptExists(java.lang.String[])
     */
    @Override
    public List<Boolean> scriptExists(String... scriptSha1) {
        return convertAndReturn(delegate.scriptExists(scriptSha1), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisScriptingCommands#eval(byte[], org.springframework.data.redis.connection.ReturnType, int, byte[][])
     */
    @Override
    public <T> T eval(byte[] script, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        return convertAndReturn(delegate.eval(script, returnType, numKeys, keysAndArgs), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisScriptingCommands#evalSha(java.lang.String, org.springframework.data.redis.connection.ReturnType, int, byte[][])
     */
    @Override
    public <T> T evalSha(String scriptSha1, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        return convertAndReturn(delegate.evalSha(scriptSha1, returnType, numKeys, keysAndArgs),
                Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisScriptingCommands#evalSha(byte[], org.springframework.data.redis.connection.ReturnType, int, byte[][])
     */
    @Override
    public <T> T evalSha(byte[] scriptSha1, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        return convertAndReturn(delegate.evalSha(scriptSha1, returnType, numKeys, keysAndArgs),
                Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hRandField(byte[])
     */
    @Nullable
    @Override
    public byte[] hRandField(byte[] key) {
        return convertAndReturn(delegate.hRandField(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hRandFieldWithValues(byte[])
     */
    @Nullable
    @Override
    public Map.Entry<byte[], byte[]> hRandFieldWithValues(byte[] key) {
        return convertAndReturn(delegate.hRandFieldWithValues(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hRandField(byte[], long)
     */
    @Nullable
    @Override
    public List<byte[]> hRandField(byte[] key, long count) {
        return convertAndReturn(delegate.hRandField(key, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hRandFieldWithValues(byte[], long)
     */
    @Nullable
    @Override
    public List<Map.Entry<byte[], byte[]>> hRandFieldWithValues(byte[] key, long count) {
        return convertAndReturn(delegate.hRandFieldWithValues(key, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zLexCount(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Long zLexCount(byte[] key, Range range) {
        return delegate.zLexCount(key, range);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zPopMin(byte[])
     */
    @Nullable
    @Override
    public Tuple zPopMin(byte[] key) {
        return delegate.zPopMin(key);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zPopMinMin(byte[], count)
     */
    @Nullable
    @Override
    public Set<Tuple> zPopMin(byte[] key, long count) {
        return delegate.zPopMin(key, count);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#bZPopMin(byte[], long, java.util.concurrent.TimeUnit)
     */
    @Nullable
    @Override
    public Tuple bZPopMin(byte[] key, long timeout, TimeUnit unit) {
        return delegate.bZPopMin(key, timeout, unit);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zPopMax(byte[])
     */
    @Nullable
    @Override
    public Tuple zPopMax(byte[] key) {
        return delegate.zPopMax(key);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zPopMax(byte[], long)
     */
    @Nullable
    @Override
    public Set<Tuple> zPopMax(byte[] key, long count) {
        return delegate.zPopMax(key, count);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#bZPopMax(byte[], long, java.util.concurrent.TimeUnit)
     */
    @Nullable
    @Override
    public Tuple bZPopMax(byte[] key, long timeout, TimeUnit unit) {
        return delegate.bZPopMax(key, timeout, unit);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRandMember(byte[])
     */
    @Override
    public byte[] zRandMember(byte[] key) {
        return delegate.zRandMember(key);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRandMember(byte[], long)
     */
    @Override
    public List<byte[]> zRandMember(byte[] key, long count) {
        return delegate.zRandMember(key, count);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRandMemberWithScore(byte[])
     */
    @Override
    public Tuple zRandMemberWithScore(byte[] key) {
        return delegate.zRandMemberWithScore(key);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRandMemberWithScore(byte[], long)
     */
    @Override
    public List<Tuple> zRandMemberWithScore(byte[] key, long count) {
        return delegate.zRandMemberWithScore(key, count);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoAdd(byte[], org.springframework.data.geo.Point, byte[])
     */
    @Override
    public Long geoAdd(byte[] key, Point point, byte[] member) {

        return convertAndReturn(delegate.geoAdd(key, point, member), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoAdd(byte[], org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation)
     */
    public Long geoAdd(byte[] key, GeoLocation<byte[]> location) {
        return convertAndReturn(delegate.geoAdd(key, location), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoAdd(byte[], java.util.Map)
     */
    @Override
    public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
        return convertAndReturn(delegate.geoAdd(key, memberCoordinateMap), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoAdd(byte[], java.lang.Iterable)
     */
    @Override
    public Long geoAdd(byte[] key, Iterable<GeoLocation<byte[]>> locations) {
        return convertAndReturn(delegate.geoAdd(key, locations), Converters.identityConverter());
    }


    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoDist(byte[], byte[], byte[])
     */
    @Override
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
        return convertAndReturn(delegate.geoDist(key, member1, member2), Converters.identityConverter());
    }


    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoDist(byte[], byte[], byte[], org.springframework.data.geo.Metric)
     */
    @Override
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
        return convertAndReturn(delegate.geoDist(key, member1, member2, metric), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoHash(byte[], byte[][])
     */
    @Override
    public List<String> geoHash(byte[] key, byte[]... members) {
        return convertAndReturn(delegate.geoHash(key, members), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoPos(byte[], byte[][])
     */
    @Override
    public List<Point> geoPos(byte[] key, byte[]... members) {
        return convertAndReturn(delegate.geoPos(key, members), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoRadius(byte[], org.springframework.data.geo.Circle)
     */
    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
        return convertAndReturn(delegate.geoRadius(key, within), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoRadius(byte[], org.springframework.data.geo.Circle, org.springframework.data.redis.core.GeoRadiusCommandArgs)
     */
    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, GeoRadiusCommandArgs args) {
        return convertAndReturn(delegate.geoRadius(key, within, args), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoRadiusByMember(byte[], byte[], double)
     */
    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, double radius) {
        return geoRadiusByMember(key, member, new Distance(radius, DistanceUnit.METERS));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoRadiusByMember(byte[], byte[], org.springframework.data.geo.Distance)
     */
    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius) {
        return convertAndReturn(delegate.geoRadiusByMember(key, member, radius), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoRadiusByMember(byte[], byte[], org.springframework.data.geo.Distance, org.springframework.data.redis.core.GeoRadiusCommandArgs)
     */
    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius,
                                                             GeoRadiusCommandArgs args) {

        return convertAndReturn(delegate.geoRadiusByMember(key, member, radius, args), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoRemove(byte[], byte[][])
     */
    @Override
    public Long geoRemove(byte[] key, byte[]... members) {
        return zRem(key, members);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoSearch(byte[], byte[], org.springframework.data.redis.connection.RedisGeoCommands.GeoShape, org.springframework.data.redis.connection.RedisGeoCommands.GeoSearchCommandArgs)
     */
    @Override
    public GeoResults<GeoLocation<byte[]>> geoSearch(byte[] key, GeoReference<byte[]> reference, GeoShape predicate,
                                                     GeoSearchCommandArgs args) {
        return convertAndReturn(delegate.geoSearch(key, reference, predicate, args), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoSearchStore(byte[], byte[], byte[], org.springframework.data.redis.connection.RedisGeoCommands.GeoShape, org.springframework.data.redis.connection.RedisGeoCommands.GeoSearchStoreCommandArgs)
     */
    @Override
    public Long geoSearchStore(byte[] destKey, byte[] key, GeoReference<byte[]> reference, GeoShape predicate,
                               GeoSearchStoreCommandArgs args) {
        return convertAndReturn(delegate.geoSearchStore(destKey, key, reference, predicate, args),
                Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnection#closePipeline()
     */
    @Override
    public List<Object> closePipeline() {

        try {
            return convertResults(delegate.closePipeline(), pipelineConverters);
        } finally {
            pipelineConverters.clear();
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnection#isPipelined()
     */
    @Override
    public boolean isPipelined() {
        return delegate.isPipelined();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnection#openPipeline()
     */
    @Override
    public void openPipeline() {
        delegate.openPipeline();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisCommands#execute(java.lang.String, byte[][])
     */
    @Override
    public Object execute(String command, byte[]... args) {
        return convertAndReturn(delegate.execute(command, args), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#time()
     */
    @Override
    public Long time() {
        return convertAndReturn(this.delegate.time(), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#time(TimeUnit)
     */
    @Override
    public Long time(TimeUnit timeUnit) {
        return convertAndReturn(this.delegate.time(timeUnit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.StringRedisConnection#getClientList()
     */
    @Override
    public List<RedisClientInfo> getClientList() {
        return convertAndReturn(this.delegate.getClientList(), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#slaveOf(java.lang.String, int)
     */
    @Override
    public void slaveOf(String host, int port) {
        this.delegate.slaveOf(host, port);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#slaveOfNoOne()
     */
    @Override
    public void slaveOfNoOne() {
        this.delegate.slaveOfNoOne();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#scan(org.springframework.data.redis.core.ScanOptions)
     */
    @Override
    public Cursor<byte[]> scan(ScanOptions options) {
        if (enabledPrefix()){
            byte[] pattern = appendPrefix(Boolean.TRUE, options.getBytePattern());

            if (options instanceof KeyScanOptions){
                options = ScanOptions.scanOptions().count(Fc.toLong(options.getCount(), 1000)).match(pattern).type(((KeyScanOptions) options).getType()).build();
            } else {
                options = ScanOptions.scanOptions().count(Fc.toLong(options.getCount(), 1000)).match(pattern).build();
            }

            Cursor<byte[]> cursor = this.delegate.scan(options);
            return new ConvertingCursor<>(cursor, key -> {
                String keyStr = serializer.deserialize(key);
                if (Fc.isNotBlank(keyStr)){
                    if (redisProperties.getPrefix().getIgnore().stream().noneMatch(ant -> antPathMatcher.match(ant, keyStr))) {
                        if (StringUtil.contains(keyStr, StringPool.COLON)){
                            return serializer.serialize(StringUtil.subAfter(keyStr, StringPool.COLON, false));
                        }
                    }
                }
                return key;
            });
        }
        return this.delegate.scan(options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zScan(byte[], org.springframework.data.redis.core.ScanOptions)
     */
    @Override
    public Cursor<Tuple> zScan(byte[] key, ScanOptions options) {
        return this.delegate.zScan(key, options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#scan(byte[], org.springframework.data.redis.core.ScanOptions)
     */
    @Override
    public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
        return this.delegate.sScan(key, options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hscan(byte[], org.springframework.data.redis.core.ScanOptions)
     */
    @Override
    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
        return this.delegate.hScan(key, options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#hStrLen(byte[], byte[])
     */
    @Nullable
    @Override
    public Long hStrLen(byte[] key, byte[] field) {
        return convertAndReturn(delegate.hStrLen(key, field), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#setClientName(java.lang.String)
     */
    @Override
    public void setClientName(byte[] name) {
        this.delegate.setClientName(name);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#killClient(byte[])
     */
    @Override
    public void killClient(String host, int port) {
        this.delegate.killClient(host, port);
    }

    /*
     * @see org.springframework.data.redis.connection.RedisServerCommands#getClientName()
     */
    @Override
    public String getClientName() {
        return convertAndReturn(this.delegate.getClientName(), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnection#getSentinelConnection()
     */
    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return delegate.getSentinelConnection();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScore(byte[], java.lang.String, java.lang.String)
     */
    @Override
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
        return convertAndReturn(delegate.zRangeByScore(key, min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScore(byte[], java.lang.String, java.lang.String, long, long)
     */
    @Override
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
        return convertAndReturn(delegate.zRangeByScore(key, min, max, offset, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHyperLogLogCommands#pfAdd(byte[], byte[][])
     */
    @Override
    public Long pfAdd(byte[] key, byte[]... values) {
        return convertAndReturn(delegate.pfAdd(key, values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHyperLogLogCommands#pfCount(byte[][])
     */
    @Override
    public Long pfCount(byte[]... keys) {
        return convertAndReturn(delegate.pfCount(keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHyperLogLogCommands#pfMerge(byte[], byte[][])
     */
    @Override
    public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
        delegate.pfMerge(destinationKey, sourceKeys);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByLex(byte[])
     */
    @Override
    public Set<byte[]> zRangeByLex(byte[] key) {
        return convertAndReturn(delegate.zRangeByLex(key), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByLex(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Set<byte[]> zRangeByLex(byte[] key, Range range) {
        return convertAndReturn(delegate.zRangeByLex(key, range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByLex(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<byte[]> zRangeByLex(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRangeByLex(key, range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByLex(java.lang.String, org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<byte[]> zRevRangeByLex(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRevRangeByLex(key, range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#migrate(byte[], org.springframework.data.redis.connection.RedisNode, int, org.springframework.data.redis.connection.RedisServerCommands.MigrateOption)
     */
    @Override
    public void migrate(byte[] key, RedisNode target, int dbIndex, @Nullable MigrateOption option) {
        delegate.migrate(key, target, dbIndex, option);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#migrate(byte[], org.springframework.data.redis.connection.RedisNode, int, org.springframework.data.redis.connection.RedisServerCommands.MigrateOption, long)
     */
    @Override
    public void migrate(byte[] key, RedisNode target, int dbIndex, @Nullable MigrateOption option, long timeout) {
        delegate.migrate(key, target, dbIndex, option, timeout);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xAck(byte[], java.lang.String, java.lang.String[])
     */
    @Override
    public Long xAck(byte[] key, String group, RecordId... recordIds) {
        return delegate.xAck(key, group, recordIds);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xAdd(MapRecord, XAddOptions)
     */
    @Override
    public RecordId xAdd(MapRecord<byte[], byte[], byte[]> record, XAddOptions options) {
        return delegate.xAdd(record, options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xClaimJustId(byte[], java.lang.String, java.lag.String, org.springframework.data.redis.connection.RedisStreamCommands.XCLaimOptions)
     */
    @Override
    public List<RecordId> xClaimJustId(byte[] key, String group, String newOwner, XClaimOptions options) {
        return delegate.xClaimJustId(key, group, newOwner, options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xClaim(byte[], java.lang.String, java.lag.String, org.springframework.data.redis.connection.RedisStreamCommands.XCLaimOptions)
     */
    @Override
    public List<ByteRecord> xClaim(byte[] key, String group, String newOwner, XClaimOptions options) {
        return delegate.xClaim(key, group, newOwner, options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xDel(byte[], RecordId)
     */
    @Override
    public Long xDel(byte[] key, RecordId... recordIds) {
        return delegate.xDel(key, recordIds);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xGroupCreate(byte[], org.springframework.data.redis.connection.RedisStreamCommands.ReadOffset, java.lang.String)
     */
    @Override
    public String xGroupCreate(byte[] key, String groupName, ReadOffset readOffset) {
        return delegate.xGroupCreate(key, groupName, readOffset);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xGroupCreate(byte[], org.springframework.data.redis.connection.RedisStreamCommands.ReadOffset, java.lang.String, boolean)
     */
    @Override
    public String xGroupCreate(byte[] key, String groupName, ReadOffset readOffset, boolean mkStream) {
        return delegate.xGroupCreate(key, groupName, readOffset, mkStream);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xGroupDelConsumer(byte[], org.springframework.data.redis.connection.RedisStreamCommands.Consumer)
     */
    @Override
    public Boolean xGroupDelConsumer(byte[] key, Consumer consumer) {
        return delegate.xGroupDelConsumer(key, consumer);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xGroupDestroy(byte[], java.lang.String)
     */
    @Override
    public Boolean xGroupDestroy(byte[] key, String groupName) {
        return delegate.xGroupDestroy(key, groupName);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xInfo(byte[])
     */
    @Override
    public StreamInfo.XInfoStream xInfo(byte[] key) {
        return delegate.xInfo(key);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xInfoGroups(byte[])
     */
    @Override
    public StreamInfo.XInfoGroups xInfoGroups(byte[] key) {
        return delegate.xInfoGroups(key);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xInfoConsumers(byte[], java.lang.String)
     */
    @Override
    public StreamInfo.XInfoConsumers xInfoConsumers(byte[] key, String groupName) {
        return delegate.xInfoConsumers(key, groupName);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xLen(byte[])
     */
    @Override
    public Long xLen(byte[] key) {
        return delegate.xLen(key);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xPending(byte[], java.lang.String)
     */
    @Override
    public PendingMessagesSummary xPending(byte[] key, String groupName) {
        return delegate.xPending(key, groupName);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xPending(byte[], java.lang.String)
     */
    @Override
    public PendingMessages xPending(byte[] key, String groupName, XPendingOptions options) {
        return delegate.xPending(key, groupName, options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xRange(byte[], org.springframework.data.domain.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public List<ByteRecord> xRange(byte[] key, org.springframework.data.domain.Range<String> range, Limit limit) {
        return delegate.xRange(key, range, limit);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xRead(org.springframework.data.redis.connection.RedisStreamCommands.StreamReadOptions, org.springframework.data.redis.connection.RedisStreamCommands.StreamOffset[])
     */
    @Override
    public List<ByteRecord> xRead(StreamReadOptions readOptions, StreamOffset<byte[]>... streams) {
        return delegate.xRead(readOptions, streams);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xReadGroup(org.springframework.data.redis.connection.RedisStreamCommands.Consumer, org.springframework.data.redis.connection.RedisStreamCommands.StreamReadOptions, org.springframework.data.redis.connection.RedisStreamCommands.StreamOffset[])
     */
    @Override
    public List<ByteRecord> xReadGroup(Consumer consumer, StreamReadOptions readOptions,
                                       StreamOffset<byte[]>... streams) {
        return delegate.xReadGroup(consumer, readOptions, streams);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xRevRange(byte[], org.springframework.data.domain.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public List<ByteRecord> xRevRange(byte[] key, org.springframework.data.domain.Range<String> range, Limit limit) {
        return delegate.xRevRange(key, range, limit);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xTrim(byte[], long)
     */
    @Override
    public Long xTrim(byte[] key, long count) {
        return xTrim(key, count, false);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xTrim(byte[], long, boolean)
     */
    @Override
    public Long xTrim(byte[] key, long count, boolean approximateTrimming) {
        return delegate.xTrim(key, count, approximateTrimming);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#bitfield(byte[], BitfieldCommand)
     */
    @Override
    public List<Long> bitField(byte[] key, BitFieldSubCommands subCommands) {
        return delegate.bitField(key, subCommands);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <T> T convertAndReturn(@Nullable Object value, Converter converter) {

        if (isFutureConversion()) {

            addResultConverter(converter);
            return null;
        }

        if (!(converter instanceof ListConverter) && value instanceof List) {
            return (T) new ListConverter<>(converter).convert((List) value);
        }

        return value == null ? null
                : ObjectUtils.nullSafeEquals(converter, Converters.identityConverter()) ? (T) value
                : (T) converter.convert(value);
    }

    private boolean isFutureConversion() {
        return isPipelined() || isQueueing();
    }

    private void addResultConverter(Converter<?, ?> converter) {
        if (isQueueing()) {
            txConverters.add(converter);
        } else {
            pipelineConverters.add(converter);
        }
    }
}
