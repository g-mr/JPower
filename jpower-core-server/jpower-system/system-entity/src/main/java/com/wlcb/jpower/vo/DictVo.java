package com.wlcb.jpower.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDict;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName DictVo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/21 0021 22:05
 * @Version 1.0
 */
@Data
public class DictVo extends TbCoreDict {

    @ApiModelProperty("语言类型")
    private String localeStr;

    @ApiModelProperty("父级字典名称")
    private String parentName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;


}
