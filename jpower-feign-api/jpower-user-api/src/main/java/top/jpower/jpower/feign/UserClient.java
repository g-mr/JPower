package top.jpower.jpower.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.jpower.jpower.dbs.entity.TbCorePost;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.utils.constants.AppConstant;
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
    ResponseData<TbCoreUser> queryUserByLoginId(@RequestParam String loginId, @RequestParam String tenantCode);

    @GetMapping("/getRoleIdsByUserId")
    ResponseData<List<Long>> getRoleIds(@RequestParam Long userId);

    @PutMapping("/updateUserLoginInfo")
    ResponseData updateUserLoginInfo(@RequestParam Long userId);

    @GetMapping("/queryUserByCode")
    ResponseData<TbCoreUser> queryUserByCode(@RequestParam String otherCode, @RequestParam String tenantCode);

    @GetMapping("/get")
    ResponseData<UserVo> get(@RequestParam Long id);

    @GetMapping("/queryUserByPhone")
    ResponseData<TbCoreUser> queryUserByPhone(@RequestParam String phone, @RequestParam String tenantCode);

    @PostMapping("/saveUser")
    ResponseData saveUser(@RequestBody TbCoreUser user, @RequestParam Long roleId);

    @GetMapping("/listByUserType")
    ResponseData<List<TbCoreUser>> listByUserType(@RequestParam Integer userType);

    /**
     * 通过ID查询岗位信息
     *
     * @author mr.g
     * @param postId
     * @return 岗位信息
     **/
    @GetMapping("/queryPostById")
    ResponseData<TbCorePost> queryPostById(@RequestParam Long postId);

    /**
     * 验证帐号密码是否正确
     * @author mr.g
     * @param account
     * @param password
     * @param tenantCode
     * @return
     **/
    @PostMapping("/validatePassword")
    boolean validatePassword(@RequestParam String account,@RequestParam  String password,@RequestParam String tenantCode);

}
