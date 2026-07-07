package com.qidiangk.smart.aster.tripartite.ali;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.tts.FlowingSpeechSynthesizer;
import com.alibaba.nls.client.protocol.tts.FlowingSpeechSynthesizerListener;
import com.alibaba.nls.client.protocol.tts.FlowingSpeechSynthesizerResponse;
import com.qidiangk.smart.aster.tripartite.property.AliProperty;
import com.qidiangk.smart.aster.tripartite.property.AliTtsOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.TtsClient;
import top.jpower.core.asterisk.audio.TtsResult;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.util.utils.StringUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 阿里语音合成
 *
 * <a href="https://help.aliyun.com/zh/isi/developer-reference/interface-description?spm=a2c4g.11186623.help-menu-30413.d_3_1_3_1.626721f5CtlhPo&scm=20140722.H_2666509._.OR_help-T_cn~zh-V_1">对接文档</a>
 */
@Slf4j
public class AliTtsClient extends AliToken implements TtsClient {

    private final NlsClient client;
    private final FlowingSpeechSynthesizer synthesizer;

    // 当前处理的请求
    private final AtomicReference<TTSRequest> currentRequest = new AtomicReference<>();
    // 互斥公平锁
    private final Semaphore requestSemaphore = new Semaphore(1, true);
    // 心跳线程
    private final ScheduledThreadPoolExecutor heartbeatFuture = new ScheduledThreadPoolExecutor(1);

    /**
     * 请求实体
     */
    @RequiredArgsConstructor
    private static class TTSRequest {

        private final TtsResult result;
        private final OutputStream audioOutput;
        private volatile boolean firstAudioReceived = false;

        public void writeAudio(byte[] bytesArray) {
            try {
                audioOutput.write(bytesArray);
                audioOutput.flush();
            } catch (IOException e) {
                log.error("写入音频流报错===>> {}", ExceptionUtil.stacktraceToString(e));
            }
            if (!firstAudioReceived) {
                firstAudioReceived = true;
                result.startLatch().countDown();
            }
        }

        public void closeOutput() {
            try { audioOutput.close(); } catch (IOException e) {
                log.error("关闭音频流报错===>> {}", ExceptionUtil.stacktraceToString(e));
            }
        }

    }

