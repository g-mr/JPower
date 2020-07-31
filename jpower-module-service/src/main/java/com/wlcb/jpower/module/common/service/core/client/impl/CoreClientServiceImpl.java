package com.wlcb.jpower.module.common.service.core.client.impl;

import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.client.CoreClientService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.RandomUtil;
import com.wlcb.jpower.module.dbs.dao.core.client.TbCoreClientDao;
import com.wlcb.jpower.module.dbs.dao.core.client.mapper.TbCoreClientMapper;
import com.wlcb.jpower.module.dbs.entity.core.client.TbCoreClient;
import com.wlcb.jpower.module.mp.support.Condition;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @ClassName CoreClientServiceImpl
 * @Description TODO 客户端service
 * @Author 郭丁志
 * @Date 2020-07-31 13:03
 * @Version 1.0
 */
@Service
@AllArgsConstructor
public class CoreClientServiceImpl extends BaseServiceImpl<TbCoreClientMapper, TbCoreClient> implements CoreClientService {

    private TbCoreClientDao coreClientDao;

    @Override
    public TbCoreClient loadClientByClientCode(String clientCode) {
        return coreClientDao.getOne(Condition.<TbCoreClient>getQueryWrapper().lambda()
                .eq(TbCoreClient::getClientCode,clientCode));
    }

    @Override
    public boolean saveOrUpdate(TbCoreClient coreClient){
        if (Fc.isBlank(coreClient.getId())){
            coreClient.setClientSecret(RandomUtil.randomString(6));
        }
        return coreClientDao.saveOrUpdate(coreClient);
    }

    @Override
    public boolean save(TbCoreClient coreClient){
        coreClient.setClientSecret(RandomUtil.randomString(6));
        return coreClientDao.save(coreClient);
    }
}
