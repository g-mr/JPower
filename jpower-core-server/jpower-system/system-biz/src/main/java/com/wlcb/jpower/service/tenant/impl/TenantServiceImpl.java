package com.wlcb.jpower.service.tenant.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public boolean save(TbCoreTenant tenant, List<String> functionCodes, List<String> dictTypeCodes){
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
            LambdaQueryWrapper<TbCoreFunction> queryWrapper = Condition.<TbCoreFunction>getQueryWrapper().lambda().select(TbCoreFunction::getId);
            if (Fc.isNotEmpty(functionCodes)){
                queryWrapper.in(TbCoreFunction::getCode,functionCodes);
            }
            queryWrapper.or(c -> c.eq(TbCoreFunction::getParentId,TOP_CODE).eq(TbCoreFunction::getIsMenu,ConstantsEnum.YN01.N.getValue()));
            List<String> functionIds = functionDao.listObjs(queryWrapper,Fc::toStr);
            List<TbCoreRoleFunction> roleFunctionList = new ArrayList<>();
            functionIds.forEach(id -> {
                TbCoreRoleFunction roleFunction = new TbCoreRoleFunction();
                roleFunction.setFunctionId(id);
                roleFunction.setRoleId(role.getId());
                roleFunctionList.add(roleFunction);
            });
            roleFunctionDao.saveBatch(roleFunctionList);

            //创建租户默认字典
//            TbCoreDictType dictType = new TbCoreDictType();
//            dictType.setDictTypeCode(tenant.getTenantCode());
//            dictType.setDictTypeName(tenant.getTenantName());
//            dictType.setParentId(TOP_CODE);
//            dictType.setDelEnabled(ConstantsEnum.YN.N.getValue());
//            dictType.setSortNum(1);
//            dictType.setIsTree(ConstantsEnum.YN01.N.getValue());
////            dictType.setTenantCode(tenant.getTenantCode());
//            dictTypeDao.save(dictType);

            if (Fc.isNotEmpty(dictTypeCodes)){
                dictTypeDao.listObjs(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                        .select(TbCoreDictType::getId)
                        .in(TbCoreDictType::getDictTypeCode,dictTypeCodes)
                        .eq(TbCoreDictType::getParentId,TOP_CODE)
                        .eq(TbCoreDictType::getTenantCode,tenant.getTenantCode())
                        ,Fc::toStr)
                .forEach(id -> saveDictType(id,TOP_CODE,tenant.getTenantCode()));
            }else {
                saveDictType(TOP_CODE,TOP_CODE,tenant.getTenantCode());
            }

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
            JpowerAssert.isTrue(data.isStatus(), JpowerError.Api, data.getCode(), data.getMessage());
            return true;
        }
        return false;
    }

    /**
     * @author 郭丁志
     * @Description //TODO 新增字典类型
     * @date 16:50 2020/10/25 0025
     * @return void
     */
    private void saveDictType(String top,String parentId,String tenantCode){
        List<TbCoreDictType> dictTypeList = dictTypeDao.list(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                .eq(TbCoreDictType::getTenantCode,DEFAULT_TENANT_CODE)
                .eq(TbCoreDictType::getParentId,top));
        dictTypeList.forEach(type -> {
            type.setOldId(type.getId());
            type.setId(null);
            type.setTenantCode(tenantCode);
            type.setParentId(parentId);
        });
        dictTypeDao.saveBatch(dictTypeList);
        dictTypeList.forEach(type -> {
            saveDictType(type.getOldId(),type.getId(),tenantCode);

            saveDict(type.getDictTypeCode(),TOP_CODE,TOP_CODE,tenantCode);
        });
    }

    /**
     * @author 郭丁志
     * @Description //TODO 新增字典
     * @date 16:50 2020/10/25 0025
     * @return void
     */
    private void saveDict(String typeCode,String top,String parentId,String tenantCode){
        List<TbCoreDict> dictList = dictDao.list(Condition.<TbCoreDict>getQueryWrapper().lambda()
                .eq(TbCoreDict::getTenantCode,DEFAULT_TENANT_CODE)
                .eq(TbCoreDict::getDictTypeCode,typeCode)
                .eq(TbCoreDict::getParentId,top));

        dictList.forEach(type -> {
            type.setOldId(type.getId());
            type.setId(null);
            type.setTenantCode(tenantCode);
            type.setParentId(parentId);
        });
        dictDao.saveBatch(dictList);

        dictList.forEach(dict -> saveDict(typeCode,dict.getOldId(),dict.getId(),tenantCode));
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
