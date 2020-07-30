package com.wlcb.jpower.module.common.service.core.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.base.BaseService;
import com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction;
import com.wlcb.jpower.module.dbs.vo.FunctionVo;

import java.util.List;

public interface CoreFunctionService extends BaseService<TbCoreFunction> {

    /**
     * @Author 郭丁志
     * @Description //TODO 通过父级查询子节点菜单
     * @Date 15:33 2020-05-20
     * @Param [coreFunction]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction>
     **/
    List<TbCoreFunction> listByParent(TbCoreFunction coreFunction);

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
    Integer listByPids(String ids);

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
     * @Description //TODO 懒加载树形功能
     * @date 22:50 2020/7/26 0026
     * @param parentCode
     * @return java.util.List<com.wlcb.jpower.module.common.node.Node>
     */
    List<Node> lazyTree(String parentCode);

    /**
     * @author 郭丁志
     * @Description //TODO 懒加载角色所有功能
     * @date 23:06 2020/7/26 0026
     * @param parentCode
     * @param roleIds
     * @return java.util.List<com.wlcb.jpower.module.common.node.Node>
     */
    List<Node> lazyTreeByRole(String parentCode, String roleIds);

    /**
     * @Author 郭丁志
     * @Description //TODO 根据橘色获取所有的权限放到redis中
     * @Date 20:20 2020-07-28
     * @Param [roleIds]
     * @return void
     **/
    void putRedisAllFunctionByRoles(List<String> roleIds, Long expiresIn, String accessToken);

    /**
     * @Author 郭丁志
     * @Description //TODO 根据角色ID查询所有菜单
     * @Date 11:23 2020-07-30
     * @Param [roleIds]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction>
     **/
    List<TbCoreFunction> listMenuByRoleId(String roleIds);

    /**
     * @Author 郭丁志
     * @Description //TODO 根据角色，查询一个菜单code下的所有按钮
     * @Date 11:38 2020-07-30
     * @Param [roleIds, code]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction>
     **/
    List<TbCoreFunction> listBtnByRoleIdAndPcode(String roleIds, String code);

    /**
     * @Author 郭丁志
     * @Description //TODO 查询菜单list树形结构
     * @Date 21:49 2020-07-30
     * @Param [roleIds, functionVoClass]
     * @return java.util.List<com.wlcb.jpower.module.dbs.vo.FunctionVo>
     **/
    List<FunctionVo> listTreeByRoleId(String roleIds);
}
