package top.jpower.user.controller;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.jpower.common.constants.CacheNames;
import top.jpower.common.validated.Mobile;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.boot.argument.RequestSingleBody;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.annotation.OperateLog;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.BusinessException;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.util.constants.ImportExportConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.support.excel.BeanExcelUtil;
import top.jpower.core.util.utils.ExceptionUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.FileUtil;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.resource.api.dto.ValidateDTO;
import top.jpower.resource.api.feign.SmsClient;
import top.jpower.user.dbs.entity.CoreUser;
import top.jpower.user.service.CoreUserService;
import top.jpower.user.vo.LoginUserVO;
import top.jpower.user.vo.UserVO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static top.jpower.common.constants.CacheNames.TOKEN_USER_KEY;
import static top.jpower.common.constants.ServiceCodeConstants.*;
import static top.jpower.core.exception.annotation.OperateLog.BusinessType.DELETE;
import static top.jpower.core.exception.annotation.OperateLog.BusinessType.UPDATE;
import static top.jpower.core.util.constants.JpowerConstants.VALIDATE_SMS_CODE;

@Slf4j
@Tag(name = "用户管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/core/user")
@Validated
public class UserController extends BaseController {

    private final CoreUserService coreUserService;
    private final RedisService redisService;
    private final SmsClient smsClient;

    @Operation(summary = "查询当前登录用户信息")
    @GetMapping(value = "/loginInfo")
    public R<LoginUserVO> getLoginInfo() {
        Long id = ShieldUtil.getUserId();
        JpowerAssert.notNull(id, JpowerError.Auth, NOT_LOGIN);
        LoginUserVO user = coreUserService.userInfo(id);
		user.setRoles(ShieldUtil.getUserRole());
        return R.data(user);
    }

	@Function(value = "用户列表", menus = {
			@Menu(client = "admin", menuCode = "SYSTEM_USER", code = "USER_LIST", type = Menu.TYPE.INTERFACE)
	})
	@Operation(summary = "查询用户分页列表")
	@Parameters({
			@Parameter(name = "pageNum", description = "第几页", example = "1", in = QUERY, schema = @Schema(type = "int"), required = true),
			@Parameter(name = "pageSize", description = "每页长度", example = "10", in = QUERY, schema = @Schema(type = "int"), required = true)
	})
	@GetMapping(value = "/list", produces = "application/json")
	public R<Pg<UserVO>> list(@RequestParam(required = false) Map<String, Object> map) {
		return R.data(coreUserService.listPage(map));
	}

	@Function(value = "新增用户", menus = {
			@Menu(client = "admin", menuCode = "SYSTEM_USER", code = "SYSTEM_USER_ADD", type = Menu.TYPE.BTN)
	})
	@Operation(summary = "新增", description = "主键不用传")
	@PostMapping(value = "/add", produces = "application/json")
	public R<Boolean> add(@Validated(Validation.Create.class) @NotNull(message = "用户信息不能为空") @RequestBody CoreUser coreUser) {
		return R.status(coreUserService.createUser(coreUser));
	}

	@Function(value = "修改用户", menus = {
			@Menu(client = "admin", menuCode = "SYSTEM_USER", code = "SYSTEM_USER_UPDATE", type = Menu.TYPE.BTN)
	})
	@Operation(summary = "修改用户信息")
	@OperateLog(title = "修改系统用户信息", businessType = UPDATE)
	@PutMapping(value = "/update", produces = "application/json")
	public R<Boolean> update(@Validated(Validation.Update.class) @RequestBody CoreUser coreUser) {
		return R.status(coreUserService.updateUser(coreUser));
	}

	@Function(value = "删除用户", menus = {
			@Menu(client = "admin", menuCode = "SYSTEM_USER", code = "SYSTEM_USER_DELETE", type = Menu.TYPE.BTN)
	})
	@Operation(summary = "删除用户")
	@OperateLog(title = "删除登录用户", businessType = DELETE)
	@DeleteMapping(value = "/delete", produces = "application/json")
	public R<Boolean> delete(@Parameter(description = "主键 多个逗号分割", required = true) @NotBlank(message = "ids不可为空") @RequestParam String ids) {
		CacheUtil.clear(CacheNames.USER_KEY);
		return R.status(coreUserService.deleteByIds(Fc.toLongList(ids)));
	}

	@Function(value = "重置密码", menus = {
			@Menu(client = "admin", menuCode = "SYSTEM_USER", code = "SYSTEM_USER_RESETPASSWORD", type = Menu.TYPE.BTN)
	})
	@Operation(summary = "重置用户登陆密码")
	@PutMapping(value = "/resetPassword", produces = "application/json")
	public R<Boolean> resetPassword(@Parameter(description = "主键 多个逗号分割", required = true) @NotEmpty(message = "用户ID不可为空") @RequestSingleBody List<Long> ids) {
		CacheUtil.clear(CacheNames.USER_KEY);
		if (coreUserService.resetPassword(ids)) {
			return R.ok(ids.size() + "位用户密码重置成功", null);
		} else {
			return R.fail();
		}
	}

