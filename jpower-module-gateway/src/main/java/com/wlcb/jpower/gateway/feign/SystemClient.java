package com.wlcb.jpower.gateway.feign;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName SystemClient
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 15:50
 * @Version 1.0
 */
@FeignClient(value = AppConstant.JPOWER_SYSTEM, fallback = SystemClientFallback.class, path = "/core/")
public interface SystemClient {

    /**
     * @author 郭丁志
     * @Description //TODO 查询匿名用户是否拥有权限
     * @date 16:42 2020/8/30 0030
     * @param url
     * @return com.wlcb.jpower.module.base.vo.ResponseData<java.lang.Boolean>
     */
    @GetMapping("/role/queryRoleByUrl")
    ResponseData<Boolean> queryRoleByUrl(@RequestParam String url);

}
