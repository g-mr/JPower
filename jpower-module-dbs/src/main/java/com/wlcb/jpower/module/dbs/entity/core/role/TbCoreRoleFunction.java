package com.wlcb.jpower.module.dbs.entity.core.role;

import com.baomidou.mybatisplus.annotations.TableField;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TbCoreRoleFunction
 * @Description TODO 角色权限信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreRoleFunction extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roleId;
    private String functionId;

    @TableField(exist = false)
    private String functionName;
}
