package com.wlcb.jpower.service.role;

import cn.hutool.core.lang.tree.Tree;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.module.common.service.BaseService;
import com.wlcb.jpower.vo.FunctionVo;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
public interface CoreFunctionService extends BaseService<TbCoreFunction> {

    /**
     * @Author 郭丁志
     * @Description //TODO map查询
     * @Date 15:33 2020-05-20
     * @Param [coreFunction]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction>
     **/
    List<FunctionVo> listFunction(Map<String,Object> coreFunction);

    /**
     * @Author 郭丁志
     * @Description //TODO 通过code查询菜单
     * @Date 15:34 2020-05-20
     * @Param [code]
     * @return com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction
     **/
    TbCoreFunction selectFunctionByCode(String code);

    /**
     * @Author 郭丁志
     * @Description //TODO 通过url查询菜单
     * @Date 15:35 2020-05-20
     * @Param [url]
     * @return com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction
     **/
    TbCoreFunction selectFunctionByUrl(String url);

    /**
     * @Author 郭丁志
     * @Description //TODO 新增菜单
     * @Date 15:35 2020-05-20
     * @Param [coreFunction]
     * @return java.lang.Integer
     **/
    Boolean add(TbCoreFunction coreFunction);

    /**
     * @Author 郭丁志
     * @Description //TODO 批量查询id下的子节点数量
     * @Date 15:40 2020-05-20
     * @Param [ids]
     * @return java.lang.Integer
     **/
    long listByPids(String ids);

    /**
     * @Author 郭丁志
     * @Description //TODO 删除菜单
     * @Date 15:43 2020-05-20
     * @Param [ids]
     * @return java.lang.Integer
     **/
    Boolean delete(String ids);

    /**
     * @Author 郭丁志
     * @Description //TODO 修改菜单
     * @Date 11:19 2020-07-16
     * @Param [coreFunction]
     * @return java.lang.Integer
     **/
    Boolean update(TbCoreFunction coreFunction);

    /**
     * @author 郭丁志
     * @Description //TODO 查询角色所有权限ID
     * @date 22:50 2020/7/26 0026
     * @param roleIds
     * @return java.util.List<com.wlcb.jpower.module.common.node.Node>
     */
    List<String> queryUrlIdByRole(String roleIds);

    /**
     * @author 郭丁志
     * @Description //TODO 懒加载角色所有功能
     * @date 23:06 2020/7/26 0026
     * @param parentId
     * @param roleIds
     * @return java.util.List<com.wlcb.jpower.module.common.node.Node>
     */
    List<Tree<String>> lazyTreeByRole(String parentId, List<String> roleIds);

    /**
     * @Author 郭丁志
     * @Description //TODO 根据角色ID查询所有菜单
     * @Date 11:23 2020-07-30
     * @Param [roleIds]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction>
     **/
    List<TbCoreFunction> listMenuByRoleId(List<String> roleIds);

    /**
     * @Author 郭丁志
     * @Description //TODO 根据角色，查询一个菜单code下的所有可用按钮
     * @Date 11:38 2020-07-30
     * @Param [roleIds, code]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction>
     **/
    List<TbCoreFunction> listBtnByRoleIdAndPcode(List<String> roleIds, String id);

    /**
     * @Author 郭丁志
     * @Description //TODO 查询菜单list树形结构
     * @Date 21:49 2020-07-30
     * @Param [roleIds, functionVoClass]
     * @return java.util.List<com.wlcb.jpower.module.dbs.vo.FunctionVo>
     **/
    List<Tree<String>> listTreeByRoleId(List<String> roleIds);

    long queryRoleByUrl(String url);

    List<Object> getUrlsByRoleIds(List<String> roleIds);

    /**
     * 查询树形菜单
     * @Author ding
     * @Date 00:46 2021-02-27
     * @param roleIds 角色ID
     * @return java.util.List<com.wlcb.jpower.module.common.node.Node>
     **/
    List<Tree<String>> menuTreeByRoleIds(List<String> roleIds);

    /**
     * 查询资源
     * @Author ding
     * @Date 00:47 2021-02-27
     * @param roleIds 角色ID
     * @param id 菜单ID
     * @return java.util.List<com.wlcb.jpower.dbs.entity.function.TbCoreFunction>
     **/
    List<TbCoreFunction> listButByMenu(List<String> roleIds, String id);
}
