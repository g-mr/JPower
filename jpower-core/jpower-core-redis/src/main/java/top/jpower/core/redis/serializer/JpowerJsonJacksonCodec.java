package top.jpower.core.redis.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.IOException;

/**
 * @author mr.g
 * @date 2024-10-27 16:11
 * @description
 */
public class JpowerJsonJacksonCodec extends BaseCodec {

    protected final RedisSerializer<String> redisStringSerializer;
    protected final RedisSerializer<Object> redisSerializer;

    private Encoder encoderString;

    private Decoder<Object> decoderString;

    private Encoder encoder;

    private Decoder<Object> decoder;

    public JpowerJsonJacksonCodec(RedisSerializer<String> keySerializer, RedisSerializer<Object> valueSerializer) {
        this.redisStringSerializer = keySerializer;
        this.redisSerializer = valueSerializer;

        initCoder();
    }

    protected void initCoder() {
        encoderString = in -> {
            ByteBuf out = ByteBufAllocator.DEFAULT.buffer();

            try {
                return out.writeBytes(redisStringSerializer.serialize(in.toString()));
            } catch (Exception io){
                out.release();
                throw io;
            }
        };

        decoderString = (buf, state) -> {
            byte[] byteArray = new byte[buf.readableBytes()];
            buf.readBytes(byteArray);
            return redisStringSerializer.deserialize(byteArray);
        };

        encoder = in -> {
            ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
            try {
                redisSerializer.serialize(in);
                return out.writeBytes(redisSerializer.serialize(in));
            } catch (Exception e) {
                out.release();
                throw new IOException(e);
            }
        };

        decoder = (buf, state) -> {
            byte[] byteArray = new byte[buf.readableBytes()];
            buf.readBytes(byteArray);
            return redisSerializer.deserialize(byteArray);
        };
    }

    @Override
    public Decoder<Object> getMapKeyDecoder() {
        return decoderString;
    }

    @Override
    public Encoder getMapKeyEncoder() {
        return encoderString;
    }

    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }

}
