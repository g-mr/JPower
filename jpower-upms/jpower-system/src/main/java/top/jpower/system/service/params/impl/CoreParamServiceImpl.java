package top.jpower.system.service.params.impl;

import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.system.dbs.dao.params.mapper.CoreParamsMapper;
import top.jpower.system.dbs.entity.params.CoreParam;
import top.jpower.system.service.params.CoreParamService;

/**
 * 参数服务实现
 * 
 * @author mr.g
 */
@Service
public class CoreParamServiceImpl extends BaseServiceImpl<CoreParamsMapper, CoreParam> implements CoreParamService {

    @Override
    public String selectByCode(String code) {
        return baseMapper.selectByCode(code);
    }

}
