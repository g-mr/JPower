package top.jpower.resource.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.resource.dbs.dao.ResourceOssDao;
import top.jpower.resource.dbs.dao.mapper.ResourceOssMapper;
import top.jpower.resource.dbs.entity.ResourceOss;
import top.jpower.resource.service.ResourceOssService;
import top.jpower.system.api.dto.SelectDTO;

import java.util.List;

/**
 * 对象存储服务实现类
 * <p>
 * 提供对象存储服务的具体实现
 * </p>
 *
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class ResourceOssServiceImpl extends BaseServiceImpl<ResourceOssMapper, ResourceOss> implements ResourceOssService {

    private final ResourceOssDao resourceOssDao;

    /**
     * 获取编码名称列表
     *
     * @author mr.g
     * @return List<SelectVO> 编码名称列表
     */
    @Override
    public List<SelectDTO> listCodeName() {
        return resourceOssDao.listCodeName();
    }
}
