package top.jpower.jpower.service.role.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.jpower.dbs.dao.client.TbCoreClientDao;
import top.jpower.jpower.dbs.dao.role.TbCoreFunctionMenuDao;
import top.jpower.jpower.dbs.dao.role.TbCoreTopMenuDao;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreTopMenuMapper;
import top.jpower.jpower.dbs.entity.function.TbCoreFunctionMenu;
import top.jpower.jpower.dbs.entity.function.TbCoreTopMenu;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.handler.JpowerAssert;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.role.CoreMenuService;

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
                        .eq(TbCoreTopMenu::getStatus, YN01Enum.Y.getValue())
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
