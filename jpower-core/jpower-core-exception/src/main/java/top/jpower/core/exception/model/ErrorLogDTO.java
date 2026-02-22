package top.jpower.core.exception.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 日志记录DTO
 *
 * @author mr.g
 **/
@Data
public class ErrorLogDTO extends LogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 错误信息
     */
    private String error;
    /**
     * 报错行号
     */
    private Integer lineNumber;
    /**
     * 异常名称
     */
    private String exceptionName;
    /**
     * 异常message
     */
    private String message;
}
