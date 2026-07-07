package top.jpower.core.asterisk.audio;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * WAV 文件写入器
 * <br/>
 * 支持流式边写边播场景（Asterisk 会读取到文件 EOF 为止）。
 * 初始化时写入占位 WAV 头（data size = 0x7FFFFFFF），
 * close() 时通过 updateHeader() 更新为真实数据长度。
 * <br/>
 * 此工具类由框架层统一管理文件 I/O，TTS 实现类只需往 {@link OutputStream} 写入 PCM 原始数据。
 */
public class WavWriter implements AutoCloseable {

    /** 流式播放时使用的最大数据长度，Asterisk 会读取到文件 EOF 为止 */
    private static final int MAX_DATA_SIZE = 0x7FFFFFFF;

    private final FileOutputStream fos;
    private final String filePath;
    private final int sampleRate;
    private final int numChannels;
    private final int bitsPerSample;
    private long dataSize = 0;

    @Getter
    private boolean closed = false;

    @SneakyThrows
    public WavWriter(String filePath, int sampleRate, int numChannels, int bitsPerSample) {
        this.filePath = filePath;
        this.sampleRate = sampleRate;
        this.numChannels = numChannels;
        this.bitsPerSample = bitsPerSample;
        this.fos = new FileOutputStream(filePath);
        writeWavHeader();
    }

    /**
     * 写入 WAV 文件头（44字节），使用最大数据长度支持流式播放。
     */
    private void writeWavHeader() throws IOException {
        int byteRate = sampleRate * numChannels * bitsPerSample / 8;
        short blockAlign = (short) (numChannels * bitsPerSample / 8);

        ByteBuffer bb = ByteBuffer.allocate(44);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        // RIFF chunk descriptor
        bb.put("RIFF".getBytes());
        bb.putInt(36 + MAX_DATA_SIZE);
        bb.put("WAVE".getBytes());

        // fmt sub-chunk
        bb.put("fmt ".getBytes());
        bb.putInt(16);
        bb.putShort((short) 1);         // PCM
        bb.putShort((short) numChannels);
        bb.putInt(sampleRate);
        bb.putInt(byteRate);
        bb.putShort(blockAlign);
        bb.putShort((short) bitsPerSample);

        // data sub-chunk
        bb.put("data".getBytes());
        bb.putInt(MAX_DATA_SIZE);

        fos.write(bb.array());
        fos.flush();
    }

    /**
     * 追加 PCM 音频数据
     */
    public void write(byte[] audioData) throws IOException {
        fos.write(audioData);
        fos.flush();
        dataSize += audioData.length;
    }

    /**
     * 追加 PCM 音频数据（带偏移和长度）
     */
    public void write(byte[] audioData, int off, int len) throws IOException {
        fos.write(audioData, off, len);
        fos.flush();
        dataSize += len;
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            closed = true;
            updateHeader();
            fos.close();
        }
    }

    /**
     * 更新 WAV 文件头为真实数据长度
     */
    private void updateHeader() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            raf.seek(0);

            raf.write("RIFF".getBytes());
            raf.writeInt(Integer.reverseBytes((int) (36 + dataSize)));
            raf.write("WAVE".getBytes());

            raf.write("fmt ".getBytes());
            raf.writeInt(Integer.reverseBytes(16));
            raf.writeShort(Short.reverseBytes((short) 1));
            raf.writeShort(Short.reverseBytes((short) numChannels));
            raf.writeInt(Integer.reverseBytes(sampleRate));
            int byteRate = sampleRate * numChannels * bitsPerSample / 8;
            raf.writeInt(Integer.reverseBytes(byteRate));
            raf.writeShort(Short.reverseBytes((short) (numChannels * bitsPerSample / 8)));
            raf.writeShort(Short.reverseBytes((short) bitsPerSample));

            raf.write("data".getBytes());
            raf.writeInt(Integer.reverseBytes((int) dataSize));
        }
    }
}
