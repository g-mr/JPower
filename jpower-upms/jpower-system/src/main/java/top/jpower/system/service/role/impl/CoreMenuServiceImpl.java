package top.jpower.system.service.role.impl;

import cn.hutool.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.dao.client.CoreClientDao;
import top.jpower.system.dbs.dao.role.CoreFunctionMenuDao;
import top.jpower.system.dbs.dao.role.CoreTopMenuDao;
import top.jpower.system.dbs.dao.role.mapper.CoreTopMenuMapper;
import top.jpower.system.dbs.entity.client.CoreClient;
import top.jpower.system.dbs.entity.function.CoreFunctionMenu;
import top.jpower.system.dbs.entity.function.CoreTopMenu;
import top.jpower.system.service.role.CoreMenuService;
import top.jpower.system.vo.MenuClientVO;
import top.jpower.system.vo.MenuSelectVO;
import top.jpower.system.vo.MenuVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static top.jpower.common.constants.ServiceCodeConstants.CODE_EXIST;
import static top.jpower.common.constants.ServiceCodeConstants.NOT_FOUND_CLIENT;
import static top.jpower.common.constants.ServiceCodeConstants.NOT_LOGIN;

/**
 * 顶部菜单实现类
 * 
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class CoreMenuServiceImpl extends BaseServiceImpl<CoreTopMenuMapper, CoreTopMenu> implements CoreMenuService {

    private final CoreTopMenuDao menuDao;
    private final CoreFunctionMenuDao functionMenuDao;
    private final CoreClientDao clientDao;

    private final String ROLE_MENU_ID = "select menu_id from tb_core_role_menu where role_id in ({})";

    @Override
    public List<Long> listFunctionId(Long menuId) {
        return functionMenuDao.listFunctionId(menuId);
    }

    @Override
    public boolean saveFunction(Long menuId, List<Long> functions) {
        functionMenuDao.removeByMenuId(menuId);

        if (Fc.isEmpty(functions)){
            return true;
        }

        List<CoreFunctionMenu> functionMenuList = new ArrayList<>();
        functions.forEach(id->{
			CoreFunctionMenu functionMenu = new CoreFunctionMenu();
            functionMenu.setFunctionId(id);
            functionMenu.setMenuId(menuId);
            functionMenuList.add(functionMenu);
        });
        return functionMenuDao.addBatchSomeColumn(functionMenuList);
    }

    @Override
    public List<MenuVO> roleMenu() {
        JpowerAssert.notNull(ShieldUtil.getUserId(), JpowerError.Auth, NOT_LOGIN);

		Optional<Long> clientId = clientDao.queryIdByCode(ShieldUtil.getClientCode());
		return menuDao.listMenuByClientIdRoleId(clientId.orElseThrow(()->new JpowerException(JpowerError.NotFind.getCode(), NOT_FOUND_CLIENT)), ShieldUtil.isRoot() ? null : ShieldUtil.getUserRole());
    }

    @Override
    public List<MenuClientVO> selectList(Long clientId) {
        JpowerAssert.notNull(ShieldUtil.getUserId(), JpowerError.Auth, NOT_LOGIN);
        return menuDao.selectList(clientId, ShieldUtil.isRoot() ? null : ShieldUtil.getUserRole());
    }

	@Override
	public Long create(CoreTopMenu topMenu) {
		topMenu.setId(null);
		JpowerAssert.notTrue(menuDao.existsByField(CoreTopMenu::getCode,topMenu.getCode()),JpowerError.Business, CODE_EXIST);

		topMenu.setStatus(Fc.toBoolean(topMenu.getStatus(), Boolean.TRUE));
		topMenu.setSortNum(Fc.toInt(topMenu.getSortNum(), 1));
		menuDao.save(topMenu);
		return topMenu.getId();
	}

	@Override
	public boolean editById(CoreTopMenu topMenu) {
		JpowerAssert.notTrue(menuDao.existsByCodeNeId(topMenu.getCode(), topMenu.getId()), JpowerError.Business, CODE_EXIST);
		return menuDao.updateAllById(topMenu);
	}

	@Override
	public boolean updateStatusById(Long id, Boolean status) {
		return menuDao.switchById(id, status);
	}

	@Override
	public List<MenuSelectVO> clientMenu() {
		List<MenuClientVO> menuList = selectList(null);
		List<CoreClient> coreClients = clientDao.list();

		return coreClients.stream().map(client -> {
			MenuSelectVO menuSelectVO = new MenuSelectVO()
					.setName(client.getName())
					.setId(client.getId())
					.setChildren(menuList.stream().filter(topMenu -> NumberUtil.equals(topMenu.getClientId(),client.getId())).toList());

			menuSelectVO.setHasChildren(Fc.isNotEmpty(menuSelectVO.getChildren()));
			return menuSelectVO;
		}).toList();
	}

}
