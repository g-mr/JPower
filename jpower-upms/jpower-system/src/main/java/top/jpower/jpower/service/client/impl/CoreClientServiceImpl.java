package top.jpower.jpower.service.client.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.RandomUtil;
import top.jpower.jpower.dbs.dao.client.TbCoreClientDao;
import top.jpower.jpower.dbs.dao.client.mapper.TbCoreClientMapper;
import top.jpower.jpower.dbs.entity.client.TbCoreClient;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.jpower.service.client.CoreClientService;

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
    public Long queryIdByCode(String clientCode) {
        return coreClientDao.queryIdByCode(clientCode);
    }

    @Override
    public boolean saveOrUpdate(TbCoreClient coreClient){
        if (Fc.isNull(coreClient.getId())){
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
