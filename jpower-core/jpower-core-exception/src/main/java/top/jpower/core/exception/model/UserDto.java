package top.jpower.core.exception.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mr.g
 * @date 2024-8-14 23:20
 * @description
 */
@Data
public class UserDto implements Serializable {

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
     * 用来表示是core_user表数据还是其他表映射的数据 0core_user系统表 1业务表 2白名单
     **/
    private Integer isSysUser = 1;

}
