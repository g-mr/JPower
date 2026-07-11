package top.jpower.common.constants;

/**
 * 业务错误提示
 *
 * @author mr.g
 */
public interface ServiceCodeConstants {

    String NOT_LOGIN = "用户未登录";

    String MOBILE_EXISTS = "手机号已存在";

    String LOGIN_ID_EXISTS = "当前登陆名已存在";

    String IDCARD_NOT_LEGAL = "身份证不合法";

    String EMAIL_NOT_LEGAL = "邮箱不合法";

	String MOBILE_NOT_LEGAL = "手机号不合法";

    String TENANT_NOT_EXIST = "租户不存在";

    String ACCOUNT_LIMIT = "账号额度已不足";

    String PASSWORD_ERROR = "密码错误";

    String MOBILE_BINGING = "该手机号已被绑定";

    String EMAIL_BINGING = "该邮箱已被绑定";

    String USER_NOT_DELETE = "超级用户和匿名用户不可删除";
    String USER_ROLE_NOT_DELETE = "不可去除超级用户或匿名用户的角色";

    String CODE_EXIST = "编码已存在";

	String DOMAIN_EXIST = "该域名已存在";

	String FILE_ID_NOT_LEGAL = "文件标识不合法";

	String FILE_NOT_EXIST = "文件不存在";

	String FILE_PATH_NOT_EXIST = "文件路径为空";

	String NO_USER_TO_IMPORT = "没有可导入的用户";

	String FILE_SAVE_PATH_NOT_CONFIG = "未配置服务器文件保存路径";

	String NOT_FOUND_FILE_INFO = "文件信息";

	String NOT_FOUND_OSS = "对象存储";

	String INVALID_STORAGE_TYPE = "storageType无效，请传递正确的storageType参数";

	String NOT_FOUND_SMS_TEMPLATE = "短信发送模版";

	String SMS_CODE_SENT = "该验证码已经发送，请一分钟后重试";

	String SMS_CODE_ERROR = "验证码错误";

	String NOT_FOUND_USER = "用户";

	String NOT_FOUND_DICT_RYPE = "字典类型";

    String DOWNLOAD_FILE_ERROR = "下载文件出错，请联系网站管理员";

	String GENERATE_FILE_ERROR = "生成失败，无法下载";

	String ROLE_ID_NOT_NULL = "角色ID不能为空";

	String DELETE_CHILD = "请先删除子级";

	String REFRESH_TOKEN_LESS_ACCESS_TOKEN = "刷新令牌时长不可小于令牌时长";

	String NOT_FOUND_DATA = "数据";

	String NOT_FOUND_CLIENT = "客户端不存在";

	String MISS_REQUIRED_PARAMETER = "缺失必须参数";

	String PARAMETER_ILLEGAL = "参数非法";

	String DICT_EXIST_CHILD = "存在启用的下级字典，不可停用";

	String NOT_SUPER_ADMIN_MODIFY_TENANT = "只可超级管理员操作租户";

	String SAVE_FAILURE = "保存失败";

	String DELETE_EXIST_CHILD = "您删除的信息存在下级，请先删除下级信息";

	String DATA_SCOPE_NOT_NULL = "数据权限值域不可为空";

	String TENANT_CODE_NOT_NULL = "租户编码不可为空";

	String NOT_OPEN_REGISTER = "未开启注册功能";

	String NOT_OPEN_FORGET_PASSWORD = "未开启忘记功能";

	String USER_EXIST = "该用户已注册";

	String LINK_EXPIRE = "链接已过期";

	String PASSWORD_RESET_DEVICE = "请在请求重置设备中重置密码";

	String EMAIL_NOT_FOUND_USER = "请输入的邮箱未找到用户";

}
