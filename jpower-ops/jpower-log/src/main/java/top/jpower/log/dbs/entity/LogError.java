package top.jpower.log.dbs.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 错误日志
 *
 * @author mr.g
 */
@Data
@Table("tb_log_error")
@EqualsAndHashCode(callSuper = true)
public class LogError extends LogBase {

    @Schema(description = "错误信息")
    private String error;
    @Schema(description = "报错行号")
    private Integer lineNumber;
    @Schema(description = "异常名称")
    private String exceptionName;
    @Schema(description = "异常message")
    private String message;
}
