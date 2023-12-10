package top.jpower.jpower.service.role.impl;

import top.jpower.jpower.dbs.dao.role.mapper.TbCoreRoleDataMapper;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleData;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.service.role.CoreRoleDataService;
import org.springframework.stereotype.Service;

/**
 * @author ding
 * @description
 * @date 2020-11-04 11:02
 */
@Service
public class CoreRoleDataServiceImpl extends BaseServiceImpl<TbCoreRoleDataMapper, TbCoreRoleData> implements CoreRoleDataService {
}
