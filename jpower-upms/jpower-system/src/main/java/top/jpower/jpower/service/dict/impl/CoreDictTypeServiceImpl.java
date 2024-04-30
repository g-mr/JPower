package top.jpower.jpower.service.dict.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.utils.constants.ConstantsEnum;
import top.jpower.core.utils.constants.JpowerConstants;
import top.jpower.core.utils.utils.Fc;
import top.jpower.jpower.dbs.dao.dict.TbCoreDictTypeDao;
import top.jpower.jpower.dbs.dao.dict.mapper.TbCoreDictTypeMapper;
import top.jpower.jpower.dbs.entity.dict.TbCoreDict;
import top.jpower.jpower.dbs.entity.dict.TbCoreDictType;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.dict.CoreDictService;
import top.jpower.jpower.service.dict.CoreDictTypeService;

import java.util.List;

/**
 * @author mr.gmac
 */
@AllArgsConstructor
@Service("tbCoreDictTypeService")
public class CoreDictTypeServiceImpl extends BaseServiceImpl<TbCoreDictTypeMapper, TbCoreDictType> implements CoreDictTypeService {

    private TbCoreDictTypeDao coreDictTypeDao;
    private CoreDictService coreDictService;

    @Override
    public List<Tree<Long>> tree() {
        return coreDictTypeDao.tree(Condition.getLambdaTreeWrapper(TbCoreDictType.class, TbCoreDictType::getId, TbCoreDictType::getParentId)
                .select(TbCoreDictType::getDictTypeName,
                        TbCoreDictType::getDictTypeCode,
                        TbCoreDictType::getIsTree).orderByAsc(TbCoreDictType::getSortNum));
    }

    @Override
    public Boolean deleteDictType(List<Long> ids) {
        List<TbCoreDictType> listType = coreDictTypeDao.list(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                .in(TbCoreDictType::getId,ids)
                .eq(TbCoreDictType::getDelEnabled, ConstantsEnum.YN.Y.getValue()));
        if (listType.size() > 0){
            JpowerAssert.geZero(coreDictTypeDao.count(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                    .in(TbCoreDictType::getParentId,ids)), JpowerError.Business,"请先删除下级字典类型");
        }

        if (coreDictTypeDao.removeReal(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                .in(TbCoreDictType::getId,ids)
                .eq(TbCoreDictType::getDelEnabled, ConstantsEnum.YN.Y.getValue()))){
            listType.forEach(type ->
                coreDictService.removeReal(Condition.<TbCoreDict>getQueryWrapper()
                        .lambda()
                        .eq(TbCoreDict::getDictTypeCode,type.getDictTypeCode()))
            );
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean addDictType(TbCoreDictType dictType) {
        dictType.setParentId(Fc.isNull(dictType.getParentId())? Fc.toLong(JpowerConstants.TOP_CODE):dictType.getParentId());
        dictType.setDelEnabled(Fc.isBlank(dictType.getDelEnabled())? ConstantsEnum.YN.Y.getValue() :dictType.getDelEnabled());

        LambdaQueryWrapper<TbCoreDictType> queryWrapper = Condition.<TbCoreDictType>getQueryWrapper().lambda().eq(TbCoreDictType::getDictTypeCode,dictType.getDictTypeCode());
        JpowerAssert.geZero(coreDictTypeDao.count(queryWrapper),JpowerError.Business,"该字典类型已存在");

        return coreDictTypeDao.save(dictType);
    }

    @Override
    public Boolean updateDictType(TbCoreDictType dictType) {
        TbCoreDictType coreDictType = coreDictTypeDao.getById(dictType.getId());

        if (coreDictTypeDao.updateById(dictType)){
            if (Fc.isNotBlank(dictType.getDictTypeCode())){
                LambdaUpdateWrapper<TbCoreDict> queryWrapper = new UpdateWrapper<TbCoreDict>().lambda()
                        .set(TbCoreDict::getDictTypeCode,dictType.getDictTypeCode())
                        .eq(TbCoreDict::getDictTypeCode,coreDictType.getDictTypeCode());
                coreDictService.update(queryWrapper);
            }
            return true;
        }
        return false;
    }

}
