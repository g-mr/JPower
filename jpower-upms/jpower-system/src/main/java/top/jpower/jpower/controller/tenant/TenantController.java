package top.jpower.jpower.controller.tenant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.jpower.dbs.entity.tenant.TbCoreTenant;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.base.annotation.OperateLog;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.controller.BaseController;
import top.jpower.jpower.module.common.page.PaginationContext;
import top.jpower.jpower.module.common.utils.CacheUtil;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.tenant.TenantService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static top.jpower.jpower.module.base.annotation.OperateLog.BusinessType.DELETE;

/**
 * @author mr.gmac
 */
@Api(tags = "租户管理")
@RestController
@AllArgsConstructor
@RequestMapping("/core/tenant")
public class TenantController extends BaseController {

    private TenantService tenantService;

    @Function(value = "租户列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "TENANT_LIST", type = Menu.TYPE.INTERFACE)
    })
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
    public ResponseData<Pg<TbCoreTenant>> list(@ApiIgnore @RequestParam Map<String, Object> map){
        LambdaQueryWrapper<TbCoreTenant> queryWrapper = Condition.getQueryWrapper(map,TbCoreTenant.class).lambda();
        if (!ShieldUtil.isRoot()){
            queryWrapper.eq(TbCoreTenant::getTenantCode, ShieldUtil.getTenantCode());
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

    @Function(value = "修改租户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("修改租户信息")
    @PutMapping("/update")
    public ResponseData update(TbCoreTenant tenant){
        JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth,"只可超级管理员修改租户");
        JpowerAssert.notNull(tenant.getId(), JpowerError.Arg,"主键不可为空");

        if (Fc.isNotBlank(tenant.getDomain())){
            TbCoreTenant coreTenant = tenantService.getOne(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getDomain,tenant.getDomain()));
            if (Fc.notNull(coreTenant) && !NumberUtil.equals(coreTenant.getId(),tenant.getId())){
                return ReturnJsonUtil.fail("该域名已存在");
            }
        }

        CacheUtil.clear(CacheNames.TENANT_KEY, Boolean.FALSE);
        return ReturnJsonUtil.status(tenantService.updateById(tenant));
    }

    @Function(value = "删除租户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除租户信息")
    @OperateLog(title = "删除租户",businessType = DELETE)
    @DeleteMapping("/delete")
    public ResponseData delete(@ApiParam("租户主键，多个逗号分隔") @RequestParam String ids){
        JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth,"只可超级管理员删除租户");
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");

        CacheUtil.clear(CacheNames.TENANT_KEY, Boolean.FALSE);
        return ReturnJsonUtil.status(tenantService.removeByIds(Fc.toLongList(ids)));
    }

    @Function(value = "新增租户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_ADD",type = Menu.TYPE.BTN)
    })
    @ApiOperation("新增租户信息")
    @PostMapping(value = "/add",produces = "application/json")
    public ResponseData add(TbCoreTenant tenant,
                            @ApiParam("功能CODE 多个逗号分隔") @RequestParam(required = false) Set<String> functionCode){

        tenant.setId(null);
        JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth,"只可超级管理员增加租户");
        JpowerAssert.notEmpty(tenant.getTenantName(), JpowerError.Arg,"租户名称不可为空");
        if (Fc.isNotBlank(tenant.getContactPhone()) && !Validator.isMobile(tenant.getContactPhone())){
            return ReturnJsonUtil.fail("手机号不合法");
        }
        if (Fc.isNotBlank(tenant.getTenantCode())){
            JpowerAssert.geZero(tenantService.count(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getTenantCode,tenant.getTenantCode()))
                    ,JpowerError.Business,"该租户已存在");
        }

        if (Fc.isNotBlank(tenant.getDomain())){
            JpowerAssert.geZero(tenantService.count(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getDomain,tenant.getDomain()))
                    ,JpowerError.Business,"该域名已存在");
        }

        CacheUtil.clear(CacheNames.TENANT_KEY, Boolean.FALSE);
        return ReturnJsonUtil.status(tenantService.save(tenant,CollUtil.removeBlank(functionCode)));
    }

    @Function(value = "授权配置",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_SETTING",type = Menu.TYPE.BTN)
    })
    @ApiOperation("租户授权配置")
    @PutMapping(value = "/setting",produces = "application/json")
    public ResponseData setting(@ApiParam(value = "租户ID 多个逗号分隔",required = true) @RequestParam List<Long> ids,
                                @ApiParam(value = "租户额度") @RequestParam(required = false) Integer accountNumber,
                                @ApiParam(value = "租户过期时间") @RequestParam(required = false) Date expireTime){
        JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth,"只可超级管理员配置租户");
        CacheUtil.clear(CacheNames.TENANT_KEY, Boolean.FALSE);
        return ReturnJsonUtil.status(tenantService.setting(ids,accountNumber,expireTime));
    }

    @Function(value = "查询租户配置",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "TENANT_CONFIG",type = Menu.TYPE.INTERFACE, btnCode = "TENANT_UPDATE_CONFIG")
    })
    @ApiOperation("查询租户配置")
    @GetMapping(value = "/config",produces = "application/json")
    public ResponseData<Map<String, String>> config(@ApiParam(value = "租户ID",required = true) @RequestParam Long id){
        return ReturnJsonUtil.data(tenantService.config(id));
    }

    @Function(value = "修改租户配置",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "TENANT_UPDATE_CONFIG",type = Menu.TYPE.BTN)
    })
    @ApiOperation("修改租户配置")
    @PutMapping(value = "/updateConfig/{id}",produces = "application/json")
    public ResponseData updateConfig(@ApiParam(value = "租户ID",required = true) @PathVariable("id") Long id,
                                     @ApiParam(value = "设置内容",required = true) @RequestBody Map<String, String> config){
        return ReturnJsonUtil.status(tenantService.updateConfig(id, config));
    }

    @ApiOperation("通过域名查询租户")
    @GetMapping("/queryByDomain")
    public ResponseData<Map<String,Object>> queryByDomain(@ApiParam(value = "域名",required = true) @RequestParam String domain){
        JpowerAssert.notEmpty(domain, JpowerError.Arg,"域名不可为空");
        domain = StringUtil.removeAllSuffix(domain, "/");
        List<TbCoreTenant> tenants = tenantService.list(Condition.<TbCoreTenant>getQueryWrapper().apply("length(domain) > 0").apply("{0} like concat('%', domain)", domain));

        ChainMap<String,Object> map = ChainMap.create();
        if (Fc.isNotEmpty(tenants) && Fc.equalsValue(tenants.size(), 1)){
            TbCoreTenant tenant = tenants.get(0);

            map.put("tenantCode",tenant.getTenantCode())
                    .put("domain",tenant.getDomain())
                    .put("title",tenant.getTenantName())
                    .put("config", tenantService.config(tenant.getId()));
        }
        return ReturnJsonUtil.ok("查询成功",map.build());
    }
}
