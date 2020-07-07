package com.wlcb.jpower.module.dbs.entity.core.role;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TbCoreUserRole
 * @Description TODO 用户角色关联信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreUserRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String roleId;

    @TableField(exist = false)
    private String roleName;
}
