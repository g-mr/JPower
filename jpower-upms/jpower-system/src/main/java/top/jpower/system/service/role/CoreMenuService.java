package top.jpower.system.service.role;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.dbs.entity.function.CoreTopMenu;

import java.util.List;
import java.util.Map;

/**
 * 顶部菜单服务接口
 * 
 * @author mr.g
 */
public interface CoreMenuService extends BaseService<CoreTopMenu> {

    /**
     * 查询顶部菜单关联的左侧一级菜单ID
     * 
     * @author mr.g
     * @param menuId 顶部菜单ID
     * @return 一级菜单ID列表
     */
    List<Long> listFunctionId(Long menuId);

    /**
     * 保存关联的一级菜单
     * 
     * @author mr.g
     * @param menuId 顶部菜单ID
     * @param functions 功能ID列表
     * @return 是否保存成功
     */
    boolean saveFunction(Long menuId, List<Long> functions);

    /**
     * 查询当前登录用户的顶部菜单
     * 
     * @author mr.g
     * @return 顶部菜单列表
     */
    List<Map<String,Object>> roleMenu();

    /**
     * 通过客户端ID查询登录用户的顶级菜单
     * 
     * @author mr.g
     * @param clientId 客户端ID
     * @return 顶级菜单列表
     */
    List<Map<String,Object>> selectList(Long clientId);
}
