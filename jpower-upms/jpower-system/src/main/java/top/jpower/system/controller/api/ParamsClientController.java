package top.jpower.system.controller.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.jpower.dbs.entity.params.TbCoreParam;
import top.jpower.system.api.feign.ParamsClient;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.system.service.params.CoreParamService;

/**
 * 
 * @author mr.g
 */
@ApiIgnore
@RestController
@RequestMapping("/core/param/feign")
@AllArgsConstructor
public class ParamsClientController implements ParamsClient {

    private CoreParamService paramService;

    @ApiOperation(value = "通过Code获取参数值")
    @Override
    @RequestMapping(value = "/queryByCode",method = RequestMethod.GET,produces="application/json")
    public ResponseData<String> queryByCode(String code){
        JpowerAssert.notEmpty(code, JpowerError.Arg,"编号值不可为空");
        return ReturnJsonUtil.ok("查询成功",paramService.selectByCode(code));
    }

    /**
     * 查询系统参数
     * 
     * @author mr.g
     * @param id 参数ID
     * @return TbCoreParam 参数详情
     */
    @ApiOperation(value = "通过Id获取参数详情")
    @Override
    @GetMapping("/queryById")
    public TbCoreParam queryById(@ApiParam("主键ID") @RequestParam Long id){
        return paramService.getById(id);
    }

}
