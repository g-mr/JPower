package com.wlcb.jpower.dbs.entity;

import com.wlcb.jpower.module.base.annotation.Dict;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author mr.g
 * @Date 2021/5/1 0001 18:59
 */
@Data
public class TbLogOperate extends TbLogBase {

    private static final long serialVersionUID = -8822380359274895613L;

    @ApiModelProperty("操作标题")
    private String title;
    @ApiModelProperty("业务类型（0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据）")
    @Dict(name = "BUSINESS_TYPE")
    private Integer businessType;
    @ApiModelProperty("返回内容")
    private String returnContent;
    @ApiModelProperty("操作状态（0正常 1异常）")
    @Dict(name = "OPERATE_STATUS")
    private Integer status;
    @ApiModelProperty("错误消息")
    private String errorMsg;
}
