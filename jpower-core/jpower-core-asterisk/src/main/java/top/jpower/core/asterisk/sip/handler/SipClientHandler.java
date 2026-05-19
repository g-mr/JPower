package top.jpower.core.asterisk.sip.handler;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import top.jpower.core.asterisk.sip.Config;
import top.jpower.core.asterisk.sip.JavaConfig;
import top.jpower.core.asterisk.sip.Slf4jLogger;
import top.jpower.core.asterisk.sip.annotation.Sip;
import top.jpower.core.asterisk.sip.dto.SipDTO;
import top.jpower.core.asterisk.sip.javaxsound.StreamxSoundManager;
import top.jpower.core.asterisk.sip.media.MediaMode;
import top.jpower.core.asterisk.sip.sip.RFC3261;
import top.jpower.core.asterisk.sip.sip.Utils;
import top.jpower.core.asterisk.sip.sip.core.useragent.SipListener;
import top.jpower.core.asterisk.sip.sip.core.useragent.UserAgent;
import top.jpower.core.asterisk.sip.sip.syntaxencoding.SipHeaderFieldName;
import top.jpower.core.asterisk.sip.sip.syntaxencoding.SipHeaders;
import top.jpower.core.asterisk.sip.sip.syntaxencoding.SipUriSyntaxException;
import top.jpower.core.asterisk.sip.sip.transactionuser.Dialog;
import top.jpower.core.asterisk.sip.sip.transactionuser.DialogManager;
import top.jpower.core.asterisk.sip.sip.transport.SipRequest;
import top.jpower.core.asterisk.sip.sip.transport.SipResponse;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.DisposableBean;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * SIP抽象实现
 *
 * @author mr.g
 */
@Slf4j
public abstract class SipClientHandler implements SipListener, DisposableBean{

    private StreamxSoundManager soundManager;
    protected UserAgent userAgent;
    protected static final ScheduledExecutorService expireRegister = Executors.newScheduledThreadPool(1);
    protected static ScheduledFuture<?> expireFuture;
    @Getter
    protected SipRequest currentSipRequest;

    // 转换为电话支持的格式 (G.711 μ-law, 8000 Hz)
    protected AudioFormat targetFormat = new AudioFormat(8000, 16, 1, true, false);

    @Getter
    private SipDTO sip;

    /**
     * 是否注册成功
     */
    @Getter
    private boolean register = false;

    /**
     * 动态注册
     * 最好别用，这种注册方式需要自己维护注册、和注销
     * @param sip
     */
    public SipClientHandler(SipDTO sip) {
        this.sip = sip;
    }

    @SneakyThrows
    public SipClientHandler() {
        Class<?> clazz = getTargetClass();
        Sip sip = clazz.getAnnotation(Sip.class);

        this.sip = SipDTO.builder()
                .password(sip.password())
                .username(sip.username())
                .port(sip.port())
                .domain(sip.domain())
                .build();

        if (sip.register()){
            register();
        }
    }

    private Class<?> getTargetClass(){
        Class<?> clazz = this.getClass();
        // 处理 Spring 代理类
        if (AopUtils.isAopProxy(this)) {
            clazz = AopUtils.getTargetClass(this);
        }
        return clazz;
    }

    /**
     * 注册
     */
    public void register() {
        register(sip.username(), sip.password(), sip.domain(), sip.port());
    }

