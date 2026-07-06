package top.jpower.core.asterisk.agi.fastagi.support;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.*;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import top.jpower.core.asterisk.ami.action.ControlPlaybackAction;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.AsrResult;
import top.jpower.core.asterisk.audio.TtsClient;
import top.jpower.core.asterisk.audio.TtsResult;
import top.jpower.core.asterisk.properties.AsteriskProperties;
import top.jpower.core.util.utils.Fc;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Accessors(fluent = true)
public abstract class AgiSupport {

    @Getter
    private final AgiChannel channel;
    @Getter
    private final AgiRequest request;
    @Getter
    private final Thread thread;
    @Getter
    private final AsrClient asrClient;
    @Getter
    private final TtsClient ttsClient;

    /**
     * 是否机器人主动挂断
     * 没有挂断值是null
     */
    @Getter
    private Boolean isBotHuang;
    private final ManagerConnection managerConnection = SpringUtil.getBean(ManagerConnection.class);
    private final AgiHangupEventManager agiHangupEventManager = SpringUtil.getBean(AgiHangupEventManager.class);
    private final String rootDir = SpringUtil.getBean(AsteriskProperties.class).getVoiceRootDir();

    public AgiSupport(AgiChannel channel, AgiRequest request, Thread thread) {
        this.channel = channel;
        this.request = request;
        this.thread = thread;

        // 补全Bean
        this.asrClient = initAsr();
        this.ttsClient = initTts();


        // 添加监听
        agiHangupEventManager.addAgi(channel.getUniqueId());
    }

    /**
     * 实时数据
     * @param isBot 是否是机器人话语
     * @param filePath 说话内容语音文件
     * @param content 说话内容
     */
    protected abstract void realtimeData(Boolean isBot, String filePath, String content);

    /**
     * 初始化asr
     *
     * @return asrClient
     */
    protected abstract AsrClient initAsr();

    /**
     * 初始化tts
     *
     * @return ttsClient
     */
    protected abstract TtsClient initTts();

    /**
     * 1=呼入 2=呼出
     */
    public String getCallType() {
        return request.getParameter("type");
    }

    public String getPhone() {
        return Fc.equalsValue(getCallType(), 2)?request.getCallerIdNumber():request.getRequest().get("callerid");
    }

    public String getUniqueId() {
        return channel.getUniqueId();
    }

    public String getName() {
        return channel.getName();
    }

    @SneakyThrows
    public void answer() {
        channel.answer();
    }

    @SneakyThrows
    public void hangup() {
        isBotHuang = Boolean.TRUE;
        channel.hangup();
    }

    public boolean isHangup() {
        return agiHangupEventManager.isAgiHangup(getUniqueId());
    }

    @SneakyThrows
    public int exec(String application) {
        return channel.exec(application);
    }

    @SneakyThrows
    public int exec(String application, String... options) {
        return channel.exec(application, options);
    }

    public void playMusicOnHold() throws AgiHangupException {
        try {
            channel.playMusicOnHold();
        } catch (AgiHangupException e){
            throw e;
        } catch (AgiException e){
            log.error("播放音乐失败==={}", ExceptionUtil.stacktraceToString(e));
        }
    }

    public void playMusicOnHold(String musicOnHoldClass) throws AgiHangupException {
        try {
            channel.playMusicOnHold(musicOnHoldClass);
        } catch (AgiHangupException e){
            throw e;
        } catch (AgiException e){
            log.error("播放音乐失败==={}", ExceptionUtil.stacktraceToString(e));
        }
    }

    @SneakyThrows
    public void stopMusicOnHold() {
        channel.stopMusicOnHold();
    }

    @SneakyThrows
    public void setVariable(String name, String value) {
        channel.setVariable(name, value);
    }

    @SneakyThrows
    public String getVariable(String name) {
        return channel.getVariable(name);
    }

    /**
     * 播放文件
     */
    public void streamFile(String say) {
        streamFile(say, false);
    }
    public void streamFile(String say, boolean isInterrupt) {
        streamFile(say, isInterrupt, true);
    }
    public void streamFile(String say, boolean isInterrupt, boolean isSaveFile) {
        try {
            log.info("播放通知=={}", say);
            String path = createWav(say, isSaveFile);
            realtimeData(true, path+".wav", say);
            if (isInterrupt){
                channel.streamFile(path, "#");
            } else {
                channel.streamFile(path);
            }
        } catch (AgiHangupException e) {
            thread.interrupt();
        } catch (AgiException e) {
            throw new RuntimeException(e);
        }
    }

