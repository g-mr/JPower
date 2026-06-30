package com.qidiangk.smart.resource.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 短信请求参数
 *
 * @author mr.g
 */
@Data
public class SmsRequestSingleDTO implements Serializable {

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
