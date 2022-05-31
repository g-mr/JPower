package com.wlcb.jpower.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName OrgVo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/22 0022 0:36
 * @Version 1.0
 */
@Data
public class OrgVo extends TbCoreOrg{

    @ApiModelProperty("是否虚拟机构")
    private String isVirtualStr;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