    private static AliProperty merged(AliTtsOption ttsOption) {
        if (ttsOption == null) {
            return SpringUtil.getBean(AliProperty.class);
        }

        AliProperty property = BeanUtil.copyProperties(SpringUtil.getBean(AliProperty.class), AliProperty.class);
        property.setTtsOption(BeanUtil.copyProperties(
                SpringUtil.getBean(AliProperty.class).getTtsOption(), AliProperty.TtsOption.class));

        BeanUtil.copyProperties(ttsOption, property, CopyOptions.create().ignoreNullValue()
                .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));
        BeanUtil.copyProperties(ttsOption, property.getTtsOption(), CopyOptions.create().ignoreNullValue()
                .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));
        return property;
    }

    public AliTtsClient() throws Exception {
        this(null);
    }

    public AliTtsClient(AliTtsOption ttsOption) throws Exception {
        super(merged(ttsOption));

        this.client = new NlsClient(super.getToken());
        synthesizer = new FlowingSpeechSynthesizer(client, getSynthesizerListener());
        synthesizer.setAppKey(aliProperty.getAppKey());

        AliProperty.TtsOption option = aliProperty.getTtsOption();
        if (Fc.notNull(option.getFormat())){
            synthesizer.setFormat(aliProperty.getTtsOption().getFormat());
        }
        if (Fc.notNull(option.getSampleRate())){
            synthesizer.setSampleRate(option.getSampleRate());
        }
        if (Fc.notNull(option.getSpeechRate())){
            synthesizer.setSpeechRate(option.getSpeechRate());
        }
        if (Fc.notNull(option.getPitchRate())){
            synthesizer.setPitchRate(option.getPitchRate());
        }
        if (Fc.notNull(option.getVolume())){
            synthesizer.setVolume(option.getVolume());
        }
        if (Fc.notNull(option.getVoice())){
            synthesizer.setVoice(option.getVoice());
        }
        if (Fc.notNull(option.getMinSendIntervalMs())){
            synthesizer.setMinSendIntervalMS(option.getMinSendIntervalMs());
        }

        // 启动心跳
        ThreadUtil.schedule(heartbeatFuture, () -> synthesizer.getConnection().sendPing(), 5,
                5, TimeUnit.SECONDS, false);
    }

    private FlowingSpeechSynthesizerListener getSynthesizerListener() {
        return new FlowingSpeechSynthesizerListener() {

            //流式文本语音合成开始
            @Override
            public void onSynthesisStart(FlowingSpeechSynthesizerResponse response) {
            }
            //服务端检测到了一句话的开始
            @Override
            public void onSentenceBegin(FlowingSpeechSynthesizerResponse response) {
            }
            //服务端检测到了一句话的结束，获得这句话的起止位置和所有时间戳
            @Override
            public void onSentenceEnd(FlowingSpeechSynthesizerResponse response) {
                // 当一句话结束的时候代表可以进行同步播放了
                currentRequest.get().result.startLatch().countDown();
            }
            //流式文本语音合成结束
            @Override
            public void onSynthesisComplete(FlowingSpeechSynthesizerResponse response) {
                currentRequest.get().closeOutput();
                currentRequest.get().result.future().complete(null);
                currentRequest.set(null);
            }
            //收到语音合成的语音二进制数据
            @Override
            public void onAudioData(ByteBuffer message) {
                byte[] bytesArray = new byte[message.remaining()];
                message.get(bytesArray, 0, bytesArray.length);
                currentRequest.get().writeAudio(bytesArray);
            }
            //收到语音合成的增量音频时间戳
            @Override
            public void onSentenceSynthesis(FlowingSpeechSynthesizerResponse response) {
            }
            @Override
            public void onFail(FlowingSpeechSynthesizerResponse response){
                log.error("阿里云TTS报错===>>session_id: {}, task_id: {}, status: {}, status_text: {}",
                        getFlowingSpeechSynthesizer().getCurrentSessionId(),
                        response.getTaskId(),
                        response.getStatus(),
                        response.getStatusText());

                if (Fc.notNull(currentRequest.get())) {
                    currentRequest.get().result.future().completeExceptionally(new RuntimeException(
                            "session_id: " + getFlowingSpeechSynthesizer().getCurrentSessionId() +
                                    ", task_id: " + response.getTaskId() +
                                    //状态码
                                    ", status: " + response.getStatus() +
                                    //错误信息
                                    ", status_text: " + response.getStatusText()));
                    currentRequest.get().closeOutput();
                    currentRequest.set(null);
                }
                // 连接断了
                if (response.getStatus() == 40000004) {
                    heartbeatFuture.shutdownNow();
                }
            }
        };
    }

    /**
     * 发送请求
     * @param say 说话内容
     * @param audioOutput 音频数据输出流
     * @return
     */
    @Override
    public synchronized TtsResult process(String say, OutputStream audioOutput) {
        TtsResult result = new TtsResult(new CompletableFuture<>(), new CountDownLatch(1), true);
        ThreadUtil.execute(() -> {
            try {
                // 保证阻塞，只有一个请求正在处理
                requestSemaphore.acquire();
                // 当前正在处理的请求
                currentRequest.set(new TTSRequest(result, audioOutput));

                // 发送文本
                String[] textArray = StrUtil.split(say, 5);
                synthesizer.start();
                for (String text: textArray) {
                    synthesizer.send(text);
                }
                synthesizer.stop();
            } catch (InterruptedException e) {
                log.warn("线程被中断了....==>>{}", e.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("阿里云语音合成异常==>>{}", ExceptionUtil.stacktraceToString(e));
                result.future().completeExceptionally(e);
            } finally {
                requestSemaphore.release();
            }
        });
        return result;
    }

    @Override
    public int getSampleRate() {
        AliProperty.TtsOption option = aliProperty.getTtsOption();
        return option != null && option.getSampleRate() != null ? option.getSampleRate().value : 8000;
    }

    @Override
    public void close() {
        heartbeatFuture.shutdownNow();
        synthesizer.close();
        client.shutdown();
    }
}
