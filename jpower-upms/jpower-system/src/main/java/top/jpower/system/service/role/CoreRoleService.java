package top.jpower.system.service.role;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.dbs.entity.role.CoreRole;
import top.jpower.system.dbs.entity.role.TbCoreRole;

import java.util.List;

/**
 * 角色服务接口
 * 
 * @author mr.g
 */
public interface CoreRoleService extends BaseService<CoreRole> {

    /**
     * 新增角色
     * 
     * @author mr.g
     * @param coreRole 角色实体
     * @return 是否新增成功
     */
    Boolean add(TbCoreRole coreRole);

    /**
     * 根据批量id查询下级角色数量
     * 
     * @author mr.g
     * @param ids ID列表
     * @return 下级角色数量
     */
    long listByPids(List<Long> ids);

    /**
     * 保存顶部菜单关联信息
     * 
     * @author mr.g
     * @param roleId 角色ID
     * @param menuIds 顶部菜单ID列表
     * @return 是否保存成功
     */
    boolean saveTopMenu(Long roleId, List<Long> menuIds);

    /**
     * 角色关联的顶部菜单ID
     * 
     * @author mr.g
     * @param roleId 角色ID
     * @return 顶部菜单ID列表
     */
    List<Long> topMenuId(Long roleId);
}
