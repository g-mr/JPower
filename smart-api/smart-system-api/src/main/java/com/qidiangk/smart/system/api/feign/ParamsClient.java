package com.qidiangk.smart.system.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.qidiangk.smart.common.constants.AppConstant;
import top.jpower.core.util.rsp.R;

/**
 * 系统参数客户端
 *
 * @author mr.g
 */
@FeignClient(name = AppConstant.JPOWER_SYSTEM, fallback = ParamsClientFallback.class, path = "/feign/core/param")
public interface ParamsClient {

    /**
     * 查询系统参数值
     *
     * @author mr.g
     */
    @GetMapping("/queryByCode")
    R<String> queryByCode(@RequestParam("code") String code);


}
