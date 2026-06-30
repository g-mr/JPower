package com.qidiangk.smart.user.pojo;

import com.mybatisflex.annotation.Column;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.util.support.excel.Excel;
import com.qidiangk.smart.user.dbs.entity.CoreUser;

import java.io.Serial;

/**
 * 用户信息
 *
 * @author mr.g
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends CoreUser {
    @Serial
    private static final long serialVersionUID = -7254193410221595563L;

    @Schema(description = "角色ID，多个逗号分割")
    @Column(ignore = true)
    private String roleIds;

    @Schema(description = "部门名称")
    @Excel(name = "部门名称",type = Excel.Type.EXPORT)
    private String orgName;
    @Schema(description = "岗位名称")
    @Excel(name = "岗位名称",type = Excel.Type.EXPORT)
    private String postName;
    @Schema(description = "角色名称")
    @Excel(name = "角色名称",type = Excel.Type.EXPORT)
    private String roleName;
    @Schema(description = "在线数量")
    private Integer onLine;

}
