package top.jpower.jpower.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.dbs.entity.TbCoreUserRole;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.base.vo.Pg;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.controller.BaseController;
import top.jpower.jpower.module.common.page.PaginationContext;
import top.jpower.jpower.module.common.utils.CacheUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;
import top.jpower.jpower.module.common.utils.constants.ConstantsEnum;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.CoreUserRoleService;
import top.jpower.jpower.service.CoreUserService;
import top.jpower.jpower.vo.UserVo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "用户角色")
@RestController
@AllArgsConstructor
@RequestMapping("/core/user")
public class RoleUserController extends BaseController {

    private CoreUserService coreUserService;
    private CoreUserRoleService coreUserRoleService;

    @Function(value = "设置角色",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "SYSTEM_USER_UPDATEROLE",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "给用户重新设置角色")
    @PostMapping(value = "/addRole", produces = "application/json")
    public ResponseData addRole(@ApiParam(value = "用户主键 多个逗号分割", required = true) @RequestParam String userIds,
                                @ApiParam(value = "角色主键 多个逗号分割") @RequestParam(required = false) String roleIds) {

        JpowerAssert.notEmpty(userIds, JpowerError.Arg, "userIds不可为空");
        JpowerAssert.notTrue(Fc.toStrArray(userIds).length <= 0, JpowerError.Arg, "userIds不可为空");

        CacheUtil.clear(CacheNames.USER_KEY);
        return ReturnJsonUtil.status(coreUserService.updateUsersRole(userIds, roleIds));
    }

    @Function(value = "角色新增用户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_ADDUSER",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "给角色新增用户")
    @PutMapping(value = "/addRoleUser", produces = "application/json")
    public ResponseData addRoleUser(@ApiParam(value = "用户主键 多个逗号分割", required = true) @RequestParam String userIds,
                                @ApiParam(value = "角色主键") @RequestParam String roleId) {

        JpowerAssert.notEmpty(userIds, JpowerError.Arg, "userIds不可为空");
        JpowerAssert.notTrue(Fc.toStrArray(userIds).length <= 0, JpowerError.Arg, "userIds不可为空");
        JpowerAssert.notEmpty(roleId, JpowerError.Arg, "roleId不可为空");

        CacheUtil.clear(CacheNames.USER_KEY);
        return ReturnJsonUtil.status(coreUserService.addRoleUsers(roleId, new ArrayList<>(Fc.toStrList(userIds))));
    }

    @Function(value = "角色去除用户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_DELUSER",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "给角色去除用户")
    @DeleteMapping(value = "/deleteRoleUser", produces = "application/json")
    public ResponseData deleteRoleUser(@ApiParam(value = "用户主键 多个逗号分割", required = true) @RequestParam String userIds,
                                    @ApiParam(value = "角色主键") @RequestParam String roleId) {

        JpowerAssert.notEmpty(userIds, JpowerError.Arg, "userIds不可为空");
        JpowerAssert.notTrue(Fc.toStrArray(userIds).length <= 0, JpowerError.Arg, "userIds不可为空");
        JpowerAssert.notEmpty(roleId, JpowerError.Arg, "roleId不可为空");

        CacheUtil.clear(CacheNames.USER_KEY);
        return ReturnJsonUtil.status(coreUserService.deleteRoleUsers(roleId, new ArrayList<>(Fc.toStrList(userIds))));
    }

    @ApiOperation(value = "查询用户所有角色ID")
    @GetMapping(value = "/userRole", produces = "application/json")
    public ResponseData<List<String>> userRole(@ApiParam(value = "用户主键", required = true) @RequestParam String userId) {

        JpowerAssert.notEmpty(userId, JpowerError.Arg, "用户ID不可为空");

        List<String> userRoleList = coreUserRoleService.listObjs(Condition.<TbCoreUserRole>getQueryWrapper().lambda().select(TbCoreUserRole::getRoleId).eq(TbCoreUserRole::getUserId, userId), Fc::toStr);
        return ReturnJsonUtil.ok("查询成功", userRoleList);
    }

    @Function(value = "角色用户列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_USER",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "通过角色查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", defaultValue = "1", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页长度", defaultValue = "10", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "orgId_eq", value = "部门ID", paramType = "query", required = false),
            @ApiImplicitParam(name = "loginId", value = "登录名", paramType = "query", required = false),
            @ApiImplicitParam(name = "nickName", value = "昵称", paramType = "query", required = false),
            @ApiImplicitParam(name = "userName", value = "姓名", paramType = "query", required = false),
            @ApiImplicitParam(name = "idNo", value = "证件号码", paramType = "query", required = false),
            @ApiImplicitParam(name = "userType_eq", value = "用户类型 字典USER_TYPE", paramType = "query", required = false),
            @ApiImplicitParam(name = "telephone", value = "电话", paramType = "query", required = false)
    })
    @GetMapping(value = "/listByRole", produces = "application/json")
    public ResponseData<Pg<UserVo>> listByRole(@ApiParam(value = "角色主键", required = true) @RequestParam String roleId,
                                               @ApiParam(value = "是否查询相等该角色", required = false) @RequestParam(required = false, defaultValue = "Y") String isEq,
                                               @ApiIgnore @RequestParam Map<String,Object> map) {
        map.remove("roleId");
        map.remove("isEq");
        JpowerAssert.notEmpty(roleId, JpowerError.Arg, "角色ID不可为空");

        StringBuffer buffer = new StringBuffer("select user_id from tb_core_user_role ");
        buffer.append("where role_id = '").append(roleId).append("'");

        LambdaQueryWrapper<TbCoreUser> wrapper = Condition.getQueryWrapper(map,TbCoreUser.class)
                .lambda()
                .orderByDesc(TbCoreUser::getCreateTime);


        if (Fc.equalsValue(isEq, ConstantsEnum.YN.N.getValue())){
            wrapper.notInSql(TbCoreUser::getId,buffer.toString());
        } else {
            wrapper.inSql(TbCoreUser::getId,buffer.toString());
        }

        return ReturnJsonUtil.ok("查询成功", coreUserService.page(PaginationContext.getMpPage(), wrapper));
    }
}
