package com.wlcb.jpower.module.dbs.dao.core.city.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName TbCoreParamsDao
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-03 13:30
 * @Version 1.0
 */
@Component("tbCoreCityMapper")
public interface TbCoreCityMapper extends BaseMapper<TbCoreCity> {

    List<Node> lazyTree(String parentCode, Map<String, Object> param);

}
