package com.qidiangk.smart.system.service.role.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import com.qidiangk.smart.system.dbs.dao.role.CoreRoleDataDao;
import com.qidiangk.smart.system.dbs.dao.role.mapper.CoreRoleDataMapper;
import com.qidiangk.smart.system.dbs.entity.role.CoreRoleData;
import com.qidiangk.smart.system.service.role.CoreRoleDataService;

import java.util.List;

/**
 * 角色数据权限服务实现
 * 
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class CoreRoleDataServiceImpl extends BaseServiceImpl<CoreRoleDataMapper, CoreRoleData> implements CoreRoleDataService {

	private final CoreRoleDataDao coreRoleDataDao;

	@Override
	public List<Long> listDataIdByRoleId(List<Long> roleIds) {
		return coreRoleDataDao.listDataIdByRoleId(roleIds);
	}
}
