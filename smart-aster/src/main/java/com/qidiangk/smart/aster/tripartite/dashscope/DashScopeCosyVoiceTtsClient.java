package com.qidiangk.smart.aster.tripartite.dashscope;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisResult;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisAudioFormat;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.utils.Constants;
import com.qidiangk.smart.aster.tripartite.property.DashScopeProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.TtsClient;
import top.jpower.core.asterisk.audio.TtsResult;
import top.jpower.core.util.utils.Fc;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

/**
 * DashScope CosyVoice 语音合成客户端
 * <br/>
 * 基于 DashScope CosyVoice 系列模型（cosyvoice-v1/v2/v3-flash/v3-plus 等）实现实时语音合成。
 * <br/>
 * 使用 WebSocket 流式通信，支持边合成边写入 OutputStream，适用于电话 VoIP 场景。
 * <br/>
 * 模型文档：<a href="https://help.aliyun.com/zh/model-studio/cosyvoice-java-sdk">CosyVoice Java SDK</a>
 *
 * @author mr.g
 */
@Slf4j
public class DashScopeCosyVoiceTtsClient implements TtsClient {

    // ─────────────────────────── 实例字段 ───────────────────────────

    /** 合并后的配置（配置文件 + 页面覆盖） */
    private final DashScopeProperty property;

    /** CosyVoice 语音合成器 */
    private final SpeechSynthesizer synthesizer;

    /** 当前处理的请求 */
    private final AtomicReference<TTSRequest> currentRequest = new AtomicReference<>();

    /** 互斥公平锁：同一实例同时只允许一个合成请求 */
    private final Semaphore requestSemaphore = new Semaphore(1, true);

    // ─────────────────────────── 内部类 ───────────────────────────

    /**
     * 请求实体
     */
    @RequiredArgsConstructor
    private static class TTSRequest {

        private final TtsResult result;
        private final OutputStream audioOutput;
        private volatile boolean firstAudioReceived = false;

        public void writeAudio(byte[] audioData) {
            try {
                audioOutput.write(audioData);
                audioOutput.flush();
            } catch (IOException e) {
                log.error("[DashScope-CosyVoice-TTS] 音频流写入报错: {}", ExceptionUtil.stacktraceToString(e));
            }
            // 首次收到音频数据时，通知框架可以开始播放
            if (!firstAudioReceived) {
                firstAudioReceived = true;
                result.startLatch().countDown();
            }
        }

        public void closeOutput() {
            try { audioOutput.close(); } catch (IOException e) {
                log.error("[DashScope-CosyVoice-TTS] 音频流关闭报错: {}", ExceptionUtil.stacktraceToString(e));
            }
        }
    }

    // ─────────────────────────── 构造器 ───────────────────────────

    /**
     * 带页面动态配置的构造器
     *
     * @param property 合并后的配置（配置文件 + 页面覆盖）
     */
    public DashScopeCosyVoiceTtsClient(DashScopeProperty property) {
        this.property = property;
        this.synthesizer = createSynthesizer();
        log.info("[DashScope-CosyVoice-TTS] 初始化完成, model={}, voice={}, sampleRate={}",
                property.getTtsOption().getModel(),
                property.getTtsOption().getVoice(),
                property.getTtsOption().getSampleRate());
    }

    private SpeechSynthesizer createSynthesizer() {
        DashScopeProperty.TtsOption ttsOpt = property.getTtsOption();

        if (Fc.isNotBlank(property.getWebsocketUrl())) {
            Constants.baseWebsocketApiUrl = property.getWebsocketUrl();
            log.debug("[DashScope-CosyVoice-TTS] 使用自定义 WebSocket 地址: {}", property.getWebsocketUrl());
        }

        return new SpeechSynthesizer(buildParam(), createCallback());
    }

    /**
     * 构建语音合成参数。
     * <br/>
     * 始终使用 PCM 格式（原始音频数据），WAV 容器由框架层负责封装。
     */
    private SpeechSynthesisParam buildParam() {
        DashScopeProperty.TtsOption ttsOpt = property.getTtsOption();
        SpeechSynthesisAudioFormat audioFormat = resolveAudioFormat(ttsOpt.getSampleRate());

        return SpeechSynthesisParam.builder()
                .model(ttsOpt.getModel())
                .apiKey(property.getApiKey())
                .voice(ttsOpt.getVoice())
                .format(audioFormat)
                .volume(ttsOpt.getVolume() != null ? ttsOpt.getVolume() : 50)
                .speechRate(ttsOpt.getSpeechRate() != null ? ttsOpt.getSpeechRate() : 1.0f)
                .pitchRate(ttsOpt.getPitchRate() != null ? ttsOpt.getPitchRate() : 1.0f)
                .build();
    }

