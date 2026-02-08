package top.jpower.jpower.dbs.entity.params;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

/**
 * 系统参数
 * 
 * @author 郭丁志
 */
@Data
@Table("tb_core_param")
@EqualsAndHashCode(callSuper = true)
public class CoreParam extends BaseEntity {

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "参数编码")
    private String code;
    @Schema(description = "参数名称")
    private String name;
    @Schema(description = "参数值")
    private String value;
    @Schema(description = "备注")
    private String note;
}
