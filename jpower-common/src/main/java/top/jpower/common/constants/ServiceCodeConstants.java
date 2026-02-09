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

    String TENANT_NOT_EXIST = "租户不存在";

    String ACCOUNT_LIMIT = "账号额度已不足";

    String PASSWORD_ERROR = "密码错误";

    String MOBILE_BINGING = "该手机号已被绑定";

    String EMAIL_BINGING = "该邮箱已被绑定";

    String USER_NOT_DELETE = "超级用户和匿名用户不可删除";
    String USER_ROLE_NOT_DELETE = "不可去除超级用户或匿名用户的角色";

}
