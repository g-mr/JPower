package com.qidiangk.smart.system.dbs.dao.role;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import com.qidiangk.smart.system.dbs.dao.role.mapper.CoreRoleDataMapper;
import com.qidiangk.smart.system.dbs.entity.role.CoreRoleData;

import java.util.List;

/**
 * 角色数据权限数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreRoleDataDao extends JpowerServiceImpl<CoreRoleDataMapper, CoreRoleData> {

	/**
	 * 根据角色ID列表查询数据权限ID列表
	 *
	 * @param roleIds 角色ID列表
	 * @return 数据权限ID列表
	 */
	public List<Long> listDataIdByRoleId(List<Long> roleIds) {
		return super.objListAs(Wrappers.getQueryWrapper()
				.select(CoreRoleData::getDataId)
				.in(CoreRoleData::getRoleId, roleIds), Long.class);
	}

	public void removeRealByRoleId(Long roleId) {
		super.removeReal(Wrappers.getQueryWrapper()
				.eq(CoreRoleData::getRoleId, roleId));
	}
}
