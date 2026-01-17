package top.jpower.user.dbs.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户角色关联信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("tb_core_user_role")
public class CoreUserRole extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long roleId;

    @Column(ignore = true)
    private String roleName;
}
