package com.wlcb.jpower.service.role.impl;

import com.wlcb.jpower.dbs.dao.role.mapper.TbCoreRoleDataMapper;
import com.wlcb.jpower.dbs.entity.role.TbCoreRoleData;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.service.role.CoreRoleDataService;
import org.springframework.stereotype.Service;

/**
 * @author ding
 * @description
 * @date 2020-11-04 11:02
 */
@Service
public class CoreRoleDataServiceImpl extends BaseServiceImpl<TbCoreRoleDataMapper, TbCoreRoleData> implements CoreRoleDataService {
}
