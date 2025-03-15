package top.jpower.jpower.dbs.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.tenant.entity.TenantEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 岗位信息
 *
 * @author mr.g
 * @date 2022-09-14 16:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TbCorePost extends TenantEntity implements Serializable {

    private static final long serialVersionUID = -4758533573737613002L;

    @NotBlank(message = "岗位名称不可为空")
    @ApiModelProperty("岗位名称")
    private String name;
    @NotBlank(message = "岗位编码不可为空")
    @ApiModelProperty("岗位编码")
    private String code;
    @NotNull(message = "岗位类型不可为空")
    @ApiModelProperty("岗位类型 字典：POST_TYPE")
    @Dict(name = "POST_TYPE")
    private Integer type;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("岗位描述")
    @TableField(value = "`describe`", updateStrategy = FieldStrategy.IGNORED)
    private String describe;
    @ApiModelProperty("上岗条件")
    @TableField(value = "`condition`", updateStrategy = FieldStrategy.IGNORED)
    private String condition;

    @ApiModelProperty("是否启用 字典：YN01")
    @Dict(name = "YN01")
    private Integer status;

}
