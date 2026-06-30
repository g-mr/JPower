package com.qidiangk.smart.aster.tripartite.dashscope;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.dashscope.audio.omni.*;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qidiangk.smart.aster.tripartite.property.DashScopeProperty;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.AsrResult;
import top.jpower.core.util.utils.Fc;

import java.io.IOException;
import java.io.PipedInputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * DashScope Qwen-ASR-Realtime（Qwen3-ASR）实时 ASR 识别客户端
 * <br/>
 * 基于 DashScope SDK 的 {@link OmniRealtimeConversation} API，通过 WebSocket 实现实时流式语音识别。
 * <br/>
 * 支持模型：
 * <ul>
 *   <li>qwen3-asr-flash-realtime（默认）</li>
 * </ul>
 * <br/>
 * <b>注意</b>：本客户端与 {@link DashScopeFunAsrClient} 使用的 API 完全不同，不能互相替代。
 * Qwen-ASR 需要 dashscope-sdk-java &gt;= 2.22.5。
 * <br/>
 * <a href="https://help.aliyun.com/zh/model-studio/qwen-asr-realtime-java-sdk">接口文档</a>
 *
 * @author mr.g
 */
@Slf4j
public class DashScopeQwenAsrClient implements AsrClient {

    // ─────────────────────────── 状态枚举 ───────────────────────────

    private enum Status {
        /** 未开始 */
        NOT_RUN,
        /** 已建立连接，等待语音输入 */
        STARTING,
        /** 正在识别（已检测到语音） */
        RUN,
        /** 识别完成 */
        COMPLETE,
        /** 发生错误 */
        ERROR
    }

    // ─────────────────────────── 实例字段 ───────────────────────────

    /** 合并后的配置（配置文件 + 页面覆盖） */
    private final DashScopeProperty property;

    /** 串行处理锁：同一实例同时只允许一个识别请求 */
    private final Semaphore semaphore = new Semaphore(1, true);

    /** 当前正在使用的 OmniRealtimeConversation 实例 */
    private final OmniRealtimeConversation currentConversation;

    /** 用户主动关闭标志，用于中断发送/等待循环 */
    private volatile boolean userClosed = false;
    private volatile Session currentSession;


    private static final class Session {
        /** 等待 conversation.item.input_audio_transcription.completed / error / onClose 触发 **/
        final CountDownLatch completionLatch = new CountDownLatch(1);

        /** 等待 session.finished / onClose 触发，用于在 process 结束时排空残留事件 **/
        final CountDownLatch sessionEndLatch = new CountDownLatch(1);

        /** 当前识别状态 */
        final AtomicReference<Status> statusReference = new AtomicReference<>(Status.NOT_RUN);
        /** 上一次有效数据时间戳（用于 noDataReference 回调传入超时判断） */
        final AtomicReference<Long> lastTimeReference = new AtomicReference<>(System.currentTimeMillis());
        /** 识别结果缓冲区（StringBuffer 本身线程安全） */
        final StringBuffer MESSAGE = new StringBuffer();
        /** 错误信息（供外部日志/异常使用） */
        final AtomicReference<String> errorMessageRef = new AtomicReference<>();
    }


    // ─────────────────────────── 构造器 ───────────────────────────

    /**
     * 带页面动态配置的构造器（asrOption 中非 null 的值会覆盖配置文件）
     */
    @SneakyThrows(Exception.class)
    public DashScopeQwenAsrClient(DashScopeProperty property) {
        this.property = property;
        this.currentConversation = this.connection();
    }



