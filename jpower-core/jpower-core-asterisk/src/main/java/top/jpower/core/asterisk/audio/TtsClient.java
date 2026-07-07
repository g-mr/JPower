package top.jpower.core.asterisk.audio;

import java.io.OutputStream;

/**
 * TTS 客户端接口
 * <br/>
 * 实现类只负责将音频合成数据（PCM 原始字节）写入提供的 {@link OutputStream}，
 * <br/>
 * <b>职责划分：</b>
 * <ul>
 *   <li>实现类：音频合成 → 写入 OutputStream → 通过 startLatch 通知框架数据已就绪 → 完成时关闭 OutputStream</li>
 *   <li>框架层：创建文件/目录 → 创建 WavWriter → 提供 PipedOutputStream → 读取管道并写入文件</li>
 * </ul>
 */
public interface TtsClient extends AutoCloseable {

    /**
     * 执行文本到语音的合成
     *
     * @param say         要合成的文本内容
     * @param audioOutput 音频数据输出流，实现类将 PCM 原始字节写入此流，完成后应关闭此流
     * @return TtsResult 包含异步控制句柄（future）和播放同步信号（startLatch）
     */
    TtsResult process(String say, OutputStream audioOutput);

    /**
     * 获取当前实现的采样率（Hz），框架层用于创建 WAV 文件头
     *
     * @return 采样率，默认 8000
     */
    default int getSampleRate() {
        return 8000;
    }

    void close();

}
