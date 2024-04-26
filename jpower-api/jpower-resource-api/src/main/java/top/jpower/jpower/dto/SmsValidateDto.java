package top.jpower.jpower.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author mr.g
 * @date 2024/4/16 22:09
 * @description
 */
@Data
@Accessors(chain = true)
public class SmsValidateDto implements Serializable {

    /**
     * 编码
     **/
    String code;
    /**
     * 手机号
     **/
    String phone;

}
