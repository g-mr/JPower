package top.jpower.jpower.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author mr.g
 * @date 2024/4/16 22:27
 * @description
 */
@Data
@Accessors(chain = true)
public class ValidatePasswordDto implements Serializable {

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
