package top.jpower.jpower.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.jpower.cache.SystemCache;
import top.jpower.jpower.cache.param.ParamConfig;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.dbs.entity.tenant.TbCoreTenant;
import top.jpower.jpower.dto.ValidateDto;
import top.jpower.jpower.feign.SmsClient;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.base.annotation.OperateLog;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.BusinessException;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.base.vo.Pg;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.auth.UserInfo;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.controller.BaseController;
import top.jpower.jpower.module.common.redis.RedisUtil;
import top.jpower.jpower.module.common.support.BeanExcelUtil;
import top.jpower.jpower.module.common.support.EnvBeanUtil;
import top.jpower.jpower.module.common.utils.*;
import top.jpower.jpower.module.common.utils.constants.*;
import top.jpower.jpower.module.configurer.argument.RequestSingleBody;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.CoreUserService;
import top.jpower.jpower.vo.UserVo;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static top.jpower.jpower.module.base.annotation.OperateLog.BusinessType.DELETE;
import static top.jpower.jpower.module.base.annotation.OperateLog.BusinessType.UPDATE;
import static top.jpower.jpower.module.common.cache.CacheNames.TOKEN_USER_KEY;
import static top.jpower.jpower.module.common.utils.constants.JpowerConstants.VALIDATE_SMS_CODE;
import static top.jpower.jpower.module.tenant.TenantConstant.*;

@Api(tags = "用户管理")
@RestController
@AllArgsConstructor
@RequestMapping("/core/user")
public class UserController extends BaseController {

    private CoreUserService coreUserService;
    private RedisUtil redisUtil;
    private SmsClient smsClient;

    @ApiOperation("查询当前登录用户信息")
    @GetMapping(value = "/getLoginInfo", produces = "application/json")
    public ResponseData<UserVo> getLoginInfo() {
        Long id = ShieldUtil.getUserId();
        JpowerAssert.notNull(id,JpowerError.Arg,"用户未登录");
        return ReturnJsonUtil.ok("获取成功", coreUserService.getById(id));
    }

    @Function(value = "用户在线信息",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER", btnCode = "USER_OFFLINE",code = "USER_ONLINE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("查询用户在线信息")
    @GetMapping(value = "/online", produces = "application/json")
    public ResponseData<List<Map<String,String>>> online(Long userId) {
        JpowerAssert.notNull(userId,JpowerError.Arg,"用户ID不可为空");

        Set<String> keys = redisUtil.pattern(TOKEN_USER_KEY+userId+StringPool.COLON);
        List<Map<String,Object>> list = new ArrayList<>();
        keys.forEach(key -> {
            Map<String,Object> map = (Map<String, Object>) redisUtil.get(key);
            map.put("token",StringUtil.split(key,StringPool.COLON).get(4));
            map.put("userId",userId);
            list.add(map);
        });

        return ReturnJsonUtil.ok("获取成功", list);
    }

    @Function(value = "踢下线",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "USER_OFFLINE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("踢下线")
    @PostMapping(value = "/offline", produces = "application/json")
    public ResponseData offline(Long userId,String token) {
        JpowerAssert.notNull(userId,JpowerError.Arg,"用户ID不可为空");
        JpowerAssert.notEmpty(token,JpowerError.Arg,"TOKEN不可为空");

        redisUtil.remove(CacheNames.TOKEN_URL_KEY+token);
        redisUtil.remove(CacheNames.TOKEN_DATA_SCOPE_KEY+token);
        redisUtil.remove(TOKEN_USER_KEY + userId + StringPool.COLON + token);

        return ReturnJsonUtil.ok("操作成功");
    }

    @Function(value = "用户列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "USER_LIST",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("查询用户分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", defaultValue = "1", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页长度", defaultValue = "10", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "orgId", value = "部门ID", paramType = "query", required = false),
            @ApiImplicitParam(name = "postId", value = "岗位ID", paramType = "query", required = false),
            @ApiImplicitParam(name = "loginId", value = "登录名", paramType = "query", required = false),
            @ApiImplicitParam(name = "nickName", value = "昵称", paramType = "query", required = false),
            @ApiImplicitParam(name = "userName", value = "姓名", paramType = "query", required = false),
            @ApiImplicitParam(name = "idNo", value = "证件号码", paramType = "query", required = false),
            @ApiImplicitParam(name = "userType", value = "用户类型 字典USER_TYPE", paramType = "query", required = false),
            @ApiImplicitParam(name = "telephone", value = "电话", paramType = "query", required = false)
    })
    @GetMapping(value = "/list", produces = "application/json")
    public ResponseData<Pg<UserVo>> list(@ApiIgnore TbCoreUser coreUser) {
        PageInfo<UserVo> list = coreUserService.listPage(coreUser);
        return ReturnJsonUtil.ok("获取成功", list);
    }

