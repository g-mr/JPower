package com.qidiangk.smart.aster.dbs.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

@Data
@Table(value = "phone_place")
@EqualsAndHashCode(callSuper = true)
public class PhonePlaceDO extends BaseEntity {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 所属省
     */
    private String province;

    /**
     * 所属市区
     */
    private String city;

}
