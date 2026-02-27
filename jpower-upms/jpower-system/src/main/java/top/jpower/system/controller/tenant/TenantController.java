package top.jpower.system.controller.tenant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.core.exception.annotation.OperateLog;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.core.util.utils.WebUtil;
import top.jpower.system.dbs.entity.tenant.CoreTenant;
import top.jpower.system.dbs.entity.tenant.TbCoreTenant;
import top.jpower.system.service.tenant.TenantService;
import top.jpower.system.vo.SelectVO;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static top.jpower.core.exception.annotation.OperateLog.BusinessType.DELETE;

/**
 * 租户管理控制器
 * 
 * @author mr.g
 */
@Tag(name = "租户管理")
@Validated
@RestController
@RequestMapping("/core/tenant")
@RequiredArgsConstructor
public class TenantController extends BaseController {

    private final TenantService tenantService;

    @Function(value = "租户列表",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "TENANT_LIST", type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "租户分页列表")
    @Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", in = QUERY, schema = @Schema(type = "int"), required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", in = QUERY, schema = @Schema(type = "int"), required = true),
		@Parameter(name = "tenantCode", description = "租户编码", in = QUERY),
		@Parameter(name = "tenantName", description = "租户名称", in = QUERY),
		@Parameter(name = "domain", description = "域名地址", in = QUERY),
		@Parameter(name = "contactPerson", description = "联系人", in = QUERY),
		@Parameter(name = "contactNumber", description = "联系电话", in = QUERY)
    })
    @GetMapping("/list")
    public R<Pg<CoreTenant>> list(@Ignore @RequestParam Map<String, Object> map){
        return R.data(tenantService.pageByMap(map));
    }

    @Operation(summary = "租户下拉项列表")
    @GetMapping("/selectors")
    public R<List<SelectVO>> selectors(@Parameter(description = "租户名称") @RequestParam(required = false) String tenantName){
        return R.data(tenantService.select(tenantName));
    }

    @Function(value = "修改租户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "修改租户信息")
    @PutMapping("/update")
    public R update(TbCoreTenant tenant){
        JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth,"只可超级管理员修改租户");
        JpowerAssert.notNull(tenant.getId(), JpowerError.Arg,"主键不可为空");

        if (Fc.isNotBlank(tenant.getDomain())){
            TbCoreTenant coreTenant = tenantService.getOne(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getDomain,tenant.getDomain()));
            if (Fc.notNull(coreTenant) && !NumberUtil.equals(coreTenant.getId(),tenant.getId())){
                return R.fail("该域名已存在");
            }
        }

        CacheUtil.clear(CacheNames.TENANT_KEY);
        return R.status(tenantService.updateById(tenant));
    }

    @Function(value = "删除租户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除租户信息")
    @OperateLog(title = "删除租户",businessType = DELETE)
    @DeleteMapping("/delete")
    public R delete(@ApiParam("租户主键，多个逗号分隔") @RequestParam String ids){
        JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth,"只可超级管理员删除租户");
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");

        CacheUtil.clear(CacheNames.TENANT_KEY);
        return R.status(tenantService.removeByIds(Fc.toLongList(ids)));
    }

    @Function(value = "新增租户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增租户信息")
    @PostMapping(value = "/add",produces = "application/json")
    public R add(TbCoreTenant tenant,
                            @ApiParam("功能CODE 多个逗号分隔") @RequestParam(required = false) Set<String> functionCode){

        tenant.setId(null);
        JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth,"只可超级管理员增加租户");
        JpowerAssert.notEmpty(tenant.getTenantName(), JpowerError.Arg,"租户名称不可为空");
        if (Fc.isNotBlank(tenant.getContactPhone()) && !Validator.isMobile(tenant.getContactPhone())){
            return R.fail("手机号不合法");
        }
        if (Fc.isNotBlank(tenant.getTenantCode())){
            JpowerAssert.geZero(tenantService.count(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getTenantCode,tenant.getTenantCode()))
                    ,JpowerError.Business,"该租户已存在");
        }

        if (Fc.isNotBlank(tenant.getDomain())){
            JpowerAssert.geZero(tenantService.count(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getDomain,tenant.getDomain()))
                    ,JpowerError.Business,"该域名已存在");
        }

        CacheUtil.clear(CacheNames.TENANT_KEY);
        return R.status(tenantService.save(tenant,CollUtil.removeBlank(functionCode)));
    }

    @Function(value = "授权配置",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_SETTING",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "租户授权配置")
    @PutMapping(value = "/setting",produces = "application/json")
    public R setting(@ApiParam(value = "租户ID 多个逗号分隔",required = true) @RequestParam List<Long> ids,
                                @ApiParam(value = "租户额度") @RequestParam(required = false) Integer accountNumber,
                                @ApiParam(value = "租户过期时间") @RequestParam(required = false) Date expireTime){
        JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth,"只可超级管理员配置租户");
        CacheUtil.clear(CacheNames.TENANT_KEY);
        return R.status(tenantService.setting(ids,accountNumber,expireTime));
    }

    @Function(value = "查询租户配置",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "TENANT_CONFIG",type = Menu.TYPE.INTERFACE, btnCode = "TENANT_UPDATE_CONFIG")
    })
    @Operation(summary = "查询租户配置")
    @GetMapping(value = "/config",produces = "application/json")
    public R<Map<String, String>> config(@ApiParam(value = "租户ID",required = true) @RequestParam Long id){
        return R.data(tenantService.config(id));
    }

    @Function(value = "修改租户配置",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "TENANT_UPDATE_CONFIG",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "修改租户配置")
    @PutMapping(value = "/updateConfig/{id}",produces = "application/json")
    public R updateConfig(@ApiParam(value = "租户ID",required = true) @PathVariable("id") Long id,
                                     @ApiParam(value = "设置内容",required = true) @RequestBody Map<String, String> config){
        return R.status(tenantService.updateConfig(id, config));
    }

    @Operation(summary = "通过域名查询租户")
    @GetMapping("/queryByDomain")
    public R<Map<String,Object>> queryByDomain(@ApiParam(value = "域名", required = false) @RequestParam(required = false) String domain){
        if (Fc.isBlank(domain)){
            domain = WebUtil.getRequest().getServerName();
        }
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
        return R.ok("查询成功",map.build());
    }
}
