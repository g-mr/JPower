package com.wlcb.jpower.module.dbs.entity.core.client;

import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

/**
 * @ClassName TbCoreClient
 * @Description TODO 客户端表
 * @Author 郭丁志
 * @Date 2020-07-31 12:55
 * @Version 1.0
 */
@Data
public class TbCoreClient extends BaseEntity {

    private String name;
    private String roleIds;
    private String clientCode;
    private String clientSecret;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String sortNum;
    private String note;

}
