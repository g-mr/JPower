package com.qidiangk.smart.aster.tripartite.dashscope;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.dashscope.audio.qwen_tts_realtime.QwenTtsRealtime;
import com.alibaba.dashscope.audio.qwen_tts_realtime.QwenTtsRealtimeAudioFormat;
import com.alibaba.dashscope.audio.qwen_tts_realtime.QwenTtsRealtimeCallback;
import com.alibaba.dashscope.audio.qwen_tts_realtime.QwenTtsRealtimeConfig;
import com.alibaba.dashscope.audio.qwen_tts_realtime.QwenTtsRealtimeParam;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qidiangk.smart.aster.tripartite.property.DashScopeProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.TtsClient;
import top.jpower.core.asterisk.audio.TtsResult;
import top.jpower.core.util.utils.Fc;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * DashScope Qwen-TTS-Realtime 语音合成客户端
 * <br/>
 * 基于 DashScope Qwen3-TTS 实时模型（qwen3-tts-flash-realtime、qwen3-tts-instruct-flash-realtime 等）实现实时语音合成。
 * <br/>
 * 使用 WebSocket 流式通信，支持边合成边写入 OutputStream。
 * <br/>
 * 模型文档：<a href="https://help.aliyun.com/zh/model-studio/qwen-tts-realtime-java-sdk">Qwen-TTS Realtime Java SDK</a>
 *
 * @author mr.g
 */
@Slf4j
public class DashScopeQwenTtsClient implements TtsClient {

    // ─────────────────────────── 内部类 ───────────────────────────

    /**
     * 请求实体
     */
    @RequiredArgsConstructor
    private static class TTSRequest {

        private final CompletableFuture<Void> future;
        private final OutputStream audioOutput;
        private final CountDownLatch startLatch;
        private volatile boolean firstAudioReceived = false;

        public void writeAudio(byte[] audioData) {
            try {
                audioOutput.write(audioData);
                audioOutput.flush();
            } catch (IOException e) {
                log.error("[DashScope-QwenTTS-TTS] 音频流写入报错: {}", ExceptionUtil.stacktraceToString(e));
            }
            // 首次收到音频数据时，通知框架可以开始播放
            if (!firstAudioReceived) {
                firstAudioReceived = true;
                startLatch.countDown();
            }
        }

        public void closeOutput() {
            try { audioOutput.close(); } catch (IOException e) {
                log.error("[DashScope-QwenTTS-TTS] 音频流关闭报错: {}", ExceptionUtil.stacktraceToString(e));
            }
        }
    }

    // ─────────────────────────── 实例字段 ───────────────────────────

    /** 合并后的配置（配置文件 + 页面覆盖） */
    private final DashScopeProperty property;

    /** Qwen-TTS 实时会话 */
    private final QwenTtsRealtime qwenTtsRealtime;

    /** 互斥公平锁：同一实例同时只允许一个合成请求 */
    private final Semaphore requestSemaphore = new Semaphore(1, true);

    /** 当前处理的请求 */
    private final AtomicReference<TTSRequest> currentRequest = new AtomicReference<>();

    /** 当前请求完成锁，用于等待 response.done 事件 */
    private final AtomicReference<CountDownLatch> completionLatch = new AtomicReference<>(new CountDownLatch(1));

    // ─────────────────────────── 构造器 ───────────────────────────

    /**
     * 带页面动态配置的构造器
     *
     * @param property 合并后的配置（配置文件 + 页面覆盖）
     */
    public DashScopeQwenTtsClient(DashScopeProperty property) {
        this.property = property;
        this.qwenTtsRealtime = createQwenTtsRealtime();
        log.info("[DashScope-QwenTTS-TTS] 初始化完成, model={}, voice={}, sampleRate={}",
                property.getTtsOption().getModel(),
                property.getTtsOption().getVoice(),
                property.getTtsOption().getSampleRate());
    }

