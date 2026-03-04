package top.jpower.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.jpower.system.dbs.entity.function.CoreFunction;

/**
 * 菜单返回视图
 * 
 * @author mr.g
 */
@Data
public class FunctionVO extends CoreFunction {

    @Schema(description = "页面打开方式")
    private String targetStr;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
