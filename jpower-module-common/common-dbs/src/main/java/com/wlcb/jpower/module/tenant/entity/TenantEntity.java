package com.wlcb.jpower.module.tenant.entity;

import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TenantEntity
 * @Description TODO 租户信息
 * @Author 郭丁志
 * @Date 2020-10-16 10:11
 * @Version 1.0
 */
@Data
public class TenantEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -7549710952290937270L;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;
}
