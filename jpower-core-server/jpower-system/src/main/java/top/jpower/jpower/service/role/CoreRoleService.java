package top.jpower.jpower.service.role;

import top.jpower.jpower.dbs.entity.role.TbCoreRole;
import top.jpower.jpower.module.common.service.BaseService;

import java.util.List;

/**
 * @author mr.gmac
 */
public interface CoreRoleService extends BaseService<TbCoreRole> {

    /**
     * @Author 郭丁志
     * @Description //TODO 新增角色
     * @Date 16:55 2020-05-19
     * @Param [coreRole]
     * @return java.lang.Integer
     **/
    Boolean add(TbCoreRole coreRole);

    /**
     * @Author 郭丁志
     * @Description //TODO 根据批量id查询下级角色数量
     * @Date 17:10 2020-05-19
     * @Param [ids]
     * @return java.lang.Integer
     **/
    long listByPids(List<Long> ids);

    /**
     * 保存顶部菜单关联信息
     *
     * @author mr.g
     * @param roleId 角色ID
     * @param menuIds 顶部菜单ID
     * @return boolean
     **/
    boolean saveTopMenu(Long roleId, List<Long> menuIds);

    /**
     * 角色关联的顶部菜单ID
     *
     * @author mr.g
     * @param roleId 角色ID
     * @return java.util.List<java.lang.String>
     **/
    List<Long> topMenuId(Long roleId);
}
