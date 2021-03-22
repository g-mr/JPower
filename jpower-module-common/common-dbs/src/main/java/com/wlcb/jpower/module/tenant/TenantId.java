package com.wlcb.jpower.module.tenant;

import com.wlcb.jpower.module.common.utils.Fc;

/**
 * @author ding
 * @description
 * @date 2021-03-22 17:57
 */
public class TenantId implements TenantConstant {

    @Override
    public String generate() {
        return Fc.random(6).toUpperCase();
    }

}
