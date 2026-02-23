package top.jpower.user.controller.api;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.user.api.dto.CoreUserDTO;
import top.jpower.user.api.dto.ValidatePasswordDTO;
import top.jpower.user.api.feign.UserClient;
import top.jpower.user.dbs.entity.CoreUser;
import top.jpower.user.service.CoreUserRoleService;
import top.jpower.user.service.CoreUserService;

import java.util.List;

/**
 * 用户feign接口实现
 *
 * @author mr.g
 **/
@Ignore
@RestController
@RequestMapping("/feign/core/user")
@AllArgsConstructor
public class UserClientController implements UserClient {

    private CoreUserService coreUserService;
    private CoreUserRoleService coreUserRoleService;

    @Override
    @Operation(summary = "通过账号查询用户")
    @GetMapping("/queryUserByLoginId")
    public R<CoreUserDTO> queryUserByLoginId(@RequestParam String loginId, @RequestParam String tenantCode) {
        CoreUser user = coreUserService.selectUserLoginId(loginId,tenantCode);
        return R.data(BeanUtil.copyProperties(user, CoreUserDTO.class));
    }

    @Override
    @Operation(summary = "通过用户ID查询所有角色ID")
    @GetMapping("/getRoleIdsByUserId")
    public R<List<Long>> getRoleIds(@RequestParam Long userId){
        return R.data(coreUserRoleService.queryRoleIds(userId));
    }

    @Override
    @Operation(summary = "更新用户登陆信息")
    @PutMapping("/updateUserLoginInfo/{userId}")
    public R<Boolean> updateLoginCount(@PathVariable("userId") Long userId){
        return R.status(coreUserService.updateLoginCount(userId));
    }

    @Override
    @Operation(summary = "通过第三方CODE查询")
    @GetMapping("/queryUserByCode")
    public R<CoreUserDTO> queryUserByCode(@RequestParam String otherCode, @RequestParam String tenantCode){
        CoreUser coreUser = coreUserService.selectUserByOtherCode(otherCode,tenantCode);
        return R.data(BeanUtil.copyProperties(coreUser, CoreUserDTO.class));
    }

    @Override
    @Operation(summary = "查询用户详情")
    @GetMapping(value = "/get")
    public R<CoreUserDTO> get(@RequestParam Long id){
        CoreUser user = coreUserService.selectUserById(id);
        return R.data(BeanUtil.copyProperties(user, CoreUserDTO.class));
    }

    @Override
    @Operation(summary = "通过手机号查询用户")
    @GetMapping("/queryUserByPhone")
    public R<CoreUserDTO> queryUserByPhone(@RequestParam String phone, @RequestParam String tenantCode){
        CoreUser user = coreUserService.selectByPhone(phone,tenantCode);
        return R.data(BeanUtil.copyProperties(user, CoreUserDTO.class));
    }

    @Override
    @Operation(summary = "保存用户")
    @PostMapping("/saveUser")
    public R<String> saveUser(@RequestBody CoreUserDTO user) {
        return R.status(coreUserService.saveUser(BeanUtil.copyProperties(user, CoreUser.class), user.getRoleIds()));
    }

    @Override
    @Operation(summary = "校验密码")
    @PostMapping("/validatePassword")
    public R<Boolean> validatePassword(@RequestBody ValidatePasswordDTO validatePasswordDto) {
        return R.data(coreUserService.validatePassword(validatePasswordDto.getAccount(), validatePasswordDto.getPassword(), validatePasswordDto.getTenantCode()));
    }
}
