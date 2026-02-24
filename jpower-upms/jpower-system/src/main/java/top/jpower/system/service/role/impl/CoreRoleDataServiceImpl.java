package top.jpower.system.service.role.impl;

import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.system.dbs.dao.role.mapper.CoreRoleDataMapper;
import top.jpower.system.dbs.entity.role.CoreRoleData;
import top.jpower.system.service.role.CoreRoleDataService;

/**
 * 角色数据权限服务实现
 * 
 * @author mr.g
 */
@Service
public class CoreRoleDataServiceImpl extends BaseServiceImpl<CoreRoleDataMapper, CoreRoleData> implements CoreRoleDataService {
}
