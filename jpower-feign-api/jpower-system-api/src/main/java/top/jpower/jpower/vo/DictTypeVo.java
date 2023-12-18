package top.jpower.jpower.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jpower.jpower.dbs.entity.dict.TbCoreDictType;

/**
 * @ClassName DictVo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/21 0021 22:05
 * @Version 1.0
 */
@Data
public class DictTypeVo extends TbCoreDictType {

    private static final long serialVersionUID = -6865706768426898476L;

    @ApiModelProperty("语言类型")
    private String delEnabledStr;

    @ApiModelProperty("语言类型")
    private String isTreeStr;

}
