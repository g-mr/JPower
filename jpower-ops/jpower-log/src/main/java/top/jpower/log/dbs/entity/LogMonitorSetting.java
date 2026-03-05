package top.jpower.log.dbs.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

/**
 * 监控设置
 *
 * @author mr.g
 */
@Data
@Table(value = "tb_log_monitor_setting")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LogMonitorSetting extends BaseEntity {

    @Schema(description = "服务名称")
    private String server;
    @Schema(description = "监控地址")
    private String path;
    @Schema(description = "所属分组")
    private String tag;
    @Schema(description = "请求方式")
    private String method;
    @Schema(description = "是否监控 字典YN01")
    private Integer isMonitor;
    @Schema(description = "respose正确status,多个逗号分割")
    private String code;
    @Schema(description = "js代码")
    private String execJs;

}
