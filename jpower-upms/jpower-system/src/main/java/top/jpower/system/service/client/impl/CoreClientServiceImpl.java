package top.jpower.system.service.client.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.RandomUtil;
import top.jpower.system.dbs.dao.client.CoreClientDao;
import top.jpower.system.dbs.dao.client.mapper.CoreClientMapper;
import top.jpower.system.dbs.entity.client.CoreClient;
import top.jpower.system.service.client.CoreClientService;

/**
 * 客户端服务实现
 * 
 * @author mr.g
 */
@Service
@AllArgsConstructor
@Slf4j
public class CoreClientServiceImpl extends BaseServiceImpl<CoreClientMapper, CoreClient> implements CoreClientService {

    private CoreClientDao coreClientDao;

    @Override
    public CoreClient loadClientByClientCode(String clientCode) {
        return coreClientDao.getOne(Condition.<CoreClient>getQueryWrapper().lambda()
                .eq(CoreClient::getClientCode,clientCode));
    }

    @Override
    public Long queryIdByCode(String clientCode) {
        return coreClientDao.queryIdByCode(clientCode);
    }

    @Override
    public boolean saveOrUpdate(CoreClient coreClient){
        if (Fc.isNull(coreClient.getId())){
            coreClient.setClientSecret(RandomUtil.randomString(6));
        }
        return coreClientDao.saveOrUpdate(coreClient);
    }

    @Override
    public boolean save(CoreClient coreClient){
        coreClient.setClientSecret(RandomUtil.randomString(6));
        return coreClientDao.save(coreClient);
    }
}
