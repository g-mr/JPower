package com.wlcb.ylth.module.dbs.entity.corporate;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Corporate
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-02-27 02:28
 * @Version 1.0
 */
@Data
public class TblCsrrgLog implements Serializable {

    private static final long serialVersionUID = 2986524133916815265L;

    private String id;
    private String keyId;
    private String userId;
    private String name;
//    private String openid;
    private String content;
    private String tableName;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private Integer status;

}
