package top.jpower.system.dbs.entity.function;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

/**
 * 顶级菜单
 * 
 * @author mr.g
 */
@Data
@Table("tb_core_top_menu")
@EqualsAndHashCode(callSuper = true)
public class CoreTopMenu extends BaseEntity {

    @Schema(description = "主键")
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    private Long id;
    @Schema(description = "客户端ID")
    private Long clientId;
    @Schema(description = "菜单编号")
    private String code;
    @Schema(description = "菜单名称")
    private String name;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "首页路由")
    private String router;
    @Schema(description = "排序")
    private Integer sortNum;
    @Schema(description = "备注")
    private String note;
    @Schema(description = "状态")
    private Boolean status;
}
