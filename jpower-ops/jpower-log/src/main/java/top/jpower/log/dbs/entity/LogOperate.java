package top.jpower.log.dbs.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dictbind.annotation.Dict;

/**
 * 操作日志
 * 
 * @author mr.g
 */
@Data
@Table(value = "tb_log_operate")
@EqualsAndHashCode(callSuper = true)
public class LogOperate extends LogBase {

    @Schema(description = "操作标题")
    private String title;
    @Schema(description = "业务类型（OTHER=其它,INSERT=新增,UPDATE=修改,DELETE=删除,GRANT=授权,EXPORT=导出,IMPORT=导入,FORCE=强退,GENCODE=生成代码,CLEAN=清空数据,REVIEW=审核）")
    @Dict(name = "BUSINESS_TYPE")
    private String businessType;
    @Schema(description = "返回内容")
    private String returnContent;
    @Schema(description = "操作状态（0正常 1异常）")
    @Dict(name = "OPERATE_STATUS")
    private Integer status;
    @Schema(description = "错误消息")
    private String errorMsg;
    @Schema(description = "记录ID")
    private String recordId;
    @Schema(description = "记录内容")
    private String content;

}
