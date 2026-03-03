package top.jpower.system.service.role;

import cn.hutool.core.lang.tree.Tree;
import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.dbs.entity.function.CoreFunction;
import top.jpower.system.vo.DataFunctionVo;
import top.jpower.system.vo.FunctionSimpleVO;
import top.jpower.system.vo.FunctionVo;
import top.jpower.system.vo.SelectIdNameVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 功能服务接口
 * 
 * @author mr.g
 */
public interface CoreFunctionService extends BaseService<CoreFunction> {

    /**
     * 查询功能层级树
     * 
     * @author mr.g
     * @param userRole 用户角色列表
     * @param clientId 客户端ID
     * @return 功能层级树列表
     */
    List<Tree<Long>> treeMenuTypeByClientId(List<Long> userRole, Long clientId);

    /**
     * map查询
     * 
     * @author mr.g
     * @param map 查询条件
     * @return 功能列表
     */
    List<FunctionVo> listFunction(Map<String,Object> map);

    /**
     * 通过code查询菜单
     * 
     * @author mr.g
     * @param coreFunction 菜单
     * @return 菜单实体
     */
    Long create(CoreFunction coreFunction);

    /**
     * 删除菜单
     * 
     * @author mr.g
     * @param ids 要删除的菜单ID列表
     * @return 是否删除成功
     */
    Boolean delete(List<Long> ids);

    /**
     * 修改菜单
     * 
     * @author mr.g
     * @param coreFunction 菜单实体
     * @return 是否修改成功
     */
    Boolean update(CoreFunction coreFunction);

    /**
     * 查询角色所有权限ID
     * 
     * @author mr.g
     * @param roleIds 角色ID列表
     * @return 权限ID集合
     */
    Set<Long> queryUrlIdByRole(List<Long> roleIds);

    /**
     * 懒加载角色所有功能
     * 
     * @author mr.g
     * @param parentId 父ID
     * @param roleIds 角色ID列表
     * @return 功能树列表
     */
    List<Tree<Long>> lazyTreeByRole(Long parentId, List<Long> roleIds);

    /**
     * 根据角色ID查询所有菜单
     * 
     * @author mr.g
     * @param roleIds 角色ID列表
     * @param clientCode 客户端编码
     * @param topMenuId 顶部菜单ID
     * @param isHide 是否去除隐藏的菜单
     * @return 菜单列表
     */
	List<Tree<Long>> listMenuByRoleId(List<Long> roleIds, String clientCode, Long topMenuId, boolean isHide);

    /**
     * 根据角色，查询一个角色下的所有可用按钮
     * 
     * @author mr.g
     * @param roleIds 角色ID列表
     * @return 按钮列表
     */
    List<String> listBtnByRoleId(List<Long> roleIds);

    /**
     * 查询菜单list树形结构
     * 
     * @author mr.g
     * @param roleIds 角色ID列表
     * @return 功能树列表
     */
    List<Tree<Long>> listTreeByRoleId(List<Long> roleIds);

    List<String> getUrlsByRoleIds(List<Long> roleIds, String clientCode);

    /**
     * 查询树形菜单
     * 
     * @author mr.g
     * @param roleIds 角色ID列表
     * @param clientId 客户端ID
     * @param topMenuId 顶部菜单ID
     * @return 菜单树列表
     */
    List<Tree<Long>> menuTreeByRoleIds(List<Long> roleIds,Long clientId, Long topMenuId);

    /**
     * 查询角色所有菜单
     * 
     * @author mr.g
     * @param roleIds 角色ID列表
     * @return 菜单列表
     */
	List<Tree<Long>> treeClientMenu(List<Long> roleIds);

    /**
     * 查询接口按钮
     * 
     * @author mr.g
     * @param roleIds 角色ID列表
     * @param id 菜单ID
     * @param clientId 客户端ID
     * @return 按钮树列表
     */
    List<Tree<Long>> treeButByMenu(List<Long> roleIds, Long id, Long clientId);

    /**
     * 生成功能点
     * 
     * @author mr.g
     * @return 是否生成成功
     */
    boolean generateFunction();

    /**
     * 保存层级
     * 
     * @author mr.g
     * @param parentId 上级ID
     * @param ids 主键列表
     * @return 是否保存成功
     */
    boolean saveHierarchy(Long parentId, List<Long> ids);

    /**
     * 查询菜单列表
     * 
     * @author mr.g
     * @param map 查询条件
     * @return 数据功能列表
     */
    List<DataFunctionVo> listDataFunction(Map<String, Object> map);

    /**
     * 客户端下的接口资源
     * 
     * @author mr.g
     * @param roleIds 角色ID列表
     * @param clientId 客户端ID
     * @return 接口资源列表
     */
    List<FunctionSimpleVO> listInterface(List<Long> roleIds, Long clientId);

	/**
     * 根据客户端查询功能
     *
     * @author mr.g
     * @param clientId 客户端ID
     * @return 功能列表
     */
    List<SelectIdNameVO> selectByClientId(Long clientId);
}
