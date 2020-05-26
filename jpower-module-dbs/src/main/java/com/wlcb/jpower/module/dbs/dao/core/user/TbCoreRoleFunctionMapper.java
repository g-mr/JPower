package com.wlcb.jpower.module.dbs.dao.core.user;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("tbCoreRoleFunctionMapper")
public interface TbCoreRoleFunctionMapper extends BaseMapper<TbCoreRoleFunction> {

    TbCoreRoleFunction selectRoleFunctionByRoleId(String roleId);

    Integer insertList(@Param("roleFunctions") List<TbCoreRoleFunction> roleFunctions);
}
