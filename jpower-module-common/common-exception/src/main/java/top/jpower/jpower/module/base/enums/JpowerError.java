package top.jpower.jpower.module.base.enums;

import top.jpower.jpower.module.common.utils.constants.ConstantsReturn;

/**
 * 异常信息
 *
 * @author mr.gmac
 */

public enum JpowerError {

    // 异常信息
    Redis(ConstantsReturn.RECODE_REDIS, "redis异常:%s"),
    Http(ConstantsReturn.RECODE_HTTP, "http异常"),
    Rpc(ConstantsReturn.RECODE_API, "rpc异常[retcode:%s,retmsg:%s]"),
    RateLimit(ConstantsReturn.RECODE_RATELIMIT, "流量限制"),
    Parser(ConstantsReturn.RECODE_PARSER, "解析异常:%s"),
    Auth(ConstantsReturn.RECODE_AUTH, "权限异常:%s"),
    Unknown(ConstantsReturn.RECODE_SYSTEM, "系统异常:%s"),
    Arg(ConstantsReturn.RECODE_NULL, "参数错误:%s"),
    Business(ConstantsReturn.RECODE_ERROR, "%s"),
    NotFind(ConstantsReturn.RECODE_NOTFOUND, "未找到:%s");

    private int    code;
    private String message;


    JpowerError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
