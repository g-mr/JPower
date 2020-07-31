package com.wlcb.jpower.module.common.service.core.client;

import com.wlcb.jpower.module.common.service.base.BaseService;
import com.wlcb.jpower.module.dbs.entity.core.client.TbCoreClient;

/**
 * @author mr.gmac
 */
public interface CoreClientService extends BaseService<TbCoreClient> {

    /**
     * @Author 郭丁志
     * @Description //TODO 通过Code查询客户端详情
     * @Date 13:11 2020-07-31
     * @Param [clientCode]
     * @return com.wlcb.jpower.module.dbs.entity.core.client.TbCoreClient
     **/
    TbCoreClient loadClientByClientCode(String clientCode);
}
