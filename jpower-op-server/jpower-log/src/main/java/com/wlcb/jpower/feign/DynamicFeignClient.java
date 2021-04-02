package com.wlcb.jpower.feign;

import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import feign.QueryMap;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.util.Map;

/**
 * @author mr.g
 * @date 2021-04-02 16:26
 */
@FeignClient(value = "dynamic")
public interface DynamicFeignClient {

    @GetMapping("/queryUserByLoginId")
    Object get(@QueryMap Map<String, Object> queryMap);

}
