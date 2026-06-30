package com.qidiangk.smart.resource.dbs.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import com.qidiangk.smart.resource.dbs.dao.mapper.ResourceFileGroupMapper;
import com.qidiangk.smart.resource.dbs.entity.ResourceFileGroupDO;

import java.util.List;
import java.util.Map;

/**
 * 文件分组管理
 *
 * @author mr.g
 */
@Repository
@RequiredArgsConstructor
public class ResourceFileGroupDao extends JpowerServiceImpl<ResourceFileGroupMapper, ResourceFileGroupDO> {

    /**
     * 查询文件分组
     *
     * @author mr.g
     * @param map 查询条件
     * @return List<ResourceFileGroupDO> 分页结果
     */
    public List<ResourceFileGroupDO> list(Map<String, Object> map) {
        return super.list(Wrappers.getQueryWrapper(map).orderBy(ResourceFileGroupDO::getCreateTime).desc());
    }

}
