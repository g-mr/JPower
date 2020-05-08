package com.wlcb.jpower.module.common.service.params.impl;

import com.wlcb.jpower.module.common.service.params.ParamService;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
import com.wlcb.jpower.module.common.utils.UUIDUtil;
import com.wlcb.jpower.module.dbs.dao.core.params.TbCoreParamsMapper;
import com.wlcb.jpower.module.dbs.entity.core.params.TbCoreParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mr.gmac
 */
@Service("paramService")
public class ParamServiceImpl implements ParamService {

    @Autowired
    private TbCoreParamsMapper paramsMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String selectByCode(String code) {
        return paramsMapper.selectByCode(code);
    }

    @Override
    public List<TbCoreParam> list(TbCoreParam coreParam) {
        return paramsMapper.listAll(coreParam);
    }

    @Override
    public Integer delete(String id) {

        TbCoreParam coreParam = paramsMapper.selectById(id);

        Integer c = paramsMapper.deleteById(id);

        if (c > 0){
            redisUtils.remove(coreParam.getCode());
        }

        return c;
    }

    @Override
    public Integer update(TbCoreParam coreParam) {
        return paramsMapper.updateByPrimaryKeySelective(coreParam);
    }

    @Override
    public Integer add(TbCoreParam coreParam) {
        coreParam.setId(UUIDUtil.getUUID());
        coreParam.setUpdateUser(coreParam.getCreateUser());
        return paramsMapper.insert(coreParam);
    }

}
