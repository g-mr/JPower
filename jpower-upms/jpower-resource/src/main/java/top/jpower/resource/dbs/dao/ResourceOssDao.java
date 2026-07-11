package top.jpower.resource.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.utils.Fc;
import top.jpower.resource.dbs.dao.mapper.ResourceOssMapper;
import top.jpower.resource.dbs.entity.ResourceOss;
import top.jpower.system.api.dto.SelectDTO;

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
     * 获取默认资源
     *
     * @author mr.g
     * @param storageType 存储类型
     * @return 资源详情
     **/
    public ResourceOss getDefaultByCode(String storageType) {
        if (Fc.isBlank(storageType)) {
            ResourceOss resourceOss = super.getOne(Wrappers.getQueryWrapper().eq(ResourceOss::getIsDefault, true).limit(1));
            if (resourceOss == null) {
                return super.getById(1L);
            }
            return resourceOss;
        }
        return getByCode(storageType);
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