    private QwenTtsRealtime createQwenTtsRealtime() {
        DashScopeProperty.TtsOption ttsOpt = property.getTtsOption();

        // 构建连接参数
        QwenTtsRealtimeParam.QwenTtsRealtimeParamBuilder<?, ?> paramBuilder = QwenTtsRealtimeParam.builder()
                .model(ttsOpt.getModel())
                .apikey(property.getApiKey());

        if (Fc.isNotBlank(property.getWebsocketUrl())) {
            paramBuilder.url(property.getWebsocketUrl());
        }

        QwenTtsRealtimeParam param = paramBuilder.build();

        // 构建回调
        QwenTtsRealtimeCallback callback = new QwenTtsRealtimeCallback() {

            @Override
            public void onOpen() {
                log.debug("[DashScope-QwenTTS-TTS] WebSocket 连接已建立");
            }

            @Override
            public void onEvent(JsonObject message) {
                String type = getString(message, "type");
                if (Fc.isBlank(type)) {
                    return;
                }

                switch (type) {
                    case "session.created":
                        log.debug("[DashScope-QwenTTS-TTS] 会话创建成功");
                        break;

                    case "response.audio.delta":
                        // 收到音频数据
                        TTSRequest request = currentRequest.get();
                        if (request == null) {
                            log.warn("[DashScope-QwenTTS-TTS] 收到音频数据但当前没有活跃请求");
                            return;
                        }
                        String audioB64 = getString(message, "delta");
                        if (Fc.isNotBlank(audioB64)) {
                            byte[] audioData = Base64.getDecoder().decode(audioB64);
                            request.writeAudio(audioData);
                        }
                        break;

                    case "response.done":
                        // 响应完成
                        log.debug("[DashScope-QwenTTS-TTS] 响应完成");
                        TTSRequest req = currentRequest.get();
                        if (req != null) {
                            req.closeOutput();
                            req.future.complete(null);
                            currentRequest.set(null);
                            requestSemaphore.release();
                        }
                        completionLatch.get().countDown();
                        break;

                    case "error":
                        log.error("[DashScope-QwenTTS-TTS] 收到错误事件: {}", message);
                        TTSRequest errReq = currentRequest.get();
                        if (errReq != null) {
                            errReq.closeOutput();
                            errReq.future.completeExceptionally(
                                    new RuntimeException("Qwen-TTS 合成出错: " + message));
                            currentRequest.set(null);
                            requestSemaphore.release();
                        }
                        completionLatch.get().countDown();
                        break;

                    case "session.finished":
                        log.info("[DashScope-QwenTTS-TTS] 会话结束");
                        completionLatch.get().countDown();
                        break;

                    default:
                        // session.updated / response.cancel_done 等忽略
                        break;
                }
            }

            @Override
            public void onClose(int code, String reason) {
                log.info("[DashScope-QwenTTS-TTS] 连接关闭: code={}, reason={}", code, reason);
                // 如果连接意外关闭且有活跃请求，释放信号量
                TTSRequest req = currentRequest.get();
                if (req != null) {
                    req.closeOutput();
                    if (!req.future.isDone()) {
                        req.future.completeExceptionally(new RuntimeException("WebSocket 连接关闭: code=" + code));
                    }
                    currentRequest.set(null);
                    requestSemaphore.release();
                }
                completionLatch.get().countDown();
            }
        };

        QwenTtsRealtime realtime = new QwenTtsRealtime(param, callback);

        // 建立连接
        try {
            realtime.connect();
        } catch (NoApiKeyException e) {
            throw new RuntimeException("[DashScope-QwenTTS-TTS] API Key 未配置", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("[DashScope-QwenTTS-TTS] 连接被中断", e);
        }

        // 更新会话配置
        realtime.updateSession(buildSessionConfig());

        return realtime;
    }

    /**
     * 构建会话配置。
     * <br/>
     * 使用 commit 模式（而非 server_commit），以便通过 {@link QwenTtsRealtime#commit()} 显式控制每次合成的触发时机，
     * 支持在同一会话中多次合成而不会因 {@link QwenTtsRealtime#finish()} 结束会话导致配置丢失。
     * <br/>
     * 始终使用 PCM 格式，WAV 容器由框架层负责封装。
     * 若 SDK 返回 WAV 格式数据（自带 RIFF 头），会与框架层 WavWriter 的 WAV 头产生双重头部冲突。
     *
     * @return 会话配置
     */
    private QwenTtsRealtimeConfig buildSessionConfig() {
        DashScopeProperty.TtsOption ttsOpt = property.getTtsOption();

        QwenTtsRealtimeConfig.QwenTtsRealtimeConfigBuilder<?, ?> configBuilder = QwenTtsRealtimeConfig.builder()
                .voice(ttsOpt.getVoice())
                .mode("commit");

        int sampleRate = ttsOpt.getSampleRate() != null ? ttsOpt.getSampleRate() : 24000;

        if (sampleRate == 24000) {
            configBuilder.responseFormat(QwenTtsRealtimeAudioFormat.PCM_24000HZ_MONO_16BIT);
        } else {
            configBuilder.sampleRate(sampleRate)
                    .format("pcm");
        }

        if (ttsOpt.getSpeechRate() != null) {
            configBuilder.speechRate(ttsOpt.getSpeechRate());
        }
        if (ttsOpt.getVolume() != null) {
            configBuilder.volume(ttsOpt.getVolume());
        }
        if (ttsOpt.getPitchRate() != null) {
            configBuilder.pitchRate(ttsOpt.getPitchRate());
        }

        return configBuilder.build();
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
        CompletableFuture<Void> future = new CompletableFuture<>();
        CountDownLatch startLatch = new CountDownLatch(1);
        TtsResult result = new TtsResult(future, startLatch, true);

        ThreadUtil.execute(() -> {
            try {
                requestSemaphore.acquire();

                // 重置完成锁
                completionLatch.set(new CountDownLatch(1));

                TTSRequest request = new TTSRequest(future, audioOutput, startLatch);
                currentRequest.set(request);

                // 清除上一次合成残留的文本缓冲，确保本次合成只包含当前文本
                qwenTtsRealtime.clearAppendedText();
                // 发送文本并通过 commit 触发单次合成（不结束会话）
                qwenTtsRealtime.appendText(say);
                qwenTtsRealtime.commit();

                // 等待合成完成（最多 5 秒）
                if (!completionLatch.get().await(5, TimeUnit.SECONDS)) {
                    log.warn("[DashScope-QwenTTS-TTS] 合成超时");
                    future.completeExceptionally(new RuntimeException("Qwen-TTS 合成超时"));
                    currentRequest.set(null);
                    requestSemaphore.release();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("[DashScope-QwenTTS-TTS] 合成线程被中断");
                requestSemaphore.release();
            } catch (Exception e) {
                log.error("[DashScope-QwenTTS-TTS] 语音合成异常: {}", ExceptionUtil.stacktraceToString(e));
                future.completeExceptionally(e);
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
            if (qwenTtsRealtime != null) {
                // 先结束会话（通知服务端合成剩余文本并结束），再关闭连接
                try {
                    qwenTtsRealtime.finish();
                } catch (Exception e) {
                    log.debug("[DashScope-QwenTTS-TTS] 结束会话时异常（可忽略）: {}", e.getMessage());
                }
                qwenTtsRealtime.close();
            }
        } catch (Exception e) {
            log.warn("[DashScope-QwenTTS-TTS] 关闭连接异常: {}", e.getMessage());
        }
        log.info("[DashScope-QwenTTS-TTS] 客户端连接关闭");
    }

    // ─────────────────────────── 内部工具 ───────────────────────────

    /**
     * 从 JsonObject 中安全获取字符串字段
     */
    private static String getString(JsonObject obj, String key) {
        if (obj == null || !obj.has(key)) {
            return null;
        }
        JsonElement element = obj.get(key);
        if (element == null || element.isJsonNull()) {
            return null;
        }
        return element.getAsString();
    }
}
