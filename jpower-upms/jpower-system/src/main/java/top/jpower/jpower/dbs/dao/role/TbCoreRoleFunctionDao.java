package top.jpower.jpower.dbs.dao.role;


import org.springframework.stereotype.Repository;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreRoleFunctionMapper;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleFunction;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mr.gmac
 */
@Repository
public class TbCoreRoleFunctionDao extends JpowerServiceImpl<TbCoreRoleFunctionMapper, TbCoreRoleFunction> {

    /**
     * 保存权限
     *
     * @author mr.g
     * @param functionIds
     * @param roleId
     * @return java.lang.Boolean
     **/
    public Boolean saveFunctions(List<Long> functionIds, Long roleId) {
        List<TbCoreRoleFunction> roleFunctions = new ArrayList<>();
        functionIds.forEach(functionId -> {
            TbCoreRoleFunction roleFunction = new TbCoreRoleFunction();
            roleFunction.setFunctionId(functionId);
            roleFunction.setRoleId(roleId);
            roleFunctions.add(roleFunction);
        });
        return super.saveBatch(roleFunctions);
    }
}
