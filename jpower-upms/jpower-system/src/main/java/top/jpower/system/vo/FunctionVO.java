package top.jpower.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import top.jpower.system.dbs.entity.function.CoreFunction;

/**
 * 菜单返回视图
 * 
 * @author mr.g
 */
@Data
public class FunctionVO extends CoreFunction {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
