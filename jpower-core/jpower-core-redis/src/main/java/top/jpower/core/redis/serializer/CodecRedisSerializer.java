package top.jpower.core.redis.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.redisson.client.codec.Codec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import top.jpower.core.util.utils.Fc;

/**
 * 序列化
 *
 * @author mr.g
 * @date 2025-6-8 17:38
 * @description
 */
@RequiredArgsConstructor
public class CodecRedisSerializer implements Codec {


    private final Codec codec;


    @Override
    public Decoder<Object> getMapValueDecoder() {
        return codec.getMapValueDecoder();
    }

    @Override
    public Encoder getMapValueEncoder() {
        return codec.getMapValueEncoder();
    }

    @Override
    public Decoder<Object> getMapKeyDecoder() {
        return codec.getMapKeyDecoder();
    }

    @Override
    public Encoder getMapKeyEncoder() {
        return codec.getMapKeyEncoder();
    }

    @Override
    public Decoder<Object> getValueDecoder() {
        return codec.getValueDecoder();
    }

    @Override
    public Encoder getValueEncoder() {
        return codec.getValueEncoder();
    }


    public RedisSerializer<String> getKeyRedisSerializer() {

        Encoder encoder = getMapKeyEncoder();
        Decoder<Object> decoder = getMapKeyDecoder();

        return new RedisSerializer<String>() {
            @SneakyThrows
            @Override
            public byte[] serialize(String obj) throws SerializationException {
                if (Fc.isNull(obj)){
                    return null;
                }
                ByteBuf byteBuf = encoder.encode(obj);

                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                byteBuf.resetReaderIndex(); // 如果需要再次读取，重置读取索引
                byteBuf.release(); // 确保释放ByteBuf资源

                return bytes;
            }

            @SneakyThrows
            @Override
            public String deserialize(byte[] bytes) throws SerializationException {
                if (Fc.isNull(bytes)){
                    return null;
                }
                Object obj = decoder.decode(Unpooled.wrappedBuffer(bytes), new State());
                if (Fc.notNull(obj)){
                    return obj.toString();
                } else {
                    return null;
                }
            }
        };

    }

    public RedisSerializer<Object> getValueRedisSerializer() {

        Encoder encoder = getValueEncoder();
        Decoder<Object> decoder = getValueDecoder();

        return new RedisSerializer<Object>() {
            @SneakyThrows
            @Override
            public byte[] serialize(Object obj) throws SerializationException {
                if (Fc.isNull(obj)){
                    return null;
                }
                ByteBuf byteBuf = encoder.encode(obj);

                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                byteBuf.resetReaderIndex(); // 如果需要再次读取，重置读取索引
                byteBuf.release(); // 确保释放ByteBuf资源

                return bytes;
            }

            @SneakyThrows
            @Override
            public Object deserialize(byte[] bytes) throws SerializationException {
                if (Fc.isNull(bytes)){
                    return null;
                }
                return decoder.decode(Unpooled.wrappedBuffer(bytes), new State());
            }
        };

    }

    @Override
    public ClassLoader getClassLoader() {
        return codec.getClassLoader();
    }

}
