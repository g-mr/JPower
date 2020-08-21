package com.wlcb.jpower.module.common.service.core.dict.impl;

import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.dict.CoreDictService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.dbs.dao.core.dict.TbCoreDictDao;
import com.wlcb.jpower.module.dbs.dao.core.dict.mapper.TbCoreDictMapper;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;
import com.wlcb.jpower.module.dbs.vo.DictVo;
import com.wlcb.jpower.module.mp.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mr.gmac
 */
@Service("coreDictService")
public class CoreDictServiceImpl extends BaseServiceImpl<TbCoreDictMapper,TbCoreDict> implements CoreDictService {

    @Autowired
    private TbCoreDictDao dictDao;

    @Override
    public TbCoreDict queryDictTypeByCode(String dictTypeCode, String code) {
        return dictDao.getOne(Condition.<TbCoreDict>getQueryWrapper().lambda()
                .eq(TbCoreDict::getDictTypeCode,dictTypeCode)
                .eq(TbCoreDict::getCode,code));
    }

    @Override
    public Boolean saveDict(TbCoreDict dict) {
        TbCoreDict coreDictType = queryDictTypeByCode(dict.getDictTypeCode(),dict.getCode());
        if(Fc.isBlank(dict.getId())){
            dict.setLocaleId(Fc.isBlank(dict.getLocaleId())? ConstantsEnum.YYZL.CHINA.getValue() :dict.getLocaleId());
            JpowerAssert.notTrue(coreDictType != null, JpowerError.BUSINESS,"该字典已存在");
        }else {
            JpowerAssert.notTrue(coreDictType != null && !StringUtil.equals(dict.getId(),coreDictType.getId()), JpowerError.BUSINESS,"该字典已存在");
        }
        return dictDao.saveOrUpdate(dict);
    }

    @Override
    public List<DictVo> listByType(TbCoreDict dict) {
        return dictDao.getBaseMapper().listByType(dict);
    }

}
