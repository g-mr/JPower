package top.jpower.core.redis.log;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.client.protocol.RedisCommand;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.DateUtil;
import top.jpower.core.util.utils.Fc;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
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
    private final RedisSerializer<String> redisSerializer;

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
                    builder.append(StringPool.SPACE).append("<--Success: ").append(getIsSuccess(writeFuture));
                    builder.append(StringPool.NEWLINE);
                    builder.append(StringPool.SPACE).append("<--Result: ").append(convert(mainPromise.toCompletableFuture().get()));
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
    private boolean getIsSuccess(ChannelFuture writeFuture) {
        TimeInterval timeInterval = DateUtil.timer();
        timeInterval.start();
        while (true){
            if (writeFuture.isDone() || timeInterval.intervalSecond() > 1){
                return writeFuture.isSuccess();
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
        for (Object param : params) {
            builder.append(StringPool.SPACE).append(convert(param));
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
    private Object convert(Object param) {
        if (Fc.isNull(param)){
            return "";
        }

        if (param instanceof byte[]){
            param = redisSerializer.deserialize((byte[]) param);
        } else if (param instanceof ByteBuf) {
            param = ((ByteBuf) param).toString(StandardCharsets.UTF_8);
        }
        return param;
    }
}
