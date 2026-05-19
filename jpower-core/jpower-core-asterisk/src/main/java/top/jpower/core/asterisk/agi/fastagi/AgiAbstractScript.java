package top.jpower.core.asterisk.agi.fastagi;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;
import top.jpower.core.asterisk.agi.annotation.Agi;
import top.jpower.core.asterisk.agi.fastagi.support.AgiSupport;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.TtsClient;

/**
 * AGI抽象脚本
 *
 * @author mr.g
 */
@Slf4j
public abstract class AgiAbstractScript implements AgiScript {

    public void service(final AgiRequest request, final AgiChannel channel) {
        // 实例化AgiSupport
        Agi agi = AnnotationUtil.getAnnotation(this.getClass(), Agi.class);
        AgiSupport agiSupport = ReflectUtil.newInstance(agi.support(),
                channel, request, AsrClient.createInstance(), TtsClient.createInstance(), Thread.currentThread());

        ThreadUtil.execAsync(()->{
            while (true){
                if (agiSupport.isHangup()){
                    hangup(agiSupport);
                    // 挂断后0.5秒清除
                    ThreadUtil.sleep(500);
                    agiSupport.clear();
                    break;
                }
            }
        }, false);

        service(agiSupport);
    }

    /**
     * 电话挂断
     */
    public abstract void hangup(final AgiSupport agiSupport);

    /**
     * 实现服务
     * @param agiSupport AGI支持
     */
    public abstract void service(final AgiSupport agiSupport);

}
