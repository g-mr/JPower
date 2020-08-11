package com.wlcb.jpower.module.common.service.core.params.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.params.CoreParamService;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
import com.wlcb.jpower.module.dbs.dao.core.params.TbCoreParamsDao;
import com.wlcb.jpower.module.dbs.dao.core.params.mapper.TbCoreParamsMapper;
import com.wlcb.jpower.module.dbs.entity.core.params.TbCoreParam;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mr.gmac
 */
@Service("coreParamService")
@AllArgsConstructor
public class CoreParamServiceImpl extends BaseServiceImpl<TbCoreParamsMapper,TbCoreParam> implements CoreParamService {

    private TbCoreParamsDao paramsDao;
    private RedisUtils redisUtils;

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
        Boolean c = paramsDao.removeRealById(id);
        if (c){
            redisUtils.remove(CacheNames.PARAMS_REDIS_KEY+coreParam.getCode());
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
                redisUtils.set(CacheNames.PARAMS_REDIS_KEY+param.getCode(),param.getValue());
            }
        }
    }

}
