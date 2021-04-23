package com.wlcb.jpower.service.params.impl;

import com.wlcb.jpower.dbs.dao.params.TbCoreParamsDao;
import com.wlcb.jpower.dbs.dao.params.mapper.TbCoreParamsMapper;
import com.wlcb.jpower.dbs.entity.params.TbCoreParam;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.params.CoreParamService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mr.gmac
 */
@Service("coreParamService")
@AllArgsConstructor
public class CoreParamServiceImpl extends BaseServiceImpl<TbCoreParamsMapper, TbCoreParam> implements CoreParamService {

    private TbCoreParamsDao paramsDao;

    @Override
    public String selectByCode(String code) {
        return baseMapper.selectByCode(code);
    }

    @Override
    public Boolean deletes(String ids) {
        List<String> id = Fc.toStrList(ids);
        List<TbCoreParam> coreParam = paramsDao.listByIds(id);
        Boolean c = paramsDao.removeByIds(id);
        if (c){
            coreParam.forEach((param) -> CacheUtil.evict(CacheNames.PARAMS_REDIS_CACHE,CacheNames.PARAMS_REDIS_CODE_KEY,param.getCode(),Boolean.FALSE));
        }
        return c;
    }

    @Override
    public Boolean update(TbCoreParam coreParam) {
        return paramsDao.updateById(coreParam);
    }

    @Override
    public void effectAll() {
        List<TbCoreParam> params = paramsDao.list(Condition.<TbCoreParam>getQueryWrapper().lambda().eq(TbCoreParam::getIsEffect, ConstantsEnum.YN01.Y.getValue()));
        JpowerAssert.notGeZero(params.size(), JpowerError.BUSINESS,"不支持立即生效，需重启项目");
        for (TbCoreParam param : params) {
            if (StringUtils.isNotBlank(param.getValue())){
                CacheUtil.put(CacheNames.PARAMS_REDIS_CACHE,CacheNames.PARAMS_REDIS_CODE_KEY,param.getCode(),param.getValue(),Boolean.FALSE);
            }
        }
    }

}
