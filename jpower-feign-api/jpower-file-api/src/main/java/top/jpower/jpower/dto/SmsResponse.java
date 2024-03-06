package top.jpower.jpower.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mr.g
 * @date 2024/3/6 10:44 AM
 */
@Data
@AllArgsConstructor
public class SmsResponse implements Serializable {
    private static final long serialVersionUID = -2681695190640168593L;

    /**
     * 是否成功
     **/
    private boolean success;

    /**
     * 信息
     **/
    private String msg;
}
