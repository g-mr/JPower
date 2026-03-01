package top.jpower.system.service.tenant;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import top.jpower.system.dbs.entity.tenant.CoreTenant;
import top.jpower.system.vo.SelectVO;
import top.jpower.system.vo.TenantCreateVO;
import top.jpower.system.vo.TenantInfoVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 租户服务接口
 * 
 * @author mr.g
 */
public interface TenantService extends BaseService<CoreTenant> {

    /**
     * 根据ID修改信息
     * 
     * @author mr.g
     * @param tenant 租户信息
     * @return boolean 是否修改成功
     */
    @Override
    boolean updateById(CoreTenant tenant);

    /**
     * 新增信息
     * 
     * @author mr.g
     * @param tenant 租户信息
     * @return boolean 是否新增成功
     */
    Long save(TenantCreateVO tenant);

    /**
     * 租户授权配置
     * 
     * @author mr.g
     * @param ids 租户ID
     * @param accountNumber 额度
     * @param expireTime 过期时间
     * @return boolean 是否配置成功
     */
    boolean setting(List<Long> ids, Integer accountNumber, Date expireTime);

    /**
     * 查询租户得设置
     *
     * @author mr.g
     * @param id
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    Map<String, String> config(Long id);

    /**
     * 租户设置
     *
     * @author mr.g
     * @param id 租户ID
     * @param config 设置内容
     * @return boolean 是否成功
     **/
    boolean updateConfig(Long id, Map<String, String> config);

	/**
     * 根据条件查询
     *
     * @author mr.g
     * @param map 查询条件
     * @return 租户分页数据
     **/
    Pg<CoreTenant> pageByMap(Map<String, Object> map);

	/**
	 * 下拉列表
	 *
	 * @author mr.g
	 * @param tenantName 租户名称
	 * @return 下拉选项
	 **/
	List<SelectVO> select(String tenantName);

	/**
	 * 根据域名查询租户信息
	 *
	 * @author mr.g
	 * @param domain 域名
	 * @return 租户信息
	 **/
	TenantInfoVO queryByDomain(String domain);
}
