package com.qidiangk.smart.aster.tripartite.dashscope;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.dashscope.audio.asr.recognition.Recognition;
import com.alibaba.dashscope.audio.asr.recognition.RecognitionParam;
import com.alibaba.dashscope.audio.asr.recognition.RecognitionResult;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.utils.Constants;
import com.qidiangk.smart.aster.tripartite.property.DashScopeProperty;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.AsrResult;
import top.jpower.core.util.utils.Fc;

import java.io.IOException;
import java.io.PipedInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * DashScope Fun-ASR / Paraformer 实时 ASR 识别客户端
 * <br/>
 * 基于 DashScope SDK 的 {@link Recognition} API，通过 WebSocket 实现实时流式语音识别。
 * 实现方式与 AliAsrClient 保持一致：流式音频输入、VAD 自动断句、异步结果回调。
 * <br/>
 * 支持模型（通过配置 model 字段运行时切换）：
 * <ul>
 *   <li>fun-asr-realtime —— 通用实时识别（默认）</li>
 *   <li>paraformer-realtime-8k-v2 —— 8kHz 电话场景专用</li>
 * </ul>
 * <br/>
 * <b>不兼容 Qwen-ASR-Realtime（qwen3-asr 系列）</b>：Qwen-ASR 使用全新的
 * {@code OmniRealtimeConversation} API，见 {@link DashScopeQwenAsrClient}。
 * <br/>
 * <a href="https://help.aliyun.com/zh/model-studio/real-time-speech-recognition-user-guide">接口文档</a>
 *
 * @author mr.g
 */
@Slf4j
public class DashScopeFunAsrClient implements AsrClient {

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

    /** 当前正在处理的识别会话，用于 {@link #close()} 时强制中断 */
    private volatile Session currentSession;
    /** 本次识别使用的 Recognition 实例 */
    private final Recognition currentRecognizer;

    // ─────────────────────────── 单次识别会话上下文 ───────────────────────────

    /**
     * 一次识别请求对应的独立状态容器。
     * 所有与本次识别相关的可变状态（latch、缓冲区、错误信息、Recognition 实例）
     * 都封装在 Session 中，process() 结束后随 Session 一起丢弃，
     * 从根本上避免前一次错误/残留回调污染下一次识别。
     */
    private static final class Session {
        /** 等待 onComplete / onError 触发的锁 */
        final CountDownLatch completionLatch = new CountDownLatch(1);
        /** 当前识别状态 */
        final AtomicReference<Status> statusReference = new AtomicReference<>(Status.NOT_RUN);
        /** 上一次有效数据时间戳（用于 noDataReference 回调传入超时判断） */
        final AtomicReference<Long> lastTimeReference = new AtomicReference<>(System.currentTimeMillis());
        /** 识别结果缓冲区 */
        final StringBuffer message = new StringBuffer();
        /** 错误信息（供外部日志/异常使用） */
        final AtomicReference<String> errorMessageRef = new AtomicReference<>();

        // 设置识别回调（运行在 SDK 内部线程），只操作本次 Session 的状态
        final ResultCallback<RecognitionResult> callback = new ResultCallback<RecognitionResult>() {

            @Override
            public void onEvent(RecognitionResult result) {
                statusReference.set(Status.RUN);
                lastTimeReference.set(System.currentTimeMillis());
                // VAD 断句：只在句子结束时才追加到结果缓冲区
                if (result.isSentenceEnd()) {
                    String text = result.getSentence().getText();
                    if (Fc.isNotBlank(text)) {
                        log.debug("[DashScope-FunASR] 句子结束，识别文本: {}", text);
                        message.append(text);
                        completionLatch.countDown();
                    }
                }
            }

            @Override
            public void onComplete() {
                log.info("[DashScope-FunASR] 识别完成");
                statusReference.set(Status.COMPLETE);
                completionLatch.countDown();
            }

            @Override
            public void onError(Exception e) {
                log.error("[DashScope-FunASR] 识别出错: {}", e.getMessage());
                statusReference.set(Status.ERROR);
                errorMessageRef.set(e.getMessage());
                completionLatch.countDown();
            }
        };

        final RecognitionParam param;

        public Session(DashScopeProperty property) {
            this.param = RecognitionParam.builder()
                    .model(property.getAsrOption().getModel())
                    .apiKey(property.getApiKey())
                    .format(property.getAsrOption().getFormat())
//                        .disfluencyRemovalEnabled()
                    .sampleRate(property.getAsrOption().getSampleRate())
                    .phraseId(Fc.isNotBlank(property.getAsrOption().getPhraseId()) ? property.getAsrOption().getPhraseId() : null)
                    .parameter("speech_noise_threshold", property.getAsrOption().getTurnDetectionThreshold())
                    .parameter("language_hints", new String[]{property.getAsrOption().getLanguage()})
                    .parameter("semantic_punctuation_enabled",
                            Boolean.TRUE.equals(property.getAsrOption().getSemanticPunctuationEnabled()))
                    .parameter("max_sentence_silence",
                            property.getAsrOption().getMaxEndSilence() != null ? property.getAsrOption().getMaxEndSilence() : 800)
                    .build();
        }

    }

    // ─────────────────────────── 构造器 ───────────────────────────

    /**
     * 带页面动态配置的构造器（asrOption 中非 null 的值会覆盖配置文件）
     */
    public DashScopeFunAsrClient(DashScopeProperty property) {
        if (Fc.isNotBlank(property.getWebsocketUrl())) {
            Constants.baseWebsocketApiUrl = property.getWebsocketUrl();
            log.info("[DashScope-FunASR] 使用自定义 WebSocket 地址: {}", property.getWebsocketUrl());
        }

        this.property = property;
        currentRecognizer = recognizer();
    }

