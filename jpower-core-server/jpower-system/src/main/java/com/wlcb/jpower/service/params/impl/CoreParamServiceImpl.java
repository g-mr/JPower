package com.wlcb.jpower.service.params.impl;

import com.wlcb.jpower.dbs.dao.params.TbCoreParamsDao;
import com.wlcb.jpower.dbs.dao.params.mapper.TbCoreParamsMapper;
import com.wlcb.jpower.dbs.entity.params.TbCoreParam;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.service.params.CoreParamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Boolean update(TbCoreParam coreParam) {
        return paramsDao.updateById(coreParam);
    }

}
