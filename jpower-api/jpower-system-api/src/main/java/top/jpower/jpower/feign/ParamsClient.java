package top.jpower.jpower.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.common.constants.AppConstant;
import top.jpower.jpower.dbs.entity.params.TbCoreParam;
import top.jpower.jpower.module.base.vo.ResponseData;

/**
 * @author mr.gmac
 */
@FeignClient(name = AppConstant.JPOWER_SYSTEM, fallback = ParamsClientFallback.class, path = "/core/param/feign")
public interface ParamsClient {

    /**
     * @author 郭丁志
     * @Description //TODO 查询系统参数值
     * @date 16:42 2020/8/30 0030
     * @param code
     * @return top.jpower.jpower.module.base.vo.ResponseData<java.lang.Boolean>
     */
    @GetMapping("/queryByCode")
    ResponseData<String> queryByCode(@RequestParam("code") String code);

    /**
     * @author 郭丁志
     * @Description //TODO 查询系统参数详情
     * @date 16:42 2020/8/30 0030
     * @param id
     * @return top.jpower.jpower.module.base.vo.ResponseData<java.lang.Boolean>
     */
    @GetMapping("/queryById")
    TbCoreParam queryById(@RequestParam("id") Long id);

}
