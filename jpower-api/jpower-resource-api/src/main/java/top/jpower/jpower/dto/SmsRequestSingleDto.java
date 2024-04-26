package top.jpower.jpower.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author mr.g
 * @date 2024/4/16 22:04
 * @description
 */
@Data
public class SmsRequestSingleDto implements Serializable {

    /**
     * 编码
     **/
    String code;
    /**
     * 参数
     **/
    Map<String,String> map;
    /**
     * 手机号码
     **/
    String phone;

}
