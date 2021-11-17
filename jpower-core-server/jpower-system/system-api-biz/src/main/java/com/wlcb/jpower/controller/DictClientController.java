package com.wlcb.jpower.controller;

import com.wlcb.jpower.feign.DictClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.service.dict.CoreDictService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@ApiIgnore
@RestController
@RequestMapping("/core/dict")
@AllArgsConstructor
public class DictClientController implements DictClient {

    private CoreDictService coreDictService;

    @ApiOperation(value = "通过code查询字典列表",hidden = true)
    @Override
    @GetMapping("queryDictByType")
    public ResponseData<List<Map<String, Object>>> queryDictByType(@RequestParam String dictTypeCode) {
        return ReturnJsonUtil.ok("查询完成",coreDictService.listByTypeCode(dictTypeCode));
    }

}
