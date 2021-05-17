package com.wlcb.jpower.module.base.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 郭丁志
 * @Description //TODO 日志记录
 * @Date 17:00 2020-07-10
 **/
@Data
public class LogDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务名称
     */
    protected String serverName;
    /**
     * 服务器 ip
     */
    protected String serverIp;
    /**
     * 服务器名
     */
    protected String serverHost;
    /**
     * 环境
     */
    protected String env;
    /**
     * 操作方式
     */
    protected String method;
    /**
     * 方法类
     */
    protected String methodClass;
    /**
     * 方法名
     */
    protected String methodName;
    /**
     * 请求url
     */
    protected String url;
    /**
     * 请求参数
     */
    protected String param;
    /**
     * 操作IP地址
     */
    protected String operIp;
    /**
     * 操作人员
     */
    protected String operName;
    /**
     * 操作人员类型，是系统用户还是业务用户 0系统1业务2白名单
     */
    protected Integer operUserType;
    /**
     * 操作客户端
     */
    protected String clientCode;

}
