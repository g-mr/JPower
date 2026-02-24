package top.jpower.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jpower.system.dbs.entity.city.TbCoreCity;

/**
 * 城市视图对象
 * 
 * @author mr.g
 */
@Data
public class CityVo extends TbCoreCity {

    private static final long serialVersionUID = 3438947425188438375L;

    @ApiModelProperty("城市类型")
    private String cityTypeStr;

    @ApiModelProperty("上级地区")
    private String pname;


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
