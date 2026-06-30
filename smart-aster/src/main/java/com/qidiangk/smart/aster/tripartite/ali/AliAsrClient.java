package com.qidiangk.smart.aster.tripartite.ali;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriber;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberListener;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberResponse;
import com.qidiangk.smart.aster.tripartite.property.AliAsrOption;
import com.qidiangk.smart.aster.tripartite.property.AliProperty;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.AsrResult;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.util.utils.StringUtil;

import java.io.IOException;
import java.io.PipedInputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 阿里实时ASR识别
 * <br />
 * <a href="https://help.aliyun.com/zh/isi/developer-reference/api-reference?spm=a2c4g.11186623.help-menu-30413.d_3_2_1_0.16217a17wyk3F3&scm=20140722.H_84428._.OR_help-T_cn~zh-V_1">接口文档</a>
 */
@Slf4j
public class AliAsrClient extends AliToken implements AsrClient {

    private final NlsClient client;
    private final SpeechTranscriber transcriber;

    private final Semaphore semaphore = new Semaphore(1, true);
    private final AtomicReference<Status> statusReference = new AtomicReference<>(Status.NOT_RUN);
    private final AtomicReference<Long> lastTimeReference = new AtomicReference<>(System.currentTimeMillis());
    private final StringBuffer MESSAGE = new StringBuffer();

    // 心跳线程
    private final ScheduledThreadPoolExecutor heartbeatFuture = new ScheduledThreadPoolExecutor(1);

    private enum Status {
        /**
         * 未运行
         */
        NOT_RUN,
        /**
         * 开始了
         */
        STARTING,
        /**
         * 运行中
         */
        RUN,
        /**
         * 报错了
         */
        ERROR
    }

