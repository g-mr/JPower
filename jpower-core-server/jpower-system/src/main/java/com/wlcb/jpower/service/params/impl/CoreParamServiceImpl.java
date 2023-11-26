package com.wlcb.jpower.service.params.impl;

import com.wlcb.jpower.dbs.dao.params.mapper.TbCoreParamsMapper;
import com.wlcb.jpower.dbs.entity.params.TbCoreParam;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.service.params.CoreParamService;
import org.springframework.stereotype.Service;

/**
 * @author mr.gmac
 */
@Service("coreParamService")
public class CoreParamServiceImpl extends BaseServiceImpl<TbCoreParamsMapper, TbCoreParam> implements CoreParamService {

    @Override
    public String selectByCode(String code) {
        return baseMapper.selectByCode(code);
    }

}
