package top.jpower.system.service.tenant.impl;

import cn.hutool.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.common.constants.CacheNames;
import top.jpower.common.enums.UserTypeEnum;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.core.util.utils.WebUtil;
import top.jpower.system.dbs.dao.dict.CoreDictDao;
import top.jpower.system.dbs.dao.org.CoreOrgDao;
import top.jpower.system.dbs.dao.role.CoreFunctionDao;
import top.jpower.system.dbs.dao.role.CoreRoleDao;
import top.jpower.system.dbs.dao.role.CoreRoleFunctionDao;
import top.jpower.system.dbs.dao.tenant.CoreTenantDao;
import top.jpower.system.dbs.dao.tenant.mapper.CoreTenantMapper;
import top.jpower.system.dbs.entity.dict.CoreDict;
import top.jpower.system.dbs.entity.org.CoreOrg;
import top.jpower.system.dbs.entity.role.CoreRole;
import top.jpower.system.dbs.entity.tenant.CoreTenant;
import top.jpower.system.service.tenant.TenantService;
import top.jpower.system.vo.SelectVO;
import top.jpower.system.vo.TenantCreateVO;
import top.jpower.system.vo.TenantInfoVO;
import top.jpower.user.api.dto.CoreUserDTO;
import top.jpower.user.api.feign.UserClient;

import java.util.*;
import java.util.stream.Collectors;

import static top.jpower.common.constants.ServiceCodeConstants.CODE_EXIST;
import static top.jpower.common.constants.ServiceCodeConstants.DOMAIN_EXIST;
import static top.jpower.common.constants.ServiceCodeConstants.SAVE_FAILURE;
import static top.jpower.core.dbs.tenant.TenantConstant.DEFAULT_TENANT_CODE;
import static top.jpower.core.dbs.tenant.TenantConstant.TENANT_ACCOUNT_NUMBER;
import static top.jpower.core.dbs.tenant.TenantConstant.getLicenseKey;
import static top.jpower.core.dbs.tenant.TenantConstant.tenantCode;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;

