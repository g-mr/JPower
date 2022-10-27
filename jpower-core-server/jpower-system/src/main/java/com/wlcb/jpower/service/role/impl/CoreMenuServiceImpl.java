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
import com.wlcb.jpower.module.common.utils.constants.StringPool;
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

    @Override
    public List<String> listFunctionId(String menuId) {
        return functionMenuDao.listObjs(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().select(TbCoreFunctionMenu::getFunctionId).eq(TbCoreFunctionMenu::getMenuId,menuId), Fc::toStr);
    }

    @Override
    public boolean saveFunction(String menuId, List<String> functions) {

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
        JpowerAssert.notEmpty(ShieldUtil.getUserId(), JpowerError.Auth, "未登录");

        String sql = StringUtil.format("select menu_id from tb_core_role_menu where role_id in ({})", StringPool.SINGLE_QUOTE.concat(Fc.join(ShieldUtil.getUserRole(),StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE));
        return menuDao.listMaps(Condition.<TbCoreTopMenu>getQueryWrapper().lambda()
                        .select(TbCoreTopMenu::getId,TbCoreTopMenu::getName,TbCoreTopMenu::getCode,TbCoreTopMenu::getIcon,TbCoreTopMenu::getRouter)
                        .eq(TbCoreTopMenu::getStatus, ConstantsEnum.YN01.Y.getValue())
                        .eq(TbCoreTopMenu::getClientId,clientDao.queryIdByCode(ShieldUtil.getClientCode()))
                        .inSql(!ShieldUtil.isRoot(),TbCoreTopMenu::getId,sql).orderByAsc(TbCoreTopMenu::getSortNum));
    }
}
