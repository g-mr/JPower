package top.jpower.jpower.service.role.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.dbs.dao.role.TbCoreFunctionDao;
import top.jpower.jpower.dbs.dao.role.TbCoreRoleDao;
import top.jpower.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import top.jpower.jpower.dbs.dao.role.TbCoreRoleMenuDao;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreRoleMapper;
import top.jpower.jpower.dbs.entity.role.TbCoreRole;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleMenu;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.role.CoreRoleService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mr.gmac
 */
@Service("coreRoleService")
@RequiredArgsConstructor
public class CoreRoleServiceImpl extends BaseServiceImpl<TbCoreRoleMapper, TbCoreRole> implements CoreRoleService {

    private final TbCoreRoleDao coreRoleDao;
    private final TbCoreRoleFunctionDao coreRoleFunctionDao;
    private final TbCoreFunctionDao coreFunctionDao;
    private final TbCoreRoleMenuDao coreRoleMenuDao;

    @Override
    public Boolean add(TbCoreRole coreRole) {
        if (coreRoleDao.save(coreRole)){
            List<Long> functionIds = coreFunctionDao.queryIdByTopChild();
            return coreRoleFunctionDao.saveFunctions(functionIds,coreRole.getId());
        }
        return false;
    }

    @Override
    public long listByPids(List<Long> ids) {
        return coreRoleDao.count(Condition.<TbCoreRole>getQueryWrapper().lambda().in(TbCoreRole::getParentId, ids).notIn(TbCoreRole::getId,ids));
    }

    @Override
    public boolean saveTopMenu(Long roleId, List<Long> menuIds) {

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
    public List<Long> topMenuId(Long roleId) {
        return coreRoleMenuDao.listObjs(Condition.<TbCoreRoleMenu>getQueryWrapper()
                .lambda()
                .select(TbCoreRoleMenu::getMenuId)
                .eq(TbCoreRoleMenu::getRoleId,roleId), Fc::toLong);
    }
}