    @Function(value = "导出用户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "SYSTEM_USER_EXPORTUSER",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "导出用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgId", value = "部门ID", paramType = "query", required = false),
            @ApiImplicitParam(name = "loginId", value = "登录名", paramType = "query", required = false),
            @ApiImplicitParam(name = "nickName", value = "昵称", paramType = "query", required = false),
            @ApiImplicitParam(name = "userName", value = "姓名", paramType = "query", required = false),
            @ApiImplicitParam(name = "idNo", value = "证件号码", paramType = "query", required = false),
            @ApiImplicitParam(name = "userType", value = "用户类型 字典USER_TYPE", paramType = "query", required = false),
            @ApiImplicitParam(name = "telephone", value = "电话", paramType = "query", required = false)
    })
    @GetMapping(value = "/exportUser")
    public void exportUser(@ApiIgnore TbCoreUser coreUser) {
        List<UserVo> list = coreUserService.list(coreUser);

        BeanExcelUtil<UserVo> beanExcelUtil = new BeanExcelUtil<>(UserVo.class, ImportExportConstants.EXPORT_PATH);
        ResponseData<String> responseData = beanExcelUtil.exportExcel(list, "用户列表");
        download(responseData,"用户数据.xlsx");
    }

    @Function(value = "用户详情",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "USER_DETAIL",type = Menu.TYPE.BTN)
    })
    @ApiOperation("查询用户详情")
    @RequestMapping(value = "/getById", method = RequestMethod.GET, produces = "application/json")
    public ResponseData<UserVo> getById(@ApiParam(value = "主键", required = true) @RequestParam @NotBlank(message = "主键不可为空") Long id) {
        JpowerAssert.notNull(id, JpowerError.Arg, "id不可为空");

        UserVo user = coreUserService.selectUserById(id);
        return ReturnJsonUtil.ok("查询成功", user);
    }

    @Function(value = "新增用户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "SYSTEM_USER_ADD",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "新增", notes = "主键不用传")
    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseData add(TbCoreUser coreUser) {

        JpowerAssert.notEmpty(coreUser.getLoginId(), JpowerError.Arg, "用户名不可为空");

        if (coreUser.getIdType() != null && ConstantsEnum.ID_TYPE.ID_CARD.getValue().equals(coreUser.getIdType())) {
            if (Fc.isNotBlank(coreUser.getIdNo()) && !Validator.isCitizenId(coreUser.getIdNo())) {
                return ReturnJsonUtil.busFail("身份证不合法");
            }
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone()) && !Validator.isMobile(coreUser.getTelephone())) {
            return ReturnJsonUtil.print(ConstantsReturn.RECODE_BUSINESS, "手机号不合法", false);
        }
        if (StringUtils.isNotBlank(coreUser.getEmail()) && !Validator.isEmail(coreUser.getEmail())) {
            return ReturnJsonUtil.busFail("邮箱不合法");
        }

        String tenantCode = Fc.toStr(coreUser.getTenantCode(), ShieldUtil.getTenantCode());
        if (EnvBeanUtil.getTenantEnable()){
            if (ShieldUtil.isRoot()) {
                tenantCode = Fc.isBlank(coreUser.getTenantCode()) ? DEFAULT_TENANT_CODE : coreUser.getTenantCode();
            }
            TbCoreTenant tenant = SystemCache.getTenantByCode(tenantCode);
            if (Fc.isNull(tenant)) {
                return ReturnJsonUtil.fail("租户不存在");
            }
            long accountNumber = getAccountNumber(tenant.getLicenseKey());
            if (!Fc.equalsValue(accountNumber, TENANT_ACCOUNT_NUMBER)) {
                long count = coreUserService.count(Condition.<TbCoreUser>getQueryWrapper().lambda().eq(TbCoreUser::getTenantCode, tenantCode));
                if (count >= accountNumber) {
                    return ReturnJsonUtil.busFail("账号额度已不足");
                }
            }
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone())) {
            JpowerAssert.isNull(coreUserService.selectByPhone(coreUser.getTelephone(), tenantCode), JpowerError.Business, "手机号已存在");
        }
        JpowerAssert.isNull(coreUserService.selectUserLoginId(coreUser.getLoginId(), tenantCode), JpowerError.Business, "当前登陆名已存在");

        coreUser.setPassword(DigestUtil.pwdEncrypt(MD5.md5HexToUpperCase(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD, ConstantsUtils.DEFAULT_USER_PASSWORD))));
        if (Fc.isNull(coreUser.getUserType())){
            coreUser.setUserType(ConstantsEnum.USER_TYPE.USER_TYPE_SYSTEM.getValue());
        }
        CacheUtil.clear(CacheNames.USER_KEY);
        return ReturnJsonUtil.status(coreUserService.save(coreUser));
    }

    @Function(value = "删除用户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "SYSTEM_USER_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "删除用户")
    @OperateLog(title = "删除登录用户", businessType = DELETE)
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE}, produces = "application/json")
    public ResponseData delete(@ApiParam(value = "主键 多个逗号分割", required = true) @RequestParam String ids) {

        JpowerAssert.notEmpty(ids, JpowerError.Arg, "ids不可为空");

        if (coreUserService.delete(Fc.toLongList(ids))) {
            CacheUtil.clear(CacheNames.USER_KEY);
            return ReturnJsonUtil.ok("删除成功");
        } else {
            return ReturnJsonUtil.fail("删除失败");
        }
    }

    @Function(value = "修改用户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "SYSTEM_USER_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "修改用户信息")
    @OperateLog(title = "修改系统用户信息", businessType = UPDATE)
    @RequestMapping(value = "/update", method = {RequestMethod.PUT}, produces = "application/json")
    public ResponseData update(TbCoreUser coreUser) {

        JpowerAssert.notNull(coreUser.getId(), JpowerError.Arg, "用户ID不可为空");

        if (Fc.notNull(coreUser.getIdType()) && ConstantsEnum.ID_TYPE.ID_CARD.getValue().equals(coreUser.getIdType())) {
            if (Fc.isNotBlank(coreUser.getIdNo()) && !Validator.isCitizenId(coreUser.getIdNo())) {
                return ReturnJsonUtil.busFail("身份证不合法");
            }
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone()) && !Validator.isMobile(coreUser.getTelephone())) {
            return ReturnJsonUtil.busFail("手机号不合法");
        }

        if (StringUtils.isNotBlank(coreUser.getEmail()) && !Validator.isEmail(coreUser.getEmail())) {
            return ReturnJsonUtil.busFail("邮箱不合法");
        }

        if (StringUtils.isNotBlank(coreUser.getLoginId())) {
            TbCoreUser user = coreUserService.selectUserLoginId(coreUser.getLoginId(), coreUser.getTenantCode());
            if (user != null && !NumberUtil.equals(user.getId(), coreUser.getId())) {
                return ReturnJsonUtil.busFail("该登录用户名已存在");
            }
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone())) {
            TbCoreUser user = coreUserService.selectByPhone(coreUser.getTelephone(), coreUser.getTenantCode());
            if (user != null && !NumberUtil.equals(user.getId(), coreUser.getId())) {
                return ReturnJsonUtil.busFail("该手机号已存在");
            }
        }

        coreUser.setPassword(null);
        CacheUtil.clear(CacheNames.USER_KEY);
        return ReturnJsonUtil.status(coreUserService.update(coreUser));
    }

    @ApiOperation(value = "修改个人信息")
    @OperateLog(title = "修改个人信息", businessType = UPDATE)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "avatar", value = "头像", paramType = "query", required = false),
        @ApiImplicitParam(name = "nickName", value = "昵称", paramType = "query", required = false),
        @ApiImplicitParam(name = "userName", value = "姓名", paramType = "query", required = false),
        @ApiImplicitParam(name = "idType", value = "证件类型", paramType = "query", required = false),
        @ApiImplicitParam(name = "idNo", value = "证件号码", paramType = "query", required = false),
        @ApiImplicitParam(name = "birthday", value = "出生日期", paramType = "query", required = false),
        @ApiImplicitParam(name = "postCode", value = "邮编", paramType = "query", required = false),
        @ApiImplicitParam(name = "address", value = "地址", paramType = "query", required = false)
    })
    @PutMapping(value = "/updateLogin", produces = "application/json")
    public ResponseData updateLogin(@ApiIgnore TbCoreUser coreUser) {
        JpowerAssert.notNull(coreUser.getId(), JpowerError.Arg, "用户ID不可为空");
        JpowerAssert.notNull(ShieldUtil.getUser(), JpowerError.Arg, "用户未登录");

        if (coreUser.getIdType() != null && ConstantsEnum.ID_TYPE.ID_CARD.getValue().equals(coreUser.getIdType())) {
            if (Fc.isNotBlank(coreUser.getIdNo()) && !Validator.isCitizenId(coreUser.getIdNo())) {
                return ReturnJsonUtil.busFail("身份证不合法");
            }
        }

        CacheUtil.clear(CacheNames.USER_KEY);
        return ReturnJsonUtil.status(coreUserService.update(Wrappers.lambdaUpdate(TbCoreUser.class)
                .set(TbCoreUser::getAvatar,coreUser.getAvatar())
                .set(TbCoreUser::getNickName,coreUser.getNickName())
                .set(TbCoreUser::getUserName,coreUser.getUserName())
                .set(TbCoreUser::getIdType,coreUser.getIdType())
                .set(TbCoreUser::getIdNo,coreUser.getIdNo())
                .set(TbCoreUser::getBirthday,coreUser.getBirthday())
                .set(TbCoreUser::getPostCode,coreUser.getPostCode())
                .set(TbCoreUser::getAddress,coreUser.getAddress())
                .eq(TbCoreUser::getId,ShieldUtil.getUser().getUserId())));
    }

    @Function(value = "重置密码",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "SYSTEM_USER_RESETPASSWORD",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "重置用户登陆密码")
    @PutMapping(value = "/resetPassword", produces = "application/json")
    public ResponseData resetPassword(@ApiParam(value = "主键 多个逗号分割", required = true) @RequestParam String ids) {

        String pass = DigestUtil.pwdEncrypt(MD5.md5HexToUpperCase(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD, ConstantsUtils.DEFAULT_USER_PASSWORD)));

        JpowerAssert.notEmpty(ids, JpowerError.Arg, "用户ids不可为空");

        if (coreUserService.updateUserPassword(Fc.toLongList(ids), pass)) {

            if (ShieldUtil.isRoot()){
                List<String> codes = coreUserService.listObjs(Condition.<TbCoreUser>getQueryWrapper().lambda().select(TbCoreUser::getTenantCode).in(TbCoreUser::getId,Fc.toLongList(ids)),Fc::toStr);
                CacheUtil.clear(CacheNames.USER_KEY, ArrayUtil.toArray(new HashSet<>(codes),String.class));
            } else {
                CacheUtil.clear(CacheNames.USER_KEY);
            }

            return ReturnJsonUtil.ok(Fc.toLongArray(ids).length + "位用户密码重置成功");
        } else {
            return ReturnJsonUtil.fail("重置失败");
        }
    }

    @Function(value = "导入用户",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "SYSTEM_USER_IMPORTUSER",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "批量导入用户")
    @PostMapping(value = "/importUser", produces = "application/json")
    public ResponseData importUser(@ApiParam(value = "Excel文件", required = true) MultipartFile file,
                                   @ApiParam(value = "是否覆盖数据 1是 0否", required = false) @RequestParam(required = false, defaultValue = "0") Integer isCover) {

        JpowerAssert.notTrue(file == null || file.isEmpty(), JpowerError.Arg, "文件不可为空");

        try {
            File saveFile = FileUtil.saveFile(file, "xls,xlsx", ImportExportConstants.IMPORT_PATH);

            if (saveFile.exists()) {
                BeanExcelUtil<TbCoreUser> beanExcelUtil = new BeanExcelUtil<>(TbCoreUser.class);
                List<TbCoreUser> list = beanExcelUtil.importExcel(saveFile);
                //获取完数据之后删除文件
                FileUtil.deleteFile(saveFile);
                if (coreUserService.insertBatch(list, isCover == 1)) {
                    CacheUtil.clear(CacheNames.USER_KEY);
                    return ReturnJsonUtil.ok("新增成功");
                } else {
                    return ReturnJsonUtil.fail("新增失败,请检查文件数据");
                }
            }

            logger.error("文件上传出错，文件不存在,{}", saveFile.getAbsolutePath());
            return ReturnJsonUtil.fail("上传出错，请稍后重试");
        } catch (Exception e) {
            logger.error("文件上传出错，error={}", ExceptionUtil.getStackTraceAsString(e));
            return ReturnJsonUtil.print(ConstantsReturn.RECODE_ERROR, "上传出错，请稍后重试", false);
        }

    }

    @Function(value = "模板下载",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER", btnCode = "SYSTEM_USER_IMPORTUSER",code = "SYSTEM_USER_DOWNLOADTEMPLATE",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation(value = "用户上传模板下载")
    @GetMapping(value = "/downloadTemplate")
    public void downloadTemplate() {
        BeanExcelUtil<TbCoreUser> beanExcelUtil = new BeanExcelUtil<>(TbCoreUser.class, ImportExportConstants.EXPORT_TEMPLATE_PATH);
        String fileName = beanExcelUtil.template("用户模板");

        if (Fc.isBlank(fileName)) {
            throw new BusinessException(fileName + "生成失败");
        }

        File file = new File(beanExcelUtil.getAbsoluteFile(fileName));
        if (file.exists()) {
            try {
                FileUtil.download(file, getResponse(), "用户导入模板.xlsx");
            } catch (IOException e) {
                logger.error("下载文件出错。file={},error={}", file.getAbsolutePath(), e.getMessage());
                throw new BusinessException("下载文件出错，请联系网站管理员");
            }

            FileUtil.deleteFile(file);
        } else {
            throw new BusinessException(fileName + "生成失败，无法下载");
        }
    }

    @ApiOperation(value = "修改密码")
    @GetMapping(value = "/updatePassword")
    public ResponseData<String> updatePassword(@ApiParam(value = "旧密码", required = true) @RequestParam String oldPw,
                                               @ApiParam(value = "新密码", required = true) @RequestParam String newPw) {
        UserInfo userInfo = ShieldUtil.getUser();
        JpowerAssert.notNull(userInfo, JpowerError.Business, "用户未登录");

        TbCoreUser user = coreUserService.getById(userInfo.getUserId());

        if (Fc.isNull(user) || !coreUserService.validatePassword(user.getLoginId(), oldPw, user.getTenantCode())) {
            return ReturnJsonUtil.fail("原密码错误");
        }
        CacheUtil.clear(CacheNames.USER_KEY);
        return ReturnJsonUtil.status(coreUserService.updateUserPassword(Collections.singletonList(user.getId()), DigestUtil.pwdEncrypt(newPw)));
    }

    @ApiOperation(value = "修改手机号")
    @PostMapping(value = "/updatePhone")
    public ResponseData updatePhone(@ApiParam(value = "手机号", required = true) @RequestSingleBody String phone,
                                    @ApiParam(value = "验证码", required = true) @RequestSingleBody String phoneCode) {
        UserInfo userInfo = ShieldUtil.getUser();
        JpowerAssert.notNull(userInfo, JpowerError.Business, "请登录");
        JpowerAssert.isTrue(Validator.isMobile(phone), JpowerError.Business, "手机号不合法");
        JpowerAssert.isTrue(smsClient.validate(new ValidateDto().setCode(VALIDATE_SMS_CODE).setPhone(phone).setPhoneCode(phoneCode)), JpowerError.Business, "验证码错误");

        boolean is = coreUserService.exists(Condition.<TbCoreUser>getQueryWrapper().lambda().eq(TbCoreUser::getTelephone, phone));
        if (is){
            return ReturnJsonUtil.fail("该手机号已被绑定");
        }

        CacheUtil.clear(CacheNames.USER_KEY);
        return ReturnJsonUtil.status(coreUserService.updatePhone(phone, userInfo.getUserId()));
    }

    @ApiOperation(value = "修改邮箱")
    @PostMapping(value = "/updateEmail")
    public ResponseData updateEmail(@ApiParam(value = "邮箱", required = true) @RequestSingleBody String email,
                                    @ApiParam(value = "邮箱消息ID", required = true) @RequestSingleBody String msgId,
                                    @ApiParam(value = "验证码", required = true) @RequestSingleBody String emailCode) {
        UserInfo userInfo = ShieldUtil.getUser();
        JpowerAssert.notNull(userInfo, JpowerError.Business, "请登录");
        JpowerAssert.isTrue(Validator.isEmail(email), JpowerError.Business, "邮箱 不合法");
        String code = Fc.toStr(redisUtil.get("email:"+email+":"+msgId));
        JpowerAssert.notTrue(Fc.notEqualsValue(code, emailCode), JpowerError.Business, "验证码错误");

        boolean is = coreUserService.exists(Condition.<TbCoreUser>getQueryWrapper().lambda().eq(TbCoreUser::getEmail, email));
        if (is){
            return ReturnJsonUtil.fail("该邮箱已被绑定");
        }

        CacheUtil.clear(CacheNames.USER_KEY);
        return ReturnJsonUtil.status(coreUserService.updateEmail(email, userInfo.getUserId()));
    }

}
