package com.wlcb.ylth.module.dbs.entity.user;

import com.wlcb.ylth.module.dbs.entity.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName User
 * @Description TODO 登录用户信息
 * @Author 郭丁志
 * @Date 2020-03-05 23:18
 * @Version 1.0
 */
@Data
public class TblUser extends BaseEntity {

    private String name;
    private String user;
    private String password;
    private Integer status;
    private int role;
    private String roleStr;
    private Integer standbyTime;
    private Integer officeId;
    private Date insertAt;
    private Integer jiedao;
    private Integer shequ;
    private String quxian;
    private Integer xiaoqu;
    private String phone;

}
