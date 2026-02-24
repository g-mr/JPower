package top.jpower.system.service.tenant.impl;

import cn.hutool.core.thread.ThreadUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.common.constants.ParamsConstants;
import top.jpower.common.enums.FunctionTypeEnum;
import top.jpower.common.enums.UserTypeEnum;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.utils.DigestUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MD5;
import top.jpower.jpower.cache.param.ParamConfig;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.system.dbs.dao.dict.TbCoreDictDao;
import top.jpower.system.dbs.dao.org.TbCoreOrgDao;
import top.jpower.system.dbs.dao.role.TbCoreFunctionDao;
import top.jpower.system.dbs.dao.role.TbCoreRoleDao;
import top.jpower.system.dbs.dao.role.TbCoreRoleFunctionDao;
import top.jpower.system.dbs.dao.tenant.TbCoreTenantDao;
import top.jpower.system.dbs.dao.tenant.mapper.CoreTenantMapper;
import top.jpower.system.dbs.entity.dict.TbCoreDict;
import top.jpower.system.dbs.entity.org.TbCoreOrg;
import top.jpower.system.dbs.entity.role.TbCoreRole;
import top.jpower.system.dbs.entity.tenant.CoreTenant;
import top.jpower.system.dbs.entity.tenant.TbCoreTenant;
import top.jpower.system.service.tenant.TenantService;
import top.jpower.user.api.feign.UserClient;

import java.util.*;
import java.util.stream.Collectors;

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
@AllArgsConstructor
public class TenantServiceImpl extends BaseServiceImpl<CoreTenantMapper, CoreTenant> implements TenantService {

    private TbCoreTenantDao tenantDao;
    private TbCoreOrgDao orgDao;
    private TbCoreRoleDao roleDao;
    private TbCoreFunctionDao functionDao;
    private TbCoreRoleFunctionDao roleFunctionDao;
    private TbCoreDictDao dictDao;
    private UserClient userClient;

    @Override
    public boolean updateById(TbCoreTenant tenant){
        tenant.setTenantCode(null);

        if (Fc.notNull(tenant.getExpireTime()) || Fc.notNull(tenant.getAccountNumber())){
            TbCoreTenant coreTenant = tenantDao.getById(tenant.getId());
            Date expireTime = Fc.isNull(tenant.getExpireTime())?coreTenant.getExpireTime():tenant.getExpireTime();
            Integer accountNumber = Fc.isNull(tenant.getAccountNumber())?coreTenant.getAccountNumber():tenant.getAccountNumber();
            tenant.setLicenseKey(getLicenseKey(accountNumber,expireTime));
        }

        return tenantDao.updateById(tenant);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, JpowerException.class})
    public boolean save(TbCoreTenant tenant, Set<String> functionCodes){
        if (Fc.isBlank(tenant.getTenantCode())){
            List<String> tenantCodeList = tenantDao.listObjs(Condition.<TbCoreTenant>getQueryWrapper().lambda()
                    .select(TbCoreTenant::getTenantCode),Fc::toStr);
            tenant.setTenantCode(tenantCode(tenantCodeList));
        }
        if (Fc.isNull(tenant.getAccountNumber())){
            tenant.setAccountNumber(TENANT_ACCOUNT_NUMBER);
        }
        tenant.setLicenseKey(getLicenseKey(tenant.getAccountNumber(),tenant.getExpireTime()));
        if (tenantDao.save(tenant)){
            //创建租户默认部门
            TbCoreOrg org = new TbCoreOrg();
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
            TbCoreRole role = new TbCoreRole();
            role.setIsSysRole(YN01Enum.Y.getValue());
            role.setName(tenant.getTenantName()+"-管理员");
            role.setParentId(Fc.toLong(TOP_CODE));
            role.setRemark("这是系统内置角色，不要删除，会影响功能");
            if (ShieldUtil.isRoot()){
                role.setTenantCode(tenant.getTenantCode());
            }
            roleDao.save(role);
            //创建租户初始权限

            List<Long> functionIds = functionDao.queryIdByTopChild();

            if (Fc.isNotEmpty(functionCodes)){
                functionIds.addAll(getFunctions(functionCodes,new LinkedList<>()));
            }
            roleFunctionDao.saveFunctions(functionIds, role.getId());

            //创建租户默认字典
            ThreadUtil.execute(()->{
                List<TbCoreDict> dictList = dictDao.list(Condition.<TbCoreDict>getQueryWrapper().lambda().eq(TbCoreDict::getTenantCode,DEFAULT_TENANT_CODE).orderByAsc(TbCoreDict::getParentId));
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
            });

            //创建租户默认用户 (必须放到最后创建，因为没有启动分布式事务)
            TbCoreUser user = new TbCoreUser();
            user.setLoginId("admin");
            user.setPassword(DigestUtil.pwdEncrypt(MD5.md5HexToUpperCase(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD, DefaultValConstants.DEFAULT_USER_PASSWORD))));
            user.setNickName(tenant.getTenantName()+"-管理员");
            user.setUserName(tenant.getTenantName()+"-管理员");
            user.setUserType(UserTypeEnum.USER_TYPE_SYSTEM.getValue());
            user.setBirthday(new Date());
            user.setActivationStatus(YN01Enum.Y.getValue());
            user.setOrgId(org.getId());
            user.setTenantCode(tenant.getTenantCode());
            user.setRoleIds(Fc.toStr(role.getId()));

            ResponseData data = userClient.saveUser(user);
            JpowerAssert.isTrue(data.isStatus(), JpowerError.Rpc, data.getCode(), data.getMessage());
            return true;
        }
        return false;
    }

    private List<Long> getFunctions(Set<String> functionCodes,LinkedList<Long> functionIds) {

        List<Long> ids = functionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getId)
                .in(TbCoreFunction::getCode,functionCodes),Fc::toLong);

        ids.forEach(id->{
            functionIds.add(id);

            List<Long> btnIds = functionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                    .select(TbCoreFunction::getId)
                    .ne(TbCoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue())
                    .eq(TbCoreFunction::getParentId,id),Fc::toLong);

            functionIds.addAll(btnIds);
        });

        return functionIds;
    }

    @Override
    public boolean setting(List<Long> ids, Integer accountNumber, Date expireTime) {
        String licenseKey = getLicenseKey(accountNumber,expireTime);
        List<TbCoreTenant> tenantList = new ArrayList<>();
        ids.forEach(id -> {
            TbCoreTenant tenant = new TbCoreTenant();
            tenant.setAccountNumber(accountNumber);
            tenant.setExpireTime(expireTime);
            tenant.setId(id);
            tenant.setLicenseKey(licenseKey);
            tenantList.add(tenant);
        });
        return tenantDao.updateBatchById(tenantList);
    }

    @Override
    public Map<String, String> config(Long id) {
        return tenantDao.config(id);
    }

    @Override
    public boolean updateConfig(Long id, Map<String, String> config) {
        return tenantDao.updateConfig(id, config);
    }

}