	@Function(value = "用户在线信息", menus = {
			@Menu(client = "admin", menuCode = "SYSTEM_USER", btnCode = "USER_OFFLINE", code = "USER_ONLINE", type = Menu.TYPE.BTN)
	})
	@Operation(summary = "查询用户在线信息")
	@GetMapping(value = "/online", produces = "application/json")
	public R<List<Map<String, Object>>> online(@Parameter(description = "用户ID") @NotNull(message = "用户ID不可为空") @RequestParam Long userId) {
		Set<String> keys = redisService.keys(TOKEN_USER_KEY + userId + StringPool.COLON + StringPool.ASTERISK);
		List<Map<String, Object>> list = new ArrayList<>();
		keys.forEach(key -> {
			Map<String, Object> map = redisService.valueOps(Map.class).get(key);
			map.put("token", StringUtil.split(key, StringPool.COLON).get(4));
			map.put("userId", userId);
			list.add(map);
		});

		return R.data(list);
	}

	@Function(value = "踢下线", menus = {
			@Menu(client = "admin", menuCode = "SYSTEM_USER", code = "USER_OFFLINE", type = Menu.TYPE.BTN)
	})
	@Operation(summary = "踢下线")
	@PostMapping(value = "/offline", produces = "application/json")
	public R<Boolean> offline(@Parameter(description = "用户ID") @NotNull(message = "用户ID不可为空") @RequestSingleBody Long userId,
							  @Parameter(description = "TOKEN") @NotBlank(message = "TOKEN不可为空") @RequestSingleBody String token) {

		redisService.delete(CacheNames.TOKEN_URL_KEY + token);
		redisService.delete(CacheNames.TOKEN_DATA_SCOPE_KEY + token);
		redisService.delete(TOKEN_USER_KEY + userId + StringPool.COLON + token);

		return R.data(true);
	}

	@Function(value = "导出用户", menus = {
			@Menu(client = "admin", menuCode = "SYSTEM_USER", code = "SYSTEM_USER_EXPORTUSER", type = Menu.TYPE.BTN)
	})
	@Operation(summary = "导出用户")
	@Parameters({
			@Parameter(name = "orgId", description = "部门ID", in = QUERY),
			@Parameter(name = "loginId", description = "登录名", in = QUERY),
			@Parameter(name = "nickName", description = "昵称", in = QUERY),
			@Parameter(name = "userName", description = "姓名", in = QUERY),
			@Parameter(name = "idNo", description = "证件号码", in = QUERY),
			@Parameter(name = "userType", description = "用户类型 字典USER_TYPE", in = QUERY),
			@Parameter(name = "telephone", description = "电话", in = QUERY)
	})
	@GetMapping(value = "/exportUser")
	public void exportUser(@Ignore @RequestParam(required = false) CoreUser coreUser) throws IOException {
		List<UserVO> list = coreUserService.list(coreUser);

		BeanExcelUtil<UserVO> beanExcelUtil = new BeanExcelUtil<>(UserVO.class, ImportExportConstants.EXPORT_PATH);
		String str = beanExcelUtil.exportExcel(list, "用户列表");
		File file = new File(ImportExportConstants.EXPORT_PATH + str);
		FileUtil.download(file, getResponse(), "用户数据.xlsx");
	}

	@Function(value = "模板下载", menus = {
			@Menu(client = "admin", menuCode = "SYSTEM_USER", btnCode = "SYSTEM_USER_IMPORTUSER", code = "SYSTEM_USER_DOWNLOADTEMPLATE", type = Menu.TYPE.INTERFACE)
	})
	@Operation(summary = "用户上传模板下载")
	@GetMapping(value = "/downloadTemplate")
	public void downloadTemplate() {
		BeanExcelUtil<CoreUser> beanExcelUtil = new BeanExcelUtil<>(CoreUser.class, ImportExportConstants.EXPORT_TEMPLATE_PATH);
		String fileName = beanExcelUtil.template("用户模板");

		JpowerAssert.notEmpty(fileName, JpowerError.Business, fileName + GENERATE_FILE_ERROR);

		File file = new File(beanExcelUtil.getAbsoluteFile(fileName));
		if (file.exists()) {
			try {
				FileUtil.download(file, getResponse(), "用户导入模板.xlsx");
			} catch (IOException e) {
				log.error("下载文件出错。file={},error={}", file.getAbsolutePath(), e.getMessage());
				throw new BusinessException(DOWNLOAD_FILE_ERROR);
			}

			FileUtil.deleteFile(file);
		} else {
			throw new BusinessException(fileName + GENERATE_FILE_ERROR);
		}
	}

