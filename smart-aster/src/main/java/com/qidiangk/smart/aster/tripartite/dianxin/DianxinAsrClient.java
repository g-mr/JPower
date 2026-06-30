package com.qidiangk.smart.aster.tripartite.dianxin;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qidiangk.smart.aster.tripartite.property.DianxinAsrOption;
import com.qidiangk.smart.aster.tripartite.property.DianxinProperty;
import jakarta.websocket.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.AsrResult;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.JsonUtil;
import top.jpower.core.util.utils.StringUtil;

import java.io.IOException;
import java.io.PipedInputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 电信ASR识别
 * <br />
 * <a href="https://www.teleai.com.cn/doc/QVY8etHoYdG4XoW4Nxvz/msHIQauqMGhdwd4iJLR1">对接文档</a>
 */
@Slf4j
public class DianxinAsrClient extends Endpoint implements AsrClient {

    /**
     * 解码器
     */
    public static class MessageDTODecoder implements Decoder.Text<MessageAsrDTO> {

        @Override
        public MessageAsrDTO decode(String text) {
            return JsonUtil.parseObject(text, MessageAsrDTO.class);
        }

        @Override
        public boolean willDecode(String text) {
            return JSONUtil.isTypeJSONObject(text);
        }
    }

    private final DianxinProperty dianxinProperty;

    private volatile Session session;
    private volatile Integer RES_STATUS = 4;
    // 收到状态RES_STATUS=2的时间
    private volatile long RES_STATUS_2_TIME = System.currentTimeMillis();
    private final StringBuffer MESSAGE = new StringBuffer();

    public DianxinAsrClient() {
        this(null);
    }