    private String createWav(String say, boolean isSaveFile) {
        String parentDir = StrUtil.concat(true, rootDir, getPhone(), (isSaveFile?"/":"/voice/"));
        FileUtil.mkdir(parentDir);

        String filePath =  StrUtil.concat(true,
                parentDir,
                ClassUtil.getClassName(ttsClient, true),
                "_",
                SecureUtil.md5(say),
                ".wav");

        if (FileUtil.exist(filePath)){
            return StrUtil.removeSuffix(filePath, ".wav");
        } else {
            TtsResult result = ttsClient.process(say, new File(filePath));

            try {
                if (result.isWriterPlay()){
                    result.waitStart();
                } else {
                    result.future().get(1L, TimeUnit.MINUTES);
                }
                return StrUtil.removeSuffix(filePath, ".wav");
            } catch (InterruptedException e) {
                thread.interrupt();
            } catch (ExecutionException e) {
                log.error("播放文件出错[播放内容={}，文件名={}]===>>{}{}", say, filePath, StrPool.CRLF, ExceptionUtil.stacktraceToString(e));
            } catch (java.util.concurrent.TimeoutException e) {
                log.error("播放文件创建超时[播放内容={}，文件名={}]===>>{}{}", say, filePath, StrPool.TAB, e.getMessage());
            }
            return "";
        }
    }

    /**
     * 播放文件
     * <br />
     * 可以通过AMI进行停止
     *
     * @param say 播放内容
     * @param isSaveFile 转换出来的文件是否要保留
     */
    public void playback(String say, boolean isSaveFile) {
        log.info("播放内容=={}", say);
        String path = createWav(say, isSaveFile);
        realtimeData(true, path+".wav", say);
        exec("Playback", path);
    }

    /**
     * 播放文件
     * <br />
     * 可以通过AMI进行停止
     *
     * @param say 播放内容
     */
    public void playback(String say) {
        playback(say, false);
    }

    public String received(String say, String end, boolean isSaveFile) {
        return received(say, end, isSaveFile, -1L);
    }
    public String received(String say, String end) {
        return received(say, end, false);
    }
    /**
     * 收号
     *
     * @param say 播放内容
     * @param end 结束字符
     * @param isSaveFile 录音文件是否保存
     * @param timeout 超时时间
     * @return 收号内容
     */
    public String received(String say, String end, boolean isSaveFile, long timeout) {
        String path = createWav(say, isSaveFile);
        String content = null;
        try {
            log.info("播放收号=={}", say);
            realtimeData(true, path+".wav", say);
            if (Fc.equalsValue(end, "#")){
                content = channel.getData(path, timeout);
            } else {
                char cr = channel.getOption(path, end, timeout);
                if (!CharUtil.isBlankChar(cr)) {
                    content = CharUtil.toString(cr);
                }
            }
        } catch (AgiHangupException e){
            thread.interrupt();
            log.warn("电话挂断了...中断线程");
        } catch (AgiException e){
            log.error("播放失败[{}]==>>{}", path, ExceptionUtil.stacktraceToString(e));
        }

        realtimeData(false, null, content);
        return content;
    }

    /**
     * 开始双向录音
     *
     * @param file  存储文件
     * @return 录音ID
     */
    public String mixMonitor(String file) {
        exec("MixMonitor", file, "i(mixMonitorId)");
        return getVariable("mixMonitorId");
    }

    /**
     * 停止双向录音
     *
     * @param monitorId  存储文件
     */
    public void stopMixMonitor(String monitorId) {
        exec("StopMixMonitor", monitorId);
    }

    /**
     * 录音
     *
     * @return 录音内容
     */
    public String radio() {
        return radio(null, false);
    }

    public String radio(String say) {
        return radio(say, false);
    }

