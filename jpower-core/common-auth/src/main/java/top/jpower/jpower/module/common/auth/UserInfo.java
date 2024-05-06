package top.jpower.jpower.module.common.auth;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.jpower.core.util.utils.Fc;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户登陆信息
 *
 * @author mr.g
 **/
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** core_user表 **/
    public static final Integer TABLE_USER_TYPE_CORE = 0;
    /** 其他表 **/
    public static final Integer TABLE_USER_TYPE_BUSS = 1;
    /** 白名单 **/
    public static final Integer TABLE_USER_TYPE_WHILT = 2;

    /**
     * 租户CODE
     **/
    private String tenantCode;

    /**
     * 用户ID
     **/
    private Long userId;

    /**
     * 客户端
     **/
    private String clientCode;

    /**
     * 账号
     **/
    private String loginId;

    /**
     * 头像
     **/
    private String avatar;

    /**
     * 姓名
     **/
    private String userName;

    /**
     * 昵称
     **/
    private String nickName;

    /**
     * 第三方平台标识
     **/
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
    private Long orgId;

    /**
     * 部门
     **/
    private String orgName;

    /**
     * 邮编
     **/
    private String postCode;

    /**
     * 邮箱
     **/
    private String email;

    /**
     * 地址
     **/
    private String address;

    /**
     * 出生日期
     **/
    @JSONField(format="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATE_PATTERN,locale = "zh_CN")
    private Date birthday;

    /**
     * 证件类型
     **/
    private Integer idType;

    /**
     * 证件号码
     **/
    private String idNo;

    /**
     * 登录次数
     **/
    private Integer loginCount;

    /**
     * 最后登录时间
     **/
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    private Date lastLoginTime;

    /**
     * 角色集合
     **/
    private List<Long> roleIds;

    /**
     * 子级部门ID
     **/
    private List<Long> childOrgId;

    /**
     * 用来表示是core_user表数据还是其他表映射的数据 0core_user系统表 1业务表 2白名单
     **/
    private Integer isSysUser = TABLE_USER_TYPE_CORE;

    /**
     * 扩展属性
     **/
    private Map<String,Object> info;

    public boolean isEmpty(){
        return Fc.allEmpty(userId,loginId);
    }
}
