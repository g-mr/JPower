package com.qidiangk.smart.aster.listener;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.qidiangk.smart.aster.dbs.dao.asterisk.EndpointsDao;
import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.event.AbstractChannelEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import top.jpower.core.asterisk.ami.listener.LinkedEventListener;
import top.jpower.core.asterisk.properties.AsteriskAmiProperties;
import top.jpower.core.dbs.tenant.TenantBroker;
import top.jpower.core.util.utils.Fc;

import java.util.Map;

@Slf4j
public class CallListener extends LinkedEventListener {

    /**
     * 端点往出打电话收到回铃声状态
     * RING
     */
    protected final static Integer CHANNEL_STATE_RING = 4;
    /**
     * 端点收到来电的状态
     * DOWN
     */
    protected final static Integer CHANNEL_STATE_CALL = 0;
    /**
     * 端点收到的接听
     * UP
     * DOWN
     */
    public final static Integer CHANNEL_STATE_ANSWER = 6;
    /**
     * 端点收到对方响铃的状态
     * RINGRING
     */
    public final static Integer CHANNEL_STATE_RINGING = 5;
    /**
     * 挂断状态为0
     * HANGUP=0
     */
    public final static Integer HANGUP_CAUSE_0 = 0;

    private final static TimedCache<String, String> TENANT_CACHE = CacheUtil.newTimedCache(1000 * 60 * 60, 1000 * 60);

    private final AsteriskAmiProperties amiProperties = SpringUtil.getBean(AsteriskAmiProperties.class);

    private final EndpointsDao endpointsDao = SpringUtil.getBean(EndpointsDao.class);

    @Override
    public void onManagerEvent(ManagerEvent event) {
        if (amiProperties.getEvent()) {
            Map<String, Object> map = BeanUtil.beanToMap(event);
            String linkedId = Fc.toStr(map.getOrDefault("linkedid", map.get("linkedId")));

            if (Fc.isNotBlank(linkedId)) {
                String tenant = TENANT_CACHE.get(linkedId);
                if (Fc.isBlank(tenant)) {

                    if (event instanceof AbstractChannelEvent abstractChannelEvent) {
                        String endpointName = StrUtil.subBetween(abstractChannelEvent.getChannel(), "/", "-");

                        EndpointsDO endpoint = endpointsDao.getById(endpointName);
                        if (endpoint != null) {
                            TENANT_CACHE.put(linkedId, endpoint.getTenantid());
                        }
                    }

                }
            }

            TenantBroker.runAs(TENANT_CACHE.get(linkedId), tenantCode -> {
                super.onManagerEvent(event);
            });
        }
    }

}
