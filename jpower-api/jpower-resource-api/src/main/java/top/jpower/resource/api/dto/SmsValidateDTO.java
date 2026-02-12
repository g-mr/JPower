package top.jpower.resource.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 短信验证参数
 *
 * @author mr.g
 */
@Data
@Accessors(chain = true)
public class SmsValidateDTO implements Serializable {

    /**
     * 编码
     **/
    String code;
    /**
     * 手机号
     **/
    String phone;

}
