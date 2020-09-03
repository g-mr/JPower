package com.wlcb.jpower.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName UserDto
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/9/3 0003 1:09
 * @Version 1.0
 */
@Data
public class UserDto {

    private String id;
    private String orgId;
    private String loginId;
    private String nickName;
    private String userName;
    private Integer userType;
    private Date birthday;
    private String email;
    private String telephone;
    private String address;
    private String postCode;
    private String otherCode;

    private Date lastLoginTime;
    private Integer loginCount;

    private Integer activationStatus;

    private String orgName;

    private String userTypeStr;
    private String idTypeStr;
}
