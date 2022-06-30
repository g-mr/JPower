package com.wlcb.jpower.module.tenant;

import com.wlcb.jpower.module.common.utils.DesedeUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;

import java.util.Date;
import java.util.List;

/**
 * @author mr.gmac
 */
public interface TenantConstant {

    String DEFAULT_TENANT_CODE = "000000";
    String TENANT_CODE = TokenConstant.TENANT_CODE;

    /** 租户额度默认值为不限制 **/
    Integer TENANT_ACCOUNT_NUMBER = -1;

    /**
     * @author 郭丁志
     * @Description // 创建一个租户Code
     * @date 22:18 2020/10/24 0024
     */
    static String tenantCode(List<String> list){
        String tenantCode = Fc.random(6).toUpperCase();
        if (list.contains(tenantCode)){
            return tenantCode(list);
        }
        return tenantCode;
    }

    /**
     * @author 郭丁志
     * @Description // 获取授权码
     * @date 22:18 2020/10/24 0024
     */
    static String getLicenseKey(Integer accountNumber, Date expireTime){
        String et = Fc.isNull(expireTime)?StringPool.NULL:Fc.formatDateTime(expireTime);
        return DesedeUtil.encrypt(accountNumber + StringPool.SEMICOLON + et);
    }

    /**
     * @author 郭丁志
     * @Description // 获取授权码中的账号额度
     * @date 22:18 2020/10/24 0024
     */
    static long getAccountNumber(String encrypt){
        encrypt = DesedeUtil.decrypt(encrypt);
        return Fc.toLong(Fc.toStrArray(StringPool.SEMICOLON, encrypt)[0]);
    }

    /**
     * @author 郭丁志
     * @Description // 获取授权码中的账号过期时间
     * @date 22:18 2020/10/24 0024
     */
    static Date getExpireTime(String encrypt){
        encrypt = DesedeUtil.decrypt(encrypt);
        String expireTime = Fc.toStrArray(StringPool.SEMICOLON, encrypt)[1];
        if (Fc.equalsValue(expireTime,StringPool.NULL)){
            return null;
        }
        return Fc.parseDate(expireTime);
    }

}