    /**
     * 注册
     * @param username
     * @param password
     * @param domain
     * @param port
     */
    private void register(String username, String password, String domain, Integer port) {
        log.info("开始注册用户[{}]代理", username);
        Optional.ofNullable(expireFuture).ifPresent(expireFuture-> expireFuture.cancel(true));
        if (userAgent != null && userAgent.isRegistered()) {
            log.warn("已经存在用户代理，下线：{}", userAgent.getUserpart());
            this.destroy();
        }
        Slf4jLogger logger = new Slf4jLogger();
//        final AbstractSoundManager soundManager = new JavaxSoundManager(false, logger, Utils.DEFAULT_PEERS_HOME);
        soundManager = new StreamxSoundManager(this::reception);
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            Config config = new JavaConfig();
            config.setUserPart(username);
            config.setDomain(domain + StrPool.COLON + port);
            config.setPassword(password);
            config.setMediaMode(MediaMode.captureAndPlayback);
            config.setLocalInetAddress(localHost);
            userAgent = new UserAgent(this, config, logger, soundManager);
            try {
                userAgent.register();
            } catch (SipUriSyntaxException e) {
                throw new RuntimeException(e);
            }
            log.warn("发起注册[{}]", username);
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 接受声音
     *
     * @return
     */
    protected abstract int reception(byte[] buffer, int offset, int length);

    /**
     * 发送声音
     *
     * @param bytes
     */
    public void send(byte[] bytes){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length + 4);
            buffer.putInt(bytes.length);  // 写入数据长度
            buffer.put(bytes);            // 写入实际数据
            buffer.flip();

            // 写入管道
            while (buffer.hasRemaining()) {
                soundManager.getSink().write(buffer);
            }

            // 计算播放需要的时间
            double ms = NumberUtil.calculate(bytes.length + "/" + 320 + "*" + 20);
            System.out.println("播放所需要的时间是===="+ ms);
            ThreadUtil.sleep(ms);

        } catch (IOException e) {
            log.error("input/output error", e);
        }
    }

    /**
     * 发送声音
     *
     * @param file
     */
    @SneakyThrows
    public void send(File file){
        @Cleanup AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        AudioFormat sourceFormat = audioStream.getFormat();
        // 仅当格式不匹配时才进行转换
        if (!isCompatibleFormat(sourceFormat, targetFormat)) {
            System.out.println("执行音频格式转换...");
            audioStream = AudioSystem.getAudioInputStream(targetFormat, audioStream);
        }
        byte[] buffer = audioStream.readAllBytes(); // 20ms 数据块（8000Hz * 0.02s = 160 样本）
        send(buffer);
    }

    private boolean isCompatibleFormat(AudioFormat source, AudioFormat target) {
        return source.getEncoding().equals(target.getEncoding())
                && source.getSampleRate() == target.getSampleRate()
                && source.getSampleSizeInBits() == target.getSampleSizeInBits()
                && source.getChannels() == target.getChannels();
    }

    /**
     * 注销
     */
    @Override
    public void destroy() {
        if (this.userAgent != null) {
            try {
                log.debug("注销代理[{}]", sip.username());
                Optional.ofNullable(expireFuture).ifPresent(expireFuture-> expireFuture.cancel(true));
                this.userAgent.unregister();
            } catch (SipUriSyntaxException e) {
                log.error("注销代理[{}]失败，{}", sip.username(), e.getMessage());
            }
        }
        this.register = false;
    }

    /**
     * 注册中
     * 回调方法
     * @param sipRequest
     */
    @Override
    public void registering(SipRequest sipRequest) {
        log.warn("注册中 [{}]", sip.username());
    }

    /**
     * 注册成功
     * 回调方法
     * @param sipResponse
     */
    @Override
    public void registerSuccessful(SipResponse sipResponse) {
        this.register = true;
        // 注册过期时间
        String expires = "";

        try {
            expires = sipResponse.getSipHeaders().get(new SipHeaderFieldName(RFC3261.HDR_EXPIRES)).getValue();
        } catch (Exception e) {
            log.debug("过期时间为空");
        }
        if (StrUtil.isNotBlank(expires)) {
            long expire = Long.parseLong(expires);
            if (expire == 0) {
                log.info("注销成功[{}]", sip.username());
            } else {
                log.debug("注册[{}]自动续期,expire:{}", sip.username(), expire);
                // 自动续期
                Optional.ofNullable(expireFuture).ifPresent(expireFuture-> expireFuture.cancel(true));
                expireFuture = expireRegister.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            if (userAgent != null && userAgent.isRegistered()) {
                                log.debug("开始自动续期[{}]", sip.username());
                                userAgent.register();
                            }
                        } catch (SipUriSyntaxException e) {
                            log.error("[{}]自动续期失败==》》{}", sip.username(), e.getMessage());
                        }
                    }
                }, expire, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * 注册失败
     * 回调方法
     * @param sipResponse
     */
    @Override
    public void registerFailed(SipResponse sipResponse) {
        log.warn("注册失败 [{}]", sip.username());
        this.register = false;
    }

    /**
     * 来电
     * 回调方法
     * @param sipRequest
     * @param provResponse
     */
    @Override
    public void incomingCall(SipRequest sipRequest, SipResponse provResponse) {
        SipHeaders sipHeaders = sipRequest.getSipHeaders();
        String headFrom = sipHeaders.get(new SipHeaderFieldName(RFC3261.HDR_FROM)).getValue();
        String tempNumber = headFrom.substring(headFrom.indexOf("<") + 5, headFrom.indexOf("@"));
        String fromNumber = "";
        if (!tempNumber.equals("unknown")) {
            fromNumber = tempNumber;
        }
        // "MonitorChannel" <sip:unknown@192.168.2.150>
        String displayName = fromNumber;
        if (headFrom.startsWith("\"")) {
            displayName = headFrom.substring(1, headFrom.lastIndexOf("\""));
        }
        log.info("[{}]来电：{}\tdisplayName：{}", sip.username(),fromNumber, displayName);

        currentSipRequest = sipRequest;
    }

    /**
     * 接听
     * @return
     */
    public boolean pickup() {
        if (currentSipRequest == null) {
            log.warn("[{}]当前无等待接听电话", sip.username());
            return false;
        }
        String callId = Utils.getMessageCallId(currentSipRequest);
        DialogManager dialogManager = userAgent.getDialogManager();
        Dialog dialog = dialogManager.getDialog(callId);
        userAgent.acceptCall(currentSipRequest, dialog);
        log.info("[{}]接听", sip.username());
        return true;
    }

    /**
     * 挂断
     * @return
     */
    public boolean hangup() {
        if (currentSipRequest == null) {
            log.warn("[{}]当前无已接听电话", sip.username());
            return false;
        }
        userAgent.terminate(currentSipRequest);
        currentSipRequest = null;
        return true;
    }

    /**
     * 拨号
     * @param number
     */
    public boolean call(String number) throws SipUriSyntaxException {
        if (StrUtil.isBlank(number)) {
            log.warn("号码为空");
            return false;
        }
        currentSipRequest = userAgent.invite("sip:" + number + "@" + userAgent.getDomain(), Utils.generateCallID(userAgent.getConfig().getLocalInetAddress()));
        return true;
    }

    /**
     * 对方挂断
     * 回调方法
     * @param sipRequest
     */
    @Override
    public void remoteHangup(SipRequest sipRequest) {
        log.warn("[{}]对方挂断", sip.username());
        currentSipRequest = null;
    }

    /**
     * 响铃
     * 回调方法
     * @param sipResponse
     */
    @Override
    public void ringing(SipResponse sipResponse) {
        log.warn("[{}]振铃中", sip.username());
    }

    /**
     * 对方接通
     * 回调方法
     * @param sipResponse
     */
    @Override
    public void calleePickup(SipResponse sipResponse) {
        log.warn("[{}]已接通", sip.username());
    }

    /**
     * 异常
     * 回调方法
     * @param sipResponse
     */
    @Override
    public void error(SipResponse sipResponse) {
        log.warn("[{}]error[{}]==>>{}", sip.username(), sipResponse.getStatusCode(), sipResponse.getReasonPhrase());
        if("Request Timeout".equals(sipResponse.getReasonPhrase())){
            log.warn("[{}]对方挂断,或者无法接通 statusCode={}", sip.username(), sipResponse.getStatusCode());
        }
        currentSipRequest = null;
    }

}
