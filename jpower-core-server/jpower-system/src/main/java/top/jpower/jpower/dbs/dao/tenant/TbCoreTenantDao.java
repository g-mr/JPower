package top.jpower.jpower.dbs.dao.tenant;

import org.springframework.stereotype.Repository;
import top.jpower.jpower.dbs.dao.tenant.mapper.TbCoreTenantMapper;
import top.jpower.jpower.dbs.entity.tenant.TbCoreTenant;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;

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
