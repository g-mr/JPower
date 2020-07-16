package com.wlcb.jpower.module.common.service.core.user;

import com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction;

import java.util.List;

public interface CoreFunctionService {

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
    Integer delete(String ids);

    /**
     * @Author 郭丁志
     * @Description //TODO 修改菜单
     * @Date 11:19 2020-07-16
     * @Param [coreFunction]
     * @return java.lang.Integer
     **/
    Boolean update(TbCoreFunction coreFunction);
}
