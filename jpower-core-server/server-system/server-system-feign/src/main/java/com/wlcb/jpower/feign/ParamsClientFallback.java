package com.wlcb.jpower.feign;

import com.wlcb.jpower.dbs.entity.params.TbCoreParam;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import org.springframework.stereotype.Component;

/**
 * @ClassName ParamsClientFallback
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-09-01 15:31
 * @Version 1.0
 */
@Component
public class ParamsClientFallback implements ParamsClient {

    @Override
    public ResponseData<String> queryByCode(String code) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<TbCoreParam> queryById(String id) {
        return ReturnJsonUtil.fail("查询失败");
    }
}
