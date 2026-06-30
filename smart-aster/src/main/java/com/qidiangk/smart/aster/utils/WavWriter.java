package com.qidiangk.smart.aster.utils;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WavWriter {
    private final FileOutputStream fos;
    private final String filePath;  // 新增：保存文件路径
    private final int sampleRate;   // 采样率（如44100）
    private final int numChannels;  // 声道数（1-单声道，2-立体声）
    private final int bitsPerSample; // 位深度（16或8）
    private long dataSize = 0;      // 音频数据总字节数

    @Getter
    private boolean isClose = false;

    // 初始化并写入临时文件头
    @SneakyThrows
    public WavWriter(String filePath, int sampleRate, int numChannels, int bitsPerSample) {
        this.filePath = filePath;  // 保存文件路径
        this.sampleRate = sampleRate;
        this.numChannels = numChannels;
        this.bitsPerSample = bitsPerSample;
        this.fos = new FileOutputStream(filePath);
        writeTempHeader(); // 写入占位文件头
    }

    // 写入临时文件头（44字节占位）
    private void writeTempHeader() throws IOException {
        fos.write(new byte[44]); // 后续会替换为真实文件头
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