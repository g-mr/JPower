package com.wlcb.jpower.feign;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ParamsClientFallback
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-09-01 15:31
 * @Version 1.0
 */
@Component
public class DictClientFallback implements DictClient {

    @Override
    public ResponseData<List<Map<String,Object>>> queryDictByType(String dictTypeCode) {
        return ReturnJsonUtil.fail("查询失败");
    }
}
