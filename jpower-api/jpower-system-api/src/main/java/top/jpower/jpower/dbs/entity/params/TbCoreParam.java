package top.jpower.jpower.dbs.entity.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * @ClassName TbCoreParam
 * @Description TODO 系统参数
 * @Author 郭丁志
 * @Date 2020-05-06 16:04
 * @Version 1.0
 */
@Data
public class TbCoreParam extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3631941874174942414L;

    @ApiModelProperty("参数编码")
    private String code;
    @ApiModelProperty("参数名称")
    private String name;
    @ApiModelProperty("参数值")
    private String value;
    @ApiModelProperty("备注")
    private String note;
}
