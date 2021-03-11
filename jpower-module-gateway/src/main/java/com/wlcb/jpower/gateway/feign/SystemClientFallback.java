package com.wlcb.jpower.gateway.feign;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName SystemClient
 * @Description TODO 调用失败得时候
 * @Author 郭丁志
 * @Date 2020/8/30 0030 15:50
 * @Version 1.0
 */
@Component
@Slf4j
public class SystemClientFallback implements FallbackFactory<SystemClient> {


    @Override
    public SystemClient create(Throwable cause) {
        return new SystemClient() {
            @Override
            public ResponseData<Boolean> queryRoleByUrl(String url) {
                log.error("调用queryRoleByUrl失败，参数：url={}", url, cause);
                return ReturnJsonUtil.fail("获取数据失败");
            }
        };
    }
}
