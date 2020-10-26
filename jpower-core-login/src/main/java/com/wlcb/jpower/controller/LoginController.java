package com.wlcb.jpower.controller;

import com.alibaba.fastjson.JSONObject;
import com.wf.captcha.SpecCaptcha;
import com.wlcb.jpower.auth.AuthInfo;
import com.wlcb.jpower.auth.granter.TokenGranter;
import com.wlcb.jpower.auth.granter.TokenGranterBuilder;
import com.wlcb.jpower.auth.utils.TokenUtil;
import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.cache.UserCache;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.utils.SmsAliyun;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.wlcb.jpower.module.tenant.TenantConstant.getExpireTime;

/**
 * @ClassName LoginController
 * @Description TODO 登录相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@Api(tags = "授权相关")
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class LoginController extends BaseController {

    private RedisUtil redisUtil;

    @ApiOperation(value = "用户登录",notes = "Authorization（客户端识别码）：由clientCode+\":\"+clientSecret组成字符串后用base64编码后获得值，再由Basic +base64编码后的值组成客户端识别码； <br/>" +
            "&nbsp;&nbsp;&nbsp;clientCode和clientSecret的值由后端统一提供，不同的登录客户端值也不一样。<br/>" +
            "&nbsp;&nbsp;&nbsp;<span style=\"color:red;\">生成以后调用任何接口都需提供，如没有这个识别码，即使token通过，也会提示非法客户端。</span>  <br/>" +
            "token如何使用：tokenType+\" \"+token组成的值要放到header；header头是jpower-auth；具体写法如下；<br/>" +
            "&nbsp;&nbsp;&nbsp;jpower-auth=tokenType+\" \"+token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantCode",required = true,value="租户编码",paramType = "form"),
            @ApiImplicitParam(name = "loginId",required = false,value="账号",paramType = "form"),
            @ApiImplicitParam(name = "passWord",required = false,value="密码",paramType = "form"),
            @ApiImplicitParam(name = "grantType",required = false,value="授权类型 (密码登录=password、验证码登录=captcha、第三方平台登录=otherCode、手机号验证码登录=phone、刷新token=refresh_token)",paramType = "form"),
            @ApiImplicitParam(name = "refreshToken",required = false,value="刷新token   token过期时用刷新token获取新token时必填",paramType = "form"),
            @ApiImplicitParam(name = "phone",required = false,value="手机号   grantType=phone时必填",paramType = "form"),
            @ApiImplicitParam(name = "phoneCode",required = false,value="手机号验证码   grantType=phone时必填",paramType = "form"),
            @ApiImplicitParam(name = "otherCode",required = false,value="第三方平台标识  grantType=otherCode时必填",paramType = "form"),
            @ApiImplicitParam(name = "User-Type",required = true,value="用户类型   具体值由后端提供",paramType = "header"),
            @ApiImplicitParam(name = "Authorization",required = true,value="客户端识别码",paramType = "header"),
            @ApiImplicitParam(name = "Captcha-Key",required = false,value="验证码key  grantType=captcha时必填",paramType = "header"),
            @ApiImplicitParam(name = "Captcha-Code",required = false,value="验证码值    grantType=captcha时必填",paramType = "header")
    })
    @PostMapping(value = "/login",produces="application/json")
    public ResponseData<AuthInfo> login(String tenantCode, String loginId, String passWord, String grantType, String refreshToken
            , String phone, String phoneCode, String otherCode) {

        JpowerAssert.notNull(tenantCode,JpowerError.Arg,"租户编码不可为空");

        String userType = Fc.toStr(WebUtil.getRequest().getHeader(TokenUtil.USER_TYPE_HEADER_KEY), TokenUtil.DEFAULT_USER_TYPE);

        ChainMap tokenParameter = ChainMap.init().set("tenantCode", tenantCode)
                .set("account", loginId)
                .set("password", passWord)
                .set("grantType", grantType)
                .set("refreshToken", refreshToken)
                //扩展参数，各自业务根据需求使用
                .set("userType", userType)
                .set("phone", phone)
                .set("phoneCode", phoneCode)
                .set("otherCode", otherCode);

        TbCoreTenant tenant = SystemCache.getTenantByCode(tenantCode);
        if (Fc.isNull(tenant)){
            return ReturnJsonUtil.fail("租户不存在");
        }
        Date expireTime = getExpireTime(tenant.getLicenseKey());
        if (Fc.notNull(tenant.getExpireTime()) && Fc.notNull(expireTime) && new Date().before(expireTime)){
            return ReturnJsonUtil.fail("租户已过期");
        }

        TokenGranter granter = TokenGranterBuilder.getGranter(grantType);
        UserInfo userInfo = granter.grant(tokenParameter);

        if (userInfo == null || userInfo.getUserId() == null) {
            return ReturnJsonUtil.fail(TokenUtil.USER_NOT_FOUND);
        }

        AuthInfo authInfo = TokenUtil.createAuthInfo(userInfo);

        List<Object> list = SystemCache.getUrlsByRoleIds(userInfo.getRoleIds());
        redisUtil.set(CacheNames.TOKEN_URL_KEY+authInfo.getAccessToken(),list , authInfo.getExpiresIn(), TimeUnit.SECONDS);
        return ReturnJsonUtil.ok("登录成功",authInfo);
    }

    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "/loginOut",method = RequestMethod.POST,produces="application/json")
    public ResponseData<String> loginOut(@ApiParam(value = "用户ID",required = true)@RequestParam String userId) {
        JpowerAssert.notEmpty(userId, JpowerError.Arg,"用户ID不可为空");
        UserInfo user = SecureUtil.getUser();
        if(Fc.notNull(user) && Fc.equals(userId,user.getUserId())){
            redisUtil.remove(CacheNames.TOKEN_URL_KEY+JwtUtil.getToken(getRequest()));
            return ReturnJsonUtil.ok("退出成功");
        }else{
            return ReturnJsonUtil.fail("该用户暂未登录");
        }
    }

    @ApiOperation(value = "获取验证码")
    @GetMapping("/captcha")
    public ResponseData<Map> captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUIDUtil.getUUID();
        // 存入redis并设置过期时间为30分钟
        redisUtil.set(CacheNames.CAPTCHA_KEY + key, verCode, 30L, TimeUnit.MINUTES);
        // 将key和base64返回给前端
        return ReturnJsonUtil.ok("操作成功",ChainMap.init().set("key", key).set("image", specCaptcha.toBase64()));
    }

    @ApiOperation(value = "发送手机登录验证码")
    @RequestMapping(value = "/phoneCaptcha",method = RequestMethod.GET,produces="application/json")
    public ResponseData<String> loginVercode(@ApiParam(value = "租户编号",required = true) @RequestParam String tenantCode, @ApiParam(value = "手机号",required = true) @RequestParam String phone) {

        JpowerAssert.notNull(tenantCode,JpowerError.Arg,"租户编码不可为空");
        if (StringUtils.isBlank(phone) || !StrUtil.isPhone(phone)){
            return ReturnJsonUtil.fail("手机号不合法");
        }

        if (redisUtil.getExpire(CacheNames.PHONE_KEY+phone,TimeUnit.MINUTES) >= 4){
            return ReturnJsonUtil.fail("该验证码已经发送，请一分钟后重试");
        }

        TbCoreUser user = UserCache.getUserByPhone(phone,tenantCode);

        if (Fc.isNull(user)){
            //用户空则返回
            return ReturnJsonUtil.notFind("手机号不存在");
        }

        String code = RandomStringUtils.randomNumeric(6);

        JSONObject json = SmsAliyun.send(phone,"乌丽吉",code);

        if ("OK".equals(json.getString("Code"))){
            redisUtil.set(CacheNames.PHONE_KEY+phone,code,5L, TimeUnit.MINUTES);
            return ReturnJsonUtil.ok(user.getLoginId()+"的验证码发送成功");
        }else {
            return ReturnJsonUtil.fail("验证码发送失败");
        }
    }

}
