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

    /** 业务类型（OTHER=其它,INSERT=新增,UPDATE=修改,DELETE=删除,GRANT=授权,EXPORT=导出,IMPORT=导入,FORCE=强退,GENCODE=生成代码,CLEAN=清空数据,REVIEW=审核） */
    private String businessType;

    /** 返回内容 */
    private String returnContent;

    /** 操作状态（0正常 1异常） */
    private Integer status;

    /** 错误消息 */
    private String errorMsg;

}
