package top.jpower.system.service.dict.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.common.constants.CacheNames;
import top.jpower.common.enums.YYZLEnum;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.WebUtil;
import top.jpower.system.api.dto.SelectDTO;
import top.jpower.system.dbs.dao.dict.CoreDictDao;
import top.jpower.system.dbs.dao.dict.CoreDictTypeDao;
import top.jpower.system.dbs.dao.dict.mapper.CoreDictMapper;
import top.jpower.system.dbs.entity.dict.CoreDict;
import top.jpower.system.dbs.entity.dict.CoreDictType;
import top.jpower.system.service.dict.CoreDictService;
import top.jpower.system.vo.DictVO;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static top.jpower.common.constants.ServiceCodeConstants.CODE_EXIST;
import static top.jpower.common.constants.ServiceCodeConstants.DELETE_CHILD;
import static top.jpower.common.constants.ServiceCodeConstants.DICT_EXIST_CHILD;
import static top.jpower.common.constants.ServiceCodeConstants.NOT_FOUND_DICT_RYPE;
import static top.jpower.common.enums.YYZLEnum.CHINA;
import static top.jpower.core.util.constants.JpowerConstants.I18N_KEY;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;

/**
 * 字典服务实现
 * 
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class CoreDictServiceImpl extends BaseServiceImpl<CoreDictMapper, CoreDict> implements CoreDictService {

    private final CoreDictDao dictDao;
	private final CoreDictTypeDao dictTypeDao;

    @Override
    public Long saveDict(CoreDict dict) {
		JpowerAssert.notTrue(dictTypeDao.existsByField(CoreDictType::getDictTypeCode, dict.getDictTypeCode()), JpowerError.NotFind, NOT_FOUND_DICT_RYPE);

        CoreDict coreDictType = dictDao.getByDictTypeCode(dict.getDictTypeCode(),dict.getCode());
        if(Fc.isNull(dict.getId())){
            dict.setLocale(Fc.isBlank(dict.getLocale()) ? CHINA.getValue() :dict.getLocale());
            dict.setIsStop(Fc.isNull(dict.getIsStop()) ? Boolean.FALSE : dict.getIsStop());
            dict.setParentId(Fc.notNull(dict.getParentId()) ? dict.getParentId() : Fc.toLong(TOP_CODE));
            JpowerAssert.notTrue(coreDictType != null, JpowerError.Business,CODE_EXIST);
        }else {
            JpowerAssert.notTrue(coreDictType != null && !NumberUtil.equals(dict.getId(),coreDictType.getId()), JpowerError.Business,CODE_EXIST);
        }

		CacheUtil.clear(CacheNames.DICT_KEY);
        dictDao.saveOrUpdate(dict);
		return dict.getId();
    }

    @Override
    public List<DictVO> listByType(Map<String, Object> map) {
        return dictDao.listByType(map);
    }

	@Override
	public Pg<DictVO> pageByType(Map<String, Object> map) {
		return dictDao.pageByType(map);
	}

    @Override
    public List<SelectDTO> listByTypeCode(String dictTypeCode) {
        String requestLocale = YYZLEnum.CHINA.getValue();
        if (Fc.notNull(WebUtil.getRequest())){
            requestLocale = Fc.toStr(Objects.requireNonNull(WebUtil.getRequest()).getHeader(I18N_KEY), YYZLEnum.CHINA.getValue());
        }
        return dictDao.listSelect(dictTypeCode, requestLocale);
    }


	@Override
	public boolean stopDict(Long id) {
		JpowerAssert.notTrue(dictDao.existsByParentIdNoStop(id), JpowerError.Business, DICT_EXIST_CHILD);
		CacheUtil.clear(CacheNames.DICT_KEY);
		return dictDao.stop(id);
	}

	@Override
	public boolean removeByIds(List<Long> ids) {
		JpowerAssert.notTrue(dictDao.existsByParentIds(ids), JpowerError.Business, DELETE_CHILD);
		CacheUtil.clear(CacheNames.DICT_KEY);
		return dictDao.removeRealByIds(ids);
	}

	@Override
	public List<Tree<Long>> tree(String dictTypeCode) {
		return dictDao.treeByType(dictTypeCode);
	}

}
