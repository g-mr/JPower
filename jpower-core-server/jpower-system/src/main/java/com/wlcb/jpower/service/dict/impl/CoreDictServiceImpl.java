package com.wlcb.jpower.service.dict.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wlcb.jpower.dbs.dao.dict.TbCoreDictDao;
import com.wlcb.jpower.dbs.dao.dict.mapper.TbCoreDictMapper;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDict;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.dict.CoreDictService;
import com.wlcb.jpower.vo.DictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.wlcb.jpower.module.common.utils.constants.JpowerConstants.TOP_CODE;
import static com.wlcb.jpower.module.tenant.TenantConstant.DEFAULT_TENANT_CODE;

/**
 * @author mr.gmac
 */
@Service("coreDictService")
public class CoreDictServiceImpl extends BaseServiceImpl<TbCoreDictMapper, TbCoreDict> implements CoreDictService {

    @Autowired
    private TbCoreDictDao dictDao;

    @Override
    public TbCoreDict queryDictTypeByCode(String dictTypeCode, String code) {
        LambdaQueryWrapper<TbCoreDict> wrapper = Condition.<TbCoreDict>getQueryWrapper().lambda()
                .eq(TbCoreDict::getDictTypeCode,dictTypeCode)
                .eq(TbCoreDict::getCode,code);
        if(SecureUtil.isRoot()){
            wrapper.eq(TbCoreDict::getTenantCode,DEFAULT_TENANT_CODE);
        }
        return dictDao.getOne(wrapper);
    }

    @Override
    public Boolean saveDict(TbCoreDict dict) {
        TbCoreDict coreDictType = queryDictTypeByCode(dict.getDictTypeCode(),dict.getCode());
        if(Fc.isBlank(dict.getId())){
            dict.setLocaleId(Fc.isBlank(dict.getLocaleId())? ConstantsEnum.YYZL.CHINA.getValue() :dict.getLocaleId());
            dict.setParentId(Fc.isNotBlank(dict.getParentId())?dict.getParentId():TOP_CODE);
            JpowerAssert.notTrue(coreDictType != null, JpowerError.BUSINESS,"该字典已存在");
        }else {
            JpowerAssert.notTrue(coreDictType != null && !StringUtil.equals(dict.getId(),coreDictType.getId()), JpowerError.BUSINESS,"该字典已存在");
        }

        return dictDao.saveOrUpdate(dict);
    }

    @Override
    public List<DictVo> listByType(TbCoreDict dict) {
        if (SecureUtil.isRoot()) {
            dict.setTenantCode(Fc.isBlank(dict.getTenantCode()) ? DEFAULT_TENANT_CODE : dict.getTenantCode());
        } else {
            dict.setTenantCode(SecureUtil.getTenantCode());
        }
        return dictDao.getBaseMapper().listByType(dict);
    }

    @Override
    public List<Map<String, Object>> listByTypeCode(String dictTypeCode) {
        LambdaQueryWrapper<TbCoreDict> queryWrapper = Condition.<TbCoreDict>getQueryWrapper().lambda()
                .select(TbCoreDict::getCode,TbCoreDict::getName)
                .eq(TbCoreDict::getDictTypeCode,dictTypeCode);
        if (SecureUtil.isRoot()){
            queryWrapper.eq(TbCoreDict::getTenantCode,DEFAULT_TENANT_CODE);
        }
        //这里不能返回实体类，不然会造成字典回写的死循环
        return dictDao.listMaps(queryWrapper);
    }

}
