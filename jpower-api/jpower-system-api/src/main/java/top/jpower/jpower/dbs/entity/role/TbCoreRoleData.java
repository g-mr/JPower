package top.jpower.jpower.dbs.entity.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * @ClassName TbCoreRoleData
 * @Description TODO 角色数据权限信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreRoleData extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色ID")
    private Long roleId;
    @ApiModelProperty("数据权限ID")
    private Long dataId;

}
