package com.wlcb.jpower.service.teannt.impl;

import com.wlcb.jpower.dbs.dao.tenant.TbCoreTenantDao;
import com.wlcb.jpower.dbs.dao.tenant.mapper.TbCoreTenantMapper;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.service.teannt.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName TenantServiceImpl
 * @Description TODO 租户业务
 * @Author 郭丁志
 * @Date 2020-10-23 15:17
 * @Version 1.0
 */
@AllArgsConstructor
@Service
public class TenantServiceImpl extends BaseServiceImpl<TbCoreTenantMapper, TbCoreTenant> implements TenantService {

    private TbCoreTenantDao tenantDao;

    @Override
    public boolean updateById(TbCoreTenant tenant){
        tenant.setTenantCode(null);

        if (Fc.isNull(tenant.getExpireTime()) || Fc.isNull(tenant.getAccountNumber())){
            TbCoreTenant coreTenant = tenantDao.getById(tenant.getId());
            Date expireTime = Fc.isNull(tenant.getExpireTime())?coreTenant.getExpireTime():tenant.getExpireTime();
            Integer accountNumber = Fc.isNull(tenant.getAccountNumber())?coreTenant.getAccountNumber():tenant.getAccountNumber();

            tenant.setLicenseKey();

        }


        return tenantDao.updateById(tenant);
    }


}
