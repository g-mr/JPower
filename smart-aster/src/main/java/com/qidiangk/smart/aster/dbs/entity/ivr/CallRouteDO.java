package com.qidiangk.smart.aster.dbs.entity.ivr;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.qidiangk.smart.common.validated.InEnum;
import com.qidiangk.smart.common.validated.group.Validation;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.tenant.entity.TenantEntity;
import com.qidiangk.smart.aster.constants.CallRouteProcessEnum;
import com.qidiangk.smart.aster.constants.CallRouteTypeEnum;
import com.qidiangk.smart.aster.constants.DictTypeConstants;
import com.qidiangk.smart.aster.pojo.UserIntent;

import java.util.List;

@Data
@Table(value = "ivr_call_route")
@EqualsAndHashCode(callSuper = true)
public class CallRouteDO extends TenantEntity {

    /**
     * 呼叫路由ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    @NotNull(groups = Validation.Update.class, message = "呼叫路由ID 不能为空")
    @Schema(description = "呼叫路由ID")
    private Long id;
    /**
     * 路由编码
     */
    @Schema(description = "路由编码")
    @NotBlank(message = "路由编码 不能为空")
    private String routeCode;
    /**
     * 路由名称
     */
    @Schema(description = "路由名称")
    @NotBlank(message = "路由名称 不能为空")
    private String routeName;
    /**
     * 主叫正则
     */
    @Schema(description = "主叫正则")
    private String callingReg;
    /**
     * 被叫正则
     */
    @Schema(description = "被叫正则")
    private String calledReg;
    /**
     * 流程类型 （1：呼入 2：呼出）
     */
    @Schema(description = "流程类型 （1：呼入 2：呼出）")
    @Dict(name = DictTypeConstants.CALL_ROUTE_PROCESS)
    @NotNull(message = "流程类型 不能为空")
    @InEnum(CallRouteProcessEnum.class)
    private Integer process;
    /**
     * 优先级
     */
    @Schema(description = "优先级")
    @NotNull(message = "优先级 不能为空")
    private Integer priority;
    /**
     * 状态 （0：不启用 1：启用）
     */
    @Schema(description = "状态 （0：不启用 1：启用）")
    private Boolean status;
    /**
     * 配置类型
     */
    @Schema(description = "配置类型 ")
    @Dict(name = DictTypeConstants.CALL_ROUTE_TYPE)
    @InEnum(CallRouteTypeEnum.class)
    private Integer type;
    /**
     * 流程配置
     */
    @Column(isLarge = true, typeHandler = JacksonTypeHandler.class)
    @Schema(hidden = true)
    private List<? extends UserIntent.Node> flow;

}
