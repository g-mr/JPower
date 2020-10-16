package com.wlcb.jpower.dbs.entity;

import com.wlcb.jpower.module.tenant.entity.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName TbCoreUserRole
 * @Description TODO 用户角色关联信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TbCoreUserOrg extends TenantEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String orgId;

}
