package com.wlcb.jpower.dbs.dao.role.mapper;

import com.wlcb.jpower.dbs.entity.role.TbCoreRoleFunction;
import com.wlcb.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("tbCoreRoleFunctionMapper")
public interface TbCoreRoleFunctionMapper extends JpowerBaseMapper<TbCoreRoleFunction> {

    List<Map<String,Object>> selectRoleFunctionByRoleId(String roleId);

    Integer insertList(@Param("roleFunctions") List<TbCoreRoleFunction> roleFunctions);

    List<String> selectFunctionIdInRoleIds(@Param("list") List<String> roleIdList);

}
