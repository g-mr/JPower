package top.jpower.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jpower.system.dbs.entity.dict.TbCoreDict;

/**
 * 字典视图对象
 * 
 * @author mr.g
 */
@Data
public class DictVo extends TbCoreDict {

    @ApiModelProperty("语言类型")
    private String localeStr;

    @ApiModelProperty("父级字典名称")
    private String parentName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;


}
