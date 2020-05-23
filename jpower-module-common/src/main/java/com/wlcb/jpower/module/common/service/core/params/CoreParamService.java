package com.wlcb.jpower.module.common.service.core.params;

import com.wlcb.jpower.module.dbs.entity.core.params.TbCoreParam;

import java.util.List;

/**
 * @author mr.gmac
 */
public interface CoreParamService {

    /**
     * @Author 郭丁志
     * @Description //TODO 通过code查询参数值
     * @Date 16:09 2020-05-06
     * @Param [key]
     * @return java.lang.String
     **/
    String selectByCode(String code);

    /**
     * @Author 郭丁志
     * @Description //TODO 系统参数列表
     * @Date 16:59 2020-05-07
     * @Param [coreParam]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.params.TbCoreParam>
     **/
    List<TbCoreParam> list(TbCoreParam coreParam);

    /**
     * @Author 郭丁志
     * @Description //TODO 删除系统参数
     * @Date 17:16 2020-05-07
     * @Param [id]
     * @return java.lang.Integer
     **/
    Integer delete(String id);

    /**
     * @Author 郭丁志
     * @Description //TODO 更新系统参数
     * @Date 17:20 2020-05-07
     * @Param [coreParam]
     * @return java.lang.Integer
     **/
    Integer update(TbCoreParam coreParam);

    /**
     * @Author 郭丁志
     * @Description //TODO 新增系统参数
     * @Date 10:17 2020-05-08
     * @Param [coreParam]
     * @return java.lang.Integer
     **/
    Integer add(TbCoreParam coreParam);

    /**
     * @Author 郭丁志
     * @Description //TODO 参数生效
     * @Date 10:23 2020-05-18
     * @Param
     * @return
     **/
    void effectAll();

}
