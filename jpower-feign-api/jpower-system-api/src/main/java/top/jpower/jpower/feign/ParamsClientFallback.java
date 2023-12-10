package top.jpower.jpower.feign;

import top.jpower.jpower.dbs.entity.params.TbCoreParam;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName ParamsClientFallback
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-09-01 15:31
 * @Version 1.0
 */
@Slf4j
@Component
public class ParamsClientFallback implements ParamsClient {

    @Override
    public ResponseData<String> queryByCode(String code) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public TbCoreParam queryById(String id) {
        log.error("feign->>查询系统参数详情失败:{}",id);
        return null;
    }
}
