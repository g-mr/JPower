package top.jpower.jpower.service.role.impl;

import top.jpower.jpower.dbs.dao.role.TbCoreFunctionDao;
import top.jpower.jpower.dbs.dao.role.TbCoreRoleDao;
import top.jpower.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import top.jpower.jpower.dbs.dao.role.TbCoreRoleMenuDao;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreRoleMapper;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.dbs.entity.role.TbCoreRole;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleFunction;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleMenu;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.constants.ConstantsEnum;
import top.jpower.jpower.module.common.utils.constants.JpowerConstants;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.role.CoreRoleService;
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
    private final TbCoreRoleFunctionDao coreRoleFunctionDao;
    private final TbCoreFunctionDao coreFunctionDao;
    private final TbCoreRoleMenuDao coreRoleMenuDao;

    @Override
    public Boolean add(TbCoreRole coreRole) {
        if (coreRoleDao.save(coreRole)){
            List<String> functionIds = coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda().select(TbCoreFunction::getId).eq(TbCoreFunction::getParentId, JpowerConstants.TOP_CODE).ne(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue()), Fc::toStr);
            List<TbCoreRoleFunction> roleFunctions = new ArrayList<>();
            functionIds.forEach(functionId -> {
                TbCoreRoleFunction roleFunction = new TbCoreRoleFunction();
                roleFunction.setFunctionId(functionId);
                roleFunction.setRoleId(coreRole.getId());
                roleFunctions.add(roleFunction);
            });
            return coreRoleFunctionDao.saveBatch(roleFunctions);
        }
        return false;
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
        return coreRoleMenuDao.listObjs(Condition.<TbCoreRoleMenu>getQueryWrapper()
                .lambda()
                .select(TbCoreRoleMenu::getMenuId)
                .eq(TbCoreRoleMenu::getRoleId,roleId), Fc::toStr);
    }
}
