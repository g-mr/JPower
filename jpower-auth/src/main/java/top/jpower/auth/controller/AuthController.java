package top.jpower.auth.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.mail.MailUtil;
import com.wf.captcha.SpecCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.auth.auth.TokenGranterBuilder;
import top.jpower.auth.auth.granter.RefreshTokenGranter;
import top.jpower.auth.dto.AuthInfo;
import top.jpower.auth.dto.TokenParameter;
import top.jpower.auth.utils.TokenUtil;
import top.jpower.auth.vo.CaptchaVO;
import top.jpower.common.constants.CacheNames;
import top.jpower.common.constants.ParamsConstants;
import top.jpower.common.enums.LoginLimitEnum;
import top.jpower.common.enums.UserTypeEnum;
import top.jpower.common.validated.Mobile;
import top.jpower.core.auth.dto.UserInfo;
import top.jpower.core.auth.utils.JwtUtil;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.tenant.JpowerTenantProperties;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.swagger.property.SwaggerProperties;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.*;
import top.jpower.resource.api.dto.SmsValidateDTO;
import top.jpower.resource.api.feign.SmsClient;
import top.jpower.system.api.cache.SystemCache;
import top.jpower.system.api.cache.param.ParamCache;
import top.jpower.system.api.dto.ClientDTO;
import top.jpower.system.api.dto.TenantDTO;
import top.jpower.user.api.cache.UserCache;
import top.jpower.user.api.dto.CoreUserDTO;
import top.jpower.user.api.feign.UserClient;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static top.jpower.common.constants.CacheNames.TOKEN_USER_KEY;
import static top.jpower.common.constants.ServiceCodeConstants.*;
import static top.jpower.core.dbs.tenant.TenantConstant.DEFAULT_TENANT_CODE;
import static top.jpower.core.dbs.tenant.TenantConstant.getExpireTime;
import static top.jpower.core.util.constants.JpowerConstants.HEADER_TENANT;

/**
 * 登录相关
 *
 * @author mr.g
 */
