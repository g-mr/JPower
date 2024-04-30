package top.jpower.jpower.dbs.dao.client;

import org.springframework.stereotype.Repository;
import top.jpower.core.utils.utils.Fc;
import top.jpower.jpower.dbs.dao.client.mapper.TbCoreClientMapper;
import top.jpower.jpower.dbs.entity.client.TbCoreClient;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;
import top.jpower.jpower.module.mp.support.Condition;

/**
 * @ClassName TbCoreClientDao
 * @Description TODO 客户端DAO
 * @Author 郭丁志
 * @Date 2020-07-31 13:00
 * @Version 1.0
 */
@Repository
public class TbCoreClientDao extends JpowerServiceImpl<TbCoreClientMapper, TbCoreClient> {

    /**
     * 通过CODE查询客户端ID
     *
     * @author mr.g
     * @param code 客户端编码
     * @return id
     **/
    public Long queryIdByCode(String code){
        return super.getObj(Condition.<TbCoreClient>getQueryWrapper().lambda().select(TbCoreClient::getId).eq(TbCoreClient::getClientCode, code), Fc::toLong);
    }

}
