package top.jpower.jpower.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson2.JSONObject;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.jpower.auth.TokenGranterBuilder;
import top.jpower.jpower.auth.granter.RefreshTokenGranter;
import top.jpower.jpower.cache.SystemCache;
import top.jpower.jpower.cache.UserCache;
import top.jpower.jpower.cache.param.ParamConfig;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.dbs.entity.client.TbCoreClient;
import top.jpower.jpower.dbs.entity.tenant.TbCoreTenant;
import top.jpower.jpower.dto.AuthInfo;
import top.jpower.jpower.dto.TokenParameter;
import top.jpower.jpower.feign.UserClient;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.auth.SecureConstant;
import top.jpower.jpower.module.common.auth.UserInfo;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.controller.BaseController;
import top.jpower.jpower.module.common.redis.RedisUtil;
import top.jpower.jpower.module.common.support.ChainMap;
import top.jpower.jpower.module.common.utils.*;
import top.jpower.jpower.module.common.utils.constants.ConstantsEnum;
import top.jpower.jpower.module.common.utils.constants.ParamsConstants;
import top.jpower.jpower.module.common.utils.constants.StringPool;
import top.jpower.jpower.module.tenant.JpowerTenantProperties;
import top.jpower.jpower.utils.SmsUtil;
import top.jpower.jpower.utils.TokenUtil;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static top.jpower.jpower.module.common.cache.CacheNames.TOKEN_USER_KEY;
import static top.jpower.jpower.module.common.utils.constants.TokenConstant.HEADER_TENANT;
import static top.jpower.jpower.module.tenant.TenantConstant.DEFAULT_TENANT_CODE;
import static top.jpower.jpower.module.tenant.TenantConstant.getExpireTime;

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
public class AuthController extends BaseController {

    private RedisUtil redisUtil;
    private JpowerTenantProperties tenantProperties;
    private TokenGranterBuilder granterBuilder;
    private UserClient userClient;

    @ApiOperation(value = "用户登录",notes = "Authorization（客户端识别码）：由clientCode+\":\"+clientSecret组成字符串后用base64编码后获得值，再由Basic +base64编码后的值组成客户端识别码； <br/>" +
            "&nbsp;&nbsp;&nbsp;clientCode和clientSecret的值由后端统一提供，不同的登录客户端值也不一样。<br/>" +
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
    public ResponseData<AuthInfo> login(@ApiIgnore TokenParameter parameter) {

        if (tenantProperties.getEnable()){
            JpowerAssert.notNull(parameter.getTenantCode(),JpowerError.Arg,"租户编码不可为空");
            if (!Fc.equalsValue(DEFAULT_TENANT_CODE,parameter.getTenantCode())){
                TbCoreTenant tenant = SystemCache.getTenantByCode(parameter.getTenantCode());
                if (Fc.isNull(tenant)){
                    return ReturnJsonUtil.notFind("租户不存在");
                }
                Date expireTime = getExpireTime(tenant.getLicenseKey());
                if (Fc.notNull(tenant.getExpireTime()) && Fc.notNull(expireTime) && DateUtil.compare(DateUtil.date(),expireTime) > 0){
                    return ReturnJsonUtil.busFail("租户已过期");
                }
            }
        }

        parameter.setUserType(Fc.toStr(getRequest().getHeader(TokenUtil.USER_TYPE_HEADER_KEY), TokenUtil.DEFAULT_USER_TYPE));
        parameter.setAuthorization(getRequest().getHeader(SecureConstant.BASIC_HEADER_KEY));
        parameter.setCaptchaKey(getRequest().getHeader(TokenUtil.CAPTCHA_HEADER_KEY));
        parameter.setCaptchaCode(getRequest().getHeader(TokenUtil.CAPTCHA_HEADER_CODE));

        UserInfo userInfo = granterBuilder.getGranter(parameter.getGrantType()).grant(parameter);

        if (Fc.isNull(userInfo) || Fc.isNull(userInfo.getUserId())) {
            return ReturnJsonUtil.fail(TokenUtil.USER_NOT_FOUND);
        }

        //判断单端登录
        TbCoreClient client = SystemCache.getClientByClientCode(ShieldUtil.getClientCodeFromHeader());
        if (StringUtil.equalsIgnoreCase(client.getLoginLimit(), ConstantsEnum.LOGIN_LIMIT.ONE.getValue())){
            Set<String> keys = redisUtil.pattern(TOKEN_USER_KEY+userInfo.getUserId());
            keys.forEach(key->{
                Map<String,Object> map = (Map<String, Object>) redisUtil.get(key);
                if (Fc.equalsValue(MapUtil.getStr(map,"client"),client.getClientCode())){
                    JpowerAssert.createException(JpowerError.RateLimit);
                }
            });
        } else if(StringUtil.equalsIgnoreCase(client.getLoginLimit(), ConstantsEnum.LOGIN_LIMIT.SQUEEZE.getValue())){
            Set<String> keys = redisUtil.pattern(TOKEN_USER_KEY+userInfo.getUserId());
            keys.forEach(key->{
                Map<String,Object> map = (Map<String, Object>) redisUtil.get(key);
                if (Fc.equalsValue(MapUtil.getStr(map,"client"),client.getClientCode())){
                    String token = StringUtil.split(key,StringPool.COLON).get(4);
                    redisUtil.remove(CacheNames.TOKEN_URL_KEY+token);
                    redisUtil.remove(CacheNames.TOKEN_DATA_SCOPE_KEY+token);
                    redisUtil.remove(TOKEN_USER_KEY+userInfo.getUserId()+ StringPool.COLON +token);
                }
            });
        }

        // 登录成功要刷新用户登录数据
        if (!Fc.equalsValue(parameter.getGrantType(), RefreshTokenGranter.GRANT_TYPE)){
            userClient.updateUserLoginInfo(userInfo.getUserId());
        }

        return ReturnJsonUtil.data(TokenUtil.createAuthInfo(userInfo));
    }

    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "/loginOut",method = RequestMethod.POST,produces="application/json")
    public ResponseData<String> loginOut(@ApiParam(value = "用户ID",required = true)@RequestParam Long userId) {
        JpowerAssert.notNull(userId, JpowerError.Arg,"用户ID不可为空");
        UserInfo user = ShieldUtil.getUser();
        if(Fc.notNull(user) && NumberUtil.equals(userId, user.getUserId())){
            getRequest().getSession().invalidate();
            redisUtil.remove(CacheNames.TOKEN_URL_KEY+ JwtUtil.getToken(getRequest()));
            redisUtil.remove(CacheNames.TOKEN_DATA_SCOPE_KEY+JwtUtil.getToken(getRequest()));
            redisUtil.remove(TOKEN_USER_KEY+userId+ StringPool.COLON +JwtUtil.getToken(getRequest()));
            return ReturnJsonUtil.ok("退出成功");
        }else{
            return ReturnJsonUtil.fail("该用户暂未登录");
        }
    }

