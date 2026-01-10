package top.jpower.jpower.dbs.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.tenant.entity.TenantEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * 岗位信息
 *
 * @author mr.g
 * @date 2022-09-14 16:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Table("tb_core_post")
public class TbCorePost extends TenantEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -4758533573737613002L;

    @NotBlank(message = "岗位名称不可为空")
    @Schema(description = "岗位名称")
    private String name;
    @NotBlank(message = "岗位编码不可为空")
    @Schema(description = "岗位编码")
    private String code;
    @NotNull(message = "岗位类型不可为空")
    @Schema(description = "岗位类型 字典：POST_TYPE")
    @Dict(name = "POST_TYPE")
    private Integer type;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "岗位描述")
    @Column(value = "`describe`", updateStrategy = FieldStrategy.IGNORED)
    private String describe;
    @Schema(description = "上岗条件")
    @TableField(value = "`condition`", updateStrategy = FieldStrategy.IGNORED)
    private String condition;

    @Schema(description = "是否启用 字典：YN01")
    @Dict(name = "YN01")
    private Integer status;

}
