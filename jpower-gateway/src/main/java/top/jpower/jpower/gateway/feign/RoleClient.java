package top.jpower.jpower.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.core.auth.utils.constant.ClientNameConstant;
import top.jpower.core.feign.config.DynamicFeignConfig;
import top.jpower.core.util.rsp.ResponseData;

import java.util.List;

import static top.jpower.core.feign.config.DynamicFeignConfig.SERVICE_PARAM_NAME;

/**
 * @author mr.g
 * @date 2025-6-27 0:11
 * @description
 */
@FeignClient(name = ClientNameConstant.JPOWER_SYSTEM, configuration = DynamicFeignConfig.class, path = "core")
public interface RoleClient {

    @GetMapping("/function/getUrlsByRoleIds")
    ResponseData<List<String>> getUrlsByRoleIds(@RequestParam(SERVICE_PARAM_NAME) String serviceName, @RequestParam("roleIds") Long roleIds,@RequestParam("clientCode") String clientCode);

}
