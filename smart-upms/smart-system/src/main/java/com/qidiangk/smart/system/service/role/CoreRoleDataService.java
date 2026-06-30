package com.qidiangk.smart.system.service.role;

import top.jpower.core.dbs.service.BaseService;
import com.qidiangk.smart.system.dbs.entity.role.CoreRoleData;

import java.util.List;

/**
 * 角色数据服务接口
 * 
 * @author mr.g
 */
public interface CoreRoleDataService extends BaseService<CoreRoleData> {

	/**
	 * 获取角色数据权限
	 *
	 * @param roleIds 角色ID
	 * @return 数据权限ID
	 */
    List<Long> listDataIdByRoleId(List<Long> roleIds);
}
