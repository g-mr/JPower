package com.qidiangk.smart.system.dbs.dao.city;

import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.util.UpdateEntity;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.BaseDaoWrapper;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import com.qidiangk.smart.system.api.cache.SystemCache;
import com.qidiangk.smart.system.dbs.dao.city.mapper.CoreCityMapper;
import com.qidiangk.smart.system.dbs.entity.city.CoreCity;
import com.qidiangk.smart.system.vo.CityVO;
import com.qidiangk.smart.system.vo.SelectVO;

import java.util.List;

import static com.qidiangk.smart.system.dbs.entity.city.table.CoreCityTableDef.CORE_CITY;


/**
 * 城市数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreCityDao extends JpowerServiceImpl<CoreCityMapper, CoreCity> implements BaseDaoWrapper<CoreCity> {

    private void convert(CityVO city) {
		city.setPname(SystemCache.getCityName(city.getPcode()));
    }

	/**
	 * 根据ID获取城市信息
	 *
	 * @param id ID
	 * @return 城市信息
	 */
	public CityVO get(Long id) {
		return convert(super.getById(id), CityVO.class, this::convert);
	}

	/**
	 * 获取城市下拉列表
	 *
	 * @param pcode 上级CODE
	 * @param name 搜索名称
	 * @return 下拉数据
	 */
	public List<SelectVO> listCodeName(String pcode, String name) {
		return super.listAs(Wrappers.getQueryWrapper().select(CoreCity::getCode,CoreCity::getName)
						.eq(CoreCity::getPcode,pcode).like(CoreCity::getName,name)
						.orderBy(CoreCity::getSortNum).desc(), SelectVO.class);
	}

	/**
	 * 根据CODE修改上级CODE
	 *
	 * @param pcode 上级CODE
	 * @param code CODE
	 */
	public void updatePcodeByCode(String pcode, String code) {
		super.update(UpdateEntity.of(CoreCity.class).setPcode(pcode), Wrappers.getQueryWrapper().eq(CoreCity::getPcode, code));
	}

	/**
	 * 根据ID列表获取CODE列表
	 *
	 * @param ids ID列表
	 * @return CODE列表
	 */
	public List<String> listCodeByIds(List<Long> ids) {
		return super.objListAs(Wrappers.getQueryWrapper().select(CoreCity::getCode).in(CoreCity::getId, ids), String.class);
	}

	public List<Tree<String>> lazyTree(String pcode) {
		return super.tree(Wrappers.getTreeWrapper(CoreCity::getCode,CoreCity::getPcode)
				.lazy(pcode)
				.select(CORE_CITY.SORT_NUM.as("sort"), CORE_CITY.FULLNAME.as("name"), CORE_CITY.ID.as("key")));
	}
}
