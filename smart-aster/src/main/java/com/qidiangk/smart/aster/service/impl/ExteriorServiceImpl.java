package com.qidiangk.smart.aster.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.JsonUtil;
import com.qidiangk.smart.aster.pojo.dto.ActionDTO;
import com.qidiangk.smart.aster.pojo.dto.NoticeCallEndDTO;
import com.qidiangk.smart.aster.pojo.dto.NoticeCallStartDTO;
import com.qidiangk.smart.aster.pojo.dto.NoticeCallStatusDTO;
import com.qidiangk.smart.aster.service.ExteriorService;
import com.qidiangk.smart.system.api.cache.param.ParamCache;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExteriorServiceImpl implements ExteriorService {

    /**
     * 通知地址
     */
    private final static String CALL_NOTICE_URL_KEY = "call.notice.start";
    /**
     * 状态通知地址
     */
    private final static String STATUS_NOTICE_URL_KEY = "call.notice.status";
    /**
     * 挂断通知地址
     */
    private final static String HUANG_NOTICE_URL_KEY = "call.notice.huang";
    /**
     * 实时对话通知
     */
    private final static String ACTION_NOTICE_URL_KEY = "call.notice.action";
    /**
     * 文件通知地址
     * <br/>
     * 配置就说明要上传，不配置说明不需要上传
     */
    private final static String FILE_NOTICE_URL_KEY = "call.notice.file";

    @Override
    @Async
    @Retryable(
            retryFor = {RuntimeException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 1000*60, multiplier = 3, maxDelay = 1000*60*60*24)
    )
    public void callReport(NoticeCallStartDTO startDTO) {
        String url = ParamCache.getString(CALL_NOTICE_URL_KEY);
        if (Fc.isBlank(url)) {
            return;
        }

        String dto = JSONUtil.toJsonStr(startDTO);
        String body = HttpUtil.post(url, dto);
        log.info("调用电话通知callReport===>>{}======>>{}======>>{}", url, dto, body);
        JSONObject json = JSONUtil.parseObj(body);
        if (json.getInt("code") != 200) {
            throw new RuntimeException("请求失败===>>>" + body);
        }
    }

    @Override
    @Async
    @Retryable(
            retryFor = {RuntimeException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 1000*60, multiplier = 3, maxDelay = 1000*60*60*24)
    )
    public void callStatus(NoticeCallStatusDTO callStatusDTO) {
        String url = ParamCache.getString(STATUS_NOTICE_URL_KEY);
        if (Fc.isBlank(url)) {
            return;
        }

        String dto = JSONUtil.toJsonStr(callStatusDTO);
        String body = HttpUtil.post(url, dto);
        log.info("调用状态通知callStatus===>>{}======>>{}======>>{}", url, dto, body);
        JSONObject json = JSONUtil.parseObj(body);
        if (json.getInt("code") != 200) {
            throw new RuntimeException("请求失败===>>>" + body);
        }
    }

    @Override
    @Async
    @Retryable(
            retryFor = {RuntimeException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 1000*60, multiplier = 3, maxDelay = 1000*60*60*24)
    )
    public void hangupReport(NoticeCallEndDTO huangDTO) {
        String url = ParamCache.getString(HUANG_NOTICE_URL_KEY);
        if (Fc.isBlank(url)) {
            return;
        }

        String dto = JSONUtil.toJsonStr(huangDTO);
        String body = HttpUtil.post(url, dto);
        log.info("调用挂断通知callStatus===>>{}======>>{}======>>{}", url, dto, body);
        JSONObject json = JSONUtil.parseObj(body);
        if (json.getInt("code") != 200) {
            throw new RuntimeException("请求失败===>>>" + body);
        }
    }

    /**
     * 测试地址：http://192.168.31.29:48088/app-api/pbx/event/sessReport
     * 乌达地址： http://192.168.200.121:48088/app-api/pbx/event/sessReport
     * @param actionDTO 对话内容
     */
    @Override
    @Async
    @Retryable(
            retryFor = {RuntimeException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 1000*60, multiplier = 3, maxDelay = 1000*60*60*24)
    )
    public void sendAction(ActionDTO actionDTO) {
        String url = ParamCache.getString(ACTION_NOTICE_URL_KEY);
        if (Fc.isBlank(url)) {
            return;
        }

        String body = HttpUtil.post(url, JsonUtil.toJson(actionDTO));
        JSONObject json = JSONUtil.parseObj(body);
        if (json.getInt("code") != 200) {
            throw new RuntimeException("请求失败===>>>" + body);
        }
    }

    @Override
    @Async
    @Retryable(
            retryFor = {RuntimeException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 1000*60, multiplier = 3, maxDelay = 1000*60*60*24)
    )
    public void uploadFile(String linkedId, String filepath) {
        String url = ParamCache.getString(FILE_NOTICE_URL_KEY);
        if (Fc.isAnyBlank(url, filepath) || !FileUtil.exist(filepath)) {
            return;
        }

        String body = HttpUtil.post(url, MapBuilder.<String, Object>create()
                        .put("callId", linkedId)
                        .put("filePath", FileUtil.file(filepath))
                        .map());
        log.info("调用文件上传uploadFile===>>{}======>>{}", url, body);
        JSONObject json = JSONUtil.parseObj(body);
        if (json.getInt("code") != 200) {
            throw new RuntimeException("请求失败===>>>" + body);
        }
    }

}
