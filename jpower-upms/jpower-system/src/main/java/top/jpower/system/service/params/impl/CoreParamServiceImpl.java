package top.jpower.system.service.params.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.system.dbs.dao.params.CoreParamsDao;
import top.jpower.system.dbs.dao.params.mapper.CoreParamsMapper;
import top.jpower.system.dbs.entity.params.CoreParam;
import top.jpower.system.service.params.CoreParamService;

import java.util.List;
import java.util.Map;

import static top.jpower.common.constants.CacheNames.PARAM_KEY;
import static top.jpower.common.constants.ServiceCodeConstants.CODE_EXIST;

/**
 * 参数服务实现
 * 
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class CoreParamServiceImpl extends BaseServiceImpl<CoreParamsMapper, CoreParam> implements CoreParamService {

	private final CoreParamsDao coreParamsDao;

    @Override
    public String selectByCode(String code) {
        return coreParamsDao.selectValueByCode(code);
    }

	public boolean removeByIds(List<Long> ids) {
		CacheUtil.clear(PARAM_KEY);
		return coreParamsDao.removeByIds(ids);
	}

	/**
	 * 更新参数
	 *
	 * @param coreParam 参数
	 * @return 是否成功
	 */
	@Override
	public boolean updateById(CoreParam coreParam) {
		CacheUtil.clear(PARAM_KEY);
		return coreParamsDao.updateById(coreParam);
	}

	/**
	 * 保存参数
	 *
	 * @param coreParam 参数
	 * @return 是否成功
	 */
	@Override
	public Long create(CoreParam coreParam) {
		JpowerAssert.notTrue(coreParamsDao.existsByField(CoreParam::getCode, coreParam.getCode()), JpowerError.Business, CODE_EXIST);
		CacheUtil.clear(PARAM_KEY);
		coreParamsDao.save(coreParam);
		return coreParam.getId();
	}

	@Override
	public Pg<CoreParam> pageByMap(Map<String, Object> map) {
		return coreParamsDao.pg(Wrappers.getQueryWrapper(map).orderBy(CoreParam::getCreateTime).desc());
	}

}
