package top.jpower.jpower.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.jpower.dbs.entity.TbCorePost;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.dto.ValidatePasswordDto;
import top.jpower.jpower.vo.UserVo;

import java.util.List;

/**
 * @ClassName UserClient
 * @Description TODO 用户
 * @Author 郭丁志
 * @Date 2020/9/3 0003 1:00
 * @Version 1.0
 */
@FeignClient(
        value = AppConstant.JPOWER_USER,
        fallbackFactory = UserClientFallback.class,
        path = "/core/user"
)
public interface UserClient {

    @GetMapping("/queryUserByLoginId")
    ResponseData<TbCoreUser> queryUserByLoginId(@RequestParam("loginId") String loginId, @RequestParam("tenantCode") String tenantCode);

    @GetMapping("/getRoleIdsByUserId")
    ResponseData<List<Long>> getRoleIds(@RequestParam("userId") Long userId);

    @PutMapping("/updateUserLoginInfo/{userId}")
    ResponseData updateUserLoginInfo(@PathVariable("userId") Long userId);

    @GetMapping("/queryUserByCode")
    ResponseData<TbCoreUser> queryUserByCode(@RequestParam("otherCode") String otherCode, @RequestParam("tenantCode") String tenantCode);

    @GetMapping("/get")
    ResponseData<UserVo> get(@RequestParam("id") Long id);

    @GetMapping("/queryUserByPhone")
    ResponseData<TbCoreUser> queryUserByPhone(@RequestParam("phone") String phone, @RequestParam("tenantCode") String tenantCode);

    @PostMapping("/saveUser")
    ResponseData saveUser(@RequestBody TbCoreUser user);

    @GetMapping("/listByUserType")
    ResponseData<List<TbCoreUser>> listByUserType(@RequestParam("userType") Integer userType);

    /**
     * 通过ID查询岗位信息
     *
     * @author mr.g
     * @param postId
     * @return 岗位信息
     **/
    @GetMapping("/queryPostById")
    ResponseData<TbCorePost> queryPostById(@RequestParam("postId") Long postId);

    /**
     * 验证帐号密码是否正确
     * @author mr.g
     * @param validatePasswordDto
     * @return 是否正确
     **/
    @PostMapping("/validatePassword")
    boolean validatePassword(@RequestBody ValidatePasswordDto validatePasswordDto);

}
