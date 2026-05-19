package top.jpower.core.asterisk.sip.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(fluent = true, makeFinal = true)
@Builder
public class SipDTO implements Serializable {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 地址
     */
    private String domain;
    /**
     * 端口
     */
    private int port;

}
