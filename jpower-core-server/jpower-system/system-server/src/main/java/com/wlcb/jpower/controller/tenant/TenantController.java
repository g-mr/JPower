package com.wlcb.jpower.controller.tenant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.teannt.TenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author mr.gmac
 */
@Api(tags = "租户管理")
@RestController
@AllArgsConstructor
@RequestMapping("/core/tenant")
public class TenantController extends BaseController {

    private TenantService tenantService;

    @ApiOperation("租户分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "tenantCode",value = "租户编码",paramType = "query"),
            @ApiImplicitParam(name = "tenantName",value = "租户名称",paramType = "query"),
            @ApiImplicitParam(name = "domain",value = "域名地址",paramType = "query"),
            @ApiImplicitParam(name = "contactPerson",value = "联系人",paramType = "query"),
            @ApiImplicitParam(name = "contactNumber",value = "联系电话",paramType = "query")
    })
    @GetMapping("/list")
    public ResponseData<Page> list(@ApiIgnore @RequestParam Map<String, Object> map){
        return ReturnJsonUtil.ok("查询成功",tenantService.page(PaginationContext.getMpPage(), Condition.getQueryWrapper(map,TbCoreTenant.class)));
    }

    @ApiOperation("修改租户信息")
    @PutMapping("/update")
    public ResponseData<Page> update(@RequestParam TbCoreTenant tenant){
        JpowerAssert.notEmpty(tenant.getId(), JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.status(tenantService.updateById(tenant));
    }

}
