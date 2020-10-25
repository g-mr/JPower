package com.wlcb.jpower.controller.tenant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.tenant.TenantService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;
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
    public ResponseData<Page<TbCoreTenant>> list(@ApiIgnore @RequestParam Map<String, Object> map){
        return ReturnJsonUtil.ok("查询成功",tenantService.page(PaginationContext.getMpPage(), Condition.getQueryWrapper(map,TbCoreTenant.class)));
    }

    @ApiOperation("修改租户信息")
    @PutMapping("/update")
    public ResponseData update(@RequestParam TbCoreTenant tenant){
        JpowerAssert.notEmpty(tenant.getId(), JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.status(tenantService.updateById(tenant));
    }

    @ApiOperation("删除租户信息")
    @DeleteMapping("/delete")
    public ResponseData delete(@ApiParam("租户主键，多个逗号分隔") @RequestParam String ids){
        JpowerAssert.isTrue(SecureUtil.isRoot(), JpowerError.Unknown,"只可超级管理员删除租户");
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.status(tenantService.removeByIds(Fc.toStrList(ids)));
    }

    @ApiOperation("新增租户信息")
    @PostMapping("/add")
    public ResponseData add(@RequestParam TbCoreTenant tenant,
                            @ApiParam("功能CODE 多个逗号分隔") @RequestParam List<String> functionCodes,
                            @ApiParam("字典类型CODE 多个逗号分隔") @RequestParam List<String> dictTypeCodes){
        JpowerAssert.isTrue(SecureUtil.isRoot(), JpowerError.Unknown,"只可超级管理员增加租户");
        JpowerAssert.notEmpty(tenant.getTenantName(), JpowerError.Arg,"租户名称不可为空");
        JpowerAssert.notGeZero(tenantService.count(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getDomain,tenant.getDomain()))
                ,JpowerError.BUSINESS,"该域名已存在");

        return ReturnJsonUtil.status(tenantService.save(tenant,functionCodes,dictTypeCodes));
    }

    @ApiOperation("租户授权配置")
    @PutMapping("/setting")
    public ResponseData setting(@ApiParam(value = "租户ID 多个逗号分隔",required = true) @RequestParam List<String> ids,
                                @ApiParam(value = "租户额度") @RequestParam(required = false) Integer accountNumber,
                                @ApiParam(value = "租户过期时间") @RequestParam(required = false) Date expireTime){
        JpowerAssert.isTrue(SecureUtil.isRoot(), JpowerError.Unknown,"只可超级管理员配置租户");
        return ReturnJsonUtil.status(tenantService.setting(ids,accountNumber,expireTime));
    }

    @ApiOperation("通过域名查询租户")
    @GetMapping("/queryByDomain")
    public ResponseData<ChainMap> queryByDomain(@ApiParam(value = "域名",required = true) @RequestParam String domain){
        JpowerAssert.notEmpty(domain, JpowerError.Arg,"域名不可为空");
        TbCoreTenant tenant = tenantService.getOne(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getDomain,domain));
        return ReturnJsonUtil.ok("查询成功",ChainMap.init().set("tenantCode",tenant.getTenantCode())
                .set("domain",tenant.getDomain())
                .set("backgroundUrl",tenant.getBackgroundUrl()));
    }
}
