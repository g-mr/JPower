package top.jpower.jpower.service.params.impl;

import org.springframework.stereotype.Service;
import top.jpower.jpower.dbs.dao.params.mapper.TbCoreParamsMapper;
import top.jpower.jpower.dbs.entity.params.TbCoreParam;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.service.params.CoreParamService;

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
