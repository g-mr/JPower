package com.wlcb.jpower.module.common.auth;

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

    /**
     * 用户基础信息
     */
    private String userId;

    /**
     * 客户端id
     *
     * 不用管，在获取当前登录用户的时候会自动获取到
     */
    private String clientCode;

    /**
     * 账号
     */
    private String loginId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 第三方平台标识
     */
    private String otherCode;

    /**
     * 电话
     **/
    private String telephone;

    /**
     * 用户类型
     **/
    private Integer userType;

    /**
     * 部门ID
     **/
    private String orgId;

    /**
     * 角色集合
     */
    private List<String> roleIds;

    /** 用来表示是core_user表数据还是其他表映射的数据 0core_user系统表 1业务表 2白名单 **/
    private Integer isSysUser = TBALE_USER_TYPE_CORE;
}
