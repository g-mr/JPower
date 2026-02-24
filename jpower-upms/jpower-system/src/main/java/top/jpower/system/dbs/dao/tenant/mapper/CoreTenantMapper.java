package top.jpower.system.dbs.dao.tenant.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.system.dbs.entity.tenant.CoreTenant;

@Mapper
public interface CoreTenantMapper extends JpowerBaseMapper<CoreTenant> {
}
