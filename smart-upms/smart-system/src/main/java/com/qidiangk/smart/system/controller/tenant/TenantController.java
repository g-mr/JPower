package com.qidiangk.smart.system.controller.tenant;

import cn.hutool.core.collection.CollUtil;
import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qidiangk.smart.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.boot.argument.RequestSingleBody;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.annotation.OperateLog;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.system.dbs.entity.tenant.CoreTenant;
import com.qidiangk.smart.system.service.tenant.TenantService;
import com.qidiangk.smart.system.vo.SelectVO;
import com.qidiangk.smart.system.vo.TenantCreateVO;
import com.qidiangk.smart.system.vo.TenantInfoVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static com.qidiangk.smart.common.constants.ServiceCodeConstants.NOT_SUPER_ADMIN_MODIFY_TENANT;
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

	@Function(value = "新增租户",menus = {
			@Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_ADD",type = Menu.TYPE.BTN)
	})
	@Operation(summary = "新增租户信息")
	@PostMapping(value = "/add",produces = APPLICATION_JSON_VALUE)
	public R<Long> add(@Validated(Validation.Create.class) @RequestBody TenantCreateVO tenant){
		JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth, NOT_SUPER_ADMIN_MODIFY_TENANT);
		tenant.setId(null);
		tenant.setFunctionCode(CollUtil.removeBlank(tenant.getFunctionCode()));

		return R.data(tenantService.save(tenant));
	}

	@Function(value = "修改租户",menus = {
			@Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_UPDATE",type = Menu.TYPE.BTN)
	})
	@Operation(summary = "修改租户信息")
	@PutMapping("/update")
	public R<Boolean> update(@Validated(Validation.Update.class) @RequestBody CoreTenant tenant){
		JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth,NOT_SUPER_ADMIN_MODIFY_TENANT);
		return R.status(tenantService.updateById(tenant));
	}

	@Function(value = "删除租户",menus = {
			@Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_DELETE",type = Menu.TYPE.BTN)
	})
	@Operation(summary = "删除租户信息")
	@OperateLog(title = "删除租户",businessType = DELETE)
	@DeleteMapping("/delete")
	public R<Boolean> delete(@Parameter(description = "租户主键，多个逗号分隔") @NotBlank(message = "主键不可为空") @RequestParam String ids){
		JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth,NOT_SUPER_ADMIN_MODIFY_TENANT);
		return R.status(tenantService.removeByIds(Fc.toLongList(ids)));
	}

	@Function(value = "授权配置",menus = {
			@Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "SYSTEM_TENANT_SETTING",type = Menu.TYPE.BTN)
	})
	@Operation(summary = "租户授权配置")
	@PutMapping(value = "/setting",produces = APPLICATION_JSON_VALUE)
	public R<Boolean> setting(@Parameter(description = "租户ID 多个逗号分隔",required = true) @NotEmpty(message = "租户ID不可为空") @RequestSingleBody List<Long> ids,
							  @Parameter(description = "租户额度") @RequestSingleBody(required = false) Integer accountNumber,
							  @Parameter(description = "租户过期时间") @RequestSingleBody(required = false) Date expireTime){
		JpowerAssert.isTrue(ShieldUtil.isRoot(), JpowerError.Auth,NOT_SUPER_ADMIN_MODIFY_TENANT);
		return R.status(tenantService.setting(ids,accountNumber,expireTime));
	}

	@Function(value = "查询租户配置",menus = {
			@Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "TENANT_CONFIG",type = Menu.TYPE.INTERFACE, btnCode = "TENANT_UPDATE_CONFIG")
	})
	@Operation(summary = "查询租户配置")
	@GetMapping(value = "/config/{id}",produces = APPLICATION_JSON_VALUE)
	public R<Map<String, String>> config(@Parameter(description = "租户ID",required = true) @PathVariable("id") Long id){
		return R.data(tenantService.config(id));
	}

	@Function(value = "修改租户配置",menus = {
			@Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "TENANT_UPDATE_CONFIG",type = Menu.TYPE.BTN)
	})
	@Operation(summary = "修改租户配置")
	@PutMapping(value = "/updateConfig/{id}",produces = APPLICATION_JSON_VALUE)
	public R<Boolean> updateConfig(@Parameter(description = "租户ID",required = true) @PathVariable("id") Long id,
								   @Parameter(description = "设置内容",required = true) @RequestBody Map<String, String> config){
		return R.status(tenantService.updateConfig(id, config));
	}

	@Operation(summary = "租户下拉项列表")
	@GetMapping("/selectors")
	public R<List<SelectVO>> selectors(@Parameter(description = "租户名称") @RequestParam(required = false) String tenantName){
		return R.data(tenantService.select(tenantName));
	}

    @Operation(summary = "通过域名查询租户")
    @GetMapping("/configure")
    public R<TenantInfoVO> queryByDomain(@Parameter(description = "域名") @RequestParam(required = false) String domain){
        return R.data(tenantService.queryByDomain(domain));
    }
}
