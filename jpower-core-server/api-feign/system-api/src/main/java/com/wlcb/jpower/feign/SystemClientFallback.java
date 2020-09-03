package com.wlcb.jpower.feign;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ParamsClientFallback
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-09-01 15:31
 * @Version 1.0
 */
@Component
public class SystemClientFallback implements SystemClient {

    @Override
    public ResponseData<List<String>> queryChildById(String id) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public boolean putRedisAllFunctionByRoles(List<String> roleIds, Long expiresIn, String accessToken) {
        return false;
    }
}
