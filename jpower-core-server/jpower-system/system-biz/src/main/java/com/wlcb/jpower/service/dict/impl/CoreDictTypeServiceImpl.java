package com.wlcb.jpower.service.dict.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wlcb.jpower.dbs.dao.dict.TbCoreDictTypeDao;
import com.wlcb.jpower.dbs.dao.dict.mapper.TbCoreDictTypeMapper;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDict;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDictType;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.dict.CoreDictService;
import com.wlcb.jpower.service.dict.CoreDictTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mr.gmac
 */
@AllArgsConstructor
@Service("tbCoreDictTypeService")
public class CoreDictTypeServiceImpl extends BaseServiceImpl<TbCoreDictTypeMapper, TbCoreDictType> implements CoreDictTypeService {

    private TbCoreDictTypeDao coreDictTypeDao;
    private CoreDictService coreDictService;
    private RedisUtil redisUtil;

    @Override
    public List<Node> tree() {
        LambdaQueryWrapper<TbCoreDictType> queryWrapper = Condition.getTreeWrapper(TbCoreDictType::getId,
                TbCoreDictType::getParentId,
                TbCoreDictType::getDictTypeName,
                TbCoreDictType::getDictTypeCode,
                TbCoreDictType::getIsTree)
                .lambda();
        return coreDictTypeDao.tree(queryWrapper.orderByAsc(TbCoreDictType::getSortNum));
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
            redisUtil.removeMembers(CacheNames.DICT_REDIS_CODE_LIST,listCode.toArray());
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

        if (redisUtil.isMember(CacheNames.DICT_REDIS_CODE_LIST,dictType.getDictTypeCode())){
            throw new BusinessException("该字典类型已存在");
        }

        if (coreDictTypeDao.save(dictType)){
            redisUtil.add(CacheNames.DICT_REDIS_CODE_LIST,dictType.getDictTypeCode());
            return true;
        }else {
            return false;
        }
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

            redisUtil.removeMembers(CacheNames.DICT_REDIS_CODE_LIST,coreDictType.getDictTypeCode());
            redisUtil.add(CacheNames.DICT_REDIS_CODE_LIST,dictType.getDictTypeCode());
            return true;
        }
        return false;
    }

}
