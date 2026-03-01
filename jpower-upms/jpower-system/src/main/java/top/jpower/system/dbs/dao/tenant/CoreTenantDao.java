package top.jpower.system.dbs.dao.tenant;

import com.alibaba.fastjson2.JSON;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.util.UpdateEntity;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.dao.tenant.mapper.CoreTenantMapper;
import top.jpower.system.dbs.entity.tenant.CoreTenant;
import top.jpower.system.vo.SelectVO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
		String config = super.getObjAs(Wrappers.getQueryWrapper()
				.select(CoreTenant::getConfig)
				.eq(CoreTenant::getId, id), String.class);
		return JSON.parseObject(Fc.toStr(config, "{}"), Map.class);
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
		return super.updateById(UpdateEntity.ofNotNull(tenant));
	}

	/**
	 * 查询租户列表
	 *
	 * @author mr.g
	 * @param tenantName 租户名称
	 * @return java.util.List<top.jpower.system.vo.SelectVO> 列表
	 **/
	public List<SelectVO> select(String tenantName) {
		return super.listAs(Wrappers.getQueryWrapper()
				.select(CoreTenant::getTenantName, CoreTenant::getTenantCode)
				.like(CoreTenant::getTenantName, tenantName, Fc.isNoneBlank(tenantName))
				.orderBy(CoreTenant::getCreateTime).desc(), SelectVO.class);
	}

	public List<String> listTenantCode() {
		return super.objListAs(Wrappers.getQueryWrapper().select(CoreTenant::getTenantCode), String.class);
	}

	/**
	 * 根据域名查询租户
	 *
	 * @author mr.g
	 * @param domain 域名
	 * @return 租户
	 **/
	public Optional<CoreTenant> getByDomain(String domain) {
		return super.getOneOpt(Wrappers.getQueryWrapper().where(QueryMethods.length(CoreTenant::getDomain).gt(0)).and("? like concat('%', domain)", domain).limit(1));
	}
}
