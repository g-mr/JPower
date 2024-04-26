package top.jpower.jpower.dbs.dao.role.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleFunction;
import top.jpower.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;

import java.util.List;
import java.util.Map;

@Component("tbCoreRoleFunctionMapper")
public interface TbCoreRoleFunctionMapper extends JpowerBaseMapper<TbCoreRoleFunction> {

    List<Map<String,Object>> selectRoleFunctionByRoleId(Long roleId);

    Integer insertList(@Param("roleFunctions") List<TbCoreRoleFunction> roleFunctions);

    List<Long> selectFunctionIdInRoleIds(@Param("list") List<Long> roleIdList);

}