    private static AliProperty merged(AliAsrOption asrOption) {
        if (asrOption == null) {
            return SpringUtil.getBean(AliProperty.class);
        }

        AliProperty property = BeanUtil.copyProperties(SpringUtil.getBean(AliProperty.class), AliProperty.class);
        property.setAsrOption(BeanUtil.copyProperties(
                SpringUtil.getBean(AliProperty.class).getAsrOption(), AliProperty.AsrOption.class));

        BeanUtil.copyProperties(asrOption, property, CopyOptions.create().ignoreNullValue()
                .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));
        BeanUtil.copyProperties(asrOption, property.getAsrOption(), CopyOptions.create().ignoreNullValue()
                .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));
        return property;
    }

    public AliAsrClient() throws Exception {
        this(null);
    }

    public AliAsrClient(AliAsrOption asrOption) throws Exception {
        super(merged(asrOption));


        client = new NlsClient(getToken());
        transcriber = new SpeechTranscriber(client, getTranscriberListener());
        transcriber.setAppKey(aliProperty.getAppKey());

        AliProperty.AsrOption option = aliProperty.getAsrOption();
        if (Fc.notNull(option.getFormat())) {
            transcriber.setFormat(option.getFormat());
        }
        if (Fc.notNull(option.getSampleRate())) {
            transcriber.setSampleRate(option.getSampleRate());
        }
        transcriber.setEnablePunctuation(option.isEnablePunctuation());
        transcriber.setEnableITN(option.isEnableItn());
        transcriber.setEnableIntermediateResult(option.isEnableIntermediateResult());
        transcriber.addCustomedParam("enable_semantic_sentence_detection", option.isEnableSemanticSentenceDetection());
        transcriber.addCustomedParam("max_sentence_silence", option.getMaxSentenceSilence());
        transcriber.addCustomedParam("disfluency", option.isDisfluency());
        if (Fc.notNull(option.getEnableWords())) {
            transcriber.addCustomedParam("enable_words", option.getEnableWords());
        }
        if (Fc.notNull(option.getSpeechNoiseThreshold())) {
            transcriber.addCustomedParam("speech_noise_threshold", option.getSpeechNoiseThreshold());
        }
        if (Fc.isNotBlank(option.getCustomizationId())) {
            transcriber.addCustomedParam("customization_id", option.getCustomizationId());
        }
        if (Fc.isNotBlank(option.getVocabularyId())) {
            transcriber.addCustomedParam("vocabulary_id", option.getVocabularyId());
        }

        // 启动心跳
        ThreadUtil.schedule(heartbeatFuture, () -> transcriber.getConnection().sendPing(), 5,
                5, TimeUnit.SECONDS, false);
    }

    private SpeechTranscriberListener getTranscriberListener() {
        return new SpeechTranscriberListener() {

            //识别出中间结果。仅当setEnableIntermediateResult为true时，才会返回该消息。
            @Override
            public void onTranscriptionResultChange(SpeechTranscriberResponse response) {
                statusReference.set(Status.RUN);
                lastTimeReference.set(System.currentTimeMillis());
            }

            @Override
            public void onTranscriberStart(SpeechTranscriberResponse response) {
                statusReference.set(Status.STARTING);
            }

            @Override
            public void onSentenceBegin(SpeechTranscriberResponse response) {
                statusReference.set(Status.RUN);
                lastTimeReference.set(System.currentTimeMillis());
            }

            //识别出一句话。服务端会智能断句，当识别到一句话结束时会返回此消息。
            @Override
            public void onSentenceEnd(SpeechTranscriberResponse response) {
                statusReference.set(Status.RUN);
                lastTimeReference.set(System.currentTimeMillis());
                MESSAGE.append(response.getTransSentenceText());
            }

            //识别完毕
            @Override
            public void onTranscriptionComplete(SpeechTranscriberResponse response) {
                statusReference.set(Status.NOT_RUN);
                lastTimeReference.set(System.currentTimeMillis());
            }

            @Override
            public void onFail(SpeechTranscriberResponse response) {
                log.error("阿里ASR报错了==========task_id: {}, status: {}, status_text: {}", response.getTaskId(), response.getStatus(), response.getStatusText());
                statusReference.set(Status.ERROR);
                lastTimeReference.set(System.currentTimeMillis());
//                MESSAGE.append("ali error==>>").append(response.getStatus()).append("===").append(response.getStatusText());

                ThreadUtil.execute(() -> {
                    // 连接断了,暂时心跳断了，这里一旦断了，电话后续都有问题，回头再实现重连
                    ThreadUtil.safeSleep(1000);
                    if (!transcriber.getConnection().isActive()) {
                        heartbeatFuture.shutdownNow();
                    }
                });
            }
        };
    }

    @Override
    public AsrResult process(PipedInputStream pipedInput) throws InterruptedException {
        semaphore.acquire();
        statusReference.set(Status.NOT_RUN);
        lastTimeReference.set(System.currentTimeMillis());
        MESSAGE.setLength(0);

        Thread main = Thread.currentThread();

        AtomicBoolean started = new AtomicBoolean(false);
        AtomicReference<Consumer<Long>> noDataReference = new AtomicReference<>(null);
        AtomicReference<Consumer<String>> dataReference = new AtomicReference<>(null);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                transcriber.start();

                // 发送数据
                CompletableFuture<Void> futureSend = CompletableFuture.runAsync(()->{
                    try {
                        byte[] buffer = new byte[3200];
                        int len = 0;

                        while (len >= 0 && !main.isInterrupted()) {
                            if ((len = pipedInput.read(buffer)) > 0) {
                                transcriber.send(buffer, len);
                            }
                        }
                    } catch (IOException e) {
                        log.error("录音流读取异常==>>{}", e.getMessage());
                    }

                    log.info("读取流完成,读取线程结束==>>");
                });


                String str = "";
                while (Fc.isBlank(str) && !futureSend.isDone()) {
                    if (statusReference.get() == Status.RUN){
                        started.set(true);
                    }

                    if (MESSAGE.length() > 0){
                        if (dataReference.get() != null){
                            // 持续返回结果
                            dataReference.get().accept(MESSAGE.toString());
                            MESSAGE.setLength(0);
                        } else {
                            // 检测到识别出来的结构立马停止识别
                            transcriber.stop();
                            pipedInput.close();
                            futureSend.cancel(true);

                            str = MESSAGE.toString();
                            MESSAGE.setLength(0);
                        }
                    } else {
                        if (Fc.notNull(noDataReference.get())) {
                            noDataReference.get().accept(DateUtil.spendMs(lastTimeReference.get()));
                        }
                    }
                }

                // 录音流发送完了，但是没有识别出结果，需要手动关闭当前请求
                if (Fc.isBlank(str) && futureSend.isDone()) {
                    transcriber.stop();
                    pipedInput.close();
                    MESSAGE.setLength(0);

                    if (statusReference.get() == Status.ERROR) {
                        throw new RuntimeException("阿里ASR识别出错了....");
                    }
                }
                return str;
            } catch (Exception e) {
                log.error("ASR处理失败==>>{}", e.getMessage());
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }

        });

        return new AsrResult(future, started, dataReference, noDataReference);
    }

    @Override
    public void close() {
        heartbeatFuture.shutdownNow();
        transcriber.close();
        client.shutdown();
    }
}
