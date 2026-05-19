package top.jpower.core.asterisk.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteToAudioByte {

    /**
     * 将字节数组转换为浮点数数组
     *
     * @param audioBytes 音频字节数据
     * @param length     有效数据长度
     * @return 浮点数数组
     */
    public static byte[] bytesToBytes(byte[] audioBytes, int length) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(audioBytes, 0, length);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN); // WAV 文件通常是小端序

        int numSamples = length / 2; // 每个样本 2 字节
        float[] floatArray = new float[numSamples];

        for (int i = 0; i < numSamples; i++) {
            short sample = byteBuffer.getShort(); // 读取 16 位样本
            floatArray[i] = sample / 32768.0f; // 归一化到 [-1.0, 1.0]
        }

        return floatArrayToBytes(floatArray);
    }

    /**
     * 将浮点数数组转换为字节数组
     *
     * @param floatArray 浮点数数组
     * @return 字节数组
     */
    private static byte[] floatArrayToBytes(float[] floatArray) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(floatArray.length * 4); // 每个 float 占 4 字节
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN); // 设置字节序

        for (float value : floatArray) {
            byteBuffer.putFloat(value);
        }

        return byteBuffer.array();
    }

}
