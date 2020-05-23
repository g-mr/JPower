package com.wlcb.jpower.module.dbs.entity.core.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName TbCoreUser
 * @Description TODO 登录用户信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8829495593714085987L;

    private String orgId;
    private String loginId;
    private String password;
    private String userName;
    private Integer idType;
    private String idNo;
    private Integer userType;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date birthday;
    private String email;
    private String telephone;
    private String address;
    private String postCode;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    private Integer loginCount;
    private Integer activationStatus;
    private String activationCode;
}
