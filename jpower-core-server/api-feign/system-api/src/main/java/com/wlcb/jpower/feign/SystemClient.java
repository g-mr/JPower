package com.wlcb.jpower.feign;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author mr.gmac
 */
@FeignClient(value = AppConstant.JPOWER_SYSTEM, fallback = SystemClientFallback.class, path = "/core/")
public interface SystemClient {

    /**
     * @author null
     * @Description //TODO 查询部门所有下级部门id
     * @date 0:42 2020/9/3 0003
     * @param id
     * @return com.wlcb.jpower.module.base.vo.ResponseData<java.lang.String>
     */
    @GetMapping("/org/queryChildById")
    ResponseData<List<String>> queryChildById(@RequestParam String id);

    @PostMapping("/function/putRedisAllFunctionByRoles")
    boolean putRedisAllFunctionByRoles(@RequestParam List<String> roleIds, @RequestParam Long expiresIn, @RequestParam String accessToken);

}
