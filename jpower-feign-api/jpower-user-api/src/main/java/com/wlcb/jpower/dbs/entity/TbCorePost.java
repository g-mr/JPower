package com.wlcb.jpower.dbs.entity;

import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @ApiModelProperty("岗位名称")
    private String name;
    @ApiModelProperty("岗位编码")
    private String code;
    @ApiModelProperty("岗位类型")
    @Dict(name = "POST_TYPE")
    private Integer type;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("岗位描述")
    private String describe;
    @ApiModelProperty("上岗条件")
    private String condition;

    @ApiModelProperty("是否启用")
    @Dict(name = "YN01")
    private Integer status;

}
