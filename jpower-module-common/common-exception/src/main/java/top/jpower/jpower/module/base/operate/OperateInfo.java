package top.jpower.jpower.module.base.operate;

import top.jpower.jpower.module.base.annotation.OperateLog;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author mr.g
 * @date 2023/6/11 12:22 PM
 */
@Data
@Accessors(fluent = true)
public class OperateInfo {

    /**
     * 标题
     **/
    private String title = "";

    /** 功能类型 **/
    private top.jpower.jpower.module.base.annotation.OperateLog.BusinessType businessType = top.jpower.jpower.module.base.annotation.OperateLog.BusinessType.OTHER;

    /**
     *  功能类型为其他时的信息<br/>
     *  必须{@link #businessType}={@link OperateLog.BusinessType#OTHER}时生效
     **/
    private String businessOther = "";

    /** 是否需要记录到数据库 **/
    private boolean isSaveLog = true;

    /** 是否获取Request信息 **/
    private boolean isSaveRequestData = true;

    /**
     * 操作内容<br/>
     **/
    private String content = "";

    /**
     * 记录ID<br/>
     **/
    private String recordId = "";


}
