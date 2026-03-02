package top.jpower.system.service.role;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.dbs.entity.function.CoreTopMenu;
import top.jpower.system.vo.MenuClientVO;
import top.jpower.system.vo.MenuSelectVO;
import top.jpower.system.vo.MenuVO;

import java.util.List;

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
    List<MenuVO> roleMenu();

    /**
     * 通过客户端ID查询登录用户的顶级菜单
     * 
     * @author mr.g
     * @param clientId 客户端ID
     * @return 顶级菜单列表
     */
    List<MenuClientVO> selectList(Long clientId);

	/**
	 * 创建菜单
	 * @param topMenu 菜单信息
	 * @return 菜单ID
	 */
	Long create(CoreTopMenu topMenu);

	/**
	 * 修改菜单
	 * @param topMenu 菜单信息
	 * @return 是否成功
	 */
	boolean editById(CoreTopMenu topMenu);

	/**
	 * 修改菜单状态
	 * @param id 菜单ID
	 * @param status 状态
	 * @return 是否成功
	 */
	boolean updateStatusById(Long id, Boolean status);

	/**
	 * 客户端顶级菜单
	 * @return 客户端菜单列表
	 */
	List<MenuSelectVO> clientMenu();
}
