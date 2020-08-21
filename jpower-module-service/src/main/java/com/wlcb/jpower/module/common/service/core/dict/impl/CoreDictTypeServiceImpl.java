package com.wlcb.jpower.module.common.service.core.dict.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.dict.CoreDictService;
import com.wlcb.jpower.module.common.service.core.dict.CoreDictTypeService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.dbs.dao.core.dict.TbCoreDictTypeDao;
import com.wlcb.jpower.module.dbs.dao.core.dict.mapper.TbCoreDictTypeMapper;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDictType;
import com.wlcb.jpower.module.mp.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return coreDictTypeDao.tree(Condition.getTreeWrapper(TbCoreDictType::getId,
                TbCoreDictType::getParentId,
                TbCoreDictType::getDictTypeName)
                .lambda().orderByAsc(TbCoreDictType::getSortNum));
    }

    @Override
    public Boolean deleteDictType(List<String> ids) {
        List<Object> listCode = coreDictTypeDao.listObjs(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                .select(TbCoreDictType::getDictTypeCode)
                .in(TbCoreDictType::getId,ids)
                .eq(TbCoreDictType::getDelEnabled, ConstantsEnum.YN.Y.getValue()));
        if (listCode.size() > 0){
            Integer count = coreDictTypeDao.count(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                    .in(TbCoreDictType::getParentId,ids));
            JpowerAssert.notTrue(count>0, JpowerError.BUSINESS,"请先删除下级字典类型");
        }

        if (coreDictTypeDao.removeReal(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                .in(TbCoreDictType::getId,ids)
                .eq(TbCoreDictType::getDelEnabled, ConstantsEnum.YN.Y.getValue()))){
            coreDictService.removeReal(Condition.<TbCoreDict>getQueryWrapper().lambda().in(TbCoreDict::getDictTypeCode,listCode));
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean addDictType(TbCoreDictType dictType) {
        dictType.setParentId(Fc.isBlank(dictType.getParentId())? JpowerConstants.TOP_CODE:dictType.getParentId());
        dictType.setDelEnabled(Fc.isBlank(dictType.getDelEnabled())? ConstantsEnum.YN.N.getValue() :dictType.getDelEnabled());
        dictType.setLocaleCode(Fc.isBlank(dictType.getLocaleCode())? ConstantsEnum.YYZL.CHINA.getValue() :dictType.getLocaleCode());

        JpowerAssert.notTrue(coreDictTypeDao.count(Condition.<TbCoreDictType>getQueryWrapper().lambda().eq(TbCoreDictType::getDictTypeCode,dictType.getDictTypeCode()))>0, JpowerError.BUSINESS,"该字典类型已存在");
        return coreDictTypeDao.save(dictType);
    }

    @Override
    public Boolean updateDictType(TbCoreDictType dictType) {
        TbCoreDictType coreDictType = coreDictTypeDao.getById(dictType.getId());

        if (coreDictTypeDao.updateById(dictType)){
            if (Fc.isNotBlank(dictType.getDictTypeCode())){
                coreDictService.update(new UpdateWrapper<TbCoreDict>().lambda()
                        .set(TbCoreDict::getDictTypeCode,dictType.getDictTypeCode())
                        .eq(TbCoreDict::getDictTypeCode,coreDictType.getDictTypeCode()));
            }
            return true;
        }
        return false;
    }


}
