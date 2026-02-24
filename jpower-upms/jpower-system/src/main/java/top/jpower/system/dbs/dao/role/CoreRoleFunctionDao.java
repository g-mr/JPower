package top.jpower.system.dbs.dao.role;


import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.system.dbs.dao.role.mapper.CoreRoleFunctionMapper;
import top.jpower.system.dbs.entity.role.CoreRoleFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色功能数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreRoleFunctionDao extends JpowerServiceImpl<CoreRoleFunctionMapper, CoreRoleFunction> {

    /**
     * 保存权限
     *
     * @author mr.g
     * @param functionIds
     * @param roleId
     * @return java.lang.Boolean
     **/
    public Boolean saveFunctions(List<Long> functionIds, Long roleId) {
        List<CoreRoleFunction> roleFunctions = new ArrayList<>();
        functionIds.forEach(functionId -> {
			CoreRoleFunction roleFunction = new CoreRoleFunction();
            roleFunction.setFunctionId(functionId);
            roleFunction.setRoleId(roleId);
            roleFunctions.add(roleFunction);
        });
        return super.saveBatch(roleFunctions);
    }
}
