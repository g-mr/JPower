package com.wlcb.jpower.service.role.impl;

import com.wlcb.jpower.dbs.dao.role.TbCoreRoleDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreRoleMenuDao;
import com.wlcb.jpower.dbs.dao.role.mapper.TbCoreRoleMapper;
import com.wlcb.jpower.dbs.entity.role.TbCoreRole;
import com.wlcb.jpower.dbs.entity.role.TbCoreRoleMenu;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.role.CoreRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mr.gmac
 */
@Service("coreRoleService")
@RequiredArgsConstructor
public class CoreRoleServiceImpl extends BaseServiceImpl<TbCoreRoleMapper, TbCoreRole> implements CoreRoleService {

    private final TbCoreRoleDao coreRoleDao;
    private final TbCoreRoleMenuDao coreRoleMenuDao;

    @Override
    public Boolean add(TbCoreRole coreRole) {
        return coreRoleDao.save(coreRole);
    }

    @Override
    public long listByPids(String ids) {
        return coreRoleDao.count(Condition.<TbCoreRole>getQueryWrapper().lambda().in(TbCoreRole::getParentId, Fc.toStrList(ids)).notIn(TbCoreRole::getId,Fc.toStrList(ids)));
    }

    @Override
    public Boolean update(TbCoreRole coreRole) {
        return coreRoleDao.updateById(coreRole);
    }

    @Override
    public boolean saveTopMenu(String roleId, List<String> menuIds) {

        coreRoleMenuDao.removeReal(Condition.<TbCoreRoleMenu>getQueryWrapper().lambda().eq(TbCoreRoleMenu::getRoleId,roleId));

        if (Fc.isEmpty(menuIds)){
            return true;
        }

        List<TbCoreRoleMenu> roleMenuList = new ArrayList<>();
        menuIds.forEach(id->{
            TbCoreRoleMenu roleMenu = new TbCoreRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(id);
            roleMenuList.add(roleMenu);
        });

        return coreRoleMenuDao.addBatchSomeColumn(roleMenuList);
    }

    @Override
    public List<String> topMenuId(String roleId) {
        return coreRoleMenuDao.listObjs(Condition.<TbCoreRoleMenu>getQueryWrapper().lambda().select(TbCoreRoleMenu::getMenuId).eq(TbCoreRoleMenu::getRoleId,roleId), Fc::toStr);
    }
}
