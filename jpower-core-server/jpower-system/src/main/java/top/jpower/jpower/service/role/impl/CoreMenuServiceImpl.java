package top.jpower.jpower.service.role.impl;

import top.jpower.jpower.dbs.dao.client.TbCoreClientDao;
import top.jpower.jpower.dbs.dao.role.TbCoreFunctionMenuDao;
import top.jpower.jpower.dbs.dao.role.TbCoreTopMenuDao;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreTopMenuMapper;
import top.jpower.jpower.dbs.entity.function.TbCoreFunctionMenu;
import top.jpower.jpower.dbs.entity.function.TbCoreTopMenu;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.common.utils.StringUtil;
import top.jpower.jpower.module.common.utils.constants.ConstantsEnum;
import top.jpower.jpower.module.common.utils.constants.StringPool;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.role.CoreMenuService;
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

        String sql = StringUtil.format(ROLE_MENU_ID, StringPool.SINGLE_QUOTE.concat(Fc.join(ShieldUtil.getUserRole(),StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE));
        return menuDao.listMaps(Condition.<TbCoreTopMenu>getQueryWrapper().lambda()
                        .select(TbCoreTopMenu::getId,TbCoreTopMenu::getName,TbCoreTopMenu::getCode,TbCoreTopMenu::getIcon,TbCoreTopMenu::getRouter)
                        .eq(TbCoreTopMenu::getStatus, ConstantsEnum.YN01.Y.getValue())
                        .eq(TbCoreTopMenu::getClientId,clientDao.queryIdByCode(ShieldUtil.getClientCode()))
                        .inSql(!ShieldUtil.isRoot(),TbCoreTopMenu::getId,sql).orderByAsc(TbCoreTopMenu::getSortNum));
    }

    @Override
    public List<Map<String, Object>> selectList(String clientId) {
        JpowerAssert.notEmpty(ShieldUtil.getUserId(), JpowerError.Auth, "未登录");

        String sql = StringUtil.format(ROLE_MENU_ID, StringPool.SINGLE_QUOTE.concat(Fc.join(ShieldUtil.getUserRole(),StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE));

        return menuDao.listMaps(Condition.<TbCoreTopMenu>getQueryWrapper().lambda()
                .select(TbCoreTopMenu::getId, TbCoreTopMenu::getName, TbCoreTopMenu::getClientId)
                .eq(Fc.isNotBlank(clientId), TbCoreTopMenu::getClientId, clientId)
                .inSql(!ShieldUtil.isRoot(), TbCoreTopMenu::getId, sql));
    }
}
