package com.wlcb.jpower.web.feign;

import com.wlcb.jpower.feign.DictClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.service.core.dict.CoreDictService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DictController
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/9/1 0001 23:16
 * @Version 1.0
 */
@Api(hidden = true)
@RestController
@RequestMapping("/core/dict")
@AllArgsConstructor
public class DictFeignController implements DictClient {

    private CoreDictService dictService;

    @Override
    @GetMapping("dictListByType")
    public ResponseData<List<Map<String, Object>>> dictListByType(List<String> list) {
        if (!Fc.isNull(list) && list.size() > 0){
            return ReturnJsonUtil.ok("查询完成",dictService.dictListByTypes(list));
        }
        return ReturnJsonUtil.ok("查询完成");
    }
}
