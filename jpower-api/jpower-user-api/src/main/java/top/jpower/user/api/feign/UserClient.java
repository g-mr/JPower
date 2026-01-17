package top.jpower.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.jpower.dbs.entity.CorePost;
import top.jpower.jpower.dbs.entity.CoreUser;
import top.jpower.user.api.dto.ValidatePasswordDTO;
import top.jpower.jpower.vo.UserVO;

import java.util.List;

/**
 * 用户 Feign 客户端
 */
@FeignClient(
        value = AppConstant.JPOWER_USER,
        fallbackFactory = UserClientFallback.class,
        path = "/core/user"
)
public interface UserClient {

    @GetMapping("/queryUserByLoginId")
    ResponseData<CoreUser> queryUserByLoginId(@RequestParam("loginId") String loginId, @RequestParam("tenantCode") String tenantCode);

    @GetMapping("/getRoleIdsByUserId")
    ResponseData<List<Long>> getRoleIds(@RequestParam("userId") Long userId);

    @PutMapping("/updateUserLoginInfo/{userId}")
    ResponseData<?> updateUserLoginInfo(@PathVariable("userId") Long userId);

    @GetMapping("/queryUserByCode")
    ResponseData<CoreUser> queryUserByCode(@RequestParam("otherCode") String otherCode, @RequestParam("tenantCode") String tenantCode);

    @GetMapping("/get")
    ResponseData<UserVO> get(@RequestParam("id") Long id);

    @GetMapping("/queryUserByPhone")
    ResponseData<CoreUser> queryUserByPhone(@RequestParam("phone") String phone, @RequestParam("tenantCode") String tenantCode);

    @PostMapping("/saveUser")
    ResponseData saveUser(@RequestBody CoreUser user);

    @GetMapping("/listByUserType")
    ResponseData<List<CoreUser>> listByUserType(@RequestParam("userType") Integer userType);

    /**
     * 通过ID查询岗位信息
     *
     * @author mr.g
     * @param postId
     * @return 岗位信息
     **/
    @GetMapping("/queryPostById")
    ResponseData<CorePost> queryPostById(@RequestParam("postId") Long postId);

    /**
     * 验证账号密码是否正确
     * @author mr.g
     * @param validatePasswordDto
     * @return 是否正确
     **/
    @PostMapping("/validatePassword")
    boolean validatePassword(@RequestBody ValidatePasswordDTO validatePasswordDto);

}