	@Function(value = "是否激活", menus = {
		@Menu(client = "admin", menuCode = "SYSTEM_USER", code = "SYSTEM_USER_ENABLE", type = Menu.TYPE.BTN)
	})
	@Operation(summary = "是否激活")
	@PostMapping(value = "/enable/{id}")
	public R<Boolean> enable(@Parameter(description = "主键", required = true) @PathVariable("id") Long id,
							 @Parameter(description = "是否激活", required = true) @RequestSingleBody Boolean status) {
		return R.status(coreUserService.enable(id, status));
	}



























    @Function(value = "用户详情", menus = {
            @Menu(client = "admin", menuCode = "SYSTEM_USER", code = "USER_DETAIL", type = Menu.TYPE.BTN)
    })
    @Operation(summary = "查询用户详情")
    @GetMapping(value = "/getById", produces = "application/json")
    public R<UserVO> getById(@Parameter(description = "主键", required = true) @RequestParam @NotNull(message = "主键不可为空") Long id) {
        return R.data(coreUserService.selectUserById(id));
    }

    @Operation(summary = "修改个人信息")
    @OperateLog(title = "修改个人信息", businessType = UPDATE)
    @PutMapping(value = "/updateLogin", produces = "application/json")
    public R<Boolean> updateLogin(@Valid @RequestBody LoginUserVO userVO) {
        // 防御性编程
        JpowerAssert.notNull(ShieldUtil.getUser(), JpowerError.Auth, NOT_LOGIN);
        return R.status(coreUserService.updateUserInfo(userVO));
    }

    @Function(value = "导入用户", menus = {
            @Menu(client = "admin", menuCode = "SYSTEM_USER", code = "SYSTEM_USER_IMPORTUSER", type = Menu.TYPE.BTN)
    })
    @Operation(summary = "批量导入用户")
    @PostMapping(value = "/importUser", produces = "application/json")
    public R<Boolean> importUser(@Parameter(description = "Excel文件", required = true) @NotNull(message = "文件不可为空") MultipartFile file,
                                 @Parameter(description = "是否覆盖数据") @RequestParam(required = false, defaultValue = "false") Boolean isCover) {

        try {
            File saveFile = FileUtil.saveFile(file, "xls,xlsx", ImportExportConstants.IMPORT_PATH);

            if (saveFile.exists()) {
                BeanExcelUtil<CoreUser> beanExcelUtil = new BeanExcelUtil<>(CoreUser.class);
                List<CoreUser> list = beanExcelUtil.importExcel(saveFile);
                //获取完数据之后删除文件
                FileUtil.deleteFile(saveFile);
                CacheUtil.clear(CacheNames.USER_KEY);
                return R.status(coreUserService.insertBatch(list, isCover));
            }

            log.error("文件上传出错，文件不存在,{}", saveFile.getAbsolutePath());
            return R.fail();
        } catch (Exception e) {
            log.error("文件上传出错，error={}", ExceptionUtil.getStackTraceAsString(e));
            return R.fail();
        }

    }

    @Operation(summary = "修改密码")
    @PutMapping(value = "/updatePassword")
    public R<Boolean> updatePassword(@Parameter(description = "旧密码", required = true) @NotBlank(message = "旧密码不可为空") @RequestSingleBody String oldPw,
                                     @Parameter(description = "新密码", required = true) @NotBlank(message = "新密码不可为空") @RequestSingleBody String newPw) {
        return R.status(coreUserService.updatePassword(oldPw, newPw));
    }

    @Operation(summary = "修改手机号")
    @PutMapping(value = "/updatePhone")
    public R<Boolean> updatePhone(@Parameter(description = "手机号", required = true) @Mobile @RequestSingleBody String phone,
                                  @Parameter(description = "验证码", required = true) @NotBlank(message = "验证码不可为空") @RequestSingleBody String phoneCode) {
        JpowerAssert.isTrue(smsClient.validate(new ValidateDTO().setCode(VALIDATE_SMS_CODE).setPhone(phone).setPhoneCode(phoneCode)).isStatus(), JpowerError.Business, SMS_CODE_ERROR);
        return R.status(coreUserService.updatePhone(phone, ShieldUtil.getUserIdThrow()));
    }

    @Operation(summary = "修改邮箱")
    @PutMapping(value = "/updateEmail")
    public R<Boolean> updateEmail(@Parameter(description = "邮箱", required = true) @Email(message = EMAIL_NOT_LEGAL) @RequestSingleBody String email,
                                  @Parameter(description = "邮箱消息ID", required = true) @NotBlank(message = "验证ID不可为空") @RequestSingleBody String msgId,
                                  @Parameter(description = "验证码", required = true) @NotBlank(message = "验证码不可为空") @RequestSingleBody String emailCode) {
        String code = redisService.valueOps(String.class).get("email:" + email + ":" + msgId);
        JpowerAssert.notTrue(Fc.notEqualsValue(code, emailCode), JpowerError.Business, SMS_CODE_ERROR);

        return R.status(coreUserService.updateEmail(email, ShieldUtil.getUserIdThrow()));
    }

}
