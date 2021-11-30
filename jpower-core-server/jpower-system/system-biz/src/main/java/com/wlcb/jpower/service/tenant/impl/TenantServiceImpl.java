package com.wlcb.jpower.service.tenant.impl;

import com.wlcb.jpower.cache.UserCache;
import com.wlcb.jpower.cache.param.ParamConfig;
import com.wlcb.jpower.dbs.dao.dict.TbCoreDictDao;
import com.wlcb.jpower.dbs.dao.dict.TbCoreDictTypeDao;
import com.wlcb.jpower.dbs.dao.org.TbCoreOrgDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreFunctionDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreRoleDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import com.wlcb.jpower.dbs.dao.tenant.TbCoreTenantDao;
import com.wlcb.jpower.dbs.dao.tenant.mapper.TbCoreTenantMapper;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDict;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDictType;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.dbs.entity.role.TbCoreRole;
import com.wlcb.jpower.dbs.entity.role.TbCoreRoleFunction;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.DigestUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.MD5;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.constants.ParamsConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.tenant.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.wlcb.jpower.module.common.utils.constants.JpowerConstants.TOP_CODE;
import static com.wlcb.jpower.module.tenant.TenantConstant.*;

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
    private TbCoreDictTypeDao dictTypeDao;
    private TbCoreDictDao dictDao;

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
    public boolean save(TbCoreTenant tenant, List<String> functionCodes){
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
            if (SecureUtil.isRoot()){
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
            role.setName("管理员");
            role.setParentId(TOP_CODE);
            if (SecureUtil.isRoot()){
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
            LinkedList<TbCoreDictType> dictTypes = new LinkedList<>();
            LinkedList<TbCoreDict> dicts = new LinkedList<>();
            getDictTypes(TOP_CODE,TOP_CODE,tenant.getTenantCode(),dictTypes,dicts);
            dictTypeDao.saveBatch(dictTypes);
            dictDao.saveBatch(dicts);

            //创建租户默认用户 (必须放到最后创建，因为没有启动分布式事务)
            TbCoreUser user = new TbCoreUser();
            user.setLoginId("admin");
            user.setPassword(DigestUtil.encrypt(MD5.parseStrToMd5U32(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD, ConstantsUtils.DEFAULT_USER_PASSWORD))));
            user.setNickName("管理员");
            user.setUserName("管理员");
            user.setUserType(ConstantsEnum.USER_TYPE.USER_TYPE_SYSTEM.getValue());
            user.setBirthday(new Date());
            user.setActivationStatus(ConstantsEnum.YN01.Y.getValue());
            user.setOrgId(org.getId());
            if (SecureUtil.isRoot()){
                user.setTenantCode(tenant.getTenantCode());
            }
            ResponseData data = UserCache.saveAdmin(user,role.getId());
            JpowerAssert.isTrue(data.isStatus(), JpowerError.Rpc, data.getCode(), data.getMessage());
            return true;
        }
        return false;
    }

    private List<String> getFunctions(List<String> functionCodes,LinkedList<String> functionIds) {

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

    /**
     * @author 郭丁志
     * @Description //TODO 查询新租户默认字典类型
     * @date 16:50 2020/10/25 0025
     * @return void
     */
    private void getDictTypes(String oldParentId,String newParentId,String tenantCode,LinkedList<TbCoreDictType> dictTypes,LinkedList<TbCoreDict> dicts){
        List<TbCoreDictType> dictTypeList = dictTypeDao.list(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                .eq(TbCoreDictType::getTenantCode,DEFAULT_TENANT_CODE)
                .eq(TbCoreDictType::getParentId,oldParentId));
        dictTypeList.forEach(type -> {
            String oldId = type.getId();
            type.setId(Fc.randomUUID());
            type.setTenantCode(tenantCode);
            type.setParentId(newParentId);
            dictTypes.add(type);
            getDicts(type.getDictTypeCode(),TOP_CODE,TOP_CODE,tenantCode,dicts);
            getDictTypes(oldId,type.getId(),tenantCode,dictTypes,dicts);
        });
    }

    /**
     * @author 郭丁志
     * @Description //TODO 新增新租户默认字典
     * @date 16:50 2020/10/25 0025
     * @return void
     */
    private void getDicts(String typeCode,String top,String parentId,String tenantCode,LinkedList<TbCoreDict> dicts){
        List<TbCoreDict> dictList = dictDao.list(Condition.<TbCoreDict>getQueryWrapper().lambda()
                .eq(TbCoreDict::getTenantCode,DEFAULT_TENANT_CODE)
                .eq(TbCoreDict::getDictTypeCode,typeCode)
                .eq(TbCoreDict::getParentId,top));

        dictList.forEach(dict -> {
            String oldId = dict.getId();
            dict.setId(Fc.randomUUID());
            dict.setTenantCode(tenantCode);
            dict.setParentId(parentId);
            dicts.add(dict);
            getDicts(typeCode,oldId,dict.getId(),tenantCode,dicts);
        });
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
