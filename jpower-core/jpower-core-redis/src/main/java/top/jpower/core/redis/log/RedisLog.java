package top.jpower.core.redis.log;

import cn.hutool.core.date.TimeInterval;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.RedisCommand;
import org.slf4j.MDC;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.redis.serializer.CodecRedisSerializer;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.DateUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;

import java.util.Map;
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
     * @param writeFutureError 错误
     **/
    private <V, R> void printLog(RedisCommand<V> command, Object[] params, ChannelFuture writeFuture, CompletableFuture<R> mainPromise, TimeInterval timeInterval, Throwable writeFutureError) {

        try {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();

            mainPromise.whenCompleteAsync((result, ex) -> {

                // 合并错误信息（初始错误和完成时错误）
                Throwable error = writeFutureError != null ? writeFutureError : ex;

                StringBuilder builder = new StringBuilder(StringPool.NEWLINE);

                builder.append("===========START REDIS==============").append(StringPool.NEWLINE);

                builder.append(StringPool.SPACE).append("-->Execute: ").append(getExecute(command, params));
                builder.append(StringPool.NEWLINE);
                if (error != null) {
                    builder.append(StringPool.SPACE).append("-->Error");
                    if (error instanceof InterruptedException) {
                        builder.append("[").append("命令被中断").append("]");
                    } else if (error instanceof ExecutionException) {
                        builder.append("[").append("命令发生异常").append("]");
                    } else if (error instanceof CancellationException) {
                        builder.append("[").append("命令被取消").append("]");
                    }
                    builder.append(": ").append(error.getMessage());
                } else {
                    builder.append(StringPool.SPACE).append("<--Success: ").append(writeFuture.isSuccess());
                    builder.append(StringPool.NEWLINE);
                    builder.append(StringPool.SPACE).append("<--Result: ");
                    try {
                        builder.append(convert(command.getName(), result, valueType));
                    } catch (Exception e) {
                        builder.append("结果序列化错误=>").append(e.getMessage());
                    }
                }
                builder.append(StringPool.NEWLINE);
                builder.append(StringPool.SPACE).append("<--Time: ").append(timeInterval.intervalPretty());
                builder.append(StringPool.NEWLINE);
                builder.append("=========== END REDIS ==============");

                log.info(builder.toString());

            }, runnable -> {
                try {
                    // 2. 将MDC上下文注入到新线程
                    if (contextMap != null) {
                        MDC.setContextMap(contextMap);
                    }
                    // 3. 执行原始任务
                    runnable.run();
                } finally {
                    // 4. 清理新线程的MDC上下文
                    MDC.clear();
                }
            });

        } catch (Exception e) {
            log.error("Redis[{}命令]打印报错==》{}", command.getName(), e.getMessage());
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

        return builder.toString();
    }

    /**
     * 数据转换
     *
     * @author mr.g
     * @param param 参数
     * @return 转换结果
     **/
    @SneakyThrows(Exception.class)
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
            ByteBuf buf = getBufferSnapshot((ByteBuf) param);
            try {
                if (Fc.equalsValue(type, keyType)){
                    param = redisSerializer.getMapKeyDecoder().decode(buf, new State());
                } else {
                    if (StringUtil.containsIgnoreCase(oper, "KEYS")){
                        param = redisSerializer.getMapKeyDecoder().decode(buf, new State());
                    } else {
                        param = redisSerializer.getValueDecoder().decode(buf, new State());
                    }
                }
            } finally {
                // 堆缓冲区（无需释放）
                if (buf.hasMemoryAddress()){
                    ReferenceCountUtil.safeRelease(buf);
                }
            }
        }
        return param;
    }

    /**
     * 安全获取缓冲区快照（核心安全机制）
     */
    private ByteBuf getBufferSnapshot(ByteBuf buf) {
        // 情况1：缓冲区有效且可访问
        if (buf.refCnt() > 0) {
            try {
                // 创建零拷贝切片（高效）
                return buf.retainedSlice();
            } catch (IllegalReferenceCountException e) {
                // 多线程竞争下可能发生，回退到复制方案
                log.debug("由于引用计数问题，切片失败，改用 copy");
                return Unpooled.copiedBuffer(buf);
            }
        }

        // 情况2：缓冲区已释放
        log.debug("尝试使用已发布的 ByteBuf，使用安全恢复");
        // 从原始字节恢复（即使缓冲区已释放）
        byte[] bytes = ByteBufUtil.getBytes(
                buf,
                buf.readerIndex(),
                buf.readableBytes(),
                false // 不修改原始缓冲区状态
        );
        return Unpooled.wrappedBuffer(bytes);
    }

}
