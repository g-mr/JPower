package com.wlcb.jpower.service.role;

import com.wlcb.jpower.dbs.entity.function.TbCoreTopMenu;
import com.wlcb.jpower.module.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2022/10/23 23:31
 */
public interface CoreMenuService extends BaseService<TbCoreTopMenu> {

    /**
     * 查询顶部菜单关联的左侧一级菜单ID
     *
     * @author mr.g
     * @param menuId 顶部菜单ID
     * @return java.util.List<java.lang.String>
     **/
    List<String> listFunctionId(String menuId);

    /**
     * 保存关联的一级菜单
     *
     * @author mr.g
     * @param menuId 顶部菜单ID
     * @param functions 功能ID
     * @return boolean
     **/
    boolean saveFunction(String menuId, List<String> functions);

    /**
     * 查询当前登录用户的顶部菜单
     *
     * @author mr.g
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> roleMenu();

    /**
     * 通过客户端ID查询登录用户的顶级菜单
     *
     * @author mr.g
     * @param clientId
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> selectList(String clientId);
}
