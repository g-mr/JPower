package com.wlcb.jpower.module.common.auth;

import lombok.Data;

/**
 * @ClassName TokenInfo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-27 15:59
 * @Version 1.0
 */

@Data
public class TokenInfo {

    /**
     * 令牌值
     */
    private String token;

    /**
     * 过期秒数
     */
    private int expire;

}