    private OmniRealtimeConversation connection() throws NoApiKeyException, InterruptedException {
        DashScopeProperty.AsrOption asrOpt = property.getAsrOption();

        // ① 若配置了自定义 WebSocket 地址，覆盖 SDK 全局 URL
        if (Fc.isNotBlank(property.getWebsocketUrl())) {
            OmniRealtimeParam.baseWebsocketApiUrl = property.getWebsocketUrl();
            log.info("[DashScope-ASR] 使用自定义 WebSocket 地址: {}", property.getWebsocketUrl());
        }

        // ② 构建语音识别专用配置
        OmniRealtimeTranscriptionParam transcriptionParam = new OmniRealtimeTranscriptionParam();
        transcriptionParam.setLanguage(asrOpt.getLanguage());
        transcriptionParam.setInputSampleRate(asrOpt.getSampleRate());
        transcriptionParam.setInputAudioFormat(asrOpt.getFormat());
        if (Fc.isNotBlank(asrOpt.getCorpusText())) {
            transcriptionParam.setCorpusText(asrOpt.getCorpusText());
        }

        OmniRealtimeConfig config = OmniRealtimeConfig.builder()
                .modalities(Collections.singletonList(OmniRealtimeModality.TEXT))
                .enableTurnDetection(Boolean.TRUE.equals(asrOpt.getEnableTurnDetection()))
                .turnDetectionThreshold(asrOpt.getTurnDetectionThreshold())
                .turnDetectionSilenceDurationMs(asrOpt.getMaxEndSilence())
                .transcriptionConfig(transcriptionParam)
                .build();

        // ③ 构建连接参数
        OmniRealtimeParam param = OmniRealtimeParam.builder()
                .model(asrOpt.getModel())
                .apikey(property.getApiKey())
                .url(Fc.isNotBlank(property.getWebsocketUrl()) ? property.getWebsocketUrl() : null)
                .build();


        // ④ 设置识别回调（运行在 SDK 内部线程）
        OmniRealtimeCallback callback = new OmniRealtimeCallback() {
            @Override
            public void onOpen() {
                log.debug("[DashScope-ASR] WebSocket 已连接");
            }

            @Override
            public void onEvent(JsonObject message) {
                if (currentSession == null) {
                    log.warn("[DashScope-ASR] currentSession还没有实例化...");
                    return;
                }
                currentSession.lastTimeReference.set(System.currentTimeMillis());
                String type = getString(message, "type");
                if (Fc.isBlank(type)) {
                    return;
                }
                switch (type) {
                    case "input_audio_buffer.speech_started":
                        currentSession.statusReference.set(Status.RUN);
                        log.debug("[DashScope-ASR] 检测到语音开始");
                        break;
                    case "conversation.item.input_audio_transcription.text":
                        currentSession.statusReference.set(Status.RUN);
                        // 当前句子实时预览 = 已确认前缀 text + 临时后缀 stash
//                                String text = getString(message, "text");
//                                String stash = getString(message, "stash");
//                                String current = (text == null ? "" : text) + (stash == null ? "" : stash);
//                                if (Fc.isNotBlank(current)) {
//                                    MESSAGE.setLength(0);
//                                    MESSAGE.append(current);
//                                }
//                                log.info("[DashScope-ASR] 实时预览: {}", current);
                        break;
                    case "conversation.item.input_audio_transcription.completed":
                        String transcript = getString(message, "transcript");
                        if (Fc.isNotBlank(transcript)) {
                            currentSession.MESSAGE.setLength(0);
                            currentSession.MESSAGE.append(transcript);
                        }
                        currentSession.statusReference.set(Status.COMPLETE);
                        currentSession.completionLatch.countDown();
                        break;
                    case "conversation.item.input_audio_transcription.failed":
                        log.error("[DashScope-ASR] 识别失败: {}", message.toString());
                        currentSession.statusReference.set(Status.ERROR);
                        currentSession.errorMessageRef.set(message.toString());
                        currentSession.completionLatch.countDown();
                        break;
                    case "error":
                        currentSession.statusReference.set(Status.ERROR);
                        log.error("[DashScope-ASR] 错误: {}", message.toString());
                        currentSession.errorMessageRef.set(message.toString());
                        currentSession.completionLatch.countDown();
                        break;
                    case "session.finished":
                        currentSession.sessionEndLatch.countDown();
                        break;
                    default:
                        // session.created / session.updated / input_audio_buffer.committed 等忽略
                        break;
                }
            }

            @Override
            public void onClose(int code, String reason) {
                log.info("[DashScope-ASR] 连接关闭: code={}, reason={}", code, reason);
                Session session = currentSession;
                if (session != null) {
                    session.completionLatch.countDown();
                    session.sessionEndLatch.countDown();
                }
            }
        };

        // ⑤ 建立 WebSocket 连接
        OmniRealtimeConversation conversation = new OmniRealtimeConversation(param, callback);
        conversation.connect();
        conversation.updateSession(config);
        log.info("[DashScope-ASR] 连接成功，开始识别，model={}, sampleRate={}, format={}",
                asrOpt.getModel(), asrOpt.getSampleRate(), asrOpt.getFormat());

        return conversation;
    }



    // ─────────────────────────── 核心方法 ───────────────────────────

