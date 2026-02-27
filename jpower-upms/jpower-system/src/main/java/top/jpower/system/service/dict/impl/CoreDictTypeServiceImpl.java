package top.jpower.system.service.dict.impl;

import cn.hutool.core.lang.tree.Tree;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.dao.dict.CoreDictDao;
import top.jpower.system.dbs.dao.dict.CoreDictTypeDao;
import top.jpower.system.dbs.dao.dict.mapper.CoreDictTypeMapper;
import top.jpower.system.dbs.entity.dict.CoreDictType;
import top.jpower.system.service.dict.CoreDictTypeService;

import java.util.List;

import static top.jpower.common.constants.ServiceCodeConstants.CODE_EXIST;
import static top.jpower.common.constants.ServiceCodeConstants.DELETE_CHILD;

/**
 * 字典类型服务实现
 * 
 * @author mr.g
 */
@Service
@AllArgsConstructor
public class CoreDictTypeServiceImpl extends BaseServiceImpl<CoreDictTypeMapper, CoreDictType> implements CoreDictTypeService {

    private CoreDictTypeDao coreDictTypeDao;
    private CoreDictDao coreDictDao;

    @Override
    public List<Tree<Long>> tree() {
        return coreDictTypeDao.tree();
    }

    @Override
    public Boolean deleteDictType(List<Long> ids) {
        List<String> listCode = coreDictTypeDao.listCodeByIdsDel(ids);
        if (listCode.size() > 0){
            JpowerAssert.notTrue(coreDictTypeDao.existsByQuery(ids), JpowerError.Business, DELETE_CHILD);
        }

        if (coreDictTypeDao.removeByIdsDel(ids)){
			coreDictDao.removeByTypeCode(listCode);
			CacheUtil.clear(CacheNames.DICT_KEY);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean addDictType(CoreDictType dictType) {
        dictType.setParentId(Fc.isNull(dictType.getParentId()) ? Fc.toLong(JpowerConstants.TOP_CODE) : dictType.getParentId());
        dictType.setDelEnabled(dictType.getDelEnabled() ? Boolean.TRUE : dictType.getDelEnabled());

        JpowerAssert.notTrue(coreDictTypeDao.existsByField(CoreDictType::getDictTypeCode,dictType.getDictTypeCode()), JpowerError.Business, CODE_EXIST);
		CacheUtil.clear(CacheNames.DICT_KEY);
        return coreDictTypeDao.save(dictType);
    }

    @Override
    public Boolean updateDictType(CoreDictType dictType) {
        String code = coreDictTypeDao.getCodeByIdsDel(dictType.getId());

        if (coreDictTypeDao.updateById(dictType)){
            if (Fc.isNotBlank(dictType.getDictTypeCode())){
				coreDictDao.updateDictTypeCode(dictType.getDictTypeCode(), code);
            }
			CacheUtil.clear(CacheNames.DICT_KEY);
            return true;
        }
        return false;
    }

}
