package com.qidiangk.smart.resource.service;

import top.jpower.core.dbs.service.BaseService;
import com.qidiangk.smart.resource.dbs.entity.ResourceSms;

/**
 * 短信配置服务接口
 * <p>
 * 短信配置表服务类
 * </p>
 *
 * @author mr.g
 */
public interface ResourceSmsService extends BaseService<ResourceSms> {

    /**
     * 通过code查询短信配置
     *
     * @author mr.g
     * @param code 短信编码
     * @return TbResourceSms 短信配置实体
     */
    ResourceSms getByCode(String code);

}