@Tag(name = "授权相关")
@Validated
@RestController
@RequestMapping("/auth")
@SecurityRequirement(name = SwaggerProperties.CLIENT)
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final RedisService redisService;
    private final JpowerTenantProperties tenantProperties;
    private final TokenGranterBuilder granterBuilder;
    private final UserClient userClient;
    private final SmsClient smsClient;

    private static final String VALIDATE_SMS_CODE = "validate";

    @Operation(summary = "用户登录",description = "Authorization（客户端识别码）：由clientCode+\":\"+clientSecret组成字符串后用base64编码后获得值，再由Basic +base64编码后的值组成客户端识别码； <br/>" +
            "&nbsp;&nbsp;&nbsp;clientCode和clientSecret的值由后端统一提供，不同的登录客户端值也不一样。<br/>" +
            "token如何使用：tokenType+\" \"+token组成的值要放到header；header头是jpower-auth；具体写法如下；<br/>" +
            "&nbsp;&nbsp;&nbsp;jpower-auth=tokenType+\" \"+token")
    @PostMapping(value = "/login",produces="application/json")
    public R<AuthInfo> login(@Valid @RequestBody TokenParameter parameter) {

        if (tenantProperties.getEnable()){
            JpowerAssert.notEmpty(parameter.getTenantCode(),JpowerError.Arg,TENANT_CODE_NOT_NULL);
            if (!Fc.equalsValue(DEFAULT_TENANT_CODE,parameter.getTenantCode())){
				TenantDTO tenant = SystemCache.getTenantByCode(parameter.getTenantCode());
                if (Fc.isNull(tenant)){
                    return R.notFind("租户不存在");
                }
                Date expireTime = getExpireTime(tenant.getLicenseKey());
                if (Fc.notNull(tenant.getExpireTime()) && Fc.notNull(expireTime) && DateUtil.compare(DateUtil.date(),expireTime) > 0){
                    return R.busFail("租户已过期");
                }
            }
        }

        parameter.setUserType(Fc.toStr(getRequest().getHeader(TokenUtil.USER_TYPE_HEADER_KEY), TokenUtil.DEFAULT_USER_TYPE));

		CoreUserDTO user = granterBuilder.getGranter(parameter.getGrantType()).grant(parameter);

        if (Fc.isNull(user) || Fc.isNull(user.getId())) {
            return R.fail(TokenUtil.USER_NOT_FOUND);
        }

        //判断单端登录
		ClientDTO client = SystemCache.getClientByClientCode(ShieldUtil.getClientCodeFromHeader());
        if (StringUtil.equalsIgnoreCase(client.getLoginLimit(), LoginLimitEnum.ONE.getValue())){
            Set<String> keys = redisService.keys(TOKEN_USER_KEY+user.getId()+ StringPool.COLON + StringPool.ASTERISK);
            keys.forEach(key->{
                Map<String,Object> map = (Map<String, Object>) redisService.valueOps().get(key);
                if (Fc.equalsValue(MapUtil.getStr(map,"client"),client.getClientCode())){
                    JpowerAssert.createException(JpowerError.RateLimit);
                }
            });
        } else if(StringUtil.equalsIgnoreCase(client.getLoginLimit(), LoginLimitEnum.SQUEEZE.getValue())){
            Set<String> keys = redisService.keys(TOKEN_USER_KEY+user.getId()+ StringPool.COLON + StringPool.ASTERISK);
            keys.forEach(key->{
                Map<String,Object> map = (Map<String, Object>) redisService.valueOps().get(key);
                if (Fc.equalsValue(MapUtil.getStr(map,"client"),client.getClientCode())){
                    String token = StringUtil.split(key,StringPool.COLON).get(4);
                    redisService.delete(CacheNames.TOKEN_URL_KEY+token);
                    redisService.delete(CacheNames.TOKEN_DATA_SCOPE_KEY+token);
                    redisService.delete(TOKEN_USER_KEY+user.getId()+ StringPool.COLON +token);
                }
            });
        }

        // 登录成功要刷新用户登录数据
        if (!Fc.equalsValue(parameter.getGrantType(), RefreshTokenGranter.GRANT_TYPE)){
            userClient.updateLoginCount(user.getId());
        }

        return R.data(TokenUtil.createAuthInfo(user));
    }

    @Operation(summary = "退出登录")
    @GetMapping(value = "/logout", produces="application/json")
    public R<String> loginOut(@Parameter(description = "用户ID",required = true) @NotNull(message = "用户ID不可为空") @RequestParam Long userId) {
        UserInfo user = ShieldUtil.getUser();
        if(Fc.notNull(user) && NumberUtil.equals(userId, user.getUserId())){
            getRequest().getSession().invalidate();
            redisService.delete(CacheNames.TOKEN_URL_KEY+ JwtUtil.getToken(getRequest()));
            redisService.delete(CacheNames.TOKEN_DATA_SCOPE_KEY+JwtUtil.getToken(getRequest()));
            redisService.delete(TOKEN_USER_KEY+userId+ StringPool.COLON +JwtUtil.getToken(getRequest()));
            String cookieToken = WebUtil.getCookieVal(JpowerConstants.AUTH_HEADER);
            if (Fc.isNotBlank(cookieToken)){
                WebUtil.removeCookie(WebUtil.getResponse(), JpowerConstants.AUTH_HEADER);
            }
            return R.ok();
        }else{
            return R.fail(NOT_LOGIN);
        }
    }

    @Operation(summary = "获取验证码")
    @GetMapping("/captcha")
    public R<CaptchaVO> captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        String verCode = specCaptcha.text().toLowerCase();
        String key = Fc.randomUUID();
        // 存入redis并设置过期时间为30分钟
        redisService.valueOps().set(CacheNames.CAPTCHA_KEY + key, verCode, 30L, TimeUnit.MINUTES);
        // 将key和base64返回给前端
        return R.data(new CaptchaVO().setKey(key).setImage(specCaptcha.toBase64()));
    }

    @Operation(summary = "发送手机验证码")
    @PostMapping(value = "/captcha/{phone}",produces="application/json")
    public R<Boolean> phoneCaptcha(@Parameter(description = "手机号", required = true) @Mobile @PathVariable("phone") String phone) {
        return R.status(smsClient.sendValidate(new SmsValidateDTO().setCode(VALIDATE_SMS_CODE).setPhone(phone)).isStatus());
    }

    @Operation(summary = "发送邮箱验证码")
    @PostMapping(value = "/sendEmailCode/{email}",produces="application/json")
    public R<String> sendEmailCode(@Parameter(description = "手机号", required = true) @PathVariable("email") String email) {

        JpowerAssert.isTrue(Validator.isEmail(email), JpowerError.Business, "邮箱 不合法");

        String code = RandomUtil.random6Num();
        String msgId = MailUtil.sendText(email,"Jpower邮件","您得验证码："+code);
        redisService.valueOps().set("email:"+email+":"+msgId, code ,5L, TimeUnit.MINUTES);
        return R.data(msgId);
    }

    @Operation(summary = "用户注册")
    @PostMapping(value = "/register")
    public R<Long> register(@Validated @RequestBody CoreUserDTO coreUser, @RequestHeader(HEADER_TENANT) String tenantCode) {

        if (!ParamCache.getBoolean(ParamsConstants.IS_REGISTER,Boolean.FALSE)){
            return R.fail(NOT_OPEN_REGISTER);
        }

        if (tenantProperties.getEnable()){
            JpowerAssert.notEmpty(tenantCode,JpowerError.Arg,TENANT_CODE_NOT_NULL);
        }
        coreUser.setUserType(UserTypeEnum.USER_TYPE_GENERAL.getValue());

		CoreUserDTO user = UserCache.getUserByLoginId(coreUser.getLoginId(),tenantCode);
        if (Fc.notNull(user)){
            return R.fail(USER_EXIST);
        }

		coreUser.setPassword(DigestUtil.pwdEncrypt(coreUser.getPassword()));
        coreUser.setRoleIds(Collections.singletonList(ParamCache.getLong(ParamsConstants.REGISTER_ROLE_ID)));
        return userClient.saveUser(coreUser);
    }

}
