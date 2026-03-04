package top.jpower.system.controller.function;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.constants.CacheNames;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.argument.RequestSingleBody;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.entity.function.CoreDataScope;
import top.jpower.system.service.role.CoreDataScopeService;
import top.jpower.system.service.role.CoreFunctionService;
import top.jpower.system.service.role.CoreRoleDataService;
import top.jpower.system.vo.DataFunctionVO;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static top.jpower.common.constants.ServiceCodeConstants.NOT_FOUND_DATA;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE_LONG;

/**
 * 数据权限入口
 * 
 * @author mr.g
 */
@Tag(name = "数据权限管理")
@Validated
@RestController
@RequestMapping("/core/dataScope")
@RequiredArgsConstructor
public class DataScopeController {

    private final CoreDataScopeService dataScopeService;
    private final CoreRoleDataService roleDataService;
    private final CoreFunctionService coreFunctionService;

    @Function(value = "数据权限菜单列表",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE",code = "SYSTEM_DATASCOPE_MENU",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "数据权限菜单列表")
    @Parameters({
		@Parameter(name = "parentId_eq", description = "父级节点",example = TOP_CODE,required = true, in = ParameterIn.QUERY),
		@Parameter(name = "alias", description = "别名", in = ParameterIn.QUERY),
		@Parameter(name = "code", description = "编码", in = ParameterIn.QUERY),
		@Parameter(name = "functionType_eq", description = "是否菜单 字典YN01", in = ParameterIn.QUERY),
		@Parameter(name = "functionName", description = "功能名称", in = ParameterIn.QUERY),
		@Parameter(name = "url", description = "功能URL", in = ParameterIn.QUERY),
		@Parameter(name = "menuId_eq", description = "顶级菜单ID", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/listDataByParent/{clientId}", produces = APPLICATION_JSON_VALUE)
    public R<List<DataFunctionVO>> listDataByParent(@Parameter(description = "客户端ID") @PathVariable("clientId") Long clientId,
                                                    @Ignore @RequestParam(required = false) Map<String,Object> map){
		map.putIfAbsent("parentId_eq", TOP_CODE_LONG);
		map.put("clientId_eq", clientId);
        return R.data(coreFunctionService.listDataFunction(map));
    }

    @Function(value = "复制",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE", btnCode = "SYSTEM_DATASCOPE_LISTPAGE",code = "SYSTEM_DATASCOPE_COPY",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "复制")
    @PostMapping(value = "/copy/{id}", produces = APPLICATION_JSON_VALUE)
    public R<Long> copy(@Parameter(description = "主建", required = true) @PathVariable("id") Long id){
        CoreDataScope dataScope = dataScopeService.getById(id);
        JpowerAssert.notNull(dataScope, JpowerError.NotFind, NOT_FOUND_DATA);

        dataScope.setId(null);
        return R.data(dataScopeService.create(dataScope));
    }

    @Function(value = "新增",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE", btnCode = "SYSTEM_DATASCOPE_LISTPAGE",code = "SYSTEM_DATASCOPE_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增")
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    public R<Long> add(@Validated(Validation.Create.class) @RequestBody CoreDataScope dataScope){
        return R.data(dataScopeService.create(dataScope));
    }

    @Function(value = "修改",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE", btnCode = "SYSTEM_DATASCOPE_LISTPAGE",code = "SYSTEM_DATASCOPE_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "修改")
    @PutMapping(value = "/update", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> update(@Validated(Validation.Update.class) @RequestBody CoreDataScope dataScope){
        CacheUtil.clear(CacheNames.DATASCOPE_KEY);
        return R.status(dataScopeService.updateById(dataScope));
    }

    @Function(value = "删除",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE", btnCode = "SYSTEM_DATASCOPE_LISTPAGE",code = "SYSTEM_DATASCOPE_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete/{id}", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> delete(@Parameter(description = "主键",required = true) @PathVariable("id") Long id){
        CacheUtil.clear(CacheNames.DATASCOPE_KEY);
        return R.status(dataScopeService.removeRealById(id));
    }

    @Function(value = "详情",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE", btnCode = "SYSTEM_DATASCOPE_LISTPAGE",code = "SYSTEM_DATASCOPE_DETAIL",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "详情")
    @GetMapping(value = "/queryById/{id}", produces = APPLICATION_JSON_VALUE)
    public R<CoreDataScope> queryById(@Parameter(description = "主键",required = true) @PathVariable("id") Long id){
        return R.data(dataScopeService.getById(id));
    }

    @Function(value = "列表",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE",code = "SYSTEM_DATASCOPE_LISTPAGE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "分页列表")
    @Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "scopeCode", description = "权限编号", in = ParameterIn.QUERY),
		@Parameter(name = "scopeName", description = "权限名称", in = ParameterIn.QUERY),
		@Parameter(name = "scopeType_eq", description = "权限类型 字典DATA_SCOPE_TYPE", in = ParameterIn.QUERY),
		@Parameter(name = "allRole_eq", description = "是否所有角色都执行", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/listPage/{menuId}", produces = APPLICATION_JSON_VALUE)
    public R<Pg<CoreDataScope>> listPage(@Parameter(description = "菜单ID", required = true) @PathVariable("menuId") Long menuId,
										 @Ignore @RequestParam(required = false) Map<String,Object> map){
		map.put("menuId_eq", menuId);
        return R.data(dataScopeService.pg(map));
    }

    @Function(value = "数据权限",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_DATASCOPE_LIST",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "通过菜单ID查询列表")
    @GetMapping(value = "/listByMenuId/{menuId}", produces = APPLICATION_JSON_VALUE)
    public R<List<CoreDataScope>> listByMenuId(@Parameter(description = "菜单ID",required = true) @PathVariable("menuId") Long menuId){
        return R.data(dataScopeService.listByField(CoreDataScope::getMenuId, menuId));
    }

    @Function(value = "数据权限ID",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_DATASCOPE_LIST",code = "SYSTEM_DATASCOPE_LISTID",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "通过角色ID查询所有数据权限ID")
    @GetMapping(value = "/listIdByRoleId", produces = APPLICATION_JSON_VALUE)
    public R<List<Long>> listIdByRoleId(@Parameter(description = "角色ID 多个逗号分割", required = true) @NotBlank(message = "角色ID不可为空") @RequestParam String roleIds){
        return R.data(roleDataService.listDataIdByRoleId(Fc.toLongList(roleIds)));
    }

    @Function(value = "数据赋权",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_DATASCOPE_LIST",code = "SYSTEM_DATASCOPE_ROLE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "角色赋权")
    @PostMapping(value = "/roleDataScope", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> roleDataScope(@Parameter(description = "角色主键",required = true) @NotNull(message = "角色主键不可为空") @RequestSingleBody Long roleId,
									@Parameter(description = "数据权限主键,多个逗号分割") @RequestSingleBody(required = false) List<Long> dataIds){
        return R.status(dataScopeService.roleDataScope(roleId, dataIds));
    }
}
