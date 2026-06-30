package com.qidiangk.smart.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import com.qidiangk.smart.system.dbs.entity.org.CoreOrg;

/**
 * 组织视图对象
 * 
 * @author mr.g
 */
@Data
public class OrgVO extends CoreOrg {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
