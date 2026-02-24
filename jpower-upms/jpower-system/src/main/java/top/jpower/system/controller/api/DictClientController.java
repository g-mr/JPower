package top.jpower.system.controller.api;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.system.service.dict.CoreDictService;
import top.jpower.system.api.dto.SelectDTO;
import top.jpower.system.api.feign.DictClient;

import java.util.List;

/**
 * 
 * @author mr.g
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
    public ResponseData<List<SelectDTO>> queryDictByType(@RequestParam String dictTypeCode) {
        return ReturnJsonUtil.ok("查询完成",coreDictService.listByTypeCode(dictTypeCode));
    }

}
