package top.jpower.core.asterisk.ami.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.action.PJSipShowEndpointAction;
import org.asteriskjava.manager.action.PJSipShowEndpointsAction;
import org.asteriskjava.manager.event.*;
import org.asteriskjava.manager.response.ManagerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.jpower.core.asterisk.ami.AmiEventEmitter;
import top.jpower.core.asterisk.ami.annotation.AmiListener;
import top.jpower.core.asterisk.ami.listener.EventAbstractListener;
import top.jpower.core.asterisk.ami.service.Dto.PJSIPDetailDTO;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.util.constants.ReturnConstants;
import top.jpower.core.util.utils.Fc;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 查询/操作PJSIP相关信息
 */
@Repository
@AmiListener({DeviceStateChangeEvent.class,
        EndpointList.class, EndpointListComplete.class,
        EndpointDetail.class, AuthDetail.class, AorDetail.class, TransportDetail.class, ContactStatusDetail.class , EndpointDetailComplete.class})
@RequiredArgsConstructor
@Slf4j
public class AmiPJSipService extends EventAbstractListener {

    @Autowired
    @Lazy
    private ManagerConnection managerConnection;

    private final AmiEventEmitter eventEmitter;

    /**
     * 接口发送成功标识
     **/
    private final String SUCCESS = "Success";

    @Override
    public void onManagerEvent(ManagerEvent event) {
        eventEmitter.emit(event);
    }

    /**
     * 获取PJSIP端点状态
     */
    public Mono<PJSIPDetailDTO> getEndpoint(String endpointId) {

        String actionId = IdUtil.nanoId();

        return Mono.create(sink -> {
            // 1. 创建事件收集器
            PJSIPDetailDTO pjsipDetailDTO = new PJSIPDetailDTO();

            // 2. 订阅相关事件流
            Disposable subscription = Flux.merge(
                            // 订阅 EndpointDetail 事件
                            eventEmitter.on(EndpointDetail.class,
                                            event -> actionId.equals(event.getActionId()))
                                    .doOnNext(pjsipDetailDTO::setEndpoint),

                            eventEmitter.on(AuthDetail.class,
                                            event -> actionId.equals(event.getActionId()))
                                    .doOnNext(pjsipDetailDTO::setAuth),
                            eventEmitter.on(AorDetail.class,
                                            event -> actionId.equals(event.getActionId()))
                                    .doOnNext(pjsipDetailDTO::setAor),
                            eventEmitter.on(TransportDetail.class,
                                            event -> actionId.equals(event.getActionId()))
                                    .doOnNext(pjsipDetailDTO::setTransport),
                            eventEmitter.on(ContactStatusDetail.class,
                                            event -> actionId.equals(event.getActionId()))
                                    .doOnNext(pjsipDetailDTO::setContactStatus),

                            // 订阅 EndpointListComplete 事件
                            eventEmitter.on(EndpointDetailComplete.class,
                                            event -> actionId.equals(event.getActionId()))
                                    .next() // 只取第一个完成事件
                                    .doOnNext(complete -> {
                                        // 收到完成事件，成功返回结果
                                        sink.success(pjsipDetailDTO);
                                    })
                    )
                    .timeout(Duration.ofSeconds(5)) // 设置5秒超时
                    .doOnError(error -> {
                        // 超时或发生错误
                        if (error instanceof TimeoutException) {
                            sink.error(new JpowerException(
                                    ReturnConstants.RECODE_ERROR,
                                    "查询PJSIP详情超时"
                            ));
                        } else {
                            sink.error(error);
                        }
                    })
                    .subscribe();

            // 3. 注册清理回调
            sink.onCancel(subscription);
            sink.onDispose(subscription);

            // 4. 发送查询请求
            try {
                PJSipShowEndpointAction action = new PJSipShowEndpointAction();
                action.setActionId(actionId);
                action.setEndpoint(endpointId);

                log.debug("Sending PJSIP query detail with actionId: {}", actionId);
                ManagerResponse response = managerConnection.sendAction(action);
                if (Fc.notEqualsValue(response.getResponse(), SUCCESS)){
                    log.error("端点列表查询失败===>>{}", JSON.toJSONString(response));
                    // 接口查询失败返回空列表
                    sink.success(null);
                }
            } catch (Exception e) {
                subscription.dispose();
                sink.error(new JpowerException(
                        ReturnConstants.RECODE_ERROR,
                        "发送查询单个端点请求失败: " + e.getMessage()
                ));
            }
        });
    }

    /**
     * 订阅PJSIP端点状态
     */
    public Flux<DeviceStateChangeEvent> subscribeEndpoint(String endpointId) {

        return eventEmitter.on(DeviceStateChangeEvent.class, event -> {
            return StrUtil.equalsIgnoreCase("PJSIP/"+endpointId, event.getDevice());
        });

    }

    /**
     * 查询PJSIP端点列表，使用CompletableFuture实现更优雅的异步处理
     */
    public Mono<List<EndpointList>> queryEndpointList() {
        String actionId = IdUtil.nanoId();

        return Mono.create(sink -> {
            // 1. 创建事件收集器
            List<EndpointList> collectedEvents = Collections.synchronizedList(new ArrayList<>());

            // 2. 订阅相关事件流
            Disposable subscription = Flux.merge(
                            // 订阅 EndpointList 事件
                            eventEmitter.on(EndpointList.class,
                                            event -> actionId.equals(event.getActionId()))
                                    .doOnNext(collectedEvents::add),

                            // 订阅 EndpointListComplete 事件
                            eventEmitter.on(EndpointListComplete.class,
                                            event -> actionId.equals(event.getActionId()))
                                    .next() // 只取第一个完成事件
                                    .doOnNext(complete -> {
                                        // 收到完成事件，成功返回结果
                                        sink.success(collectedEvents);
                                    })
                    )
                    .timeout(Duration.ofSeconds(5)) // 设置5秒超时
                    .doOnError(error -> {
                        // 超时或发生错误
                        if (error instanceof TimeoutException) {
                            sink.error(new JpowerException(
                                    ReturnConstants.RECODE_ERROR,
                                    "查询PJSIP超时"
                            ));
                        } else {
                            sink.error(error);
                        }
                    })
                    .subscribe();

            // 3. 注册清理回调
            sink.onCancel(subscription);
            sink.onDispose(subscription);

            // 4. 发送查询请求
            try {
                PJSipShowEndpointsAction action = new PJSipShowEndpointsAction();
                action.setActionId(actionId);

                log.debug("Sending PJSIP query with actionId: {}", actionId);
                ManagerResponse response = managerConnection.sendAction(action);
                if (Fc.notEqualsValue(response.getResponse(), SUCCESS)){
                    log.error("端点列表查询失败===>>{}", JSON.toJSONString(response));
                    // 接口查询失败返回空列表
                    sink.success(collectedEvents);
                }
            } catch (Exception e) {
                subscription.dispose();
                sink.error(new JpowerException(
                        ReturnConstants.RECODE_ERROR,
                        "发送查询请求失败: " + e.getMessage()
                ));
            }
        });
    }
}
