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
    @ApiModelProperty("业务类型（OTHER=其它,INSERT=新增,UPDATE=修改,DELETE=删除,GRANT=授权,EXPORT=导出,IMPORT=导入,FORCE=强退,GENCODE=生成代码,CLEAN=清空数据,REVIEW=审核）")
    @Dict(name = "BUSINESS_TYPE")
    private String businessType;
    @ApiModelProperty("返回内容")
    private String returnContent;
    @ApiModelProperty("操作状态（0正常 1异常）")
    @Dict(name = "OPERATE_STATUS")
    private Integer status;
    @ApiModelProperty("错误消息")
    private String errorMsg;
}
