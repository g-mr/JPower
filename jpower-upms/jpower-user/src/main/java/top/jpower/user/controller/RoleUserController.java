package top.jpower.user.controller;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.validated.NotBlankSpilt;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.argument.RequestSingleBody;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.user.service.CoreUserRoleService;
import top.jpower.user.service.CoreUserService;
import top.jpower.user.vo.UserVO;

import java.util.List;
import java.util.Map;

import static top.jpower.common.constants.ServiceCodeConstants.ROLE_ID_NOT_NULL;

/**
 * 用户角色关联Controller
 * 
 * @author mr.g
 */
@Tag(name = "用户角色")
@RestController
@RequiredArgsConstructor
@RequestMapping("/core/user")
@Validated
public class RoleUserController extends BaseController {

    private final CoreUserService coreUserService;
    private final CoreUserRoleService coreUserRoleService;

    @Function(value = "设置角色",menus = {
        @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "SYSTEM_USER_UPDATEROLE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "给用户设置角色")
    @PostMapping(value = "/addRole", produces = "application/json")
    public R<Boolean> addRole(@Parameter(description = "用户主键 多个逗号分割", required = true) @NotEmpty(message = "用户ID不能为空") @RequestSingleBody List<Long> userIds,
                              @Parameter(description = "角色主键 多个逗号分割") @RequestSingleBody(required = false) List<Long> roleIds) {
        return R.status(coreUserService.updateUsersRole(userIds, roleIds));
    }

    @Function(value = "角色成员",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_USER",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "通过角色查询用户列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
            @Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true),
            @Parameter(name = "roleId_eq", description = "查询角色ID", in = ParameterIn.QUERY),
            @Parameter(name = "roleId_ne", description = "不查询角色ID", in = ParameterIn.QUERY),
            @Parameter(name = "orgId_eq", description = "部门ID", in = ParameterIn.QUERY),
            @Parameter(name = "loginId", description = "登录名", in = ParameterIn.QUERY),
            @Parameter(name = "nickName", description = "昵称", in = ParameterIn.QUERY),
            @Parameter(name = "userName", description = "姓名", in = ParameterIn.QUERY),
            @Parameter(name = "idNo", description = "证件号码", in = ParameterIn.QUERY),
            @Parameter(name = "userType_eq", description = "用户类型 字典USER_TYPE", in = ParameterIn.QUERY),
            @Parameter(name = "telephone", description = "电话", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/listByRole", produces = "application/json")
    public R<Pg<UserVO>> listByRole(@Ignore @RequestParam(required = false) Map<String,Object> map) {
        JpowerAssert.isTrue(MapUtil.containsAnyKey(map, "roleId_eq", "roleId_ne"), JpowerError.Arg, ROLE_ID_NOT_NULL);
        return R.data(coreUserService.pageByRoleId(map));
    }

    @Function(value = "角色新增用户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_USER",code = "SYSTEM_ROLE_ADDUSER",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "给角色新增用户")
    @PostMapping(value = "/addRoleUser", produces = "application/json")
    public R<Boolean> addRoleUser(@Parameter(description = "用户主键 多个逗号分割", required = true) @NotBlankSpilt(message = "用户ID不能为空") @RequestSingleBody String userIds,
                                  @Parameter(description = "角色主键") @NotNull(message = "角色ID不能为空") @RequestSingleBody Long roleId) {
        return R.status(coreUserService.addRoleUsers(roleId, Fc.toLongList(userIds)));
    }

    @Function(value = "角色去除用户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_USER",code = "SYSTEM_ROLE_DELUSER",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "给角色去除用户")
    @PostMapping(value = "/deleteRoleUser", produces = "application/json")
    public R<Boolean> deleteRoleUser(@Parameter(description = "用户主键 多个逗号分割", required = true) @NotBlankSpilt(message = "用户ID不能为空") @RequestSingleBody String userIds,
                                     @Parameter(description = "角色主键") @NotNull(message = "角色ID不能为空") @RequestSingleBody Long roleId) {
        return R.status(coreUserService.deleteRoleUsers(roleId, Fc.toLongList(userIds)));
    }



















    @Operation(summary = "查询用户所有角色ID")
    @GetMapping(value = "/userRole", produces = "application/json")
    public R<List<Long>> userRole(@Parameter(description = "用户主键", required = true) @NotNull(message = "用户ID不可为空") @RequestParam Long userId) {
        return R.data(coreUserRoleService.queryRoleIds(userId));
    }

}
