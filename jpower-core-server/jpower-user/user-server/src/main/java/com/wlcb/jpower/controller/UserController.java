package com.wlcb.jpower.controller;

import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.cache.param.ParamConfig;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.base.annotation.OperateLog;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.support.BeanExcelUtil;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.*;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.CoreUserService;
import com.wlcb.jpower.vo.UserVo;
import com.wlcb.jpower.wrapper.UserWrapper;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.wlcb.jpower.module.base.annotation.OperateLog.BusinessType.*;
import static com.wlcb.jpower.module.tenant.TenantConstant.*;

@Api(tags = "用户管理")
@RestController
@AllArgsConstructor
@RequestMapping("/core/user")
public class UserController extends BaseController {

    private CoreUserService coreUserService;

    @ApiOperation("查询当前登录用户信息")
    @GetMapping(value = "/getLoginInfo", produces = "application/json")
    public ResponseData<UserVo> getLoginInfo() {
        String id = SecureUtil.getUserId();
        JpowerAssert.notEmpty(id,JpowerError.Arg,"用户未登录");
        TbCoreUser user = coreUserService.getById(id);
        return ReturnJsonUtil.ok("获取成功", UserWrapper.builder().entityVO(user));
    }

    @ApiOperation("查询用户分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", defaultValue = "1", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页长度", defaultValue = "10", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "orgId", value = "部门ID", paramType = "query", required = false),
            @ApiImplicitParam(name = "loginId", value = "登录名", paramType = "query", required = false),
            @ApiImplicitParam(name = "nickName", value = "昵称", paramType = "query", required = false),
            @ApiImplicitParam(name = "userName", value = "姓名", paramType = "query", required = false),
            @ApiImplicitParam(name = "idNo", value = "证件号码", paramType = "query", required = false),
            @ApiImplicitParam(name = "userType", value = "用户类型 字典USER_TYPE", paramType = "query", required = false),
            @ApiImplicitParam(name = "telephone", value = "电话", paramType = "query", required = false)
    })
    @GetMapping(value = "/list", produces = "application/json")
    public ResponseData<PageInfo<UserVo>> list(@ApiIgnore TbCoreUser coreUser) {
        PageInfo<UserVo> list = coreUserService.listPage(coreUser);
        return ReturnJsonUtil.ok("获取成功", list);
    }

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

    @ApiOperation("查询用户详情")
    @RequestMapping(value = "/getById", method = RequestMethod.GET, produces = "application/json")
    public ResponseData<UserVo> getById(@ApiParam(value = "主键", required = true) @RequestParam @NotBlank(message = "主键不可为空") String id) {
        JpowerAssert.notEmpty(id, JpowerError.Arg, "id不可为空");

        TbCoreUser user = coreUserService.selectUserById(id);
        return ReturnJsonUtil.ok("查询成功", UserWrapper.builder().entityVO(user));
    }

    @ApiOperation(value = "新增", notes = "主键不用传")
    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseData add(TbCoreUser coreUser) {

        JpowerAssert.notEmpty(coreUser.getLoginId(), JpowerError.Arg, "用户名不可为空");

        if (coreUser.getIdType() != null && ConstantsEnum.ID_TYPE.ID_CARD.getValue().equals(coreUser.getIdType())) {
            if (!StrUtil.cardCodeVerifySimple(coreUser.getIdNo())) {
                return ReturnJsonUtil.busFail("身份证不合法");
            }
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone()) && !StrUtil.isPhone(coreUser.getTelephone())) {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS, "手机号不合法", false);
        }

        if (StringUtils.isNotBlank(coreUser.getEmail()) && !StrUtil.isEmail(coreUser.getEmail())) {
            return ReturnJsonUtil.busFail("邮箱不合法");
        }

        String tenantCode = SecureUtil.getTenantCode();
        if (SecureUtil.isRoot()) {
            tenantCode = Fc.isBlank(coreUser.getTenantCode()) ? DEFAULT_TENANT_CODE : coreUser.getTenantCode();
        }
        TbCoreTenant tenant = SystemCache.getTenantByCode(tenantCode);
        if (Fc.isNull(tenant)) {
            return ReturnJsonUtil.fail("租户不存在");
        }
        Integer accountNumber = getAccountNumber(tenant.getLicenseKey());
        if (!Fc.equals(accountNumber, TENANT_ACCOUNT_NUMBER)) {
            Integer count = coreUserService.count(Condition.<TbCoreUser>getQueryWrapper().lambda().eq(TbCoreUser::getTenantCode, tenantCode));
            if (count >= accountNumber) {
                return ReturnJsonUtil.busFail("账号额度已不足");
            }
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone())) {
            JpowerAssert.isNull(coreUserService.selectByPhone(coreUser.getTelephone(), coreUser.getTenantCode()), JpowerError.BUSINESS, "手机号已存在");
        }
        JpowerAssert.isNull(coreUserService.selectUserLoginId(coreUser.getLoginId(), coreUser.getTenantCode()), JpowerError.BUSINESS, "当前登陆名已存在");

        coreUser.setPassword(DigestUtil.encrypt(MD5.parseStrToMd5U32(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD, ConstantsUtils.DEFAULT_USER_PASSWORD))));
        coreUser.setUserType(ConstantsEnum.USER_TYPE.USER_TYPE_SYSTEM.getValue());
        CacheUtil.clear(CacheNames.USER_REDIS_CACHE);
        return ReturnJsonUtil.status(coreUserService.save(coreUser));
    }

    @ApiOperation(value = "删除用户")
    @OperateLog(title = "删除登录用户", businessType = DELETE)
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE}, produces = "application/json")
    public ResponseData delete(@ApiParam(value = "主键 多个逗号分割", required = true) @RequestParam String ids) {

        JpowerAssert.notEmpty(ids, JpowerError.Arg, "ids不可为空");

        if (coreUserService.delete(ids)) {
            CacheUtil.clear(CacheNames.USER_REDIS_CACHE);
            return ReturnJsonUtil.ok("删除成功");
        } else {
            return ReturnJsonUtil.fail("删除失败");
        }
    }

    @ApiOperation(value = "修改用户信息")
    @OperateLog(title = "修改系统用户信息", businessType = UPDATE)
    @RequestMapping(value = "/update", method = {RequestMethod.PUT}, produces = "application/json")
    public ResponseData update(TbCoreUser coreUser) {

        JpowerAssert.notEmpty(coreUser.getId(), JpowerError.Arg, "用户ID不可为空");

        if (Fc.notNull(coreUser.getIdType()) && ConstantsEnum.ID_TYPE.ID_CARD.getValue().equals(coreUser.getIdType())) {
            if (Fc.isNotBlank(coreUser.getIdNo()) && !StrUtil.cardCodeVerifySimple(coreUser.getIdNo())) {
                return ReturnJsonUtil.busFail("身份证不合法");
            }
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone()) && !StrUtil.isPhone(coreUser.getTelephone())) {
            return ReturnJsonUtil.busFail("手机号不合法");
        }

        if (StringUtils.isNotBlank(coreUser.getEmail()) && !StrUtil.isEmail(coreUser.getEmail())) {
            return ReturnJsonUtil.busFail("邮箱不合法");
        }

        if (StringUtils.isNotBlank(coreUser.getLoginId())) {
            TbCoreUser user = coreUserService.selectUserLoginId(coreUser.getLoginId(), coreUser.getTenantCode());
            if (user != null && !StringUtils.equals(user.getId(), coreUser.getId())) {
                return ReturnJsonUtil.busFail("该登录用户名已存在");
            }
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone())) {
            TbCoreUser user = coreUserService.selectByPhone(coreUser.getTelephone(), coreUser.getTenantCode());
            if (user != null && !StringUtils.equals(user.getId(), coreUser.getId())) {
                return ReturnJsonUtil.busFail("该手机号已存在");
            }
        }

        coreUser.setPassword(null);
        CacheUtil.clear(CacheNames.USER_REDIS_CACHE);
        return ReturnJsonUtil.status(coreUserService.update(coreUser));
    }

    @ApiOperation(value = "修改个人信息")
    @OperateLog(title = "修改个人信息", businessType = UPDATE)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "avatar", value = "头像", paramType = "query", required = false),
        @ApiImplicitParam(name = "orgId", value = "部门ID", paramType = "query", required = false),
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
        JpowerAssert.notEmpty(coreUser.getId(), JpowerError.Arg, "用户ID不可为空");
        JpowerAssert.notNull(SecureUtil.getUser(), JpowerError.Arg, "用户未登录");

        if (coreUser.getIdType() != null && ConstantsEnum.ID_TYPE.ID_CARD.getValue().equals(coreUser.getIdType())) {
            if (!StrUtil.cardCodeVerifySimple(coreUser.getIdNo())) {
                return ReturnJsonUtil.busFail("身份证不合法");
            }
        }

        coreUser.setPassword(null);
        coreUser.setRoleIds(null);
        coreUser.setId(SecureUtil.getUser().getUserId());
        CacheUtil.clear(CacheNames.USER_REDIS_CACHE);
        return ReturnJsonUtil.status(coreUserService.update(coreUser));
    }

    @ApiOperation(value = "重置用户登陆密码")
    @PutMapping(value = "/resetPassword", produces = "application/json")
    public ResponseData resetPassword(@ApiParam(value = "主键 多个逗号分割", required = true) @RequestParam String ids) {

        String pass = DigestUtil.encrypt(MD5.parseStrToMd5U32(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD, ConstantsUtils.DEFAULT_USER_PASSWORD)));

        JpowerAssert.notEmpty(ids, JpowerError.Arg, "用户ids不可为空");

        if (coreUserService.updateUserPassword(ids, pass)) {
            CacheUtil.clear(CacheNames.USER_REDIS_CACHE);
            return ReturnJsonUtil.ok(ids.split(",").length + "位用户密码重置成功");
        } else {
            return ReturnJsonUtil.fail("重置失败");
        }
    }

    @ApiOperation(value = "批量导入用户")
    @PostMapping(value = "/importUser", produces = "application/json")
    public ResponseData importUser(@ApiParam(value = "Excel文件", required = true) MultipartFile file,
                                   @ApiParam(value = "是否覆盖数据 1是 0否", required = false) @RequestParam(required = false, defaultValue = "0") Integer isCover) {

        JpowerAssert.notTrue(file == null || file.isEmpty(), JpowerError.Arg, "文件不可为空");

        try {
            String savePaths = MultipartFileUtil.saveFile(file, "xls,xlsx", ImportExportConstants.IMPORT_PATH);

            File saveFile = new File(ImportExportConstants.IMPORT_PATH + savePaths);

            if (saveFile.exists()) {
                BeanExcelUtil<TbCoreUser> beanExcelUtil = new BeanExcelUtil<>(TbCoreUser.class);
                List<TbCoreUser> list = beanExcelUtil.importExcel(saveFile);
                //获取完数据之后删除文件
                FileUtil.deleteFile(saveFile);
                if (coreUserService.insertBatch(list, isCover == 1)) {
                    CacheUtil.clear(CacheNames.USER_REDIS_CACHE);
                    return ReturnJsonUtil.ok("新增成功");
                } else {
                    return ReturnJsonUtil.fail("新增失败,请检查文件数据");
                }
            }

            logger.error("文件上传出错，文件不存在,{}", saveFile.getAbsolutePath());
            return ReturnJsonUtil.fail("上传出错，请稍后重试");
        } catch (Exception e) {
            logger.error("文件上传出错，error={}", ExceptionsUtil.getStackTraceAsString(e));
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_ERROR, "上传出错，请稍后重试", false);
        }

    }

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
        UserInfo userInfo = SecureUtil.getUser();
        JpowerAssert.notNull(userInfo, JpowerError.BUSINESS, "用户未登录");

        TbCoreUser user = coreUserService.getById(userInfo.getUserId());
        if (Fc.isNull(user) || !Fc.equals(user.getPassword(), DigestUtil.encrypt(oldPw))) {
            return ReturnJsonUtil.fail("原密码错误");
        }
        CacheUtil.clear(CacheNames.USER_REDIS_CACHE);
        return ReturnJsonUtil.status(coreUserService.updateUserPassword(user.getId(), DigestUtil.encrypt(newPw)));
    }
}
