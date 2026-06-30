package com.qidiangk.smart.log.dbs.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.util.support.excel.Excel;

/**
 * @author mr.g
 */
@Data
@Table(value = "tb_log_monitor_result")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LogMonitorResult extends BaseEntity {

    @Schema(description = "服务名称")
    @Excel(name = "服务名称")
    private String name;
    @Schema(description = "监控地址")
    @Excel(name = "监控地址")
    private String path;
    @Schema(description = "接口地址")
    @Excel(name = "测试地址")
    private String url;
    @Schema(description = "请求方式")
    @Excel(name = "请求方式")
    private String method;
    @Schema(description = "请求异常")
    @Excel(name = "请求异常")
    private String error;
    @Schema(description = "响应数据")
    @Excel(name = "响应数据")
    private String respose;
    @Schema(description = "响应编码")
    @Excel(name = "响应编码")
    private Integer resposeCode;
    @Schema(description = "接口返回数据")
    @Excel(name = "接口返回数据")
    private String restfulResponse;
    @Schema(description = "header参数")
    @Excel(name = "header参数")
    private String header;
    @Schema(description = "body参数")
    @Excel(name = "请求参数")
    private String body;
    @Schema(description = "是否成功")
    @Excel(name = "是否成功")
    private Boolean isSuccess;
    @Schema(description = "响应时长 单位毫秒")
    @Excel(name = "响应时长",suffix="毫秒")
    private Long responseTime;

}
