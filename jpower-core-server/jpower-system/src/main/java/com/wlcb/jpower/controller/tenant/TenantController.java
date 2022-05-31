package com.wlcb.jpower.controller.tenant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.base.annotation.OperateLog;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.common.utils.StrUtil;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.tenant.TenantService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.wlcb.jpower.module.base.annotation.OperateLog.BusinessType.DELETE;

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
        LambdaQueryWrapper<TbCoreTenant> queryWrapper = Condition.getQueryWrapper(map,TbCoreTenant.class).lambda();
        if (!SecureUtil.isRoot()){
            queryWrapper.eq(TbCoreTenant::getTenantCode,SecureUtil.getTenantCode());
        }
        return ReturnJsonUtil.ok("查询成功",tenantService.page(PaginationContext.getMpPage(), queryWrapper));
    }

    @ApiOperation("租户下拉项列表")
    @GetMapping("/selectors")
    public ResponseData<List<Map<String,Object>>> selectors(@ApiParam("租户名称") @RequestParam(required = false) String tenantName){
        LambdaQueryWrapper<TbCoreTenant> wrapper = Condition.<TbCoreTenant>getQueryWrapper().lambda()
                .select(TbCoreTenant::getTenantName,TbCoreTenant::getTenantCode);

        if (Fc.isNotBlank(tenantName)){
            wrapper.like(TbCoreTenant::getTenantName,tenantName);
        }
        return ReturnJsonUtil.ok("查询成功",tenantService.listMaps(wrapper));
    }

    @ApiOperation("修改租户信息")
    @PutMapping("/update")
    public ResponseData update(TbCoreTenant tenant){
        JpowerAssert.isTrue(SecureUtil.isRoot(), JpowerError.Auth,"只可超级管理员修改租户");
        JpowerAssert.notEmpty(tenant.getId(), JpowerError.Arg,"主键不可为空");

        if (Fc.isNotBlank(tenant.getDomain())){
            TbCoreTenant coreTenant = tenantService.getOne(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getDomain,tenant.getDomain()));
            if (Fc.notNull(coreTenant) && !Fc.equals(coreTenant.getId(),tenant.getId())){
                return ReturnJsonUtil.fail("该域名已存在");
            }
        }

        return ReturnJsonUtil.status(tenantService.updateById(tenant));
    }

    @ApiOperation("删除租户信息")
    @OperateLog(value = "删除租户",businessType = DELETE)
    @DeleteMapping("/delete")
    public ResponseData delete(@ApiParam("租户主键，多个逗号分隔") @RequestParam String ids){
        JpowerAssert.isTrue(SecureUtil.isRoot(), JpowerError.Auth,"只可超级管理员删除租户");
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.status(tenantService.removeByIds(Fc.toStrList(ids)));
    }

    @ApiOperation("新增租户信息")
    @PostMapping(value = "/add",produces = "application/json")
    public ResponseData add(TbCoreTenant tenant,
                            @ApiParam("功能CODE 多个逗号分隔") @RequestParam(required = false) List<String> functionCodes){
        tenant.setId(null);
        JpowerAssert.isTrue(SecureUtil.isRoot(), JpowerError.Auth,"只可超级管理员增加租户");
        JpowerAssert.notEmpty(tenant.getTenantName(), JpowerError.Arg,"租户名称不可为空");
        if (Fc.isNotBlank(tenant.getContactPhone()) && !StrUtil.isPhone(tenant.getContactPhone())){
            return ReturnJsonUtil.fail("手机号不合法");
        }
        if (Fc.isNotBlank(tenant.getTenantCode())){
            JpowerAssert.geZero(tenantService.count(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getTenantCode,tenant.getTenantCode()))
                    ,JpowerError.BUSINESS,"该租户已存在");
        }

        if (Fc.isNotBlank(tenant.getDomain())){
            JpowerAssert.geZero(tenantService.count(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getDomain,tenant.getDomain()))
                    ,JpowerError.BUSINESS,"该域名已存在");
        }

        return ReturnJsonUtil.status(tenantService.save(tenant,functionCodes));
    }

    @ApiOperation("租户授权配置")
    @PutMapping(value = "/setting",produces = "application/json")
    public ResponseData setting(@ApiParam(value = "租户ID 多个逗号分隔",required = true) @RequestParam List<String> ids,
                                @ApiParam(value = "租户额度") @RequestParam(required = false) Integer accountNumber,
                                @ApiParam(value = "租户过期时间") @RequestParam(required = false) Date expireTime){
        JpowerAssert.isTrue(SecureUtil.isRoot(), JpowerError.Auth,"只可超级管理员配置租户");
        return ReturnJsonUtil.status(tenantService.setting(ids,accountNumber,expireTime));
    }

    @ApiOperation("通过域名查询租户")
    @GetMapping("/queryByDomain")
    public ResponseData<ChainMap> queryByDomain(@ApiParam(value = "域名",required = true) @RequestParam String domain){
        JpowerAssert.notEmpty(domain, JpowerError.Arg,"域名不可为空");
        TbCoreTenant tenant = tenantService.getOne(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getDomain,domain));
        ChainMap map = ChainMap.init();
        if (Fc.notNull(tenant)){
            map.set("tenantCode",tenant.getTenantCode())
                    .set("domain",tenant.getDomain())
                    .set("logo",tenant.getLogo());
        }
        return ReturnJsonUtil.ok("查询成功",map);
    }
}
