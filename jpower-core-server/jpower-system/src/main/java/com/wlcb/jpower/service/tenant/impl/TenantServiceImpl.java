package com.wlcb.jpower.service.tenant.impl;

import com.wlcb.jpower.cache.param.ParamConfig;
import com.wlcb.jpower.dbs.dao.dict.TbCoreDictDao;
import com.wlcb.jpower.dbs.dao.org.TbCoreOrgDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreFunctionDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreRoleDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import com.wlcb.jpower.dbs.dao.tenant.TbCoreTenantDao;
import com.wlcb.jpower.dbs.dao.tenant.mapper.TbCoreTenantMapper;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDict;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.dbs.entity.role.TbCoreRole;
import com.wlcb.jpower.dbs.entity.role.TbCoreRoleFunction;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.feign.UserClient;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.exception.JpowerException;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.DigestUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.MD5;
import com.wlcb.jpower.module.common.utils.ShieldUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.constants.ParamsConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.tenant.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.wlcb.jpower.module.common.utils.constants.JpowerConstants.TOP_CODE;
import static com.wlcb.jpower.module.tenant.TenantConstant.DEFAULT_TENANT_CODE;
import static com.wlcb.jpower.module.tenant.TenantConstant.TENANT_ACCOUNT_NUMBER;
import static com.wlcb.jpower.module.tenant.TenantConstant.getLicenseKey;
import static com.wlcb.jpower.module.tenant.TenantConstant.tenantCode;

/**
 * @ClassName TenantServiceImpl
 * @Description TODO 租户业务
 * @Author 郭丁志
 * @Date 2020-10-23 15:17
 * @Version 1.0
 */
@Service
@AllArgsConstructor
public class TenantServiceImpl extends BaseServiceImpl<TbCoreTenantMapper, TbCoreTenant> implements TenantService {

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
            org.setParentId(TOP_CODE);
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
            role.setIsSysRole(ConstantsEnum.YN01.Y.getValue());
            role.setName(tenant.getTenantName()+"-管理员");
            role.setParentId(TOP_CODE);
            role.setRemark("这是系统内置角色，不要删除，会影响功能");
            if (ShieldUtil.isRoot()){
                role.setTenantCode(tenant.getTenantCode());
            }
            roleDao.save(role);
            //创建租户初始权限

            List<String> functionIds = functionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                    .select(TbCoreFunction::getId)
                    .eq(TbCoreFunction::getParentId,TOP_CODE)
                    .eq(TbCoreFunction::getIsMenu,ConstantsEnum.YN01.N.getValue()),Fc::toStr);

            if (Fc.isNotEmpty(functionCodes)){
                functionIds.addAll(getFunctions(functionCodes,new LinkedList<>()));
            }
            List<TbCoreRoleFunction> roleFunctionList = new ArrayList<>();
            functionIds.forEach(id -> {
                TbCoreRoleFunction roleFunction = new TbCoreRoleFunction();
                roleFunction.setFunctionId(id);
                roleFunction.setRoleId(role.getId());
                roleFunctionList.add(roleFunction);
            });
            roleFunctionDao.saveBatch(roleFunctionList);

            //创建租户默认字典
            List<TbCoreDict> dictList = dictDao.list(Condition.<TbCoreDict>getQueryWrapper().lambda().eq(TbCoreDict::getTenantCode,DEFAULT_TENANT_CODE).orderByAsc(TbCoreDict::getParentId));
            Map<String,String> map = new HashMap<>(dictList.size());
            dictList = dictList.stream().peek(dict->{
              dict.setTenantCode(tenant.getTenantCode());
              String id = Fc.randomUUID();
              //把旧ID和新ID的对应关系存储起来
              map.put(dict.getId(),id);
              dict.setId(id);
            }).collect(Collectors.toList());
            dictList = dictList.stream().peek(dict -> {
                if (!Fc.equalsValue(dict.getParentId(),TOP_CODE)){
                    dict.setParentId(map.get(dict.getParentId()));
                }
            }).collect(Collectors.toList());
            dictDao.addBatchSomeColumn(dictList);

            //创建租户默认用户 (必须放到最后创建，因为没有启动分布式事务)
            TbCoreUser user = new TbCoreUser();
            user.setLoginId("admin");
            user.setPassword(DigestUtil.pwdEncrypt(MD5.md5HexToUpperCase(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD, ConstantsUtils.DEFAULT_USER_PASSWORD))));
            user.setNickName(tenant.getTenantName()+"-管理员");
            user.setUserName(tenant.getTenantName()+"-管理员");
            user.setUserType(ConstantsEnum.USER_TYPE.USER_TYPE_SYSTEM.getValue());
            user.setBirthday(new Date());
            user.setActivationStatus(ConstantsEnum.YN01.Y.getValue());
            user.setOrgId(org.getId());
            user.setTenantCode(tenant.getTenantCode());

            ResponseData data = userClient.saveUser(user,role.getId());
            JpowerAssert.isTrue(data.isStatus(), JpowerError.Rpc, data.getCode(), data.getMessage());
            return true;
        }
        return false;
    }

    private List<String> getFunctions(Set<String> functionCodes,LinkedList<String> functionIds) {

        List<String> ids = functionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getId)
                .in(TbCoreFunction::getCode,functionCodes),Fc::toStr);

        ids.forEach(id->{
            functionIds.add(id);

            List<String> btnIds = functionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                    .select(TbCoreFunction::getId)
                    .eq(TbCoreFunction::getIsMenu,ConstantsEnum.YN01.N.getValue())
                    .eq(TbCoreFunction::getParentId,id),Fc::toStr);

            functionIds.addAll(btnIds);
        });

        return functionIds;
    }

    @Override
    public boolean setting(List<String> ids, Integer accountNumber, Date expireTime) {
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

}
