package top.jpower.jpower.dbs.entity.function;

import com.baomidou.mybatisplus.annotation.OrderBy;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * 顶级菜单
 *
 * @author mr.g
 * @date 2022/10/23 23:09
 */
@Data
public class TbCoreTopMenu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7634665521029358012L;

    @ApiModelProperty("客户端ID")
    private Long clientId;
    @ApiModelProperty("菜单编号")
    private String code;
    @ApiModelProperty("菜单名称")
    private String name;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("首页路由")
    private String router;
    @ApiModelProperty("排序")
    @OrderBy(asc = true)
    private Integer sortNum;
    @ApiModelProperty("备注")
    private String note;
    @ApiModelProperty("状态 字典：YN01")
    @Dict(name = "YN01")
    private Integer status;
}
