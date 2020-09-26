package com.wlcb.jpower.module.base.enums;

public enum JpowerError {

    // 异常信息
    Redis(550, "redis异常"),
    Http(540, "http异常"),
    Rpc(530, "rpc异常"),
    RateLimit(520, "流量限制"),
    Parser(510, "解析异常"),
    Unknown(500, "系统异常:%s"),
    Api(600, "api错误[retcode:%s,retmsg:%s]"),
    Arg(406, "参数错误:%s"),
    BUSINESS(300, "%s");

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
