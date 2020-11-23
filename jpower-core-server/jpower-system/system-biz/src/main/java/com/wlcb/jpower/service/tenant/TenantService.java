package com.wlcb.jpower.service.tenant;

import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.common.service.BaseService;

import java.util.Date;
import java.util.List;

/**
 * @author mr.gmac
 */
public interface TenantService extends BaseService<TbCoreTenant> {

    /**
     * @author 郭丁志
     * @Description // 根据ID修改信息
     * @date 22:14 2020/10/24 0024
     */
    @Override
    boolean updateById(TbCoreTenant tenant);

    /**
     * @author 郭丁志
     * @Description // 新增信息
     * @date 22:14 2020/10/24 0024
     * @param tenant 租户信息
     * @param functionCodes 权限Code
     * @param dictTypeCodes 字典类型Code
     */
    boolean save(TbCoreTenant tenant, List<String> functionCodes, List<String> dictTypeCodes);

    /**
     * @author 郭丁志
     * @Description //TODO 租户授权配置
     * @date 13:34 2020/10/25 0025
     * @param ids 租户ID
     * @param accountNumber 额度
     * @param expireTime 过期时间
     */
    boolean setting(List<String> ids, Integer accountNumber, Date expireTime);
}