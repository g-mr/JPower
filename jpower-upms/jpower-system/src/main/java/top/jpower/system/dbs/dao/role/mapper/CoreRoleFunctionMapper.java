package top.jpower.system.dbs.dao.role.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.system.dbs.entity.role.CoreRoleFunction;

import java.util.List;
import java.util.Map;

@Mapper
public interface CoreRoleFunctionMapper extends JpowerBaseMapper<CoreRoleFunction> {

    List<Map<String,Object>> selectRoleFunctionByRoleId(Long roleId);

    Integer insertList(@Param("roleFunctions") List<CoreRoleFunction> roleFunctions);

    List<Long> selectFunctionIdInRoleIds(@Param("list") List<Long> roleIdList);

}
