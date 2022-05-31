package com.wlcb.jpower.service.params;

import com.wlcb.jpower.dbs.entity.params.TbCoreParam;
import com.wlcb.jpower.module.common.service.BaseService;

/**
 * @author mr.gmac
 */
public interface CoreParamService extends BaseService<TbCoreParam> {

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
     * @Description //TODO 删除系统参数
     * @Date 17:16 2020-05-07
     * @Param [id]
     * @return java.lang.Integer
     **/
    Boolean deletes(String id);

    /**
     * @Author 郭丁志
     * @Description //TODO 更新系统参数
     * @Date 17:20 2020-05-07
     * @Param [coreParam]
     * @return java.lang.Integer
     **/
    Boolean update(TbCoreParam coreParam);

    /**
     * @Author 郭丁志
     * @Description //TODO 参数生效
     * @Date 10:23 2020-05-18
     * @Param
     * @return
     **/
    void effectAll();

}
