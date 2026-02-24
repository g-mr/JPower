package top.jpower.system.service.client;

import top.jpower.system.dbs.entity.client.CoreClient;
import top.jpower.core.dbs.service.BaseService;

/**
 * 客户端服务接口
 * 
 * @author mr.g
 */
public interface CoreClientService extends BaseService<CoreClient> {

    /**
     * 通过Code查询客户端详情
     * 
     * @author mr.g
     * @param clientCode 客户端编码
     * @return 客户端实体
     */
    CoreClient loadClientByClientCode(String clientCode);

    /**
     * 通过CODE查询客户端ID
     *
     * @author mr.g
     * @param clientCode 客户端编码
     * @return id
     **/
    Long queryIdByCode(String clientCode);


}
