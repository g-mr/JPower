package com.qidiangk.smart.aster.tripartite.dianxin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.qidiangk.smart.aster.tripartite.property.DianxinProperty;
import com.qidiangk.smart.aster.tripartite.property.DianxinTtsOption;
import jakarta.websocket.*;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.TtsClient;
import top.jpower.core.asterisk.audio.TtsResult;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.JsonUtil;
import top.jpower.core.util.utils.StringUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 电信TTS识别
 * <br />
 * <a href="https://www.teleai.com.cn/doc/4UqYzHGWf0SzdkY8cf2f/PjIFm7qmyp633CEPWWKR">对接文档</a>
 */
@Slf4j
public class DianxinTtsClient extends Endpoint implements TtsClient {

    /**
     * 请求体
     */
    @Data
    @Accessors(fluent = true)
    private static class TTSRequest {

        private final CompletableFuture<Void> future;
        private final DianxinProperty.TtsOption ttsOption;
        private final OutputStream audioOutput;
        private final CountDownLatch countDownLatch = new CountDownLatch(1);

        public TTSRequest(CompletableFuture<Void> future, DianxinProperty.TtsOption ttsOption, OutputStream audioOutput){
            this.future = future;
            this.ttsOption = ttsOption;
            this.audioOutput = audioOutput;
        }

        public void write(byte[] audioData) {
            try {
                audioOutput.write(audioData);
                audioOutput.flush();
            } catch (IOException e) {
                log.error("[dianxin-tts]音频流写入报错={}", ExceptionUtil.stacktraceToString(e));
            }
        }

        public void closeOutput() {
            try {
                audioOutput.close();
            } catch (IOException e) {
                log.error("[dianxin-tts]音频流关闭报错={}", ExceptionUtil.stacktraceToString(e));
            }
        }
    }

    /**
     * 解码器
     */
    public static class MessageDTODecoder implements Decoder.Text<MessageTtsDTO> {

        @Override
        public MessageTtsDTO decode(String text) {
            return JsonUtil.parseObject(text, MessageTtsDTO.class);
        }

        @Override
        public boolean willDecode(String text) {
            return JSONUtil.isTypeJSONObject(text);
        }
    }

    private volatile Session session;
    private final DianxinProperty dianxinProperty;

    // 请求队列
    private final BlockingQueue<TTSRequest> requestQueue = new LinkedBlockingQueue<>();
    // 请求通过，等待处理的请求
    private final BlockingQueue<TTSRequest> pendingQueue = new LinkedBlockingQueue<>();
    // 当前处理的请求
    private final AtomicReference<TTSRequest> currentRequest = new AtomicReference<>();

    public DianxinTtsClient() {
        this(null);
    }

