package com.wlcb.jpower.service.role;

import com.wlcb.jpower.dbs.entity.function.TbCoreDataScope;
import com.wlcb.jpower.module.common.service.BaseService;

import java.util.List;

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

    /**
     * 查询所有角色都可执行得数据权限
     *
     * @author 郭丁志
     * @date 23:36 2020/11/5 0005
     */
    List<TbCoreDataScope> getAllRoleDataScope();

    /**
     * 根据角色ID查询数据权限
     *
     * @author 郭丁志
     * @date 23:42 2020/11/5 0005
     * @param roleIds 角色ID
     * @return java.util.List<com.wlcb.jpower.dbs.entity.function.TbCoreDataScope>
     */
    List<TbCoreDataScope> getDataScopeByRole(List<String> roleIds);
}
