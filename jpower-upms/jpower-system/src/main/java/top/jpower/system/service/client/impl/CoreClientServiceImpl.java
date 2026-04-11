package top.jpower.system.service.client.impl;

import cn.hutool.core.util.NumberUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.RandomUtil;
import top.jpower.system.dbs.dao.client.CoreClientDao;
import top.jpower.system.dbs.dao.client.mapper.CoreClientMapper;
import top.jpower.system.dbs.entity.client.CoreClient;
import top.jpower.system.service.client.CoreClientService;
import top.jpower.system.vo.SelectIdNameVO;
import top.jpower.system.vo.SelectVO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static top.jpower.common.constants.ServiceCodeConstants.CODE_EXIST;
import static top.jpower.common.constants.ServiceCodeConstants.NOT_FOUND_DATA;
import static top.jpower.common.constants.ServiceCodeConstants.REFRESH_TOKEN_LESS_ACCESS_TOKEN;

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
    public Optional<Long> queryIdByCode(String clientCode) {
        return coreClientDao.queryIdByCode(clientCode);
    }

	@Override
	public Long createOrUpdate(CoreClient coreClient) {
		if (Fc.isNull(coreClient.getId())){
			JpowerAssert.notTrue(coreClient.getRefreshTokenValidity() <= coreClient.getAccessTokenValidity(), JpowerError.Arg,REFRESH_TOKEN_LESS_ACCESS_TOKEN);
			JpowerAssert.notTrue(coreClientDao.existsByField(CoreClient::getClientCode,coreClient.getClientCode()), JpowerError.Business, CODE_EXIST);
		}else {
			CoreClient client =coreClientDao.getById(coreClient.getId());
			JpowerAssert.notNull(client, JpowerError.NotFind, NOT_FOUND_DATA);

			long refreshTokenValidity = Fc.isNull(coreClient.getRefreshTokenValidity())?client.getRefreshTokenValidity():coreClient.getRefreshTokenValidity();
			long accessTokenValidity = Fc.isNull(coreClient.getAccessTokenValidity())?client.getAccessTokenValidity():coreClient.getAccessTokenValidity();
			JpowerAssert.notTrue(refreshTokenValidity <= accessTokenValidity,JpowerError.Arg,REFRESH_TOKEN_LESS_ACCESS_TOKEN);

			Optional<Long> idOpt  = queryIdByCode(coreClient.getClientCode());
			idOpt.ifPresent(id -> JpowerAssert.isTrue(NumberUtil.equals(id,client.getId()), JpowerError.Business, CODE_EXIST));
		}

		CacheUtil.clear(CacheNames.CLIENT_KEY);
		coreClientDao.saveOrUpdate(coreClient);
		return coreClient.getId();
	}

	@Override
	public Pg<CoreClient> page(Map<String, Object> map) {
		return coreClientDao.pg(Wrappers.getQueryWrapper(map).orderBy(CoreClient::getSortNum).asc());
	}

	@Override
	public List<SelectIdNameVO> select() {
		return coreClientDao.select();
	}

	@Override
	public List<SelectVO> selectCode() {
		return coreClientDao.selectCode();
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
