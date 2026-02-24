package top.jpower.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jpower.system.dbs.entity.dict.TbCoreDictType;

/**
 * 字典类型视图对象
 * 
 * @author mr.g
 */
@Data
public class DictTypeVo extends TbCoreDictType {

    private static final long serialVersionUID = -6865706768426898476L;

    @ApiModelProperty("语言类型")
    private String delEnabledStr;

    @ApiModelProperty("语言类型")
    private String isTreeStr;

}