    /**
     * 边播边录音，一旦有录音立马进行打断
     * <br />
     * 锁机制，一个通话只能同时一条,确保线程安全
     *
     * @param say 播报内容,为空代表不播放
     * @return 录音内容
     */
    public synchronized String radio(String say, boolean isSaveFile) {

        // 创建录音文件
        String parentPath = StrUtil.concat(true, rootDir, getPhone(), "/voice/");
        FileUtil.mkdir(parentPath);

        String fileName = IdUtil.getSnowflakeNextIdStr();
        String file = parentPath + fileName +".wav";
        String userFile = parentPath + "user_" + fileName +".wav";
        FileUtil.mkParentDirs(userFile);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 创建录音输入流管道
        try (PipedOutputStream pipedOut = new PipedOutputStream()){

            // 标识是否播完了
            AtomicBoolean isVoiceEnd = new AtomicBoolean(false);
            // 记录停止播放时间
            AtomicLong voiceStopTime = new AtomicLong(0);

            // 识别内容
            AsrResult result = asrClient.process(new PipedInputStream(pipedOut, 1024 * 1024));
            result.noDataReference().set(ms->{
                // 已经播放完了，但是等了10秒用户还不说话，就提示
                if (isVoiceEnd.get() && !result.isStarted() && voiceStopTime.get() > 0 && DateUtil.spendMs(voiceStopTime.get()) > 10000){
                    streamFile("喂。您有什么问题？", true);
                    // 播放一次就不用重复播了
                    result.noDataReference().set(null);
                }

                // 已经播放完了，识别到用户说话，但是等了3秒用户还没有新的话语，标识没有识别清楚就给个提示
//                if (isVoiceEnd.get() && result.isStarted() && ms > 3000) {
//                    streamFile("您请说...", true);
                    // 播放一次就不用重复播了
//                    result.noDataReference().set(null);
//                }

            });

            // 开始录音
            exec("MixMonitor", file, "r("+userFile+")i(monitorId)"); //v(4)

            // 启动线程持续发送录音内容
            executor.execute(()->{
                // 等待录音文件创建（MixMonitor启动到文件实际创建存在微小延迟）
                File recordFile = new File(userFile);
                int maxRetries = 50; // 最多等待5秒（50 * 100ms）
                while (!recordFile.exists() && maxRetries-- > 0 && !isHangup()) {
                    ThreadUtil.sleep(100);
                }
                if (!recordFile.exists()) {
                    log.error("录音文件等待超时仍未创建[{}]", userFile);
                    return;
                }

                try (FileInputStream fis = new FileInputStream(recordFile)){
                    byte[] buffer = new byte[3200];
                    int len;

                    TimeInterval timer = DateUtil.timer();
                    timer.start();
                    while (!isHangup()) {
                        while ((len = fis.read(buffer)) > 0) {
                            pipedOut.write(buffer, 0, len);
                            pipedOut.flush();
                            timer.restart();
                        }

                        if (timer.intervalSecond() > 5) {
                            log.warn("当前文件长时间未检测到新增录音,结束读取,文件={}", userFile);
                            break;
                        }
                    }
                    timer.clear();

                } catch (FileNotFoundException e) {
                    log.error("录音文件未找到[{}]==>>{}", userFile, e.getMessage());
                } catch (IOException e) {
                    log.warn("录音流传输异常==>>{}", e.getMessage());
                } finally {
                    if (Fc.notNull(pipedOut)){
                        try {
                            pipedOut.close();
                        } catch (IOException ex) {
                            log.error("关闭管道流异常==={}", ex.getMessage());
                        }
                    }
                }

                log.info("录音发送完成,写入线程结束==>>{}", userFile);
            });

            // 不播放内容就不需要监控
            if (Fc.isNotBlank(say)){
                String fileVoice = createWav(say, isSaveFile);
                // 播放文件，这条指令会阻塞，直到播放完毕或者中断，所以单独启动一个线程
                executor.execute(() -> {
                    log.info("播放内容=={}", say);
                    realtimeData(true, fileVoice+".wav", say);
                    // 播放文件
                    exec("ControlPlayback", fileVoice);

                    isVoiceEnd.set(true);
                    voiceStopTime.set(System.currentTimeMillis());
                });

                // 监控线程，是否识别到说话了，说话就打断播放
                executor.execute(() -> {
                    while (!isHangup() && !result.isDone() && FileUtil.exist(fileVoice+".wav")) {
                        try {
                            if (result.isStarted()){
                                log.info("识别到说话了，进行停止播放==>>{}", channel.getName());
                                ControlPlaybackAction action = new ControlPlaybackAction();
                                action.setActionId(IdUtil.getSnowflakeNextIdStr());
                                action.setChannel(channel.getName());
                                action.setControl("stop");
                                managerConnection.sendAction(action);
                                break;
                            }
                        } catch (IOException | TimeoutException e) {
                            log.error("停止播放异常==>>{}", ExceptionUtil.stacktraceToString(e));
                        }
                    }
                });
            } else {
                isVoiceEnd.set(true);
                voiceStopTime.set(System.currentTimeMillis());
            }

            String str = result.getResult();
            log.info("识别录音={}", str);
            realtimeData(false, userFile, str);
            return str;
        } catch (IOException e) {
            log.error("录音流连接异常==>>{}", ExceptionUtil.stacktraceToString(e));
        } catch (ExecutionException e) {
            log.error("录音流解析异常==>>{}", ExceptionUtil.stacktraceToString(e));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("线程断了，可能是电话挂断了==>>{}", e.getMessage());
        } finally {
            // 完成以后清除资源
            executor.shutdownNow();
            // 停止录音
            exec("StopMixMonitor", getVariable("monitorId"));
        }

        return "";
    }

    public void clear() {
        agiHangupEventManager.removeAgi(getUniqueId());

        String parentPath = StrUtil.concat(true, rootDir, getPhone(), "/voice/");
        FileUtil.del(parentPath);

        ttsClient.close();
        asrClient.close();
    }
}
