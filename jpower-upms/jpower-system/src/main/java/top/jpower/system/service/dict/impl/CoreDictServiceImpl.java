package top.jpower.system.service.dict.impl;

import cn.hutool.core.lang.tree.Tree;
import top.jpower.core.util.utils.NumberUtil;
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
		JpowerAssert.isTrue(dictTypeDao.existsByField(CoreDictType::getDictTypeCode, dict.getDictTypeCode()), JpowerError.NotFind, NOT_FOUND_DICT_RYPE);

        CoreDict coreDict = dictDao.getByDictTypeCode(dict.getDictTypeCode(),dict.getCode());
        if(Fc.isNull(dict.getId())){
            dict.setLocale(Fc.isBlank(dict.getLocale()) ? CHINA.getValue() :dict.getLocale());
            dict.setIsStop(Fc.isNull(dict.getIsStop()) ? Boolean.FALSE : dict.getIsStop());
            dict.setParentId(Fc.notNull(dict.getParentId()) ? dict.getParentId() : Fc.toLong(TOP_CODE));
            JpowerAssert.notTrue(coreDict != null, JpowerError.Business,CODE_EXIST);
        }else {
            JpowerAssert.notTrue(coreDict != null && !NumberUtil.equals(dict.getId(),coreDict.getId()), JpowerError.Business,CODE_EXIST);
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
	public boolean stopDict(Long id, Boolean status) {
		JpowerAssert.notTrue(dictDao.existsByParentIdNoStop(id), JpowerError.Business, DICT_EXIST_CHILD);
		CacheUtil.clear(CacheNames.DICT_KEY);
		return dictDao.stop(id, status);
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

	/**
	 * 字典选择框
	 *
	 * @param dictTypeCode 字典类型编码
	 * @return 字典选择框
	 * @author mr.g
	 */
	@Override
	public List<Tree<Long>> dictSelect(String dictTypeCode) {
		List<Tree<Long>> treeList =  dictDao.dictSelect(dictTypeCode);
		convertTreeValue(treeList);
		return treeList;
	}

	/**
	 * 递归转换树节点中value字段为真实数据类型
	 *
	 * @param treeList 树列表
	 */
	private void convertTreeValue(List<Tree<Long>> treeList) {
		if (treeList == null) {
			return;
		}
		for (Tree<Long> tree : treeList) {
			Object value = tree.get("value");
			if (value instanceof String) {
				tree.put("value", convertValueType((String) value));
			}
			convertTreeValue(tree.getChildren());
		}
	}

	/**
	 * 智能识别字符串值并转换为真实类型
	 * <p>
	 * 转换优先级：Boolean > Integer > String
	 * 布尔值仅识别 "true" 和 "false"（不区分大小写）
	 * 整数识别标准Java数字格式（不含前导零，支持负号）
	 * </p>
	 *
	 * @param value 字符串值
	 * @return 转换后的值
	 */
	private Object convertValueType(String value) {
		if ("true".equalsIgnoreCase(value)) {
			return Boolean.TRUE;
		}
		if ("false".equalsIgnoreCase(value)) {
			return Boolean.FALSE;
		}
		if (NumberUtil.isCompleteInteger(value)) {
			return Integer.parseInt(value);
		}
		return value;
	}

}
