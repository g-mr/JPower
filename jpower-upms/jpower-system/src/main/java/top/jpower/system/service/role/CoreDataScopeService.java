package top.jpower.system.service.role;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.api.dto.DataScopeDTO;
import top.jpower.system.dbs.entity.function.CoreDataScope;

import java.util.List;

/**
 * 数据权限业务
 * 
 * @author mr.g
 */
public interface CoreDataScopeService extends BaseService<CoreDataScope> {

    /**
     * 保存数据权限
     * 
     * @author mr.g
     * @param dataScope 数据权限bean
     * @return 是否保存成功
     */
    Long create(CoreDataScope dataScope);

    /**
     * 给角色设置数据权限
     * 
     * @author mr.g
     * @param roleId 角色ID
     * @param dataIds 数据权限ID列表
     * @return 是否设置成功
     */
    boolean roleDataScope(Long roleId, List<Long> dataIds);

    /**
     * 根据角色ID查询所有拥有权限的数据权限
     * 
     * @author mr.g
     * @param roleIds 角色ID列表
     * @param clientCode 客户端编码
     * @return 数据权限列表
     */
    List<DataScopeDTO> getDataScopeByRole(List<Long> roleIds, String clientCode);

    /**
     * 查询角色下一个菜单的数据权限
     * 
     * @author mr.g
     * @param roleIds 角色ID列表
     * @param menuCode 菜单编码
     * @return 数据权限列表
     */
    List<DataScopeDTO> getDataScopeByRoleAndMenu(List<Long> roleIds, String menuCode);
}
