package com.wlcb.jpower.gateway.feign;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import org.springframework.stereotype.Component;

/**
 * @ClassName SystemClient
 * @Description TODO 调用失败得时候
 * @Author 郭丁志
 * @Date 2020/8/30 0030 15:50
 * @Version 1.0
 */
@Component
public class SystemClientFallback implements SystemClient {

    @Override
    public ResponseData<Boolean> queryRoleByUrl(String url) {
        return ReturnJsonUtil.fail("获取数据失败");
    }
}
