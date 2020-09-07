package com.wlcb.jpower.controller;

import com.wlcb.jpower.dbs.entity.params.TbCoreParam;
import com.wlcb.jpower.feign.ParamsClient;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.service.params.CoreParamService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author mr.gmac
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
     * @author 郭丁志
     * @Description //TODO 查询系统参数
     * @date 16:42 2020/8/30 0030
     * @param id
     * @return com.wlcb.jpower.module.base.vo.ResponseData<java.lang.Boolean>
     */
    @ApiOperation(value = "通过Id获取参数详情")
    @Override
    @GetMapping("/queryById")
    public TbCoreParam queryById(@ApiParam("主键ID") @RequestParam String id){
        return paramService.getById(id);
    }

}
