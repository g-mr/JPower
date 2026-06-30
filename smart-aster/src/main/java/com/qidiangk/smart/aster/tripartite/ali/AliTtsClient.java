package com.qidiangk.smart.aster.tripartite.ali;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.tts.FlowingSpeechSynthesizer;
import com.alibaba.nls.client.protocol.tts.FlowingSpeechSynthesizerListener;
import com.alibaba.nls.client.protocol.tts.FlowingSpeechSynthesizerResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.TtsClient;
import top.jpower.core.asterisk.audio.TtsResult;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.tripartite.property.AliProperty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        private final File file;
        private FileOutputStream fileStream;

        @SneakyThrows(FileNotFoundException.class)
        public void readyWriter() {
            FileUtil.del(file);
            FileUtil.mkParentDirs(file);
            this.fileStream = new FileOutputStream(file);
        }

        public void firstStream() {
            result.startLatch().countDown();
        }

        public void closeWriter() {
            try {
                if (fileStream != null){
                    fileStream.close();
                }
            } catch (IOException e) {
                log.error("关闭文件流报错===>> {}", ExceptionUtil.stacktraceToString(e));
            }
        }

        public void writer(byte[] bytesArray) {
            try {
                if (fileStream == null){
                    log.error("文件流为空===>>fileStream");
                } else {
                    fileStream.write(bytesArray);
                }
            } catch (IOException e) {
                log.error("写入文件流报错===>> {}", ExceptionUtil.stacktraceToString(e));
            }
        }

    }

    public AliTtsClient() throws Exception {
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
                currentRequest.get().readyWriter();
            }
            //服务端检测到了一句话的开始
            @Override
            public void onSentenceBegin(FlowingSpeechSynthesizerResponse response) {
            }
            //服务端检测到了一句话的结束，获得这句话的起止位置和所有时间戳
            @Override
            public void onSentenceEnd(FlowingSpeechSynthesizerResponse response) {
                // 当一句话结束的时候代表可以进行同步播放了
                currentRequest.get().firstStream();
            }
            //流式文本语音合成结束
            @Override
            public void onSynthesisComplete(FlowingSpeechSynthesizerResponse response) {
                currentRequest.get().closeWriter();
                currentRequest.get().result.future().complete(null);
                currentRequest.set(null);
            }
            //收到语音合成的语音二进制数据
            @Override
            public void onAudioData(ByteBuffer message) {
                byte[] bytesArray = new byte[message.remaining()];
                message.get(bytesArray, 0, bytesArray.length);
                currentRequest.get().writer(bytesArray);
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
                    currentRequest.get().closeWriter();
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
     * @param file 写入文件
     * @return
     */
    @Override
    public synchronized TtsResult process(String say, File file) {
        TtsResult result = new TtsResult(new CompletableFuture<>(), new CountDownLatch(1), true);
        ThreadUtil.execute(() -> {
            try {
                // 保证阻塞，只有一个请求正在处理
                requestSemaphore.acquire();
                // 当前正在处理的请求
                currentRequest.set(new TTSRequest(result, file));

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
    public void close() {
        heartbeatFuture.shutdownNow();
        synthesizer.close();
        client.shutdown();
    }
}
