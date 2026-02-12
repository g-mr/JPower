package top.jpower.resource.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 短信请求参数
 *
 * @author mr.g
 */
@Data
public class SmsRequestDTO implements Serializable {

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
    List<String> phones;

}
