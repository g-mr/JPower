package top.jpower.core.redis.log;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.RedisCommand;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.redis.serializer.CodecRedisSerializer;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.DateUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * Redis日志实现类
 *
 * @author mr.g
 * @date 2024-11-7 22:10
 */
@Slf4j
@RequiredArgsConstructor
public class RedisLog {

    private final RedisProperties redisProperties;
    private final CodecRedisSerializer redisSerializer;

    private final Integer keyType = 1;
    private final Integer valueType = 2;

    /**
     * 代理发送Redis命令，答应日志
     *
     * @author mr.g
     * @param command 命令
     * @param params 参数
     * @param mainPromise 返回数据
     * @param supplier 发送
     * @return io.netty.channel.ChannelFuture
     **/
    public <V, R> ChannelFuture broker(RedisCommand<V> command, Object[] params, CompletableFuture<R> mainPromise, Supplier<ChannelFuture> supplier) {

        if (!redisProperties.getLog()){
            return supplier.get();
        }

        ChannelFuture writeFuture = null;
        Throwable error = null;
        TimeInterval timeInterval = DateUtil.timer(true);
        timeInterval.start();
        try {
            writeFuture = supplier.get();
            return writeFuture;
        } catch (Throwable throwable) {
            error = throwable;
            throw throwable;
        } finally {
            printLog(command, params, writeFuture, mainPromise, timeInterval, error);
        }

    }

    /**
     * 打印日志
     *
     * @author mr.g
     * @param command 命令
     * @param params 参数
     * @param writeFuture 执行结果
     * @param mainPromise 返回数据
     * @param timeInterval 计时器
     * @param error 错误
     **/
    private <V, R> void printLog(RedisCommand<V> command, Object[] params, ChannelFuture writeFuture, CompletableFuture<R> mainPromise, TimeInterval timeInterval, Throwable error) {
        ThreadUtil.execute(() -> {

            try {
                StringBuilder builder = new StringBuilder(StringPool.NEWLINE);

                builder.append("===========START REDIS==============").append(StringPool.NEWLINE);

                builder.append(StringPool.SPACE).append("-->Execute: ").append(getExecute(command, params));
                builder.append(StringPool.NEWLINE);
                if (error != null){
                    builder.append(StringPool.SPACE).append("-->Error: ").append(error.getMessage());
                } else {
                    builder.append(StringPool.SPACE).append("<--Success: ").append(getIsSuccess(writeFuture, mainPromise));
                    builder.append(StringPool.NEWLINE);
                    builder.append(StringPool.SPACE).append("<--Result: ");
                    try {
                        builder.append(convert(command.getName(), mainPromise.toCompletableFuture().get(), valueType));
                    } catch (InterruptedException e) {
                        builder.append("命令被中断=>").append(e.getMessage());
                    } catch (ExecutionException e) {
                        builder.append("命令发生异常=>").append(e.getMessage());
                    } catch (CancellationException e){
                        builder.append("命令被取消=>").append(e.getMessage());
                    }
                }
                builder.append(StringPool.NEWLINE);
                builder.append(StringPool.SPACE).append("<--Time: ").append(timeInterval.intervalPretty());
                builder.append(StringPool.NEWLINE);
                builder.append("=========== END REDIS ==============");

                log.info(builder.toString());
            } catch (Exception e) {
                log.error("Redis打印报错==》{}", e.getMessage());
            }

        });
    }

    /**
     * 获取是否执行成功
     *
     * @author mr.g
     * @param writeFuture 执行结果
     * @return 是否成功
     **/
    private <R> boolean getIsSuccess(ChannelFuture writeFuture, CompletableFuture<R> mainPromise) {
        TimeInterval timeInterval = DateUtil.timer();
        timeInterval.start();
        while (true){
            if (writeFuture.isDone() || timeInterval.intervalSecond() > 1){
                return writeFuture.isSuccess() && !mainPromise.isCompletedExceptionally();
            }
        }
    }

    /**
     * 获取执行命令
     *
     * @author mr.g
     * @param command 命令
     * @param params 参数
     * @return 命令
     **/
    private <V> String getExecute(RedisCommand<V> command, Object[] params) {
        StringBuilder builder = new StringBuilder(command.getName());


        for (int i = 0; i < params.length; i++) {

            if ((i+1) == params.length && StringUtil.containsAnyIgnoreCase(command.getName(), "SET", "PUSH", "ADD")){
                builder.append(StringPool.SPACE).append(convert(command.getName(), params[i], valueType));
            } else {
                builder.append(StringPool.SPACE).append(convert(command.getName(), params[i], keyType));
            }
        }

        for (Object param : params) {
            builder.append(StringPool.SPACE).append(convert(command.getName(), param, keyType));
        }
        return builder.toString();
    }

    /**
     * 数据转换
     *
     * @author mr.g
     * @param param 参数
     * @return 转换结果
     **/
    @SneakyThrows
    private Object convert(String oper, Object param, Integer type) {
        if (Fc.isNull(param)){
            return "";
        }
        if (param instanceof byte[]){
            if (Fc.equalsValue(type, keyType)){
                param = redisSerializer.getKeyRedisSerializer().deserialize((byte[]) param);
            } else {
                if (StringUtil.containsIgnoreCase(oper, "KEYS")){
                    param = redisSerializer.getKeyRedisSerializer().deserialize((byte[]) param);
                } else {
                    param = redisSerializer.getValueRedisSerializer().deserialize((byte[]) param);
                }
            }
        } else if (param instanceof ByteBuf) {
            if (Fc.equalsValue(type, keyType)){
                param = redisSerializer.getMapKeyDecoder().decode((ByteBuf) param, new State());
            } else {
                if (StringUtil.containsIgnoreCase(oper, "KEYS")){
                    param = redisSerializer.getMapKeyDecoder().decode((ByteBuf) param, new State());
                } else {
                    param = redisSerializer.getValueDecoder().decode((ByteBuf) param, new State());
                }
            }
        }
        return param;
    }
}
