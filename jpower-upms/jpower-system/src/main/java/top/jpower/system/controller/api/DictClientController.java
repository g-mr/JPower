package top.jpower.system.controller.api;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.util.rsp.R;
import top.jpower.system.api.dto.SelectDTO;
import top.jpower.system.api.feign.DictClient;
import top.jpower.system.service.dict.CoreDictService;

import java.util.List;

/**
 * 
 * @author mr.g
 */
@Hidden
@RestController
@RequestMapping("feign//core/dict")
@RequiredArgsConstructor
public class DictClientController implements DictClient {

    private final CoreDictService coreDictService;

    @Override
    @Operation(description = "通过code查询字典列表", hidden = true)
    @GetMapping("queryDictByType")
    public R<List<SelectDTO>> queryDictByType(@RequestParam String dictTypeCode) {
        return R.data(coreDictService.listByTypeCode(dictTypeCode));
    }

}
