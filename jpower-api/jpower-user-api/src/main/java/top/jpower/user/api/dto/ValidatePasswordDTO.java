package top.jpower.user.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 账号密码
 *
 * @author mr.g
 */
@Data
public class ValidatePasswordDTO implements Serializable {

    /**
     * 账号
     **/
    String account;
    /**
     * 密码
     **/
    String password;
    /**
     * 租户编码
     **/
    String tenantCode;

}