    @SneakyThrows
    public DianxinAsrClient(DianxinAsrOption asrOption) {
        dianxinProperty = BeanUtil.copyProperties(SpringUtil.getBean(DianxinProperty.class), DianxinProperty.class);
        if (asrOption != null) {
            dianxinProperty.setAsrOption(BeanUtil.copyProperties(SpringUtil.getBean(DianxinProperty.class).getAsrOption(), DianxinProperty.AsrOption.class));
            BeanUtil.copyProperties(asrOption, dianxinProperty, CopyOptions.create().ignoreNullValue()
                    .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));
            BeanUtil.copyProperties(asrOption, dianxinProperty.getAsrOption(), CopyOptions.create().ignoreNullValue()
                    .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));
        }

        // 连接时传入 config 和 endpoint 类
        URI uri = URI.create(dianxinProperty.getAsrUrl());

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                .configurator(new HeaderAuthConfigurator(dianxinProperty, uri))
                .decoders(Collections.singletonList(MessageDTODecoder.class))
                .build();

        //noinspection resource
        container.connectToServer(this, config, uri);
    }

    @SneakyThrows
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        // 将最大文本消息缓冲区大小设置为 1MB (单位是字节)
        session.setMaxTextMessageBufferSize(1024 * 1024);
        // 添加消息处理器
        session.addMessageHandler(new StringMessageHandler());
        this.session = session;
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        log.info("[dianxin-asr]连接关闭={}", JSON.toJSONString(closeReason));
        this.session = null;
        RES_STATUS = 4;
    }

    @Override
    public void onError(Session session, Throwable thr) {
        log.error("[dianxin-asr]连接报错={}", ExceptionUtil.stacktraceToString(thr));
    }

    // 自定义消息处理器
    private class StringMessageHandler implements MessageHandler.Whole<MessageAsrDTO> {
        @Override
        public void onMessage(MessageAsrDTO message) {
            if (Fc.equalsValue(message.code(), 10000)){
                RES_STATUS = message.resStatus();
                if (Fc.equalsValue(RES_STATUS, 2) || Fc.equalsValue(RES_STATUS, 3) || Fc.equalsValue(RES_STATUS, 4)) {
                    RES_STATUS_2_TIME = System.currentTimeMillis();
                }
                if (Fc.equalsValue(RES_STATUS, 3) || Fc.equalsValue(RES_STATUS, 4)){
                    message.data().results().forEach(result -> {
                        synchronized (MESSAGE) {
                            MESSAGE.append(result.text());
                        }
                    });
                }
            } else {
                log.warn("收到电信ASR解析出错=>>{}", JsonUtil.toJson(message));
                // 标记当前这个请求出错了，取消转换
                RES_STATUS = -1;
            }
        }
    }

    @SneakyThrows
    public void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(message);
        } else {
            throw new IllegalStateException("WebSocket 会话未打开");
        }
    }

    @Override
    public AsrResult process(PipedInputStream pipedInput) throws InterruptedException {
        Thread main = Thread.currentThread();
        while (Fc.notEqualsValue(RES_STATUS, 4)){
            if (main.isInterrupted()){
                throw new InterruptedException();
            }
            ThreadUtil.safeSleep(100);
            log.info("当前正在识别==={}", RES_STATUS);
        }

        AtomicBoolean started = new AtomicBoolean(false);
        AtomicReference<Consumer<Long>> noDataReference = new AtomicReference<>(null);
        AtomicReference<Consumer<String>> dataReference = new AtomicReference<>(null);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 发送开始识别
            synchronized (MESSAGE) {
                MESSAGE.setLength(0);
            }
            JSONObject initSend = new JSONObject();
            initSend.put("req_id", IdUtil.getSnowflakeNextIdStr());
            initSend.put("rec_status", 0);
            initSend.put("option", dianxinProperty.getAsrOption());
            sendMessage(initSend.toJSONString());

            // 发送数据
            CompletableFuture<Void> futureSend = CompletableFuture.runAsync(()->{
                try {
                    byte[] buffer = new byte[3200];
                    int len = 0;

                    while (len >= 0 && session.isOpen()) {
                        if (RES_STATUS == -1) {
                            log.error("当前这个请求出错了，取消发送");
                            break;
                        }
                        if (RES_STATUS < 4){
                            if ((len = pipedInput.read(buffer)) > 0) {
                                JSONObject audioSend = new JSONObject();
                                audioSend.put("rec_status", 1);
                                audioSend.put("audio_stream", Base64.getEncoder().encodeToString(Arrays.copyOfRange(buffer, 0, len)));
                                sendMessage(audioSend.toJSONString());
                            }
                        }
                    }
                } catch (IOException e) {
                    log.error("录音流读取异常==>>{}", e.getMessage());
                }

                log.info("读取流完成,读取线程结束==>>");
            });

            // 监控收到了用户话语
            String str = "";
            while (Fc.isBlank(str) && !futureSend.isDone()) {
                if (!started.get()){
                    started.set(RES_STATUS == 1 || RES_STATUS == 2 || RES_STATUS == 3);
                    // 刷新开始时间
                    if (started.get()) {
                        RES_STATUS_2_TIME = System.currentTimeMillis();
                    }
                }

                if (MESSAGE.length() > 0) {

                    if (Fc.notNull(dataReference.get())){
                        // 持续返回结果
                        dataReference.get().accept(MESSAGE.toString());
                        synchronized (MESSAGE) {
                            MESSAGE.setLength(0);
                        }
                    } else {
                        JSONObject endSend = new JSONObject();
                        endSend.put("rec_status", 2);
                        sendMessage(endSend.toJSONString());

                        // 停止接受数据
                        futureSend.cancel(true);

                        try {
                            pipedInput.close();
                        } catch (IOException e) {
                            log.error("录音流读取关闭异常==>>{}", ExceptionUtil.stacktraceToString(e));
                        }

                        str = MESSAGE.toString();

                        // 清空取走的内容
                        synchronized (MESSAGE) {
                            MESSAGE.setLength(0);
                        }
                    }
                } else {
                    if (Fc.notNull(noDataReference.get())) {
                        noDataReference.get().accept(DateUtil.spendMs(RES_STATUS_2_TIME));
                    }
                }
            }

            // 录音流发送完了，但是没有识别出结果，需要手动关闭当前请求
            if (Fc.isBlank(str) && futureSend.isDone()){
                if (RES_STATUS == -1){
                    throw new RuntimeException("电信ASR识别出错了....");
                }
                JSONObject endSend = new JSONObject();
                endSend.put("rec_status", 2);
                sendMessage(endSend.toJSONString());

                synchronized (MESSAGE) {
                    MESSAGE.setLength(0);
                }

                try {
                    pipedInput.close();
                } catch (IOException e) {
                    log.error("录音流读取关闭异常==>>{}", ExceptionUtil.stacktraceToString(e));
                }
            }
            return str;
        });

        return new AsrResult(future, started, dataReference, noDataReference);
    }

    @SneakyThrows
    @Override
    public void close() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
}
