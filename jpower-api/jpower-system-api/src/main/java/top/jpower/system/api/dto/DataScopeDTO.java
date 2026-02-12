package top.jpower.system.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据权限信息
 *
 * @author mr.g
 */
@Data
public class DataScopeDTO implements Serializable {

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "菜单ID")
    private Long menuId;
    @Schema(description = "权限编号")
    private String scopeCode;
    @Schema(description = "数据权限名称")
    private String scopeName;
    @Schema(description = "数据权限类名")
    private String scopeClass;
    @Schema(description = "数据权限字段")
    private String scopeColumn;
    @Schema(description = "数据权限类型 字典：DATA_SCOPE_TYPE")
    private Integer scopeType;
    @Schema(description = "数据权限值域")
    private String scopeValue;
    @Schema(description = "是否所有角色都执行")
    private Integer allRole;
    @Schema(description = "备注")
    private String note;

}
