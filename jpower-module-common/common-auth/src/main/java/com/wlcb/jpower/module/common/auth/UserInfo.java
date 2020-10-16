package com.wlcb.jpower.module.common.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName UserInfo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-27 23:20
 * @Version 1.0
 */
@Data
public class UserInfo {

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
    private String userCode;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("客户端")
    private String clientCode;

    @ApiModelProperty("账号")
    private String loginId;

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

    @ApiModelProperty("角色集合")
    private List<String> roleIds;

    @ApiModelProperty("用来表示是core_user表数据还是其他表映射的数据 0core_user系统表 1业务表 2白名单")
    private Integer isSysUser = TBALE_USER_TYPE_CORE;
}
