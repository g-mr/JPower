package com.wlcb.jpower.module.dbs.entity.core.user;

import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TbCoreUser
 * @Description TODO 组织机构信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreOrg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8829495593714085987L;

    private String code;
    private String name;
    private String parentId;
    private String parentCode;
    private String icon;
    private Integer sort;
    private String headName;
    private String headPhone;
    private String headEmail;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String address;
    private Integer isVirtual;
    private String remark;
}
