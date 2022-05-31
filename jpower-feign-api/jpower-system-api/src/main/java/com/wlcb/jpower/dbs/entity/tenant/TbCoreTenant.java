package com.wlcb.jpower.dbs.entity.tenant;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wlcb.jpower.module.common.utils.DateUtil;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName TbCoreTenant
 * @Description TODO 租户
 * @Author 郭丁志
 * @Date 2020-10-23 10:07
 * @Version 1.0
 */
@Data
public class TbCoreTenant extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "租户Code",hidden = true)
    private String tenantCode;
    @ApiModelProperty(value = "租户名称")
    private String tenantName;
    @ApiModelProperty(value = "域名地址")
    private String domain;
    @ApiModelProperty(value = "租户LOGO")
    private String logo;
    @ApiModelProperty(value = "联系人")
    private String contactName;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "联系地址")
    private String address;
    @ApiModelProperty(value = "账号额度")
    private Integer accountNumber;
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DateUtil.PATTERN_DATETIME,locale = "zh_CN")
    @JSONField(format= DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "过期时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date expireTime;
    @ApiModelProperty(value = "授权码",hidden = true)
    private String licenseKey;

}
