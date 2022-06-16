package com.wlcb.jpower.module.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页信息
 *
 * @Author mr.g
 * @Date 2022/6/15 1:30
 * @Description
 */
@Data
@AllArgsConstructor
@ApiModel("分页数据")
public class Pg<T> implements Serializable {

    private static final long serialVersionUID = 6885616031035948144L;

    @ApiModelProperty("总条数")
    private long total;
    @ApiModelProperty("数据")
    private List<T> list;

}
