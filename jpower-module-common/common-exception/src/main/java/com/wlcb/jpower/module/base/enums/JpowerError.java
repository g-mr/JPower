package com.wlcb.jpower.module.base.enums;

import static com.wlcb.jpower.module.common.utils.constants.ConstantsReturn.*;

/**
 * 异常信息
 *
 * @author mr.gmac
 */

public enum JpowerError {

    // 异常信息
    Redis(RECODE_REDIS, "redis异常:%s"),
    Http(RECODE_HTTP, "http异常"),
    Rpc(RECODE_API, "rpc异常[retcode:%s,retmsg:%s]"),
    RateLimit(RECODE_RATELIMIT, "流量限制"),
    Parser(RECODE_PARSER, "解析异常:%s"),
    Auth(RECODE_AUTH, "权限异常:%s"),
    Unknown(RECODE_SYSTEM, "系统异常:%s"),
    Arg(RECODE_NULL, "参数错误:%s"),
    Business(RECODE_ERROR, "%s"),
    NotFind(RECODE_NOTFOUND, "未找到:%s");

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
