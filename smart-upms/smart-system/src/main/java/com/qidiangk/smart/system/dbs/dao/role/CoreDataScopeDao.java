package com.qidiangk.smart.system.dbs.dao.role;


import org.springframework.stereotype.Repository;
import com.qidiangk.smart.common.enums.FunctionTypeEnum;
import com.qidiangk.smart.common.enums.YN01Enum;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import com.qidiangk.smart.system.api.dto.DataScopeDTO;
import com.qidiangk.smart.system.dbs.dao.role.mapper.CoreDataScopeMapper;
import com.qidiangk.smart.system.dbs.entity.function.CoreDataScope;
import com.qidiangk.smart.system.dbs.entity.function.CoreFunction;
import com.qidiangk.smart.system.dbs.entity.role.CoreRoleData;

import java.util.List;

/**
 * 数据权限数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreDataScopeDao extends JpowerServiceImpl<CoreDataScopeMapper, CoreDataScope> {

	/**
	 * 根据角色获取数据权限
	 *
	 * @param roleIds 角色ID
	 * @param clientId 客户端ID
	 * @return 数据权限
	 */
	public List<DataScopeDTO> getDataScopeByRole(List<Long> roleIds, Long clientId, String menuCode) {
		return super.listAs(Wrappers.getQueryWrapper()
				.leftJoin(CoreFunction.class).on(CoreDataScope::getMenuId, CoreFunction::getId)
				.leftJoin(CoreRoleData.class).on(CoreRoleData::getDataId, CoreDataScope::getId)
				.eq(CoreFunction::getClientId, clientId)
				.eq(CoreFunction::getCode, menuCode)
				.eq(CoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue())
				.and(query-> {
					query.eq(CoreRoleData::getRoleId, roleIds)
							.or(CoreDataScope::getAllRole).eq(YN01Enum.Y.getValue());
				}), DataScopeDTO.class);
	}
}
