package com.wlcb.jpower.service.teannt;

import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.common.service.BaseService;

/**
 * @author mr.gmac
 */
public interface TenantService extends BaseService<TbCoreTenant> {

    @Override
    boolean updateById(TbCoreTenant tenant);

}
