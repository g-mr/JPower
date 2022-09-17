package com.wlcb.jpower.vo;

import com.wlcb.jpower.dbs.entity.TbCorePost;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author mr.g
 * @date 2022-09-16 17:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostVo extends TbCorePost {

    private static final long serialVersionUID = -4320881275642047517L;

    @ApiModelProperty("岗位人数")
    private Long userNum;
}
