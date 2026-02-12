package top.jpower.resource.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 短信请求结果
 *
 * @author mr.g
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class SmsResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -2681695190640168593L;

    /**
     * 是否成功
     **/
    private boolean success;

    /**
     * http 状态码
     **/
    private int code;

    /**
     * 信息
     **/
    private String msg;
}
