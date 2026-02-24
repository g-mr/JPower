package top.jpower.system.service.role;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.dbs.entity.role.CoreRoleFunction;

import java.util.List;
import java.util.Map;

/**
 * 角色功能服务接口
 * 
 * @author mr.g
 */
public interface CoreRoleFunctionService extends BaseService<CoreRoleFunction> {

    /**
     * 通过角色ID查询权限菜单
     * 
     * @author mr.g
     * @param roleId 角色ID
     * @return 角色功能映射列表
     */
    List<Map<String,Object>> selectRoleFunctionByRoleId(Long roleId);

    /**
     * 新增角色权限
     * 
     * @author mr.g
     * @param roleId 角色ID
     * @param functionIds 功能ID列表
     * @param isAutoSaveInterface 是否自动保存接口
     * @return 是否新增成功
     */
    boolean addRoleFunctions(Long roleId, List<Long> functionIds, boolean isAutoSaveInterface);

}
