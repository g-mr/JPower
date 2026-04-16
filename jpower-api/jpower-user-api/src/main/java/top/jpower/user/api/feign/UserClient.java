package top.jpower.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.util.rsp.R;
import top.jpower.user.api.dto.CoreUserDTO;
import top.jpower.user.api.dto.ValidatePasswordDTO;

import java.util.List;

/**
 * 用户 Feign 客户端
 */
@FeignClient(
        value = AppConstant.JPOWER_USER,
        fallbackFactory = UserClientFallback.class,
        path = "feign/core/user"
)
public interface UserClient {

    @GetMapping("/queryUserByLoginId")
    R<CoreUserDTO> queryUserByLoginId(@RequestParam("loginId") String loginId, @RequestParam("tenantCode") String tenantCode);

    @GetMapping("/getRoleIdsByUserId")
    R<List<Long>> getRoleIds(@RequestParam("userId") Long userId);

    @PutMapping("/updateUserLoginInfo/{userId}")
    R<Boolean> updateLoginCount(@PathVariable("userId") Long userId);

    @GetMapping("/queryUserByCode")
    R<CoreUserDTO> queryUserByCode(@RequestParam("otherCode") String otherCode, @RequestParam("tenantCode") String tenantCode);

    @GetMapping("/get")
    R<CoreUserDTO> get(@RequestParam("id") Long id);

    @GetMapping("/queryUserByPhone")
    R<CoreUserDTO> queryUserByPhone(@RequestParam("phone") String phone, @RequestParam("tenantCode") String tenantCode);

	@GetMapping("/queryUserByEmail")
	R<CoreUserDTO> queryUserByEmail(@RequestParam("email") String email, @RequestParam("tenantCode") String tenantCode);

    @PostMapping("/saveUser")
    R<Long> saveUser(@RequestBody CoreUserDTO user);

    /**
     * 验证账号密码是否正确
     * @author mr.g
     * @param validatePasswordDto
     * @return 是否正确
     **/
    @PostMapping("/validatePassword")
    R<Boolean> validatePassword(@RequestBody ValidatePasswordDTO validatePasswordDto);

	/**
	 * 根据租户代码删除用户
	 * @param tenantCodes
	 * @return
	 */
	@PostMapping("/removeTenantAll")
	R<Boolean> removeTenantAll(@RequestBody List<String> tenantCodes);

	@PostMapping("/updatePasswordById")
    R<Boolean> updatePasswordById(@RequestParam("userId") Long userId, @RequestParam("password") String password);

}
