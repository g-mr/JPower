package com.qidiangk.smart.log.dbs.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.dbs.dictbind.annotation.Dict;

/**
 * 监控参数
 *
 * @author mr.g
 */
@Data
@Table("tb_log_monitor_param")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LogMonitorParam extends BaseEntity {

    @Schema(description = "服务名称",hidden = true)
    private String server;
    @Schema(description = "监控地址",hidden = true)
    private String path;
    @Schema(description = "请求方式",hidden = true)
    private String method;

    @Schema(description = "参数类型 字典:PARAM_TYPE")
    @Dict(name = "PARAM_TYPE")
    private String type;
    @Schema(description = "参数名称")
    private String name;
    @Schema(description = "参数值")
    private String value;

}
