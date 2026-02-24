package top.jpower.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;

/**
 * 菜单返回视图
 * 
 * @author mr.g
 */
@Data
public class FunctionVo extends TbCoreFunction {

    @ApiModelProperty("页面打开方式")
    private String targetStr;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