    /**
     * 创建语音合成回调，处理音频数据接收、合成完成和异常事件。
     * <br/>
     * 回调通过实例字段 {@code currentRequest} 和 {@code requestSemaphore} 与外部协调。
     */
    private ResultCallback<SpeechSynthesisResult> createCallback() {
        return new ResultCallback<SpeechSynthesisResult>() {

            @Override
            public void onEvent(SpeechSynthesisResult result) {
                TTSRequest request = currentRequest.get();
                if (request == null) {
                    log.warn("[DashScope-CosyVoice-TTS] 收到音频数据但当前没有活跃请求");
                    return;
                }

                ByteBuffer audioFrame = result.getAudioFrame();
                if (audioFrame != null && audioFrame.hasRemaining()) {
                    byte[] bytesArray = new byte[audioFrame.remaining()];
                    audioFrame.get(bytesArray, 0, bytesArray.length);
                    request.writeAudio(bytesArray);
                }
            }

            @Override
            public void onComplete() {
                TTSRequest request = currentRequest.get();
                if (request != null) {
                    request.closeOutput();
                    request.result.future().complete(null);
                    currentRequest.set(null);
                    log.debug("[DashScope-CosyVoice-TTS] 语音合成完成");
                }
                requestSemaphore.release();
            }

            @Override
            public void onError(Exception e) {
                log.error("[DashScope-CosyVoice-TTS] 语音合成出错: {}", e.getMessage());
                TTSRequest request = currentRequest.get();
                if (request != null) {
                    request.closeOutput();
                    request.result.future().completeExceptionally(
                            new RuntimeException("CosyVoice TTS 合成出错: " + e.getMessage(), e));
                    currentRequest.set(null);
                }
                requestSemaphore.release();
            }
        };
    }

    // ─────────────────────────── 核心方法 ───────────────────────────

    /**
     * 执行一次文本到语音的合成。
     * <br/>
     * 使用信号量确保同一实例同时只处理一个请求。
     * 合成结果通过 WebSocket 回调实时写入 OutputStream（PCM 原始字节）。
     *
     * @param say         要合成的文本
     * @param audioOutput 音频数据输出流
     * @return TtsResult（包含 future 和 startLatch 用于异步控制和播放同步）
     */
    @Override
    public TtsResult process(String say, OutputStream audioOutput) {
        TtsResult result = new TtsResult(new CompletableFuture<>(), new CountDownLatch(1), true);

        ThreadUtil.execute(() -> {
            try {
                requestSemaphore.acquire();
                TTSRequest request = new TTSRequest(result, audioOutput);
                currentRequest.set(request);

                // 流式发送文本并通知完成
                synthesizer.updateParamAndCallback(buildParam(), createCallback());
                synthesizer.streamingCall(say);
                synthesizer.streamingComplete();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("[DashScope-CosyVoice-TTS] 合成线程被中断");
                requestSemaphore.release();
            } catch (Exception e) {
                log.error("[DashScope-CosyVoice-TTS] 语音合成异常: {}", ExceptionUtil.stacktraceToString(e));
                result.future().completeExceptionally(e);
                requestSemaphore.release();
            }
        });

        return result;
    }

    @Override
    public int getSampleRate() {
        return property.getTtsOption().getSampleRate() != null ? property.getTtsOption().getSampleRate() : 8000;
    }

    @Override
    public void close() {
        try {
            if (synthesizer != null) {
                synthesizer.getDuplexApi().close(1000, "Normal Shutdown");
            }
        } catch (Exception e) {
            log.warn("[DashScope-CosyVoice-TTS] 关闭连接异常: {}", e.getMessage());
        }
        log.info("[DashScope-CosyVoice-TTS] 客户端连接关闭");
    }

    // ─────────────────────────── 内部工具 ───────────────────────────

    /**
     * 根据采样率解析 SDK 音频格式枚举。
     * <br/>
     * 始终使用 PCM 格式（原始音频数据），因为 WAV 容器由框架层负责封装。
     * <br/>
     * 若 SDK 返回 WAV 格式数据（自带 RIFF 头），会与框架层 WavWriter 写入的 WAV 头产生双重头部冲突，
     * 导致 Asterisk 读取 "Does not begin with RIFF" 或音频损坏。
     *
     * @param sampleRate 采样率
     */
    private static SpeechSynthesisAudioFormat resolveAudioFormat(Integer sampleRate) {
        int sr = sampleRate != null ? sampleRate : 8000;

        return switch (sr) {
            case 16000 -> SpeechSynthesisAudioFormat.PCM_16000HZ_MONO_16BIT;
            case 22050 -> SpeechSynthesisAudioFormat.PCM_22050HZ_MONO_16BIT;
            case 24000 -> SpeechSynthesisAudioFormat.PCM_24000HZ_MONO_16BIT;
            case 44100 -> SpeechSynthesisAudioFormat.PCM_44100HZ_MONO_16BIT;
            case 48000 -> SpeechSynthesisAudioFormat.PCM_48000HZ_MONO_16BIT;
            default -> SpeechSynthesisAudioFormat.PCM_8000HZ_MONO_16BIT;
        };
    }
}
