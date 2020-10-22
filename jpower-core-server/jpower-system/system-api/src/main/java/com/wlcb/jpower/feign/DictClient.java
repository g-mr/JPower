package com.wlcb.jpower.feign;

import com.wlcb.jpower.dbs.entity.dict.TbCoreDict;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author mr.gmac
 */
@FeignClient(value = AppConstant.JPOWER_SYSTEM, fallback = DictClientFallback.class, path = "/core/dict")
public interface DictClient {

    /**
     * @author 郭丁志
     * @Description //TODO 查询dictType
     * @date 16:42 2020/8/30 0030
     */
    @GetMapping("/queryDictByType")
    ResponseData<List<TbCoreDict>> queryDictByType(String dictTypeCode);
}
