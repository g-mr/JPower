package top.jpower.core.asterisk.agi.fastagi.support;

import org.asteriskjava.manager.event.*;
import top.jpower.core.asterisk.ami.annotation.AmiListener;
import top.jpower.core.asterisk.ami.listener.EventAbstractListener;
import top.jpower.core.util.utils.Fc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监控AGI程序的通道是否挂断了
 *
 * HangupEvent: 完全挂断通知，AGI程序必须结束
 * HangupRequestEvent: 挂断立即通知，AGI程序还没有结束
 */
@AmiListener({HangupEvent.class, HangupRequestEvent.class, SoftHangupRequestEvent.class})
public class AgiHangupEventManager extends EventAbstractListener {

    private static final Map<String, Boolean> AGI_HANGUP_CACHE = new ConcurrentHashMap<>(30);

    /**
     * 是否挂断
     *
     * 如果缓存中有，则返回缓存中的值；如果缓存中没有，则返回true
     */
    public boolean isAgiHangup(String uniqueId){
        return Fc.toBoolean(AGI_HANGUP_CACHE.get(uniqueId), Boolean.TRUE);
    }

    public void addAgi(String uniqueId){
        AGI_HANGUP_CACHE.put(uniqueId, Boolean.FALSE);
    }

    public void removeAgi(String uniqueId) {
        AGI_HANGUP_CACHE.remove(uniqueId);
    }

    @Override
    public void onManagerEvent(ManagerEvent event) {
        AbstractChannelEvent hangupEvent = (AbstractChannelEvent) event;
        if (AGI_HANGUP_CACHE.containsKey(hangupEvent.getUniqueId())){
            AGI_HANGUP_CACHE.put(hangupEvent.getUniqueId(), Boolean.TRUE);
        }
    }

}
