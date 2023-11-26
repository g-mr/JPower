package com.wlcb.jpower.service.role.impl;

import com.wlcb.jpower.dbs.dao.client.TbCoreClientDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreFunctionMenuDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreTopMenuDao;
import com.wlcb.jpower.dbs.dao.role.mapper.TbCoreTopMenuMapper;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunctionMenu;
import com.wlcb.jpower.dbs.entity.function.TbCoreTopMenu;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ShieldUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.role.CoreMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 顶部菜单实现类
 *
 * @author mr.g
 * @date 2022/10/23 23:33
 */
@Service
@RequiredArgsConstructor
public class CoreMenuServiceImpl extends BaseServiceImpl<TbCoreTopMenuMapper, TbCoreTopMenu> implements CoreMenuService {

    private final TbCoreTopMenuDao menuDao;
    private final TbCoreFunctionMenuDao functionMenuDao;
    private final TbCoreClientDao clientDao;

    private final String ROLE_MENU_ID = "select menu_id from tb_core_role_menu where role_id in ({})";

    @Override
    public List<Long> listFunctionId(Long menuId) {
        return functionMenuDao.listObjs(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().select(TbCoreFunctionMenu::getFunctionId).eq(TbCoreFunctionMenu::getMenuId,menuId), Fc::toLong);
    }

    @Override
    public boolean saveFunction(Long menuId, List<Long> functions) {

        functionMenuDao.removeReal(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().eq(TbCoreFunctionMenu::getMenuId,menuId));

        if (Fc.isEmpty(functions)){
            return true;
        }

        List<TbCoreFunctionMenu> functionMenuList = new ArrayList<>();
        functions.forEach(id->{
            TbCoreFunctionMenu functionMenu = new TbCoreFunctionMenu();
            functionMenu.setFunctionId(id);
            functionMenu.setMenuId(menuId);
            functionMenuList.add(functionMenu);
        });

        return functionMenuDao.addBatchSomeColumn(functionMenuList);
    }

    @Override
    public List<Map<String, Object>> roleMenu() {
        JpowerAssert.notNull(ShieldUtil.getUserId(), JpowerError.Auth, "未登录");

        return menuDao.listMaps(Condition.<TbCoreTopMenu>getQueryWrapper().lambda()
                        .select(TbCoreTopMenu::getId,TbCoreTopMenu::getName,TbCoreTopMenu::getCode,TbCoreTopMenu::getIcon,TbCoreTopMenu::getRouter)
                        .eq(TbCoreTopMenu::getStatus, ConstantsEnum.YN01.Y.getValue())
                        .eq(TbCoreTopMenu::getClientId,clientDao.queryIdByCode(ShieldUtil.getClientCode()))
                        .inSql(!ShieldUtil.isRoot(),TbCoreTopMenu::getId, StringUtil.format(ROLE_MENU_ID, Fc.join(ShieldUtil.getUserRole()))).orderByAsc(TbCoreTopMenu::getSortNum));
    }

    @Override
    public List<Map<String, Object>> selectList(Long clientId) {
        JpowerAssert.notNull(ShieldUtil.getUserId(), JpowerError.Auth, "未登录");

        return menuDao.listMaps(Condition.<TbCoreTopMenu>getQueryWrapper().lambda()
                .select(TbCoreTopMenu::getId, TbCoreTopMenu::getName, TbCoreTopMenu::getClientId)
                .eq(Fc.notNull(clientId), TbCoreTopMenu::getClientId, clientId)
                .inSql(!ShieldUtil.isRoot(), TbCoreTopMenu::getId, StringUtil.format(ROLE_MENU_ID, Fc.join(ShieldUtil.getUserRole()))));
    }
}
