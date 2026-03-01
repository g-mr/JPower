package top.jpower.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import top.jpower.system.dbs.entity.org.CoreOrg;

/**
 * 组织视图对象
 * 
 * @author mr.g
 */
@Data
public class OrgVo extends CoreOrg {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
