package com.wlcb.jpower.feign;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author mr.gmac
 */
@FeignClient(value = AppConstant.JPOWER_SYSTEM, path = "/core/dict")
public interface TestClient {

    @GetMapping("/test")
    ResponseData<String> test(@RequestParam Integer t);

}
