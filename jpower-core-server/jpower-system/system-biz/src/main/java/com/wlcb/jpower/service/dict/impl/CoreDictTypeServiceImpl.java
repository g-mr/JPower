package com.wlcb.jpower.service.dict.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wlcb.jpower.dbs.dao.dict.TbCoreDictTypeDao;
import com.wlcb.jpower.dbs.dao.dict.mapper.TbCoreDictTypeMapper;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDict;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDictType;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.module.mp.support.LambdaTreeWrapper;
import com.wlcb.jpower.service.dict.CoreDictService;
import com.wlcb.jpower.service.dict.CoreDictTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wlcb.jpower.module.tenant.TenantConstant.DEFAULT_TENANT_CODE;

/**
 * @author mr.gmac
 */
@AllArgsConstructor
@Service("tbCoreDictTypeService")
public class CoreDictTypeServiceImpl extends BaseServiceImpl<TbCoreDictTypeMapper, TbCoreDictType> implements CoreDictTypeService {

    private TbCoreDictTypeDao coreDictTypeDao;
    private CoreDictService coreDictService;

    @Override
    public List<Tree<String>> tree() {
//        LambdaQueryWrapper<TbCoreDictType> queryWrapper = Condition.getTreeWrapper(TbCoreDictType::getId,
//                TbCoreDictType::getParentId,
//                TbCoreDictType::getDictTypeName,
//                TbCoreDictType::getDictTypeCode,
//                TbCoreDictType::getIsTree)
//                .lambda();
//        if (SecureUtil.isRoot()){
//            queryWrapper.eq(TbCoreDictType::getTenantCode,DEFAULT_TENANT_CODE);
//        }
//        return coreDictTypeDao.tree(queryWrapper.orderByAsc(TbCoreDictType::getSortNum));
        LambdaTreeWrapper<TbCoreDictType> queryWrapper = Condition.getLambdaTreeWrapper(TbCoreDictType.class,TbCoreDictType::getId,
                TbCoreDictType::getParentId)
                .select(TbCoreDictType::getDictTypeName,
                        TbCoreDictType::getDictTypeCode,
                        TbCoreDictType::getIsTree);
        if (SecureUtil.isRoot()){
            queryWrapper.eq(TbCoreDictType::getTenantCode,DEFAULT_TENANT_CODE);
        }
        return coreDictTypeDao.tree(queryWrapper.orderByAsc(TbCoreDictType::getSortNum));
    }

    @Override
    public List<Tree<String>> listTree(TbCoreDictType dictType) {
//        LambdaQueryWrapper<TbCoreDictType> queryWrapper =
//                SecureUtil.isRoot()
//                ? Condition.getQueryWrapper(dictType).lambda().eq(TbCoreDictType::getTenantCode,DEFAULT_TENANT_CODE).orderByAsc(TbCoreDictType::getSortNum)
//                : Condition.getQueryWrapper(dictType).lambda().orderByAsc(TbCoreDictType::getSortNum);
//        return coreDictTypeDao.listTree(queryWrapper, DictTypeVo.class);
        LambdaTreeWrapper<TbCoreDictType> queryWrapper =
                SecureUtil.isRoot()
                        ? Condition.getLambdaTreeWrapper(dictType,TbCoreDictType::getId,TbCoreDictType::getParentId).eq(TbCoreDictType::getTenantCode,DEFAULT_TENANT_CODE).orderByAsc(TbCoreDictType::getSortNum)
                        : Condition.getLambdaTreeWrapper(dictType,TbCoreDictType::getId,TbCoreDictType::getParentId).orderByAsc(TbCoreDictType::getSortNum);
        return coreDictTypeDao.tree(queryWrapper);
    }

    @Override
    public Boolean deleteDictType(List<String> ids) {
        List<TbCoreDictType> listType = coreDictTypeDao.list(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                .in(TbCoreDictType::getId,ids)
                .eq(TbCoreDictType::getDelEnabled, ConstantsEnum.YN.Y.getValue()));
        if (listType.size() > 0){
            JpowerAssert.geZero(coreDictTypeDao.count(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                    .in(TbCoreDictType::getParentId,ids)), JpowerError.BUSINESS,"请先删除下级字典类型");
        }

        if (coreDictTypeDao.removeReal(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                .in(TbCoreDictType::getId,ids)
                .eq(TbCoreDictType::getDelEnabled, ConstantsEnum.YN.Y.getValue()))){
            listType.forEach(type ->
                coreDictService.removeReal(Condition.<TbCoreDict>getQueryWrapper()
                        .lambda()
                        .eq(TbCoreDict::getDictTypeCode,type.getDictTypeCode())
                        .eq(TbCoreDict::getTenantCode,type.getTenantCode()))
            );
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean addDictType(TbCoreDictType dictType) {
        dictType.setParentId(Fc.isBlank(dictType.getParentId())? JpowerConstants.TOP_CODE:dictType.getParentId());
        dictType.setDelEnabled(Fc.isBlank(dictType.getDelEnabled())? ConstantsEnum.YN.Y.getValue() :dictType.getDelEnabled());

        LambdaQueryWrapper<TbCoreDictType> queryWrapper = Condition.<TbCoreDictType>getQueryWrapper().lambda().eq(TbCoreDictType::getDictTypeCode,dictType.getDictTypeCode());
        if (SecureUtil.isRoot()){
            String tenant = Fc.isBlank(dictType.getTenantCode())?DEFAULT_TENANT_CODE:dictType.getTenantCode();
            dictType.setTenantCode(tenant);
            queryWrapper.eq(TbCoreDictType::getTenantCode,tenant);
        }
        JpowerAssert.geZero(coreDictTypeDao.count(queryWrapper),JpowerError.BUSINESS,"该字典类型已存在");

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
                if (SecureUtil.isRoot()){
                    queryWrapper.eq(TbCoreDict::getTenantCode,coreDictType.getTenantCode());
                }
                coreDictService.update(queryWrapper);
            }
            return true;
        }
        return false;
    }

}
