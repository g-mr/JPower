package top.jpower.system.dbs.dao.tenant;

import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.dao.tenant.mapper.CoreTenantMapper;
import top.jpower.system.dbs.entity.tenant.CoreTenant;
import top.jpower.system.vo.SelectVO;

import java.util.List;
import java.util.Map;

/**
 * 租户数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreTenantDao extends JpowerServiceImpl<CoreTenantMapper, CoreTenant> {

	/**
	 * 查询租户得设置
	 *
	 * @author mr.g
	 * @param id 租户ID
	 * @return java.util.Map<java.lang.String,java.lang.String> 设置内容
	 **/
	public Map<String, String> config(Long id) {
		return super.getObj(Condition.<CoreTenant>getQueryWrapper().lambda().select(CoreTenant::getConfig).eq(CoreTenant::getId, id), config-> JSON.parseObject(Fc.toStr(config, "{}"), Map.class));
	}

	/**
	 * 租户设置
	 *
	 * @author mr.g
	 * @param id 租户ID
	 * @param config 设置内容
	 * @return boolean 是否成功
	 **/
	public boolean updateConfig(Long id, Map<String, String> config) {
		CoreTenant tenant = new CoreTenant();
		tenant.setId(id);
		tenant.setConfig(config);
		return super.updateById(tenant);
	}

	/**
	 * 查询租户列表
	 *
	 * @author mr.g
	 * @param tenantName 租户名称
	 * @return java.util.List<top.jpower.system.vo.SelectVO> 列表
	 **/
	public List<SelectVO> select(String tenantName) {
		return super.listAs(Wrappers.getQueryWrapper().select(CoreTenant::getTenantName, CoreTenant::getTenantCode)
				.like(CoreTenant::getTenantName, tenantName, Fc.isNoneBlank(tenantName))
				.orderBy(CoreTenant::getCreateTime).desc(), SelectVO.class);
	}
}
