package top.jpower.user.controller.api;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.rsp.R;
import top.jpower.jpower.dbs.entity.TbCorePost;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.dto.ValidatePasswordDto;
import top.jpower.jpower.vo.UserVo;
import top.jpower.user.api.feign.UserClient;
import top.jpower.user.service.CorePostService;
import top.jpower.user.service.CoreUserRoleService;
import top.jpower.user.service.CoreUserService;

import java.util.List;

/**
 * @ClassName UserClient
 * @Description TODO 用户feign接口实现
 * @Author 郭丁志
 * @Date 2020/9/3 0003 1:00
 * @Version 1.0
 */
@Ignore
@RestController
@RequestMapping("/core/user")
@AllArgsConstructor
public class UserClientController implements UserClient {

    private CorePostService corePostService;
    private CoreUserService coreUserService;
    private CoreUserRoleService coreUserRoleService;

    @ApiOperation(value = "通过账号查询用户")
    @Override
    @GetMapping("/queryUserByLoginId")
    public R<TbCoreUser> queryUserByLoginId(@RequestParam String loginId, @RequestParam String tenantCode){
        return R.ok("查询成功",coreUserService.selectUserLoginId(loginId,tenantCode));
    }

    @ApiOperation(value = "通过用户ID查询所有角色ID")
    @Override
    @GetMapping("/getRoleIdsByUserId")
    public R<List<Long>> getRoleIds(@RequestParam Long userId){
        return R.ok("查询成功",coreUserRoleService.queryRoleIds(userId));
    }

    @ApiOperation(value = "更新用户登陆信息")
    @Override
    @PutMapping("/updateUserLoginInfo/{userId}")
    public R updateUserLoginInfo(@PathVariable("userId") Long userId){
        return R.status(coreUserService.updateLoginInfo(userId));
    }

    @ApiOperation(value = "通过第三方CODE查询")
    @Override
    @GetMapping("/queryUserByCode")
    public R<TbCoreUser> queryUserByCode(@RequestParam String otherCode, @RequestParam String tenantCode){
        return R.ok("查询成功",coreUserService.selectUserByOtherCode(otherCode,tenantCode));
    }

    @ApiOperation("查询用户详情")
    @Override
    @GetMapping(value = "/get")
    public R<UserVo> get(@RequestParam Long id){
        return R.ok("查询成功", coreUserService.selectUserById(id));
    }

    @ApiOperation(value = "通过手机号查询用户")
    @Override
    @GetMapping("/queryUserByPhone")
    public R<TbCoreUser> queryUserByPhone(@RequestParam String phone,@RequestParam String tenantCode){
        TbCoreUser user = coreUserService.selectByPhone(phone,tenantCode);
        return R.ok("查询成功",user);
    }

    @Override
    @PostMapping("/saveUser")
    public R saveUser(@RequestBody TbCoreUser user) {
        return coreUserService.saveUser(user)?R.ok("用户创建成功"):R.fail("用户创建失败");
    }


    @Override
    @GetMapping(value = "/listByUserType")
    public R<List<TbCoreUser>> listByUserType(@ApiParam(value = "用户类型", required = true) @RequestParam Integer userType) {
        List<TbCoreUser> list = coreUserService.list(Condition.<TbCoreUser>getQueryWrapper().lambda().eq(TbCoreUser::getUserType, userType));
        return R.ok("获取成功", list);
    }

    @Override
    @GetMapping("/queryPostById")
    public R<TbCorePost> queryPostById(@RequestParam Long postId) {
        return R.data(corePostService.getById(postId));
    }

    @Override
    @PostMapping("/validatePassword")
    public boolean validatePassword(@RequestBody ValidatePasswordDto validatePasswordDto) {
        return coreUserService.validatePassword(validatePasswordDto.getAccount(), validatePasswordDto.getPassword(), validatePasswordDto.getTenantCode());
    }
}
