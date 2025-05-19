package top.jpower.jpower.dbs.dao.role.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleFunction;

import java.util.List;
import java.util.Map;

@Mapper
public interface TbCoreRoleFunctionMapper extends JpowerBaseMapper<TbCoreRoleFunction> {

    List<Map<String,Object>> selectRoleFunctionByRoleId(Long roleId);

    Integer insertList(@Param("roleFunctions") List<TbCoreRoleFunction> roleFunctions);

    List<Long> selectFunctionIdInRoleIds(@Param("list") List<Long> roleIdList);

}
