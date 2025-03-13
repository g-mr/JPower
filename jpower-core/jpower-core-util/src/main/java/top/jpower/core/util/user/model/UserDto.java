package top.jpower.core.util.user.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author mr.g
 * @date 2024-8-14 23:20
 * @description
 */
@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 5921415988154507508L;

    /**
     * 用户ID
     **/
    private Long userId;
    /**
     * 姓名
     **/
    private String userName;
    /**
     * 客户端
     **/
    private String clientCode;
    /**
     * 租户编码
     **/
    private String tenantCode;
    /**
     * 部门ID
     **/
    private Long orgId;
    /**
     * 子级部门ID
     **/
    private List<Long> childOrgId;
    /**
     * 角色集合
     **/
    private List<Long> roleIds;

    /**
     * 是否超级用户
     **/
    private boolean isRoot = Boolean.FALSE;
    /**
     * 用来表示是否是系统用户
     *
     * 0：系统用户
     * 1：业务用户
     * 2：白名单
     **/
    private Integer isSysUser = 1;

}
