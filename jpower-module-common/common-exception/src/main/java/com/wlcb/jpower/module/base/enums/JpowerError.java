package com.wlcb.jpower.module.base.enums;

import org.springframework.http.HttpStatus;

public enum JpowerError {

    // 异常信息
    Redis(550, "redis异常"),
    Http(540, "http异常"),
    Rpc(530, "rpc异常[retcode:%s,retmsg:%s]"),
    RateLimit(520, "流量限制"),
    Parser(510, "解析异常:%s"),
    Auth(401, "权限异常:%s"),
    Unknown(500, "系统异常:%s"),
    Arg(406, "参数错误:%s"),
    BUSINESS(HttpStatus.NOT_IMPLEMENTED.value(), "%s");

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
