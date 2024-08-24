package top.jpower.jpower.service;

import top.jpower.jpower.dbs.entity.TbResourceSms;
import top.jpower.jpower.module.service.BaseService;

/**
 * <p>
 * 短信配置表 服务类
 * </p>
 *
 * @author mr.g
 * @since 2024-03-04
 */
public interface ResourceSmsService extends BaseService<TbResourceSms> {

    /**
     * 通过code查询
     * @author mr.g
     * @param code
     * @return
     **/
    TbResourceSms getByCode(String code);

}
