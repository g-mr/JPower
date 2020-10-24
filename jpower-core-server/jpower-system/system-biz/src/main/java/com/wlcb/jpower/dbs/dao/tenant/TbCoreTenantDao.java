package com.wlcb.jpower.dbs.dao.tenant;

import com.wlcb.jpower.dbs.dao.tenant.mapper.TbCoreTenantMapper;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @ClassName TbCoreTenantDao
 * @Description TODO 租户
 * @Author 郭丁志
 * @Date 2020-10-23 15:18
 * @Version 1.0
 */
@Repository
public class TbCoreTenantDao extends JpowerServiceImpl<TbCoreTenantMapper, TbCoreTenant> {
}
