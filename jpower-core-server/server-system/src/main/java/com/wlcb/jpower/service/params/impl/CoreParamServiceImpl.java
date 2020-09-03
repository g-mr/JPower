package com.wlcb.jpower.service.params.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.dbs.dao.params.TbCoreParamsDao;
import com.wlcb.jpower.dbs.dao.params.mapper.TbCoreParamsMapper;
import com.wlcb.jpower.dbs.entity.params.TbCoreParam;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.CacheUtil;
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
    private RedisUtil redisUtil;

    @Override
    public String selectByCode(String code) {
        return baseMapper.selectByCode(code);
    }

    @Override
    public List<TbCoreParam> list(TbCoreParam coreParam) {

        LambdaQueryWrapper<TbCoreParam> wrapper = new QueryWrapper<TbCoreParam>().lambda();

        if (StringUtils.isNotBlank(coreParam.getCode())){
            wrapper.eq(TbCoreParam::getCode,coreParam.getCode());
        }

        if (StringUtils.isNotBlank(coreParam.getName())){
            wrapper.eq(TbCoreParam::getName,coreParam.getName());
        }

        if (StringUtils.isNotBlank(coreParam.getName())){
            wrapper.like(TbCoreParam::getValue,coreParam.getValue());
        }

        wrapper.orderByDesc(TbCoreParam::getCreateTime);

        return paramsDao.list(wrapper);
    }

    @Override
    public Boolean delete(String id) {
        TbCoreParam coreParam = paramsDao.getById(id);
        Boolean c = paramsDao.removeById(id);
        if (c){
            CacheUtil.evict(CacheNames.PARAMS_REDIS_CACHE,CacheNames.PARAMS_REDIS_CODE_KEY,coreParam.getCode());
        }
        return c;
    }

    @Override
    public Boolean update(TbCoreParam coreParam) {
        return paramsDao.updateById(coreParam);
    }

    @Override
    public void effectAll() {
        List<TbCoreParam> params = paramsDao.list();
        for (TbCoreParam param : params) {
            if (StringUtils.isNotBlank(param.getValue())){
                CacheUtil.put(CacheNames.PARAMS_REDIS_CACHE,CacheNames.PARAMS_REDIS_CODE_KEY,param.getCode(),param.getValue());
            }
        }
    }

}
