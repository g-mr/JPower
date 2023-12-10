package top.jpower.jpower.controller;

import top.jpower.jpower.feign.DictClient;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;
import top.jpower.jpower.service.dict.CoreDictService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
