package com.wlcb.jpower.module.dbs.entity.log;

import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

/**
 * @Author 郭丁志
 * @Description //TODO 日志记录
 * @Date 17:00 2020-07-10
 **/
@Data
public class SysOperLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志主键 */
    private String id;

    /** 操作模块 */
    private String title;

    /** 业务类型（0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据） */
    private Integer businessType;

    /** 业务类型数组 */
    private Integer[] businessTypes;

    /** 请求方法 */
    private String method;

    /** 操作人员类型，是系统用户还是业务用户 0系统1业务2白名单 */
    private Integer operUserType;

    /** 操作人员ID */
    private String operId;

    /** 操作人员 */
    private String operName;

    /** 请求url */
    private String operUrl;

    /** 操作地址 */
    private String operIp;

    /** 操作地点 */
    private String operLocation;

    /** 请求参数 */
    private String operParam;

    /** 操作状态（0正常 1异常） */
    private Integer status;

    /** 错误消息 */
    private String errorMsg;

}
