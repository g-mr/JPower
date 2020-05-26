package com.wlcb.jpower.module.dbs.dao.core.user;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("tbCoreUserRoleMapper")
public interface TbCoreUserRoleMapper extends BaseMapper<TbCoreUserRole> {

    /**
     * @author 郭丁志
     * @Description //TODO 批量新增用户角色
     * @date 0:55 2020/5/25 0025
     * @param userRoles
     * @return java.lang.Integer
     */
    Integer insertList(@Param("userRoles") List<TbCoreUserRole> userRoles);

    /**
     * @author 郭丁志
     * @Description //TODO 查询用户所有角色
     * @date 22:50 2020/5/26 0026
     * @param userId 用户ID
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.role.TbCoreUserRole>
     */
    List<TbCoreUserRole> selectUserRoleByUserId(String userId);
}
