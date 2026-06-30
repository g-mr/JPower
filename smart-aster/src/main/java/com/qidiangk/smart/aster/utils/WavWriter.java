package com.qidiangk.smart.aster.utils;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WavWriter {
    /** 流式播放时使用的最大数据长度，Asterisk会读取到文件EOF为止 */
    private static final int MAX_DATA_SIZE = 0x7FFFFFFF;

    private final FileOutputStream fos;
    private final String filePath;  // 保存文件路径
    private final int sampleRate;   // 采样率（如8000）
    private final int numChannels;  // 声道数（1-单声道，2-立体声）
    private final int bitsPerSample; // 位深度（16或8）
    private long dataSize = 0;      // 音频数据总字节数

    @Getter
    private boolean isClose = false;

    // 初始化并写入有效WAV文件头
    @SneakyThrows
    public WavWriter(String filePath, int sampleRate, int numChannels, int bitsPerSample) {
        this.filePath = filePath;
        this.sampleRate = sampleRate;
        this.numChannels = numChannels;
        this.bitsPerSample = bitsPerSample;
        this.fos = new FileOutputStream(filePath);
        writeWavHeader(); // 写入有效的RIFF/WAVE文件头（支持流式边写边播）
    }

    /**
     * 写入有效的WAV文件头（44字节）。
     * <br/>
     * 使用最大数据长度（0x7FFFFFFF），使Asterisk在流式播放时能持续读取到文件EOF。
     * close()时会通过updateHeader()更新为真实数据长度。
     */
    private void writeWavHeader() throws IOException {
        int byteRate = sampleRate * numChannels * bitsPerSample / 8;
        short blockAlign = (short) (numChannels * bitsPerSample / 8);

        ByteBuffer bb = ByteBuffer.allocate(44);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        // RIFF chunk descriptor
        bb.put("RIFF".getBytes());
        bb.putInt(36 + MAX_DATA_SIZE);  // chunkSize（文件总大小-8）
        bb.put("WAVE".getBytes());

        // fmt sub-chunk
        bb.put("fmt ".getBytes());
        bb.putInt(16);                  // subchunk1Size
        bb.putShort((short) 1);         // audioFormat = PCM
        bb.putShort((short) numChannels);
        bb.putInt(sampleRate);
        bb.putInt(byteRate);
        bb.putShort(blockAlign);
        bb.putShort((short) bitsPerSample);

        // data sub-chunk
        bb.put("data".getBytes());
        bb.putInt(MAX_DATA_SIZE);       // subchunk2Size（流式播放时使用最大值）

        fos.write(bb.array());
        fos.flush();
    }

    // 追加音频数据（byte[]）
    public void write(byte[] audioData) throws IOException {
        fos.write(audioData);
        fos.flush();
        dataSize += audioData.length; // 更新总数据大小
    }

    // 完成写入并更新文件头
    public void close() throws IOException {
        if (!isClose){
            isClose = true;
            updateHeader(); // 更新文件头
            fos.close();
        }
    }

    // 更新WAV文件头（使用真实参数）
    private void updateHeader() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            raf.seek(0); // 回到文件开头

            // RIFF块
            raf.write("RIFF".getBytes());
            raf.writeInt(Integer.reverseBytes((int) (36 + dataSize))); // 文件总大小-8
            raf.write("WAVE".getBytes());

            // fmt子块
            raf.write("fmt ".getBytes());
            raf.writeInt(Integer.reverseBytes(16)); // fmt块长度
            raf.writeShort(Short.reverseBytes((short) 1)); // PCM格式
            raf.writeShort(Short.reverseBytes((short) numChannels));
            raf.writeInt(Integer.reverseBytes(sampleRate));
            int byteRate = sampleRate * numChannels * bitsPerSample / 8;
            raf.writeInt(Integer.reverseBytes(byteRate));
            raf.writeShort(Short.reverseBytes((short) (numChannels * bitsPerSample / 8))); // 块对齐
            raf.writeShort(Short.reverseBytes((short) bitsPerSample));

            // data子块
            raf.write("data".getBytes());
            raf.writeInt(Integer.reverseBytes((int) dataSize)); // 音频数据大小
        }
    }

}