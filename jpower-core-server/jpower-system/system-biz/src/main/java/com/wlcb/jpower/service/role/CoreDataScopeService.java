package com.wlcb.jpower.service.role;

import com.wlcb.jpower.dbs.entity.function.TbCoreDataScope;
import com.wlcb.jpower.module.common.service.BaseService;

/**
 * @author ding
 * @description 数据权限业务
 * @date 2020-11-03 15:01
 */
public interface CoreDataScopeService extends BaseService<TbCoreDataScope> {

    /**
     * 保存数据权限
     * @Author ding
     * @Date 10:48 2020-11-04
     * @param dataScope 数据权限bean
     * @return boolean
     **/
    @Override
    boolean save(TbCoreDataScope dataScope);

    /**
     * 给角色设置数据权限
     * @Author ding
     * @Date 10:49 2020-11-04
     * @param roleId
     * @param dataIds
     * @return boolean
     **/
    boolean roleDataScope(String roleId, String dataIds);
}