    @ApiOperation(value = "获取验证码")
    @GetMapping("/captcha")
    public ResponseData<Map<String,Object>> captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        String verCode = specCaptcha.text().toLowerCase();
        String key = Fc.randomUUID();
        // 存入redis并设置过期时间为30分钟
        redisUtil.set(CacheNames.CAPTCHA_KEY + key, verCode, 30L, TimeUnit.MINUTES);
        // 将key和base64返回给前端
        return ReturnJsonUtil.ok("操作成功",ChainMap.create().put("key", key).put("image", specCaptcha.toBase64()).build());
    }

    @ApiOperation(value = "发送手机登录验证码")
    @RequestMapping(value = "/phoneCaptcha",method = RequestMethod.GET,produces="application/json")
    public ResponseData<String> loginVercode(@ApiParam(value = "租户编号",required = false) @RequestParam(required = false) String tenantCode,
                                             @ApiParam(value = "手机号",required = true) @RequestParam String phone) {
        if (tenantProperties.getEnable()){
            JpowerAssert.notNull(tenantCode,JpowerError.Arg,"租户编码不可为空");
        }

        if (StringUtils.isBlank(phone) || !Validator.isMobile(phone)){
            return ReturnJsonUtil.fail("手机号不合法");
        }

        if (redisUtil.getExpire(CacheNames.PHONE_KEY+phone+tenantCode,TimeUnit.MINUTES) >= 4){
            return ReturnJsonUtil.fail("该验证码已经发送，请一分钟后重试");
        }

        TbCoreUser user = UserCache.getUserByPhone(phone,tenantCode);

        if (Fc.isNull(user)){
            //用户空则返回
            return ReturnJsonUtil.notFind("手机号不存在");
        }

        String code = RandomStringUtils.randomNumeric(6);

        JSONObject json = SmsUtil.send(phone,code);

        if (json.getBoolean("isSuccess")){
            redisUtil.set(CacheNames.PHONE_KEY+phone+tenantCode,code,5L, TimeUnit.MINUTES);
            return ReturnJsonUtil.ok(user.getLoginId()+"的验证码发送成功");
        } else {
            return ReturnJsonUtil.fail("验证码发送失败");
        }
    }

    @ApiOperation(value = "用户注册")
    @PostMapping(value = "/register")
    public ResponseData register(TbCoreUser coreUser,@RequestHeader(HEADER_TENANT) String tenantCode) {

        if (!ParamConfig.getBoolean(ParamsConstants.IS_REGISTER,Boolean.FALSE)){
            return ReturnJsonUtil.fail("未开启注册功能");
        }

        JpowerAssert.notEmpty(coreUser.getLoginId(),JpowerError.Arg,"用户名不可为空");
        JpowerAssert.notEmpty(coreUser.getPassword(),JpowerError.Arg,"密码不可为空");
        JpowerAssert.notEmpty(coreUser.getNickName(),JpowerError.Arg,"昵称不可为空");
        if (tenantProperties.getEnable()){
            JpowerAssert.notEmpty(tenantCode,JpowerError.Arg,"租户不可为空");
        }
        coreUser.setUserType(ConstantsEnum.USER_TYPE.USER_TYPE_GENERAL.getValue());

        TbCoreUser user = UserCache.getUserByLoginId(coreUser.getLoginId(),tenantCode);
        if (Fc.notNull(user)){
            return ReturnJsonUtil.fail("该用户已注册");
        }

        user.setPassword(DigestUtil.pwdEncrypt(coreUser.getPassword()));
        return userClient.saveUser(coreUser, ParamConfig.getLong(ParamsConstants.REGISTER_ROLE_ID));
    }

}
