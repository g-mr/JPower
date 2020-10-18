package com.wlcb.jpower.dbs.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlcb.jpower.dbs.entity.TbCoreUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
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
     * @Description //TODO 查询用户所属所有的角色ID
     * @date 0:14 2020/6/10 0010
     * @param userId
     * @return java.util.List<java.lang.String>
     */
    List<String> selectRoleIdByUserId(String userId);
}