    // ─────────────────────────── 会话创建 ───────────────────────────

    /**
     * 创建并启动一次新的识别会话。
     * 每个 Session 拥有独立的 {@link Recognition} 实例与回调，
     * SDK 全局连接池会复用底层 WebSocket/TCP 连接。
     */
    private Recognition recognizer() {
        Recognition recognizer = new Recognition();

        log.info("[DashScope-FunASR] 会话已建立，开始识别，model={}, sampleRate={}",
                property.getAsrOption().getModel(), property.getAsrOption().getSampleRate());

        return recognizer;
    }

    // ─────────────────────────── 核心方法 ───────────────────────────

    /**
     * 处理一次语音识别请求。
     * <br/>
     * 从 pipedInput 持续读取 PCM 音频数据并发送给 DashScope 服务；
     * 服务通过 WebSocket 回调识别结果，VAD 断句后写入本次 Session 的结果缓冲区。
     * <br/>
     * 每次调用都会创建新的 {@link Session}，出错或完成后仅清理本次 Session，
     * 不会影响同一实例上的下一次识别。
     *
     * @param pipedInput 音频输入流（持续写入的 PCM 数据）
     * @return AsrResult（包含 CompletableFuture&lt;String&gt; 用于异步获取识别文本）
     */
    @Override
    public AsrResult process(PipedInputStream pipedInput) throws InterruptedException {
        semaphore.acquire();

        Thread main = Thread.currentThread();

        currentSession = new Session(property);
        currentSession.statusReference.set(Status.STARTING);
        // 建立 WebSocket 连接并开始识别
        currentRecognizer.call(currentSession.param, currentSession.callback);

        // 这三个引用由框架（AgiSupport）在返回 AsrResult 后赋值，用于控制识别行为
        AtomicBoolean started = new AtomicBoolean(false);
        AtomicReference<Consumer<Long>> noDataReference = new AtomicReference<>(null);
        AtomicReference<Consumer<String>> dataReference = new AtomicReference<>(null);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {

            try {

                // ⑤ 异步发送音频帧（独立线程，持续读取 PipedInputStream 并发送给服务端）
                CompletableFuture<Void> futureSend = CompletableFuture.runAsync(() -> {
                    log.info("[DashScope-FunASR] 音频流读取开始");
                    try {
                        byte[] buffer = new byte[3200];
                        int len;
                        while ((len = pipedInput.read(buffer)) >= 0 && !main.isInterrupted()) {
                            if (len > 0) {
                                // 拷贝实际数据，防止 SDK 异步处理时读到后续帧脏数据
                                ByteBuffer byteBuffer = ByteBuffer.wrap(Arrays.copyOf(buffer, len));
                                currentRecognizer.sendAudioFrame(byteBuffer);
                            }
                        }
                    } catch (Exception e) {
                        log.error("[DashScope-FunASR] 录音流读取异常: {}", e.getMessage());
                    }
                    log.info("[DashScope-FunASR] 音频流读取完毕，发送线程结束");
                });

                // ⑥ 主监控循环：等待识别结果或发送完成
                String str = "";
                while (Fc.isBlank(str) && !futureSend.isDone()) {

                    // 检测到语音输入时标记 started（通知框架开始计时）
                    if (currentSession.statusReference.get() == Status.RUN) {
                        started.set(true);
                    }

                    if (!currentSession.message.isEmpty()) {
                        if (dataReference.get() != null) {
                            // ── 持续返回模式 ──
                            // 将当前积累的文本通过回调传出，然后继续识别
                            dataReference.get().accept(currentSession.message.toString());
                            currentSession.message.setLength(0);

                        } else {
                            futureSend.cancel(true);

                            str = currentSession.message.toString();
                            currentSession.message.setLength(0);
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

                // ⑦ 音频流已全部发送完毕，但仍未获得识别结果
                if (Fc.isBlank(str) && futureSend.isDone()) {
                    // 等待服务端最终响应（最多 5 秒）
                    currentSession.completionLatch.await(5, TimeUnit.SECONDS);
                    str = currentSession.message.toString();
                    currentSession.message.setLength(0);

                    if (currentSession.statusReference.get() == Status.ERROR) {
                        throw new RuntimeException("[DashScope-FunASR] 识别出错: " + currentSession.errorMessageRef.get());
                    }
                }

                log.info("[DashScope-FunASR] 本次识别完成，结果长度={}", str.length());
                return str;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("[DashScope-FunASR] 识别线程被中断");
                return "";
            } catch (Exception e) {
                log.error("[DashScope-FunASR] ASR处理失败: {}", e.getMessage(), e);
                throw new RuntimeException(e);
            } finally {
                // ⑧ 清理本次识别资源：先关输入流，再停止识别会话，最后释放锁
                try {
                    pipedInput.close();
                } catch (IOException ignored) { }
                // 停止识别会话,阻塞的,需要等音频全部翻译完成，如果新线程执行会导致下一次识别报错，所以只能阻塞等待
                currentRecognizer.stop();
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
        try {
            currentRecognizer.stop();
        } catch (Exception ignored) { }
        try {
            currentRecognizer.getDuplexApi().close(1000, "Normal Shutdown");
        } catch (Exception ignored) { }
        log.info("[DashScope-FunASR] 客户端连接关闭");
    }

}
