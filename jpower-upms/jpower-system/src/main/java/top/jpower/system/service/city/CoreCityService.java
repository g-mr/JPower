package top.jpower.system.service.city;


import cn.hutool.core.lang.tree.Tree;
import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.dbs.entity.city.CoreCity;
import top.jpower.system.vo.CityVO;
import top.jpower.system.vo.SelectVO;

import java.util.List;

/**
 * 城市服务接口
 *
 * @author mr.g
 */
public interface CoreCityService extends BaseService<CoreCity> {

    /**
     * 通过code查询下级元素
     *
     * @author mr.g
     * @param pcode 父级code
	 * @param name 名称
     * @return 下级元素列表
     */
    List<SelectVO> listChild(String pcode, String name);

    /**
     * 新增行政区域
     *
     * @author mr.g
     * @param coreCity 行政区域信息
     * @return 是否新增成功
     */
    boolean add(CoreCity coreCity);

    /**
     * 通过编号查询城市
     *
     * @author mr.g
     * @param cityCode 城市编号
     * @return 城市实体
     */
	CoreCity queryByCode(String cityCode);

    /**
     * 批量删除行政区域
     *
     * @author mr.g
     * @param ids ID列表
     * @return 是否删除成功
     */
    Boolean deleteBatch(List<Long> ids);

    /**
     * 懒加载树形结构
     *
     * @author mr.g
     * @param pcode 父级编码
     * @return 树形结构列表
     */
    List<Tree<String>> lazyTree(String pcode);

	/**
     * 修改行政区域
     *
     * @author mr.g
     * @param coreCity 行政区域信息
     * @return 是否修改成功
     */
    Boolean update(CoreCity coreCity);

	/**
     * 通过ID查询城市
     *
     * @author mr.g
     * @param id ID
     * @return 城市信息
     */
	CityVO getById(Long id);
}
