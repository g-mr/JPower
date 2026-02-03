package top.jpower.core.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.jpower.core.util.constants.ReturnConstants;

/**
 * 异常信息
 *
 * @author mr.gmac
 */
@RequiredArgsConstructor
@Getter
public enum JpowerError {

    // 异常信息
    Redis(ReturnConstants.RECODE_REDIS, "redis异常:%s"),
    Http(ReturnConstants.RECODE_HTTP, "http异常"),
    Rpc(ReturnConstants.RECODE_API, "rpc异常[retcode:%s,retmsg:%s]"),
    RateLimit(ReturnConstants.RECODE_RATELIMIT, "流量限制"),
    Parser(ReturnConstants.RECODE_PARSER, "解析异常:%s"),
    Auth(ReturnConstants.RECODE_AUTH, "权限异常:%s"),
    Unknown(ReturnConstants.RECODE_SYSTEM, "系统异常:%s"),
    Arg(ReturnConstants.RECODE_NULL, "参数错误:%s"),
    Business(ReturnConstants.RECODE_BUSINESS, "%s"),
    NotFind(ReturnConstants.RECODE_NOTFOUND, "未找到:%s");

    private final int    code;
    private final String message;



}
