package com.wlcb.jpower.feign;

import com.wlcb.jpower.entity.ParamDto;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author mr.gmac
 */
@FeignClient(name = AppConstant.JPOWER_SYSTEM, fallback = ParamsClientFallback.class, path = "/core/param")
public interface ParamsClient {

    /**
     * @author 郭丁志
     * @Description //TODO 查询系统参数值
     * @date 16:42 2020/8/30 0030
     * @param code
     * @return com.wlcb.jpower.module.base.vo.ResponseData<java.lang.Boolean>
     */
    @GetMapping("/queryByCode")
    ResponseData<String> queryByCode(@RequestParam String code);

    /**
     * @author 郭丁志
     * @Description //TODO 查询系统参数
     * @date 16:42 2020/8/30 0030
     * @param id
     * @return com.wlcb.jpower.module.base.vo.ResponseData<java.lang.Boolean>
     */
    @GetMapping("/queryById")
    ResponseData<ParamDto> queryById(@RequestParam String id);

}
