package com.wlcb.jpower.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ParamDto
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/9/2 0002 22:39
 * @Version 1.0
 */
@Data
public class ParamDto implements Serializable {

    private String id;
    private String code;
    private String name;
    private String value;
    private Integer isEffect;
    private String isEffectStr;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
}
