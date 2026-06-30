package com.qidiangk.smart.log.dbs.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

/**
 * 基础日志
 *
 * @author mr.g
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogBase extends BaseEntity {

	@Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	@Schema(description = "主键")
	private Long id;
    @Schema(description = "服务名称")
    private String serverName;
    @Schema(description = "服务器ip")
    private String serverIp;
    @Schema(description = "服务器名")
    private String serverHost;
    @Schema(description = "环境")
    private String env;
    @Schema(description = "请求url")
    private String url;
    @Schema(description = "操作方式")
    private String method;
    @Schema(description = "方法类")
    private String methodClass;
    @Schema(description = "方法名")
    private String methodName;
    @Schema(description = "请求参数")
    private String param;
    @Schema(description = "操作IP地址")
    private String operIp;
    @Schema(description = "操作人员")
    private String operName;
    @Schema(description = "操作人员类型，是系统用户还是业务用户 0系统1业务2白名单")
    private Integer operUserType;
    @Schema(description = "操作客户端")
    private String clientCode;
}
