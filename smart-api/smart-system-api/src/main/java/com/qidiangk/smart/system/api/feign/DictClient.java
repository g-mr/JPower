package com.qidiangk.smart.system.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.qidiangk.smart.common.constants.AppConstant;
import top.jpower.core.util.rsp.R;
import com.qidiangk.smart.system.api.dto.SelectDTO;

import java.util.List;

/**
 * system服务字典Feign客户端
 *
 * @author mr.g
 */
@FeignClient(value = AppConstant.JPOWER_SYSTEM, fallbackFactory = DictClientFallback.class, path = "feign/core/dict")
public interface DictClient {

    /**
     * 根据dictTypeCode查询字典
     *
     * @author mr.g
     */
    @GetMapping("/queryDictByType")
    R<List<SelectDTO>> queryDictByType(@RequestParam("dictTypeCode") String dictTypeCode);
}