    /**
     * 处理一次语音识别请求。
     * <br/>
     * 从 pipedInput 持续读取 PCM 音频数据并发送给 DashScope 服务；
     * 服务通过 WebSocket 回调识别结果，VAD 断句后写入 MESSAGE 缓冲区。
     *
     * @param pipedInput 音频输入流（持续写入的 PCM 数据）
     * @return AsrResult（包含 CompletableFuture&lt;String&gt; 用于异步获取识别文本）
     */
    @Override
    public AsrResult process(PipedInputStream pipedInput) throws InterruptedException {
        semaphore.acquire();

        // 重置本次识别的状态
        currentSession = new Session();
        currentSession.statusReference.set(Status.STARTING);
        userClosed = false;

        Thread main = Thread.currentThread();

        // 这三个引用由框架（AgiSupport）在返回 AsrResult 后赋值，用于控制识别行为
        AtomicBoolean started = new AtomicBoolean(false);
        AtomicReference<Consumer<Long>> noDataReference = new AtomicReference<>(null);
        AtomicReference<Consumer<String>> dataReference = new AtomicReference<>(null);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                // ⑥ 异步发送音频帧（独立线程，持续读取 PipedInputStream 并发送给服务端）
                CompletableFuture<Void> futureSend = CompletableFuture.runAsync(() -> {
                    log.info("[DashScope-ASR] 开始发送音频帧");
                    try {
                        byte[] buffer = new byte[3200];
                        int len;
                        Base64.Encoder encoder = Base64.getEncoder();
                        while ((len = pipedInput.read(buffer)) >= 0 && !main.isInterrupted() && !userClosed) {
                            if (len > 0) {
                                // SDK 要求每次发送 Base64 编码后的音频片段
                                currentConversation.appendAudio(encoder.encodeToString(Arrays.copyOf(buffer, len)));
                            }
                        }
                    } catch (Exception e) {
                        log.error("[DashScope-ASR] 录音流读取异常: {}", e.getMessage());
                    }
                    log.info("[DashScope-ASR] 音频流读取完毕，发送线程结束");
                });

                // ⑦ 主监控循环：等待识别结果或发送完成
                String str = "";
                while (Fc.isBlank(str) && !futureSend.isDone()) {

                    // 检测到语音输入时标记 started（通知框架开始计时）
                    if (currentSession.statusReference.get() == Status.RUN) {
                        started.set(true);
                    }

                    if (!currentSession.MESSAGE.isEmpty()) {
                        if (dataReference.get() != null) {
                            // ── 持续返回模式 ──
                            // 将当前积累的文本通过回调传出，然后继续识别
                            dataReference.get().accept(currentSession.MESSAGE.toString());
                            currentSession.MESSAGE.setLength(0);
                        } else {
                            futureSend.cancel(true);
                            // ── 单次识别模式 ──
                            str = currentSession.MESSAGE.toString();
                            currentSession.MESSAGE.setLength(0);
                        }
                    } else {
                        // 尚无识别结果，通知框架当前已等待的时长（用于超时控制）
                        if (Fc.notNull(noDataReference.get())) {
                            noDataReference.get().accept(DateUtil.spendMs(currentSession.lastTimeReference.get()));
                        }
                    }

                    if (Fc.isBlank(str)) {
                        ThreadUtil.safeSleep(50);
                    }
                }

                // ⑧ 音频流已全部发送完毕，但仍未获得识别结果
                if (Fc.isBlank(str) && futureSend.isDone()) {
                    // 再等待 completed / error / onClose（最多 3 秒）
                    log.info("[DashScope-ASR]  没有识别出来正在等..........{}", currentSession.statusReference.get());
                    currentSession.completionLatch.await(5, TimeUnit.SECONDS);
                    log.info("[DashScope-ASR]  等完了..........{}", currentSession.statusReference.get());
                    str = currentSession.MESSAGE.toString();
                    currentSession.MESSAGE.setLength(0);

                    if (currentSession.statusReference.get() == Status.ERROR) {
                        throw new RuntimeException("[DashScope-ASR] 识别出错: " + currentSession.errorMessageRef.get());
                    }
                }

                log.info("[DashScope-ASR] 本次识别完成，结果长度={}", str.length());
                return str;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("[DashScope-ASR] 识别线程被中断");
                return "";
            } catch (Exception e) {
                log.error("[DashScope-ASR] ASR处理失败: {}", e.getMessage(), e);
                throw new RuntimeException(e);
            } finally {
                try {
                    pipedInput.close();
                } catch (IOException ignored) { }
                currentConversation.endSessionAsync();
                // 等待 session.finished 排空上一轮残留的识别事件，防止泄漏到下一次 process
                try {
                    if (!currentSession.sessionEndLatch.await(5, TimeUnit.SECONDS)) {
                        log.warn("[DashScope-ASR] 等待 session 结束超时，可能存在残留事件");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("[DashScope-ASR] 等待 session 结束被中断");
                }
                semaphore.release();
            }
        });

        return new AsrResult(future, started, dataReference, noDataReference);
    }

    /**
     * 关闭客户端，中断正在进行的识别并释放资源。
     * <br/>
     * 通常在通话结束（AgHangup）时由框架调用。
     */
    @Override
    public void close() {
        userClosed = true;
        currentConversation.close();
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
