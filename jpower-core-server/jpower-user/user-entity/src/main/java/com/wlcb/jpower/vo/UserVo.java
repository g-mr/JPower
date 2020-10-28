package com.wlcb.jpower.vo;

import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.base.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName UserVo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-10-16 15:15
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVo extends TbCoreUser {
    private static final long serialVersionUID = -7254193410221595563L;

    @ApiModelProperty("是否激活")
    private String activationStatusStr;
    @ApiModelProperty("用户类型")
    private String userTypeStr;
    @ApiModelProperty("证件类型")
    private String idTypeStr;

    @ApiModelProperty("部门名称")
    @Excel(name = "部门名称",type = Excel.Type.EXPORT)
    private String orgName;

}
