package com.wlcb.jpower.module.common.service.core.params.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.params.CoreParamService;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.dbs.dao.core.params.TbCoreParamsDao;
import com.wlcb.jpower.module.dbs.dao.core.params.mapper.TbCoreParamsMapper;
import com.wlcb.jpower.module.dbs.entity.core.params.TbCoreParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mr.gmac
 */
@Service("coreParamService")
public class CoreParamServiceImpl extends BaseServiceImpl<TbCoreParamsMapper,TbCoreParam> implements CoreParamService {

    @Autowired
    private TbCoreParamsMapper paramsMapper;
    @Autowired
    private TbCoreParamsDao paramsDao;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String selectByCode(String code) {
        return paramsMapper.selectByCode(code);
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
    public Integer delete(String id) {

        TbCoreParam coreParam = paramsDao.getById(id);

        Boolean c = paramsDao.removeById(id);

        if (c){
            redisUtils.remove(coreParam.getCode());
        }

        return c?1:0;
    }

    @Override
    public Integer update(TbCoreParam coreParam) {
        return paramsDao.updateById(coreParam)?1:0;
    }

    @Override
    public Integer add(TbCoreParam coreParam) {
        coreParam.setUpdateUser(coreParam.getCreateUser());
        return paramsDao.save(coreParam)?1:0;
    }

    @Override
    public void effectAll() {
        List<TbCoreParam> params = list(new TbCoreParam());

        for (TbCoreParam param : params) {
            if (StringUtils.isNotBlank(param.getValue())){
                redisUtils.set(CacheNames.PARAMS_REDIS_KEY+param.getCode(),param.getValue());
            }
        }
    }

}
