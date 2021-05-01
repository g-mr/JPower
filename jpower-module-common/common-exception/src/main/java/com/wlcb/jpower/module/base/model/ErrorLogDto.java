package com.wlcb.jpower.module.base.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 郭丁志
 * @Description //TODO 日志记录
 * @Date 17:00 2020-07-10
 **/
@Data
public class ErrorLogDto extends LogDto implements Serializable {
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
