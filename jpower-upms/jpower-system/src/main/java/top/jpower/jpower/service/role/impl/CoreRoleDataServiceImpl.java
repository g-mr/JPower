package top.jpower.jpower.service.role.impl;

import org.springframework.stereotype.Service;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreRoleDataMapper;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleData;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.jpower.service.role.CoreRoleDataService;

/**
 * @author ding
 * @description
 * @date 2020-11-04 11:02
 */
@Service
public class CoreRoleDataServiceImpl extends BaseServiceImpl<TbCoreRoleDataMapper, TbCoreRoleData> implements CoreRoleDataService {
}
