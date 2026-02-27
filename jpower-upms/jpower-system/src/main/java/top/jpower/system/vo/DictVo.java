package top.jpower.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.jpower.system.dbs.entity.dict.CoreDict;

/**
 * 字典视图对象
 * 
 * @author mr.g
 */
@Data
public class DictVo extends CoreDict {

    @Schema(description = "父级字典名称")
    private String parentName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;


}
