package top.jpower.system.service.city;


import cn.hutool.core.lang.tree.Tree;
import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.dbs.entity.city.CoreCity;
import top.jpower.system.vo.CityVo;

import java.util.List;
import java.util.Map;

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
     * @param city 城市查询条件
     * @return 下级元素列表
     */
    List<Map<String, Object>> listChild(Map<String, Object> city);

    /**
     * 查询行政区域列表
     *
     * @author mr.g
     * @param coreCity 查询条件
     * @return 城市列表
     */
    List<CoreCity> list(CoreCity coreCity);

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

    Boolean update(CoreCity coreCity);

    CityVo getById(Long id);
}
