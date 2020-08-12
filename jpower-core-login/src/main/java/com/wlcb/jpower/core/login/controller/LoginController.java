package com.wlcb.jpower.core.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.wf.captcha.SpecCaptcha;
import com.wlcb.jpower.auth.AuthInfo;
import com.wlcb.jpower.auth.granter.TokenGranter;
import com.wlcb.jpower.auth.granter.TokenGranterBuilder;
import com.wlcb.jpower.auth.utils.TokenUtil;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.service.core.user.CoreFunctionService;
import com.wlcb.jpower.module.common.service.core.user.CoreUserService;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginController
 * @Description TODO 登录相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@Api(tags = "登陆相关")
@RestController
@RequestMapping("/auth")
public class LoginController extends BaseController {

    @Resource
    private CoreUserService coreUserService;
    @Resource
    private CoreFunctionService coreFunctionService;
    @Resource
    private RedisUtils redisUtils;

    /**
     * @author 郭丁志
     * @Description //TODO 用户登陆
     * @date 0:35 2020/6/10 0010
     * @param loginId 账号
     * @param passWord 密码
     * @param grantType 授权类型 (密码、验证码、第三方平台、手机号验证码、刷新token)
     * @param refreshToken 刷新token
     * @param phone 手机号 手机号登录需要）
     * @param phoneCode 手机号验证码 （手机号登录需要）
     * @param otherCode 第三方码登录 （比如微信登录，通过code获取openid后再请求登录）
     * @header User-Type(用户类型，所有授权类型都需要传，前端可先死)，框架默认WEB
     *         Authorization(客户端识别码，由后端统一提供)
     * ········Captcha-Key：验证码key、Captcha-Code：验证码值 （验证码登录需要）
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces="application/json")
    public ResponseData login(String loginId,String passWord,String grantType,String refreshToken
            ,String phone,String phoneCode,String otherCode) {

        String userType = Fc.toStr(WebUtil.getRequest().getHeader(TokenUtil.USER_TYPE_HEADER_KEY), TokenUtil.DEFAULT_USER_TYPE);

        ChainMap tokenParameter = ChainMap.init().set("account", loginId)
                .set("password", passWord)
                .set("grantType", grantType)
                .set("refreshToken", refreshToken)
                .set("userType", userType)  //扩展参数，各自业务根据需求使用
                .set("phone", phone)
                .set("phoneCode", phoneCode)
                .set("otherCode", otherCode);

        TokenGranter granter = TokenGranterBuilder.getGranter(grantType);
        UserInfo userInfo = granter.grant(tokenParameter);

        if (userInfo == null || userInfo.getUserId() == null) {
            return ReturnJsonUtil.fail(TokenUtil.USER_NOT_FOUND);
        }

        AuthInfo authInfo = TokenUtil.createAuthInfo(userInfo);

        coreFunctionService.putRedisAllFunctionByRoles(userInfo.getRoleIds(),authInfo.getExpiresIn(),authInfo.getAccessToken());

        return ReturnJsonUtil.ok("登录成功",authInfo);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 退出登录
     * @Date 11:05 2020-07-29
     * @Param []
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/loginOut",method = RequestMethod.POST,produces="application/json")
    public ResponseData loginOut(String userId) {
        JpowerAssert.notEmpty(userId, JpowerError.Arg,"用户ID不可为空");
        UserInfo user = SecureUtil.getUser();
        if(Fc.equals(userId,user.getUserId())){
            redisUtils.remove(CacheNames.TOKEN_URL_KEY+JwtUtil.getToken(getRequest()));
            return ReturnJsonUtil.ok("退出成功");
        }else{
            return ReturnJsonUtil.fail("该用户暂未登录");
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取验证码
     * @Date 22:51 2020-07-27
     **/
    @GetMapping("/captcha")
    public ResponseData captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUIDUtil.getUUID();
        // 存入redis并设置过期时间为30分钟
        redisUtils.set(CacheNames.CAPTCHA_KEY + key, verCode, 30L, TimeUnit.MINUTES);
        // 将key和base64返回给前端
        return ReturnJsonUtil.ok("操作成功",ChainMap.init().set("key", key).set("image", specCaptcha.toBase64()));
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 发送登录验证码
     * @Date 17:10 2020-04-30
     * @Param [phone]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/phoneCaptcha",method = RequestMethod.GET,produces="application/json")
    public ResponseData loginVercode(String phone) {

        if (StringUtils.isBlank(phone) || !StrUtil.isPhone(phone)){
            return ReturnJsonUtil.fail("手机号不合法");
        }

        if (redisUtils.getExpire(CacheNames.PHONE_KEY+phone,TimeUnit.MINUTES) >= 4){
            return ReturnJsonUtil.fail("该验证码已经发送，请一分钟后重试");
        }

        TbCoreUser user = coreUserService.selectByPhone(phone);

        if (user == null){
            //用户名空则返回
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_NOTFOUND,"手机号不存在",false);
        }

        String code = RandomStringUtils.randomNumeric(6);

        JSONObject json = SmsAliyun.send(phone,"乌丽吉","code");

        if ("OK".equals(json.getString("Code"))){
            redisUtils.set(CacheNames.PHONE_KEY+phone,code,5L, TimeUnit.MINUTES);
            return ReturnJsonUtil.ok(user.getLoginId()+"的验证码发送成功");
        }else {
            return ReturnJsonUtil.fail("验证码发送失败");
        }
    }

}
