package com.wlcb.jpower.module.dbs.entity.core.role;

import com.alibaba.fastjson.annotation.JSONField;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName TbCoreUser
 * @Description TODO 角色信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7093626905745914312L;

    private String code;
    private String name;
    private String parentId;
    private String parentCode;
    private String iconUrl;
    private Integer isSysRole;
    private String remark;
}
