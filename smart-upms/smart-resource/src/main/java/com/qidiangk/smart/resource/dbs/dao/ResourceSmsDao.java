package com.qidiangk.smart.resource.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import com.qidiangk.smart.resource.dbs.dao.mapper.ResourceSmsMapper;
import com.qidiangk.smart.resource.dbs.entity.ResourceSms;

/**
* <p>
 * 短信配置表 数据类
 * </p>
*
* @author mr.g
*/
@Repository
public class ResourceSmsDao extends JpowerServiceImpl<ResourceSmsMapper, ResourceSms> {

    /**
     * 通过编号获取
     * @author mr.g
     * @param code 编号
     * @return 短信资源
     **/
    public ResourceSms getByCode(String code) {
        return super.getOne(Wrappers.getQueryWrapper().eq(ResourceSms::getCode, code));
    }
}
