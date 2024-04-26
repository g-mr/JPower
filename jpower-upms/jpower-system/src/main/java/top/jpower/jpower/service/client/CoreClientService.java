package top.jpower.jpower.service.client;

import top.jpower.jpower.dbs.entity.client.TbCoreClient;
import top.jpower.jpower.module.common.service.BaseService;

/**
 * @author mr.gmac
 */
public interface CoreClientService extends BaseService<TbCoreClient> {

    /**
     * @Author 郭丁志
     * @Description //TODO 通过Code查询客户端详情
     * @Date 13:11 2020-07-31
     * @Param [clientCode]
     * @return top.jpower.jpower.module.dbs.entity.core.client.TbCoreClient
     **/
    TbCoreClient loadClientByClientCode(String clientCode);

    /**
     * 通过CODE查询客户端ID
     *
     * @author mr.g
     * @param clientCode 客户端编码
     * @return id
     **/
    Long queryIdByCode(String clientCode);

}
