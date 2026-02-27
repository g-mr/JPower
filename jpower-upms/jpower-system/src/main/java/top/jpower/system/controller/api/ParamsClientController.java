package top.jpower.system.controller.api;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.util.rsp.R;
import top.jpower.system.api.feign.ParamsClient;
import top.jpower.system.service.params.CoreParamService;

/**
 * 
 * @author mr.g
 */
@Hidden
@RestController
@RequestMapping("/feign/core/param")
@RequiredArgsConstructor
public class ParamsClientController implements ParamsClient {

    private final CoreParamService paramService;

    @Override
    @Operation(summary = "通过Code获取参数值")
    @GetMapping(value = "/queryByCode", produces="application/json")
    public R<String> queryByCode(@RequestParam("code") String code){
        return R.data(paramService.selectByCode(code));
    }

}
