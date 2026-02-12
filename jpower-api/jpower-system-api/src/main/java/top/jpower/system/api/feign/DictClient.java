package top.jpower.system.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.util.rsp.R;

import java.util.List;
import java.util.Map;

/**
 * system服务字典Feign客户端
 *
 * @author mr.g
 */
@FeignClient(value = AppConstant.JPOWER_SYSTEM, fallback = DictClientFallback.class, path = "/feign/core/dict")
public interface DictClient {

    /**
     * 根据dictTypeCode查询字典
     *
     * @author mr.g
     */
    @GetMapping("/queryDictByType")
    R<List<Map<String, Object>>> queryDictByType(@RequestParam("dictTypeCode") String dictTypeCode);
}
