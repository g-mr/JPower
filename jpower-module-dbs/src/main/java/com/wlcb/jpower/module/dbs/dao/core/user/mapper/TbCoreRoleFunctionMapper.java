package com.wlcb.jpower.module.dbs.dao.core.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("tbCoreRoleFunctionMapper")
public interface TbCoreRoleFunctionMapper extends BaseMapper<TbCoreRoleFunction> {

    TbCoreRoleFunction selectRoleFunctionByRoleId(String roleId);

    Integer insertList(@Param("roleFunctions") List<TbCoreRoleFunction> roleFunctions);

    List<String> selectFunctionIdInRoleIds(@Param("list") List<String> roleIdList);
}