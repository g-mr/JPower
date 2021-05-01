package com.wlcb.jpower.module.base.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 郭丁志
 * @Description //TODO 日志记录
 * @Date 17:00 2020-07-10
 **/
@Data
public class OperateLogDto  extends LogDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 操作模块 */
    private String title;

    /** 业务类型（0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据） */
    private Integer businessType;

    /** 返回内容 */
    private String returnContent;

    /** 操作状态（0正常 1异常） */
    private Integer status;

    /** 错误消息 */
    private String errorMsg;

}
