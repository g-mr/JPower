package com.wlcb.jpower.module.common.service.core.city;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.module.dbs.dao.core.city.mapper.TbCoreCityMapper;
import com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
public interface CoreCityService extends IService<TbCoreCity> {

    /**
     * @Author 郭丁志
     * @Description //TODO 通过code查询下级元素
     * @Date 09:59 2020-07-16
     * @Param [code]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    List<Map<String, Object>> listChild(Map<String, Object> city);

    /**
     * @Author 郭丁志
     * @Description //TODO 查询行政取区域列表
     * @Date 11:16 2020-07-23
     * @Param [coreCity]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity>
     *
     * @param coreCity*/
    List<TbCoreCity> list(TbCoreCity coreCity);

    /**
     * @author 郭丁志
     * @Description //TODO 新增或者新增行政区域
     * @date 17:55 2020/7/25 0025
     * @param coreCity
     * @return java.lang.Boolean
     */
    @Override
    boolean save(TbCoreCity coreCity);

    /**
     * @author 郭丁志
     * @Description //TODO 通过编号查询城市
     * @date 19:20 2020/7/25 0025
     * @param cityCode
     * @return com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity
     */
    TbCoreCity queryByCode(String cityCode);

    /**
     * @author 郭丁志
     * @Description //TODO 批量删除行政区域
     * @date 22:30 2020/7/25 0025
     * @param ids
     * @return java.lang.Boolean
     */
    Boolean deleteBatch(List<String> ids);

    /**
     * @Author 郭丁志
     * @Description //TODO 懒加载树形结构
     * @Date 23:18 2020-07-25
     * @Param [pcode]
     * @return java.util.List<com.wlcb.jpower.module.common.node.Node>
     **/
    List<Node> lazyTree(String pcode);
}