    @SneakyThrows
    public DianxinTtsClient(DianxinTtsOption ttsOption) {
        dianxinProperty = BeanUtil.copyProperties(SpringUtil.getBean(DianxinProperty.class), DianxinProperty.class);
        if (ttsOption != null) {
            dianxinProperty.setTtsOption(BeanUtil.copyProperties(SpringUtil.getBean(DianxinProperty.class).getTtsOption(), DianxinProperty.TtsOption.class));
            BeanUtil.copyProperties(ttsOption, dianxinProperty, CopyOptions.create().ignoreNullValue()
                    .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));
            BeanUtil.copyProperties(ttsOption, dianxinProperty.getTtsOption(), CopyOptions.create().ignoreNullValue()
                    .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));
        }

        // 连接时传入 config 和 endpoint 类
        URI uri = URI.create(dianxinProperty.getTtsUrl());

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                .configurator(new HeaderAuthConfigurator(dianxinProperty, uri))
                .decoders(Collections.singletonList(MessageDTODecoder.class))
                .build();

        //noinspection resource
        container.connectToServer(this, config, uri);
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        // 将最大文本消息缓冲区大小设置为 1MB (单位是字节)
        session.setMaxTextMessageBufferSize(1024 * 1024);
        // 添加消息处理器
        session.addMessageHandler(new StringMessageHandler());
        this.session = session;
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        log.info("[dianxin-tts]连接关闭={}", JSON.toJSONString(closeReason));
        this.session = null;

        List<TTSRequest> list = new ArrayList<>();
        requestQueue.drainTo(list);
        pendingQueue.drainTo(list);
        if (currentRequest.get() != null){
            list.add(currentRequest.get());
            currentRequest.set(null);
        }
        for (TTSRequest request : list) {
            if (!request.future().isDone()) {
                request.future().completeExceptionally(new IllegalStateException("连接关闭..."));
            }
            request.closeOutput();
        }
    }

    @Override
    public void onError(Session session, Throwable thr) {
        log.error("[dianxin-tts]连接报错={}", ExceptionUtil.stacktraceToString(thr));
    }

    /**
     * 消息处理器
     */
    private class StringMessageHandler implements MessageHandler.Whole<MessageTtsDTO> {

        @SneakyThrows(InterruptedException.class)
        @Override
        public void onMessage(MessageTtsDTO message) {
            if (Fc.equalsValue(message.status(), 10000)){

                if (Fc.isNull(message.result()) && Fc.isNotBlank(message.sid())){
                    // 代表一个请求通过，可以处理了
                    TTSRequest request = requestQueue.poll();
                    if (request != null){
                        // 从请求队列中取出来，放到处理队列中
                        if (pendingQueue.offer(request)){
                            // 代表一个请求已经通过，可以处理了
                        } else {
                            log.warn("当前有请求正在处理，正常情况不可能执行这里，如果执行就需要好好检查代码并发逻辑，或者电信得文档骗人，不是按发送顺序返回的。当前的请求={}==={}", JSON.toJSONString(currentRequest.get().ttsOption()),JsonUtil.toJson(message));
                        }
                    } else {
                        log.warn("收到电信TTS解析结果，但是没有请求，正常情况不可能执行这里==>>{}", JsonUtil.toJson(message));
                    }
                } else if (Fc.notNull(message.result())){
                    // 代表这是正常返回了数据流, 这里锁住，防止连续多个在这里阻塞从队列取出
                    synchronized (currentRequest){
                        if (currentRequest.get() == null) {
                            // 这是一个新的请求已经开始处理了，需要从待处理队列中取出来，放到当前处理中
                            currentRequest.set(pendingQueue.take());
                        }
                    }

                    TTSRequest request = currentRequest.get();
                    byte[] buffer = Base64.getDecoder().decode(message.result().audio());
                    request.write(buffer);
                    // 标记为已经开始写入
                    request.countDownLatch().countDown();

                    if (message.result().isEnd()){
                        request.closeOutput();
                        // 标记为已完成
                        request.future().complete( null);
                        currentRequest.set(null);
                    }

                } else {
                    log.warn("收到电信TTS解析奇怪的结果，正常不应该进入到这里，如果打印这行得找电信得人问原因===>消息体：{}", JsonUtil.toJson(message));
                }

            } else {
                log.error("收到电信TTS解析出错=>>{}", JsonUtil.toJson(message));
                // 从处理的请求队列取出第一个，代表这个请求不会处理了，返回异常
                TTSRequest request = requestQueue.poll();
                if (request != null) {
                    request.future().completeExceptionally(new RuntimeException("TTS服务返回错误: "+message.statusMsg()));
                    log.error("TTS解析出错，请求抛弃==>>{}", JSON.toJSONString(request.ttsOption()));
                }
            }
        }
    }

    @SneakyThrows
    public synchronized void sendMessage(DianxinProperty.TtsOption message) {
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(JSON.toJSONString(message));
        } else {
            throw new IllegalStateException("WebSocket 会话未打开");
        }
    }

    /**
     * 处理时
     * 使用锁机制，单实例下只能同时识别一个
     *
     * @param say 要转语音的内容
     * @param audioOutput 音频数据输出流
     * @return
     */
    @Override
    public TtsResult process(String say, OutputStream audioOutput) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // 创建TTS请求选项
        DianxinProperty.TtsOption ttsOption = BeanUtil.copyProperties(
                dianxinProperty.getTtsOption(), DianxinProperty.TtsOption.class);
        ttsOption.setText(say);
        ttsOption.setReqId(IdUtil.getSnowflakeNextIdStr());

        // 加入队列成功则发送请求
        TTSRequest request = new TTSRequest(future, ttsOption, audioOutput);
        if (requestQueue.offer(request)) {
            try {
                sendMessage(ttsOption);
            } catch (Exception e){
                //noinspection ResultOfMethodCallIgnored
                requestQueue.remove(request);
                future.cancel( true);
                throw new IllegalStateException("发送失败", e);
            }
        } else {
            future.cancel( true);
            throw new IllegalStateException("请求队列已满");
        }

        return new TtsResult(future, request.countDownLatch(), false);
    }

    @SneakyThrows(IOException.class)
    @Override
    public void close() {
        if (session != null && session.isOpen()){
            session.close();
        }
    }

}
