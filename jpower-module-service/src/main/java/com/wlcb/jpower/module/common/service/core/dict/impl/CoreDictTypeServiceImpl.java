package com.wlcb.jpower.module.common.service.core.dict.impl;

import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.dict.CoreDictService;
import com.wlcb.jpower.module.common.service.core.dict.CoreDictTypeService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.module.dbs.dao.core.dict.TbCoreDictTypeDao;
import com.wlcb.jpower.module.dbs.dao.core.dict.mapper.TbCoreDictTypeMapper;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDictType;
import com.wlcb.jpower.module.mp.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Service("tbCoreDictTypeService")
public class CoreDictTypeServiceImpl extends BaseServiceImpl<TbCoreDictTypeMapper,TbCoreDictType> implements CoreDictTypeService {

    @Autowired
    private TbCoreDictTypeDao coreDictTypeDao;
    @Autowired
    private CoreDictService coreDictService;

    @Override
    public List<Node> tree() {
        return coreDictTypeDao.tree(Condition.getTreeWrapper(TbCoreDictType::getDictTypeCode,TbCoreDictType::getDictTypePcode,TbCoreDictType::getDictTypeName).lambda().orderByAsc(TbCoreDictType::getSortNum));
    }

    @Override
    public Boolean deleteDictType(List<String> ids) {
        List<Object> listCode = coreDictTypeDao.listObjs(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                .select(TbCoreDictType::getDictTypeCode)
                .in(TbCoreDictType::getId,ids)
                .eq(TbCoreDictType::getDelEnabled, ConstantsEnum.YN.Y.getValue()));
        if(listCode.size()>0){
            Integer count = coreDictTypeDao.count(Condition.<TbCoreDictType>getQueryWrapper().lambda().in(TbCoreDictType::getDictTypePcode,listCode));
            JpowerAssert.notTrue(count>0, JpowerError.BUSINESS,"请先删除下级字典类型");
        }
        if (coreDictTypeDao.remove(Condition.<TbCoreDictType>getQueryWrapper().lambda().in(TbCoreDictType::getId,ids).eq(TbCoreDictType::getDelEnabled, ConstantsEnum.YN.Y.getValue()))){
            coreDictService.remove(Condition.<TbCoreDict>getQueryWrapper().lambda().in(TbCoreDict::getDictTypeCode,listCode));
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean saveDictType(TbCoreDictType dictType) {
        TbCoreDictType coreDictType = coreDictTypeDao.getOne(Condition.<TbCoreDictType>getQueryWrapper().lambda().eq(TbCoreDictType::getDictTypeCode,dictType.getDictTypeCode()));
        if(Fc.isBlank(dictType.getId())){
            dictType.setDictTypePcode(Fc.isBlank(dictType.getDictTypePcode())? JpowerConstants.TOP_CODE:dictType.getDictTypePcode());
            dictType.setDelEnabled(Fc.isBlank(dictType.getDelEnabled())? ConstantsEnum.YN.N.getValue() :dictType.getDelEnabled());
            dictType.setLocaleCode(Fc.isBlank(dictType.getLocaleCode())? ConstantsEnum.YYZL.CHINA.getValue() :dictType.getLocaleCode());

            JpowerAssert.notTrue(coreDictType != null, JpowerError.BUSINESS,"该字典类型已存在");
        }else {
            JpowerAssert.notTrue(coreDictType != null && !StringUtil.equals(dictType.getId(),coreDictType.getId()), JpowerError.BUSINESS,"该字典类型已存在");
        }
        return coreDictTypeDao.saveOrUpdate(dictType);
    }


}
