package com.wlcb.jpower.dbs.dao.client;

import com.wlcb.jpower.dbs.dao.client.mapper.TbCoreClientMapper;
import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.module.mp.support.Condition;
import org.springframework.stereotype.Repository;

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
    public String queryIdByCode(String code){
        return super.getObj(Condition.<TbCoreClient>getQueryWrapper().lambda().select(TbCoreClient::getId).eq(TbCoreClient::getClientCode, code), Fc::toStr);
    }

}
