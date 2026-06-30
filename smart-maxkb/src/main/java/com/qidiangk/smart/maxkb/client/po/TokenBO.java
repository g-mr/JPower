package com.qidiangk.smart.maxkb.client.po;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author mr.g
 */
@Data
@Builder
public class TokenBO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7587280216129887691L;

    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;

}
