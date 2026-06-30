package com.qidiangk.smart.resource.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import com.qidiangk.smart.resource.dbs.dao.mapper.ResourceOssMapper;
import com.qidiangk.smart.resource.dbs.entity.ResourceOss;
import com.qidiangk.smart.system.api.dto.SelectDTO;

import java.util.List;

/**
 * OSS配置管理
 *
 * @author mr.g
 */
@Repository
public class ResourceOssDao extends JpowerServiceImpl<ResourceOssMapper, ResourceOss> {

    /**
     * 获取资源详情
     *
     * @author mr.g
     * @param code 编码
     * @return 资源详情
     **/
    public ResourceOss getByCode(String code) {
        return super.getOne(Wrappers.getQueryWrapper().eq(ResourceOss::getCode, code));
    }

    /**
     * 查询选择列表
     *
     * @author mr.g
     * @return
     **/
    public List<SelectDTO> listCodeName() {
        return super.listAs(Wrappers.getQueryWrapper().select(ResourceOss::getCode, ResourceOss::getName), SelectDTO.class);
    }
}