/**
 * 租户业务实现
 * 
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends BaseServiceImpl<CoreTenantMapper, CoreTenant> implements TenantService {

    private final CoreTenantDao tenantDao;
    private final CoreOrgDao orgDao;
    private final CoreRoleDao roleDao;
    private final CoreFunctionDao functionDao;
    private final CoreRoleFunctionDao roleFunctionDao;
    private final CoreDictDao dictDao;
    private final UserClient userClient;

    @Override
    public boolean updateById(CoreTenant tenant){
		if (Fc.isNotBlank(tenant.getDomain())) {
			CoreTenant coreTenant = tenantDao.getOneByField(CoreTenant::getDomain,tenant.getDomain());
			JpowerAssert.notTrue(Fc.notNull(coreTenant) && !NumberUtil.equals(coreTenant.getId(),tenant.getId()), JpowerError.Business, DOMAIN_EXIST);
		}

        tenant.setTenantCode(null);
        if (Fc.notNull(tenant.getExpireTime()) || Fc.notNull(tenant.getAccountNumber())){
            CoreTenant coreTenant = tenantDao.getById(tenant.getId());
            Date expireTime = Fc.isNull(tenant.getExpireTime()) ? coreTenant.getExpireTime() : tenant.getExpireTime();
            Integer accountNumber = Fc.isNull(tenant.getAccountNumber()) ? coreTenant.getAccountNumber() : tenant.getAccountNumber();
            tenant.setLicenseKey(getLicenseKey(accountNumber,expireTime));
        }

		CacheUtil.clear(CacheNames.TENANT_KEY);
        return tenantDao.updateById(tenant);
    }

    @Override
    public Long save(TenantCreateVO tenant) {
		if (Fc.isNotBlank(tenant.getTenantCode())) {
			JpowerAssert.notTrue(tenantDao.existsByField(CoreTenant::getTenantCode,tenant.getTenantCode()), JpowerError.Business,CODE_EXIST);
		}
		if (Fc.isNotBlank(tenant.getDomain())){
			JpowerAssert.notTrue(tenantDao.existsByField(CoreTenant::getDomain,tenant.getDomain()), JpowerError.Business,DOMAIN_EXIST);
		}

        if (Fc.isBlank(tenant.getTenantCode())){
			List<String> tenantCodeList = tenantDao.listTenantCode();
            tenant.setTenantCode(tenantCode(tenantCodeList));
        }
        if (Fc.isNull(tenant.getAccountNumber())){
            tenant.setAccountNumber(TENANT_ACCOUNT_NUMBER);
        }
        tenant.setLicenseKey(getLicenseKey(tenant.getAccountNumber(),tenant.getExpireTime()));
        if (tenantDao.save(tenant)){
            //创建租户默认部门
            CoreOrg org = new CoreOrg();
            org.setParentId(Fc.toLong(TOP_CODE));
            org.setName(tenant.getTenantName());
            org.setCode(tenant.getTenantCode());
            if (ShieldUtil.isRoot()){
                org.setTenantCode(tenant.getTenantCode());
            }
            org.setAncestorId(TOP_CODE);
            org.setSort(1);
            org.setContactName(tenant.getContactName());
            org.setContactPhone(tenant.getContactPhone());
            org.setAddress(tenant.getAddress());
            orgDao.save(org);
            //创建租户默认角色
            CoreRole role = new CoreRole();
            role.setIsSysRole(Boolean.TRUE);
            role.setName(tenant.getTenantName()+"-管理员");
            role.setParentId(Fc.toLong(TOP_CODE));
            role.setRemark("这是系统内置角色，不要删除，会影响功能");
            if (ShieldUtil.isRoot()){
                role.setTenantCode(tenant.getTenantCode());
            }
            roleDao.save(role);
            //创建租户初始权限

            List<Long> functionIds = functionDao.queryNoMenuIdByTop();

            if (Fc.isNotEmpty(tenant.getFunctionCode())){
                functionIds.addAll(functionDao.getAllIdByCode(tenant.getFunctionCode()));
            }
            roleFunctionDao.saveFunctions(functionIds, role.getId());

            //创建租户默认字典
			List<CoreDict> dictList = dictDao.listByField(CoreDict::getTenantCode, DEFAULT_TENANT_CODE);
			Map<Long,Long> map = new HashMap<>(dictList.size());
			dictList = dictList.stream().peek(dict->{
				dict.setTenantCode(tenant.getTenantCode());
				Long id = Fc.randomSnowFlakeId();
				//把旧ID和新ID的对应关系存储起来
				map.put(dict.getId(),id);
				dict.setId(id);
			}).collect(Collectors.toList());
			dictList = dictList.stream().peek(dict -> {
				if (!Fc.equalsValue(dict.getParentId(),TOP_CODE)){
					dict.setParentId(map.get(dict.getParentId()));
				}
			}).collect(Collectors.toList());
			dictDao.saveBatch(dictList);

            //创建租户默认用户 (必须放到最后创建，因为没有启动分布式事务)
			CoreUserDTO user = new CoreUserDTO();
            user.setLoginId("admin");
            user.setNickName(tenant.getTenantName()+"-管理员");
            user.setUserName(tenant.getTenantName()+"-管理员");
            user.setUserType(UserTypeEnum.USER_TYPE_SYSTEM.getValue());
            user.setBirthday(new Date());
            user.setActivationStatus(Boolean.TRUE);
            user.setOrgId(org.getId());
            user.setTenantCode(tenant.getTenantCode());
            user.setRoleIds(Collections.singletonList(role.getId()));

            R<Long> r = userClient.saveUser(user);
            JpowerAssert.isTrue(r.isStatus(), JpowerError.Rpc, r.getCode(), r.getMessage());
			JpowerAssert.notTrue(Fc.isNull(r.getData()), JpowerError.Rpc, r.getCode(), SAVE_FAILURE);

			CacheUtil.clear(CacheNames.TENANT_KEY);
            return tenant.getId();
        }
        return null;
    }

    @Override
    public boolean setting(List<Long> ids, Integer accountNumber, Date expireTime) {
        String licenseKey = getLicenseKey(accountNumber,expireTime);
        List<CoreTenant> tenantList = new ArrayList<>();
        ids.forEach(id -> {
			CoreTenant tenant = new CoreTenant();
            tenant.setAccountNumber(accountNumber);
            tenant.setExpireTime(expireTime);
            tenant.setId(id);
            tenant.setLicenseKey(licenseKey);
            tenantList.add(tenant);
        });
		CacheUtil.clear(CacheNames.TENANT_KEY);
        return tenantDao.updateBatch(tenantList);
    }

    @Override
    public Map<String, String> config(Long id) {
        return tenantDao.config(id);
    }

    @Override
    public boolean updateConfig(Long id, Map<String, String> config) {
        return tenantDao.updateConfig(id, config);
    }

	@Override
	public Pg<CoreTenant> pageByMap(Map<String, Object> map) {
		return tenantDao.pg(Wrappers.getQueryWrapper(map));
	}

	@Override
	public List<SelectVO> select(String tenantName) {
		return tenantDao.select(tenantName);
	}

	@Override
	public TenantInfoVO queryByDomain(String domain) {
		if (Fc.isBlank(domain) && WebUtil.getRequest() != null){
			domain = WebUtil.getRequest().getServerName();
		}
		domain = StringUtil.removeAllSuffix(domain, "/");
		Optional<CoreTenant> tenantOptional = tenantDao.getByDomain(domain);

		if (tenantOptional.isPresent()){
			CoreTenant tenant = tenantOptional.get();
			return new TenantInfoVO()
					.setConfig(tenantDao.config(tenant.getId()))
					.setTitle(tenant.getTenantName())
					.setDomain(tenant.getDomain())
					.setTenantCode(tenant.getTenantCode());
		}
		return null;
	}

}
