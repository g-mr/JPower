package top.jpower.core.redis.connection;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.NameMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.*;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.connection.convert.ListConverter;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.data.redis.domain.geo.GeoShape;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import top.jpower.core.redis.handler.RedisPrefixHandler;
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

    private final RedisProperties.Prefix prefixProperties;
    private final RedisPrefixHandler redisPrefixHandler;
    private final NameMapper nameMapper;
    private final RedisSerializer<String> serializer;

    @SuppressWarnings("rawtypes") private final Queue<Converter> pipelineConverters = new LinkedList<>();
    @SuppressWarnings("rawtypes") private final Queue<Converter> txConverters = new LinkedList<>();

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Setter
    private boolean deserializePipelineAndTxResults = false;

    public JpowerRedisConnection(RedisConnection connection, RedisProperties.Prefix redisProperties, RedisPrefixHandler redisPrefixHandler, NameMapper nameMapper) {
        this(connection, redisProperties, redisPrefixHandler, nameMapper, RedisSerializer.string());
    }

    public JpowerRedisConnection(RedisConnection connection, RedisProperties.Prefix prefixProperties, RedisPrefixHandler redisPrefixHandler, NameMapper nameMapper, RedisSerializer redisSerializer) {
        this.delegate = connection;
        this.prefixProperties = prefixProperties;
        this.redisPrefixHandler = redisPrefixHandler;
        this.nameMapper = nameMapper;
        this.serializer = redisSerializer;
    }

    private byte[] addPrefix(byte[] key){
        String name = serializer.deserialize(key);
        name = nameMapper.map(name);
        key = serializer.serialize(name);
        return key;
    }

    private byte[][] addPrefix(byte[]... keys){
        for (int i = 0; i < keys.length; i++) {
            keys[i] = addPrefix(keys[i]);
        }
        return keys;
    }

    /**
     * 扫描所有的KEY
     *
     * @author mr.g
     * @param keys 键
     * @return 键
     **/
    private byte[][] scanAllPrefixForKey(byte[]... keys){
        if (Fc.isNull(redisPrefixHandler) || !prefixProperties.getEnabled()) {
            return keys;
        }

        Set<byte[]> keyList = new HashSet<>();
        for (byte[] key : keys) {
            String name = serializer.deserialize(key);
            if (StringUtil.isNotBlank(name)){
                if (prefixProperties.getIgnore().stream().anyMatch(pattern -> ANT_PATH_MATCHER.match(pattern, name))){
                    keyList.add(key);
                } else {
                    boolean isAll = redisPrefixHandler.deleteForAll(name);
                    if (isAll) {
                        Set<byte[]> set = convertAndReturn(delegate.keys(serializer.serialize(StringPool.ASTERISK+StringPool.COLON+name)), Converters.identityConverter());
                        keyList.addAll(set);
                    } else {
                        keyList.add(serializer.serialize(nameMapper.map(name)));
                    }
                }
            }
        }
        return keyList.toArray(new byte[0][]);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#append(byte[], byte[])
     */
    @Override
    public Long append(byte[] key, byte[] value) {
        return convertAndReturn(delegate.append(this.addPrefix(key), value), Converters.identityConverter());
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
        return convertAndReturn(delegate.bLPop(timeout, this.addPrefix(keys)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#bRPop(int, byte[][])
     */
    @Override
    public List<byte[]> bRPop(int timeout, byte[]... keys) {
        return convertAndReturn(delegate.bRPop(timeout, this.addPrefix(keys)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#bRPopLPush(int, byte[], byte[])
     */
    @Override
    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        return convertAndReturn(delegate.bRPopLPush(timeout, this.addPrefix(srcKey), this.addPrefix(dstKey)), Converters.identityConverter());
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
        return convertAndReturn(delegate.copy(this.addPrefix(sourceKey), this.addPrefix(targetKey), replace), Converters.identityConverter());
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
        return convertAndReturn(delegate.decr(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#decrBy(byte[], long)
     */
    @Override
    public Long decrBy(byte[] key, long value) {
        return convertAndReturn(delegate.decrBy(this.addPrefix(key), value), Converters.identityConverter());
    }


    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#del(byte[][])
     */
    @Override
    public Long del(byte[]... keys) {
        keys = this.scanAllPrefixForKey(keys);
        if (keys.length <= 0){
            return 0L;
        }
        return convertAndReturn(delegate.del(keys), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#unlink(byte[][])
     */
    @Override
    public Long unlink(byte[]... keys) {
        keys = this.scanAllPrefixForKey(keys);
        if (keys.length <= 0){
            return 0L;
        }
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
        return convertAndReturn(delegate.exists(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#exists(byte[][])
     */
    @Override
    public Long exists(byte[]... keys) {
        return convertAndReturn(delegate.exists(this.addPrefix(keys)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#expire(byte[], long)
     */
    @Override
    public Boolean expire(byte[] key, long seconds) {
        return convertAndReturn(delegate.expire(this.addPrefix(key), seconds), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#expireAt(byte[], long)
     */
    @Override
    public Boolean expireAt(byte[] key, long unixTime) {
        return convertAndReturn(delegate.expireAt(this.addPrefix(key), unixTime), Converters.identityConverter());
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
        return convertAndReturn(delegate.get(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#getDel(byte[])
     */
    @Nullable
    @Override
    public byte[] getDel(byte[] key) {
        return convertAndReturn(delegate.getDel(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#get(byte[], org.springframework.data.redis.core.types.Expiration)
     */
    @Nullable
    @Override
    public byte[] getEx(byte[] key, Expiration expiration) {
        return convertAndReturn(delegate.getEx(this.addPrefix(key), expiration), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#getBit(byte[], long)
     */
    @Override
    public Boolean getBit(byte[] key, long offset) {
        return convertAndReturn(delegate.getBit(this.addPrefix(key), offset), Converters.identityConverter());
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
        return convertAndReturn(delegate.getRange(this.addPrefix(key), start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#getSet(byte[], byte[])
     */
    @Override
    public byte[] getSet(byte[] key, byte[] value) {
        return convertAndReturn(delegate.getSet(this.addPrefix(key), value), Converters.identityConverter());
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
        return convertAndReturn(delegate.hDel(this.addPrefix(key), fields), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hExists(byte[], byte[])
     */
    @Override
    public Boolean hExists(byte[] key, byte[] field) {
        return convertAndReturn(delegate.hExists(this.addPrefix(key), field), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hGet(byte[], byte[])
     */
    @Override
    public byte[] hGet(byte[] key, byte[] field) {
        return convertAndReturn(delegate.hGet(this.addPrefix(key), field), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hGetAll(byte[])
     */
    @Override
    public Map<byte[], byte[]> hGetAll(byte[] key) {
        return convertAndReturn(delegate.hGetAll(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hIncrBy(byte[], byte[], long)
     */
    @Override
    public Long hIncrBy(byte[] key, byte[] field, long delta) {
        return convertAndReturn(delegate.hIncrBy(this.addPrefix(key), field, delta), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hIncrBy(byte[], byte[], double)
     */
    @Override
    public Double hIncrBy(byte[] key, byte[] field, double delta) {
        return convertAndReturn(delegate.hIncrBy(this.addPrefix(key), field, delta), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hKeys(byte[])
     */
    @Override
    public Set<byte[]> hKeys(byte[] key) {
        return convertAndReturn(delegate.hKeys(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hLen(byte[])
     */
    @Override
    public Long hLen(byte[] key) {
        return convertAndReturn(delegate.hLen(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hMGet(byte[], byte[][])
     */
    @Override
    public List<byte[]> hMGet(byte[] key, byte[]... fields) {
        return convertAndReturn(delegate.hMGet(this.addPrefix(key), fields), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hMSet(byte[], java.util.Map)
     */
    @Override
    public void hMSet(byte[] key, Map<byte[], byte[]> hashes) {
        delegate.hMSet(this.addPrefix(key), hashes);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hSet(byte[], byte[], byte[])
     */
    @Override
    public Boolean hSet(byte[] key, byte[] field, byte[] value) {
        return convertAndReturn(delegate.hSet(this.addPrefix(key), field, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hSetNX(byte[], byte[], byte[])
     */
    @Override
    public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
        return convertAndReturn(delegate.hSetNX(this.addPrefix(key), field, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hVals(byte[])
     */
    @Override
    public List<byte[]> hVals(byte[] key) {
        return convertAndReturn(delegate.hVals(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#incr(byte[])
     */
    @Override
    public Long incr(byte[] key) {
        return convertAndReturn(delegate.incr(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#incrBy(byte[], long)
     */
    @Override
    public Long incrBy(byte[] key, long value) {
        return convertAndReturn(delegate.incrBy(this.addPrefix(key), value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#incrBy(byte[], double)
     */
    @Override
    public Double incrBy(byte[] key, double value) {
        return convertAndReturn(delegate.incrBy(this.addPrefix(key), value), Converters.identityConverter());
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
        Set<byte[]> set = convertAndReturn(delegate.keys(this.addPrefix(pattern)), Converters.identityConverter());
        return set.stream().map(key-> serializer.serialize(nameMapper.unmap(serializer.deserialize(key)))).filter(Objects::nonNull).collect(Collectors.toSet());
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
        return convertAndReturn(delegate.lIndex(this.addPrefix(key), index), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lInsert(byte[], org.springframework.data.redis.connection.RedisListCommands.Position, byte[], byte[])
     */
    @Override
    public Long lInsert(byte[] key, Position where, byte[] pivot, byte[] value) {
        return convertAndReturn(delegate.lInsert(this.addPrefix(key), where, pivot, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lMove(byte[], byte[], org.springframework.data.redis.connection.RedisListCommands.Direction, org.springframework.data.redis.connection.RedisListCommands.Direction)
     */
    @Override
    public byte[] lMove(byte[] sourceKey, byte[] destinationKey, Direction from, Direction to) {
        return convertAndReturn(delegate.lMove(this.addPrefix(sourceKey), this.addPrefix(destinationKey), from, to), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#bLMove(byte[], byte[], org.springframework.data.redis.connection.RedisListCommands.Direction, org.springframework.data.redis.connection.RedisListCommands.Direction, double)
     */
    @Override
    public byte[] bLMove(byte[] sourceKey, byte[] destinationKey, Direction from, Direction to, double timeout) {
        return convertAndReturn(delegate.bLMove(this.addPrefix(sourceKey), this.addPrefix(destinationKey), from, to, timeout),
                Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lLen(byte[])
     */
    @Override
    public Long lLen(byte[] key) {
        return convertAndReturn(delegate.lLen(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lPop(byte[])
     */
    @Override
    public byte[] lPop(byte[] key) {
        return convertAndReturn(delegate.lPop(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lPop(byte[], long)
     */
    @Override
    public List<byte[]> lPop(byte[] key, long count) {
        return convertAndReturn(delegate.lPop(this.addPrefix(key), count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lPos(byte[], byte[], java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<Long> lPos(byte[] key, byte[] element, @Nullable Integer rank, @Nullable Integer count) {
        return convertAndReturn(delegate.lPos(this.addPrefix(key), element, rank, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lPush(byte[], byte[][])
     */
    @Override
    public Long lPush(byte[] key, byte[]... values) {
        return convertAndReturn(delegate.lPush(this.addPrefix(key), values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lPushX(byte[], byte[])
     */
    @Override
    public Long lPushX(byte[] key, byte[] value) {
        return convertAndReturn(delegate.lPushX(this.addPrefix(key), value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lRange(byte[], long, long)
     */
    @Override
    public List<byte[]> lRange(byte[] key, long start, long end) {
        return convertAndReturn(delegate.lRange(this.addPrefix(key), start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lRem(byte[], long, byte[])
     */
    @Override
    public Long lRem(byte[] key, long count, byte[] value) {
        return convertAndReturn(delegate.lRem(this.addPrefix(key), count, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lSet(byte[], long, byte[])
     */
    @Override
    public void lSet(byte[] key, long index, byte[] value) {
        delegate.lSet(this.addPrefix(key), index, value);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#lTrim(byte[], long, long)
     */
    @Override
    public void lTrim(byte[] key, long start, long end) {
        delegate.lTrim(this.addPrefix(key), start, end);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#mGet(byte[][])
     */
    @Override
    public List<byte[]> mGet(byte[]... keys) {
        return convertAndReturn(delegate.mGet(this.addPrefix(keys)), Converters.identityConverter());
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
                        entry -> this.addPrefix(entry.getKey()),
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
                        entry -> this.addPrefix(entry.getKey()),
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
        return convertAndReturn(delegate.persist(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#move(byte[], int)
     */
    @Override
    public Boolean move(byte[] key, int dbIndex) {
        return convertAndReturn(delegate.move(this.addPrefix(key), dbIndex), Converters.identityConverter());
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
        return serializer.serialize(nameMapper.unmap(serializer.deserialize(key)));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#rename(byte[], byte[])
     */
    @Override
    public void rename(byte[] oldKey, byte[] newKey) {
        delegate.rename(this.addPrefix(oldKey), this.addPrefix(newKey));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#renameNX(byte[], byte[])
     */
    @Override
    public Boolean renameNX(byte[] oldKey, byte[] newKey) {
        return convertAndReturn(delegate.renameNX(this.addPrefix(oldKey), this.addPrefix(newKey)), Converters.identityConverter());
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
        return convertAndReturn(delegate.rPop(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#rPop(byte[], long)
     */
    @Override
    public List<byte[]> rPop(byte[] key, long count) {
        return convertAndReturn(delegate.rPop(this.addPrefix(key), count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#rPopLPush(byte[], byte[])
     */
    @Override
    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        return convertAndReturn(delegate.rPopLPush(this.addPrefix(srcKey), this.addPrefix(dstKey)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#rPush(byte[], byte[][])
     */
    @Override
    public Long rPush(byte[] key, byte[]... values) {
        return convertAndReturn(delegate.rPush(this.addPrefix(key), values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisListCommands#rPushX(byte[], byte[])
     */
    @Override
    public Long rPushX(byte[] key, byte[] value) {
        return convertAndReturn(delegate.rPushX(this.addPrefix(key), value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sAdd(byte[], byte[][])
     */
    @Override
    public Long sAdd(byte[] key, byte[]... values) {
        return convertAndReturn(delegate.sAdd(this.addPrefix(key), values), Converters.identityConverter());
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
        return convertAndReturn(delegate.sCard(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sDiff(byte[][])
     */
    @Override
    public Set<byte[]> sDiff(byte[]... keys) {
        return convertAndReturn(delegate.sDiff(this.addPrefix(keys)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sDiffStore(byte[], byte[][])
     */
    @Override
    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        return convertAndReturn(delegate.sDiffStore(this.addPrefix(destKey), this.addPrefix(keys)), Converters.identityConverter());
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
        return convertAndReturn(delegate.set(this.addPrefix(key), value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#set(byte[], byte[], org.springframework.data.redis.core.types.Expiration, org.springframework.data.redis.connection.RedisStringCommands.SetOptions)
     */
    @Override
    public Boolean set(byte[] key, byte[] value, Expiration expiration, SetOption option) {
        return convertAndReturn(delegate.set(this.addPrefix(key), value, expiration, option), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#setBit(byte[], long, boolean)
     */
    @Override
    public Boolean setBit(byte[] key, long offset, boolean value) {
        return convertAndReturn(delegate.setBit(this.addPrefix(key), offset, value), Converters.identityConverter());
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
        return convertAndReturn(delegate.setEx(this.addPrefix(key), seconds, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#pSetEx(byte[], long, byte[])
     */
    @Override
    public Boolean pSetEx(byte[] key, long milliseconds, byte[] value) {
        return convertAndReturn(delegate.pSetEx(this.addPrefix(key), milliseconds, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#setNX(byte[], byte[])
     */
    @Override
    public Boolean setNX(byte[] key, byte[] value) {
        return convertAndReturn(delegate.setNX(this.addPrefix(key), value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#setRange(byte[], byte[], long)
     */
    @Override
    public void setRange(byte[] key, byte[] value, long start) {
        delegate.setRange(this.addPrefix(key), value, start);
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
        return convertAndReturn(delegate.sInter(this.addPrefix(keys)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sInterStore(byte[], byte[][])
     */
    @Override
    public Long sInterStore(byte[] destKey, byte[]... keys) {
        return convertAndReturn(delegate.sInterStore(this.addPrefix(destKey), this.addPrefix(keys)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sIsMember(byte[], byte[])
     */
    @Override
    public Boolean sIsMember(byte[] key, byte[] value) {
        return convertAndReturn(delegate.sIsMember(this.addPrefix(key), value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sIsMember(byte[], byte[]...)
     */
    @Override
    public List<Boolean> sMIsMember(byte[] key, byte[]... values) {
        return convertAndReturn(delegate.sMIsMember(this.addPrefix(key), values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sMembers(byte[])
     */
    @Override
    public Set<byte[]> sMembers(byte[] key) {
        return convertAndReturn(delegate.sMembers(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sMove(byte[], byte[], byte[])
     */
    @Override
    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        return convertAndReturn(delegate.sMove(this.addPrefix(srcKey), this.addPrefix(destKey), value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#sort(byte[], org.springframework.data.redis.connection.SortParameters, byte[])
     */
    @Override
    public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
        return convertAndReturn(delegate.sort(this.addPrefix(key), params, this.addPrefix(storeKey)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#sort(byte[], org.springframework.data.redis.connection.SortParameters)
     */
    @Override
    public List<byte[]> sort(byte[] key, SortParameters params) {
        return convertAndReturn(delegate.sort(this.addPrefix(key), params), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.StringRedisConnection#encoding(byte[])
     */
    @Override
    public ValueEncoding encodingOf(byte[] key) {
        return convertAndReturn(delegate.encodingOf(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.StringRedisConnection#idletime(byte[])
     */
    @Override
    public Duration idletime(byte[] key) {
        return convertAndReturn(delegate.idletime(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.StringRedisConnection#refcount(byte[])
     */
    @Override
    public Long refcount(byte[] key) {
        return convertAndReturn(delegate.refcount(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sPop(byte[])
     */
    @Override
    public byte[] sPop(byte[] key) {
        return convertAndReturn(delegate.sPop(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sPop(byte[], long)
     */
    @Override
    public List<byte[]> sPop(byte[] key, long count) {
        return convertAndReturn(delegate.sPop(this.addPrefix(key), count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sRandMember(byte[])
     */
    @Override
    public byte[] sRandMember(byte[] key) {
        return convertAndReturn(delegate.sRandMember(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sRandMember(byte[], long)
     */
    @Override
    public List<byte[]> sRandMember(byte[] key, long count) {
        return convertAndReturn(delegate.sRandMember(this.addPrefix(key), count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sRem(byte[], byte[][])
     */
    @Override
    public Long sRem(byte[] key, byte[]... values) {
        return convertAndReturn(delegate.sRem(this.addPrefix(key), values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#strLen(byte[])
     */
    @Override
    public Long strLen(byte[] key) {
        return convertAndReturn(delegate.strLen(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#bitCount(byte[])
     */
    @Override
    public Long bitCount(byte[] key) {
        return convertAndReturn(delegate.bitCount(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#bitCount(byte[], long, long)
     */
    @Override
    public Long bitCount(byte[] key, long start, long end) {
        return convertAndReturn(delegate.bitCount(this.addPrefix(key), start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#bitOp(org.springframework.data.redis.connection.RedisStringCommands.BitOperation, byte[], byte[][])
     */
    @Override
    public Long bitOp(BitOperation op, byte[] destination, byte[]... keys) {
        return convertAndReturn(delegate.bitOp(op, this.addPrefix(destination), this.addPrefix(keys)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.StringRedisConnection#bitPos(byte[], boolean, org.springframework.data.domain.Range)
     */
    @Nullable
    @Override
    public Long bitPos(byte[] key, boolean bit, org.springframework.data.domain.Range<Long> range) {
        return convertAndReturn(delegate.bitPos(this.addPrefix(key), bit, range), Converters.identityConverter());
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
        return convertAndReturn(delegate.sUnion(this.addPrefix(keys)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#sUnionStore(byte[], byte[][])
     */
    @Override
    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        return convertAndReturn(delegate.sUnionStore(this.addPrefix(destKey), this.addPrefix(keys)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#ttl(byte[])
     */
    @Override
    public Long ttl(byte[] key) {
        return convertAndReturn(delegate.ttl(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#ttl(byte[], java.util.concurrent.TimeUnit)
     */
    @Override
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        return convertAndReturn(delegate.ttl(this.addPrefix(key), timeUnit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#type(byte[])
     */
    @Override
    public DataType type(byte[] key) {
        return convertAndReturn(delegate.type(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#touch(byte[][])
     */
    @Override
    public Long touch(byte[]... keys) {
        return convertAndReturn(delegate.touch(this.addPrefix(keys)), Converters.identityConverter());
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
        delegate.watch(this.addPrefix(keys));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zAdd(byte[], double, byte[], org.springframework.data.redis.connection.RedisZSetCommands.ZAddArgs)
     */
    @Override
    public Boolean zAdd(byte[] key, double score, byte[] value, ZAddArgs args) {
        return convertAndReturn(delegate.zAdd(this.addPrefix(key), score, value, args), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zAdd(byte[], java.util.Set, org.springframework.data.redis.connection.RedisZSetCommands.ZAddArgs)
     */
    @Override
    public Long zAdd(byte[] key, Set<Tuple> tuples, ZAddArgs args) {
        return convertAndReturn(delegate.zAdd(this.addPrefix(key), tuples, args), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zCard(byte[])
     */
    @Override
    public Long zCard(byte[] key) {
        return convertAndReturn(delegate.zCard(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zCount(byte[], double, double)
     */
    @Override
    public Long zCount(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zCount(this.addPrefix(key), min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zCount(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Long zCount(byte[] key, Range range) {
        return convertAndReturn(delegate.zCount(this.addPrefix(key), range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zIncrBy(byte[], double, byte[])
     */
    @Override
    public Double zIncrBy(byte[] key, double increment, byte[] value) {
        return convertAndReturn(delegate.zIncrBy(this.addPrefix(key), increment, value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zDiff(byte[][])
     */
    @Nullable
    @Override
    public Set<byte[]> zDiff(byte[]... sets) {
        return convertAndReturn(delegate.zDiff(this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zDiffWithScores(byte[][])
     */
    @Nullable
    @Override
    public Set<Tuple> zDiffWithScores(byte[]... sets) {
        return convertAndReturn(delegate.zDiffWithScores(this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zDiffStore(byte[], byte[][])
     */
    @Nullable
    @Override
    public Long zDiffStore(byte[] destKey, byte[]... sets) {
        return convertAndReturn(delegate.zDiffStore(this.addPrefix(destKey), this.addPrefix(sets)), Converters.identityConverter());
    }


    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zInter(byte[][])
     */
    @Nullable
    @Override
    public Set<byte[]> zInter(byte[]... sets) {
        return convertAndReturn(delegate.zInter(this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zInterWithScores(byte[][])
     */
    @Nullable
    @Override
    public Set<Tuple> zInterWithScores(byte[]... sets) {
        return convertAndReturn(delegate.zInterWithScores(this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zInterWithScores(org.springframework.data.redis.connection.RedisZSetCommands.Aggregate, org.springframework.data.redis.connection.RedisZSetCommands.Weights, byte[][])
     */
    @Nullable
    @Override
    public Set<Tuple> zInterWithScores(Aggregate aggregate, Weights weights, byte[]... sets) {
        return convertAndReturn(delegate.zInterWithScores(aggregate, weights, this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zInterStore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Aggregate, org.springframework.data.redis.connection.RedisZSetCommands.Weights, byte[][])
     */
    @Override
    public Long zInterStore(byte[] destKey, Aggregate aggregate, Weights weights, byte[]... sets) {
        return convertAndReturn(delegate.zInterStore(destKey, aggregate, weights, this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zInterStore(byte[], byte[][])
     */
    @Override
    public Long zInterStore(byte[] destKey, byte[]... sets) {
        return convertAndReturn(delegate.zInterStore(this.addPrefix(destKey), this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRange(byte[], long, long)
     */
    @Override
    public Set<byte[]> zRange(byte[] key, long start, long end) {
        return convertAndReturn(delegate.zRange(this.addPrefix(key), start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScore(byte[], double, double, long, long)
     */
    @Override
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return convertAndReturn(delegate.zRangeByScore(this.addPrefix(key), min, max, offset, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Set<byte[]> zRangeByScore(byte[] key, Range range) {
        return convertAndReturn(delegate.zRangeByScore(this.addPrefix(key), range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<byte[]> zRangeByScore(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRangeByScore(this.addPrefix(key), range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScoreWithScores(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, Range range) {
        return convertAndReturn(delegate.zRangeByScoreWithScores(this.addPrefix(key), range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScore(byte[], double, double)
     */
    @Override
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zRangeByScore(this.addPrefix(key), min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScoreWithScores(byte[], double, double, long, long)
     */
    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return convertAndReturn(delegate.zRangeByScoreWithScores(this.addPrefix(key), min, max, offset, count),
                Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScoreWithScores(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRangeByScoreWithScores(this.addPrefix(key), range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScoreWithScores(byte[], double, double)
     */
    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zRangeByScoreWithScores(this.addPrefix(key), min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeWithScores(byte[], long, long)
     */
    @Override
    public Set<Tuple> zRangeWithScores(byte[] key, long start, long end) {
        return convertAndReturn(delegate.zRangeWithScores(this.addPrefix(key), start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScore(byte[], double, double, long, long)
     */
    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return convertAndReturn(delegate.zRevRangeByScore(this.addPrefix(key), min, max, offset, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, Range range) {
        return convertAndReturn(delegate.zRevRangeByScore(this.addPrefix(key), range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScore(byte[], double, double)
     */
    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zRevRangeByScore(this.addPrefix(key), min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRevRangeByScore(this.addPrefix(key), range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScoreWithScores(byte[], double, double, long, long)
     */
    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return convertAndReturn(delegate.zRevRangeByScoreWithScores(this.addPrefix(key), min, max, offset, count),
                Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScoreWithScores(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, Range range) {
        return convertAndReturn(delegate.zRevRangeByScoreWithScores(this.addPrefix(key), range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScoreWithScores(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRevRangeByScoreWithScores(this.addPrefix(key), range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByScoreWithScores(byte[], double, double)
     */
    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zRevRangeByScoreWithScores(this.addPrefix(key), min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRank(byte[], byte[])
     */
    @Override
    public Long zRank(byte[] key, byte[] value) {
        return convertAndReturn(delegate.zRank(this.addPrefix(key), value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRem(byte[], byte[][])
     */
    @Override
    public Long zRem(byte[] key, byte[]... values) {
        return convertAndReturn(delegate.zRem(this.addPrefix(key), values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRemRange(byte[], long, long)
     */
    @Override
    public Long zRemRange(byte[] key, long start, long end) {
        return convertAndReturn(delegate.zRemRange(this.addPrefix(key), start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRemRangeByLex(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Long zRemRangeByLex(byte[] key, Range range) {
        return convertAndReturn(delegate.zRemRangeByLex(this.addPrefix(key), range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRemRangeByScore(byte[], double, double)
     */
    @Override
    public Long zRemRangeByScore(byte[] key, double min, double max) {
        return convertAndReturn(delegate.zRemRangeByScore(this.addPrefix(key), min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRemRangeByScore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Long zRemRangeByScore(byte[] key, Range range) {
        return convertAndReturn(delegate.zRemRangeByScore(this.addPrefix(key), range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRange(byte[], long, long)
     */
    @Override
    public Set<byte[]> zRevRange(byte[] key, long start, long end) {
        return convertAndReturn(delegate.zRevRange(this.addPrefix(key), start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeWithScores(byte[], long, long)
     */
    @Override
    public Set<Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
        return convertAndReturn(delegate.zRevRangeWithScores(this.addPrefix(key), start, end), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRank(byte[], byte[])
     */
    @Override
    public Long zRevRank(byte[] key, byte[] value) {
        return convertAndReturn(delegate.zRevRank(this.addPrefix(key), value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zScore(byte[], byte[])
     */
    @Override
    public Double zScore(byte[] key, byte[] value) {
        return convertAndReturn(delegate.zScore(this.addPrefix(key), value), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zMScore(byte[], byte[][])
     */
    @Override
    public List<Double> zMScore(byte[] key, byte[]... values) {
        return convertAndReturn(delegate.zMScore(this.addPrefix(key), values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zUnion(byte[][])
     */
    @Nullable
    @Override
    public Set<byte[]> zUnion(byte[]... sets) {
        return convertAndReturn(delegate.zUnion(this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zUnionWithScores(byte[][])
     */
    @Nullable
    @Override
    public Set<Tuple> zUnionWithScores(byte[]... sets) {
        return convertAndReturn(delegate.zUnionWithScores(this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zUnionWithScores(org.springframework.data.redis.connection.RedisZSetCommands.Aggregate, org.springframework.data.redis.connection.RedisZSetCommands.Weights, byte[][])
     */
    @Nullable
    @Override
    public Set<Tuple> zUnionWithScores(Aggregate aggregate, Weights weights, byte[]... sets) {
        return convertAndReturn(delegate.zUnionWithScores(aggregate, weights, this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zUnionStore(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Aggregate, org.springframework.data.redis.connection.RedisZSetCommands.Weights, byte[][])
     */
    @Override
    public Long zUnionStore(byte[] destKey, Aggregate aggregate, Weights weights, byte[]... sets) {
        return convertAndReturn(delegate.zUnionStore(this.addPrefix(destKey), aggregate, weights, this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zUnionStore(byte[], byte[][])
     */
    @Override
    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        return convertAndReturn(delegate.zUnionStore(this.addPrefix(destKey), this.addPrefix(sets)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#pExpire(byte[], long)
     */
    @Override
    public Boolean pExpire(byte[] key, long millis) {
        return convertAndReturn(delegate.pExpire(this.addPrefix(key), millis), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#pExpireAt(byte[], long)
     */
    @Override
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        return convertAndReturn(delegate.pExpireAt(this.addPrefix(key), unixTimeInMillis), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#pTtl(byte[])
     */
    @Override
    public Long pTtl(byte[] key) {
        return convertAndReturn(delegate.pTtl(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#pTtl(byte[], java.util.concurrent.TimeUnit)
     */
    @Override
    public Long pTtl(byte[] key, TimeUnit timeUnit) {
        return convertAndReturn(delegate.pTtl(this.addPrefix(key), timeUnit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#dump(byte[])
     */
    @Override
    public byte[] dump(byte[] key) {
        return convertAndReturn(delegate.dump(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisKeyCommands#restore(byte[], long, byte[], boolean)
     */
    @Override
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue, boolean replace) {
        delegate.restore(this.addPrefix(key), ttlInMillis, serializedValue, replace);
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
        return convertAndReturn(delegate.hRandField(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hRandFieldWithValues(byte[])
     */
    @Nullable
    @Override
    public Map.Entry<byte[], byte[]> hRandFieldWithValues(byte[] key) {
        return convertAndReturn(delegate.hRandFieldWithValues(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hRandField(byte[], long)
     */
    @Nullable
    @Override
    public List<byte[]> hRandField(byte[] key, long count) {
        return convertAndReturn(delegate.hRandField(this.addPrefix(key), count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hRandFieldWithValues(byte[], long)
     */
    @Nullable
    @Override
    public List<Map.Entry<byte[], byte[]>> hRandFieldWithValues(byte[] key, long count) {
        return convertAndReturn(delegate.hRandFieldWithValues(this.addPrefix(key), count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zLexCount(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Long zLexCount(byte[] key, Range range) {
        return delegate.zLexCount(this.addPrefix(key), range);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zPopMin(byte[])
     */
    @Nullable
    @Override
    public Tuple zPopMin(byte[] key) {
        return delegate.zPopMin(this.addPrefix(key));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zPopMinMin(byte[], count)
     */
    @Nullable
    @Override
    public Set<Tuple> zPopMin(byte[] key, long count) {
        return delegate.zPopMin(this.addPrefix(key), count);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#bZPopMin(byte[], long, java.util.concurrent.TimeUnit)
     */
    @Nullable
    @Override
    public Tuple bZPopMin(byte[] key, long timeout, TimeUnit unit) {
        return delegate.bZPopMin(this.addPrefix(key), timeout, unit);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zPopMax(byte[])
     */
    @Nullable
    @Override
    public Tuple zPopMax(byte[] key) {
        return delegate.zPopMax(this.addPrefix(key));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zPopMax(byte[], long)
     */
    @Nullable
    @Override
    public Set<Tuple> zPopMax(byte[] key, long count) {
        return delegate.zPopMax(this.addPrefix(key), count);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#bZPopMax(byte[], long, java.util.concurrent.TimeUnit)
     */
    @Nullable
    @Override
    public Tuple bZPopMax(byte[] key, long timeout, TimeUnit unit) {
        return delegate.bZPopMax(this.addPrefix(key), timeout, unit);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRandMember(byte[])
     */
    @Override
    public byte[] zRandMember(byte[] key) {
        return delegate.zRandMember(this.addPrefix(key));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRandMember(byte[], long)
     */
    @Override
    public List<byte[]> zRandMember(byte[] key, long count) {
        return delegate.zRandMember(this.addPrefix(key), count);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRandMemberWithScore(byte[])
     */
    @Override
    public Tuple zRandMemberWithScore(byte[] key) {
        return delegate.zRandMemberWithScore(this.addPrefix(key));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRandMemberWithScore(byte[], long)
     */
    @Override
    public List<Tuple> zRandMemberWithScore(byte[] key, long count) {
        return delegate.zRandMemberWithScore(this.addPrefix(key), count);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoAdd(byte[], org.springframework.data.geo.Point, byte[])
     */
    @Override
    public Long geoAdd(byte[] key, Point point, byte[] member) {
        return convertAndReturn(delegate.geoAdd(this.addPrefix(key), point, member), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoAdd(byte[], org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation)
     */
    @Override
    public Long geoAdd(byte[] key, GeoLocation<byte[]> location) {
        return convertAndReturn(delegate.geoAdd(this.addPrefix(key), location), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoAdd(byte[], java.util.Map)
     */
    @Override
    public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
        return convertAndReturn(delegate.geoAdd(this.addPrefix(key), memberCoordinateMap), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoAdd(byte[], java.lang.Iterable)
     */
    @Override
    public Long geoAdd(byte[] key, Iterable<GeoLocation<byte[]>> locations) {
        return convertAndReturn(delegate.geoAdd(this.addPrefix(key), locations), Converters.identityConverter());
    }


    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoDist(byte[], byte[], byte[])
     */
    @Override
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
        return convertAndReturn(delegate.geoDist(this.addPrefix(key), member1, member2), Converters.identityConverter());
    }


    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoDist(byte[], byte[], byte[], org.springframework.data.geo.Metric)
     */
    @Override
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
        return convertAndReturn(delegate.geoDist(this.addPrefix(key), member1, member2, metric), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoHash(byte[], byte[][])
     */
    @Override
    public List<String> geoHash(byte[] key, byte[]... members) {
        return convertAndReturn(delegate.geoHash(this.addPrefix(key), members), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoPos(byte[], byte[][])
     */
    @Override
    public List<Point> geoPos(byte[] key, byte[]... members) {
        return convertAndReturn(delegate.geoPos(this.addPrefix(key), members), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoRadius(byte[], org.springframework.data.geo.Circle)
     */
    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
        return convertAndReturn(delegate.geoRadius(this.addPrefix(key), within), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoRadius(byte[], org.springframework.data.geo.Circle, org.springframework.data.redis.core.GeoRadiusCommandArgs)
     */
    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, GeoRadiusCommandArgs args) {
        return convertAndReturn(delegate.geoRadius(this.addPrefix(key), within, args), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoRadiusByMember(byte[], byte[], double)
     */
    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, double radius) {
        return geoRadiusByMember(this.addPrefix(key), member, new Distance(radius, DistanceUnit.METERS));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoRadiusByMember(byte[], byte[], org.springframework.data.geo.Distance)
     */
    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius) {
        return convertAndReturn(delegate.geoRadiusByMember(this.addPrefix(key), member, radius), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoRadiusByMember(byte[], byte[], org.springframework.data.geo.Distance, org.springframework.data.redis.core.GeoRadiusCommandArgs)
     */
    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius,
                                                             GeoRadiusCommandArgs args) {
        return convertAndReturn(delegate.geoRadiusByMember(this.addPrefix(key), member, radius, args), Converters.identityConverter());
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
        return convertAndReturn(delegate.geoSearch(this.addPrefix(key), reference, predicate, args), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisGeoCommands#geoSearchStore(byte[], byte[], byte[], org.springframework.data.redis.connection.RedisGeoCommands.GeoShape, org.springframework.data.redis.connection.RedisGeoCommands.GeoSearchStoreCommandArgs)
     */
    @Override
    public Long geoSearchStore(byte[] destKey, byte[] key, GeoReference<byte[]> reference, GeoShape predicate,
                               GeoSearchStoreCommandArgs args) {
        return convertAndReturn(delegate.geoSearchStore(this.addPrefix(destKey), this.addPrefix(key), reference, predicate, args),
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
        byte[] pattern = serializer.serialize(nameMapper.map(options.getPattern()));

        if (options instanceof KeyScanOptions){
            options = ScanOptions.scanOptions().count(Fc.toLong(options.getCount(), 1000)).match(pattern).type(((KeyScanOptions) options).getType()).build();
        } else {
            options = ScanOptions.scanOptions().count(Fc.toLong(options.getCount(), 1000)).match(pattern).build();
        }

        Cursor<byte[]> cursor = this.delegate.scan(options);
        return new ConvertingCursor<>(cursor, key -> {
            String keyStr = serializer.deserialize(key);
            return serializer.serialize(nameMapper.unmap(keyStr));
        });
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zScan(byte[], org.springframework.data.redis.core.ScanOptions)
     */
    @Override
    public Cursor<Tuple> zScan(byte[] key, ScanOptions options) {
        return this.delegate.zScan(this.addPrefix(key), options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#scan(byte[], org.springframework.data.redis.core.ScanOptions)
     */
    @Override
    public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
        return this.delegate.sScan(this.addPrefix(key), options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHashCommands#hscan(byte[], org.springframework.data.redis.core.ScanOptions)
     */
    @Override
    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
        return this.delegate.hScan(this.addPrefix(key), options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSetCommands#hStrLen(byte[], byte[])
     */
    @Nullable
    @Override
    public Long hStrLen(byte[] key, byte[] field) {
        return convertAndReturn(delegate.hStrLen(this.addPrefix(key), field), Converters.identityConverter());
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
        return convertAndReturn(delegate.zRangeByScore(this.addPrefix(key), min, max), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByScore(byte[], java.lang.String, java.lang.String, long, long)
     */
    @Override
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
        return convertAndReturn(delegate.zRangeByScore(this.addPrefix(key), min, max, offset, count), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHyperLogLogCommands#pfAdd(byte[], byte[][])
     */
    @Override
    public Long pfAdd(byte[] key, byte[]... values) {
        return convertAndReturn(delegate.pfAdd(this.addPrefix(key), values), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHyperLogLogCommands#pfCount(byte[][])
     */
    @Override
    public Long pfCount(byte[]... keys) {
        return convertAndReturn(delegate.pfCount(this.addPrefix(keys)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisHyperLogLogCommands#pfMerge(byte[], byte[][])
     */
    @Override
    public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
        delegate.pfMerge(this.addPrefix(destinationKey), this.addPrefix(sourceKeys));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByLex(byte[])
     */
    @Override
    public Set<byte[]> zRangeByLex(byte[] key) {
        return convertAndReturn(delegate.zRangeByLex(this.addPrefix(key)), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByLex(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range)
     */
    @Override
    public Set<byte[]> zRangeByLex(byte[] key, Range range) {
        return convertAndReturn(delegate.zRangeByLex(this.addPrefix(key), range), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRangeByLex(byte[], org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<byte[]> zRangeByLex(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRangeByLex(this.addPrefix(key), range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisZSetCommands#zRevRangeByLex(java.lang.String, org.springframework.data.redis.connection.RedisZSetCommands.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public Set<byte[]> zRevRangeByLex(byte[] key, Range range, Limit limit) {
        return convertAndReturn(delegate.zRevRangeByLex(this.addPrefix(key), range, limit), Converters.identityConverter());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#migrate(byte[], org.springframework.data.redis.connection.RedisNode, int, org.springframework.data.redis.connection.RedisServerCommands.MigrateOption)
     */
    @Override
    public void migrate(byte[] key, RedisNode target, int dbIndex, @Nullable MigrateOption option) {
        byte[][] keys = this.scanAllPrefixForKey(key);
        for (byte[] k : keys) {
            delegate.migrate(k, target, dbIndex, option);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisServerCommands#migrate(byte[], org.springframework.data.redis.connection.RedisNode, int, org.springframework.data.redis.connection.RedisServerCommands.MigrateOption, long)
     */
    @Override
    public void migrate(byte[] key, RedisNode target, int dbIndex, @Nullable MigrateOption option, long timeout) {
        byte[][] keys = this.scanAllPrefixForKey(key);
        for (byte[] k : keys) {
            delegate.migrate(k, target, dbIndex, option, timeout);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xAck(byte[], java.lang.String, java.lang.String[])
     */
    @Override
    public Long xAck(byte[] key, String group, RecordId... recordIds) {
        return delegate.xAck(this.addPrefix(key), group, recordIds);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xAdd(MapRecord, XAddOptions)
     */
    @Override
    public RecordId xAdd(MapRecord<byte[], byte[], byte[]> record, XAddOptions options) {
        record = StreamRecords.newRecord().in(this.addPrefix(record.getStream())).withId(record.getId()).ofMap(record.getValue());
        return delegate.xAdd(record, options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xClaimJustId(byte[], java.lang.String, java.lag.String, org.springframework.data.redis.connection.RedisStreamCommands.XCLaimOptions)
     */
    @Override
    public List<RecordId> xClaimJustId(byte[] key, String group, String newOwner, XClaimOptions options) {
        return delegate.xClaimJustId(this.addPrefix(key), group, newOwner, options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xClaim(byte[], java.lang.String, java.lag.String, org.springframework.data.redis.connection.RedisStreamCommands.XCLaimOptions)
     */
    @Override
    public List<ByteRecord> xClaim(byte[] key, String group, String newOwner, XClaimOptions options) {
        return delegate.xClaim(this.addPrefix(key), group, newOwner, options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xDel(byte[], RecordId)
     */
    @Override
    public Long xDel(byte[] key, RecordId... recordIds) {
        return delegate.xDel(this.addPrefix(key), recordIds);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xGroupCreate(byte[], org.springframework.data.redis.connection.RedisStreamCommands.ReadOffset, java.lang.String)
     */
    @Override
    public String xGroupCreate(byte[] key, String groupName, ReadOffset readOffset) {
        return delegate.xGroupCreate(this.addPrefix(key), groupName, readOffset);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xGroupCreate(byte[], org.springframework.data.redis.connection.RedisStreamCommands.ReadOffset, java.lang.String, boolean)
     */
    @Override
    public String xGroupCreate(byte[] key, String groupName, ReadOffset readOffset, boolean mkStream) {
        return delegate.xGroupCreate(this.addPrefix(key), groupName, readOffset, mkStream);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xGroupDelConsumer(byte[], org.springframework.data.redis.connection.RedisStreamCommands.Consumer)
     */
    @Override
    public Boolean xGroupDelConsumer(byte[] key, Consumer consumer) {
        return delegate.xGroupDelConsumer(this.addPrefix(key), consumer);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xGroupDestroy(byte[], java.lang.String)
     */
    @Override
    public Boolean xGroupDestroy(byte[] key, String groupName) {
        return delegate.xGroupDestroy(this.addPrefix(key), groupName);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xInfo(byte[])
     */
    @Override
    public StreamInfo.XInfoStream xInfo(byte[] key) {
        return delegate.xInfo(this.addPrefix(key));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xInfoGroups(byte[])
     */
    @Override
    public StreamInfo.XInfoGroups xInfoGroups(byte[] key) {
        return delegate.xInfoGroups(this.addPrefix(key));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xInfoConsumers(byte[], java.lang.String)
     */
    @Override
    public StreamInfo.XInfoConsumers xInfoConsumers(byte[] key, String groupName) {
        return delegate.xInfoConsumers(this.addPrefix(key), groupName);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xLen(byte[])
     */
    @Override
    public Long xLen(byte[] key) {
        return delegate.xLen(this.addPrefix(key));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xPending(byte[], java.lang.String)
     */
    @Override
    public PendingMessagesSummary xPending(byte[] key, String groupName) {
        return delegate.xPending(this.addPrefix(key), groupName);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xPending(byte[], java.lang.String)
     */
    @Override
    public PendingMessages xPending(byte[] key, String groupName, XPendingOptions options) {
        return delegate.xPending(this.addPrefix(key), groupName, options);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xRange(byte[], org.springframework.data.domain.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public List<ByteRecord> xRange(byte[] key, org.springframework.data.domain.Range<String> range, Limit limit) {
        return delegate.xRange(this.addPrefix(key), range, limit);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xRead(org.springframework.data.redis.connection.RedisStreamCommands.StreamReadOptions, org.springframework.data.redis.connection.RedisStreamCommands.StreamOffset[])
     */
    @Override
    public List<ByteRecord> xRead(StreamReadOptions readOptions, StreamOffset<byte[]>... streams) {
        streams = Arrays.stream(streams)
                .map(it -> StreamOffset.create(this.addPrefix(it.getKey()), it.getOffset()))
                .toArray(it -> new StreamOffset[it]);
        return delegate.xRead(readOptions, streams);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xReadGroup(org.springframework.data.redis.connection.RedisStreamCommands.Consumer, org.springframework.data.redis.connection.RedisStreamCommands.StreamReadOptions, org.springframework.data.redis.connection.RedisStreamCommands.StreamOffset[])
     */
    @Override
    public List<ByteRecord> xReadGroup(Consumer consumer, StreamReadOptions readOptions,
                                       StreamOffset<byte[]>... streams) {
        streams = Arrays.stream(streams)
                .map(it -> StreamOffset.create(this.addPrefix(it.getKey()), it.getOffset())) //
                .toArray(it -> new StreamOffset[it]);
        return delegate.xReadGroup(consumer, readOptions, streams);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xRevRange(byte[], org.springframework.data.domain.Range, org.springframework.data.redis.connection.RedisZSetCommands.Limit)
     */
    @Override
    public List<ByteRecord> xRevRange(byte[] key, org.springframework.data.domain.Range<String> range, Limit limit) {
        return delegate.xRevRange(this.addPrefix(key), range, limit);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xTrim(byte[], long)
     */
    @Override
    public Long xTrim(byte[] key, long count) {
        return xTrim(this.addPrefix(key), count, false);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStreamCommands#xTrim(byte[], long, boolean)
     */
    @Override
    public Long xTrim(byte[] key, long count, boolean approximateTrimming) {
        return delegate.xTrim(this.addPrefix(key), count, approximateTrimming);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisStringCommands#bitfield(byte[], BitfieldCommand)
     */
    @Override
    public List<Long> bitField(byte[] key, BitFieldSubCommands subCommands) {
        return delegate.bitField(this.addPrefix(key), subCommands);
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
