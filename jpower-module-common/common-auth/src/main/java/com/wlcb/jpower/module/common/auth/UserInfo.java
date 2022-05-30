package com.wlcb.jpower.module.common.auth;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wlcb.jpower.module.common.utils.DateUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserInfo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-27 23:20
 * @Version 1.0
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** core_user表 **/
    public static final Integer TBALE_USER_TYPE_CORE = 0;
    /** 其他表 **/
    public static final Integer TBALE_USER_TYPE_BUSS = 1;
    /** 白名单 **/
    public static final Integer TBALE_USER_TYPE_WHILT = 2;

    @ApiModelProperty("租户CODE")
    private String tenantCode;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("客户端")
    private String clientCode;

    @ApiModelProperty("账号")
    private String loginId;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("第三方平台标识")
    private String otherCode;

    @ApiModelProperty("电话")
    private String telephone;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("部门ID")
    private String orgId;

    @ApiModelProperty("部门")
    private String orgName;

    @ApiModelProperty("邮编")
    private String postCode;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("出生日期")
    @JSONField(format="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DateUtil.PATTERN_DATE,locale = "zh_CN")
    private Date birthday;

    @ApiModelProperty("证件类型")
    private Integer idType;

    @ApiModelProperty("证件号码")
    private String idNo;

    @ApiModelProperty("登录次数")
    private Integer loginCount;

    @ApiModelProperty("最后登录时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DateUtil.PATTERN_DATETIME,locale = "zh_CN")
    private Date lastLoginTime;

    @ApiModelProperty("角色集合")
    private List<String> roleIds;

    @ApiModelProperty("子级部门ID")
    private List<String> childOrgId;

    @ApiModelProperty("用来表示是core_user表数据还是其他表映射的数据 0core_user系统表 1业务表 2白名单")
    private Integer isSysUser = TBALE_USER_TYPE_CORE;

    @ApiModelProperty("扩展属性")
    private Map<String,Object> info;

    public boolean isEmpty(){
        return Fc.allEmpty(userId,loginId);
    }
}
