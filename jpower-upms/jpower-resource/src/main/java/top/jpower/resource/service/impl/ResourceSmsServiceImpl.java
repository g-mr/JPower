package top.jpower.resource.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.resource.dbs.dao.ResourceSmsDao;
import top.jpower.resource.dbs.dao.mapper.ResourceSmsMapper;
import top.jpower.resource.dbs.entity.ResourceSms;
import top.jpower.resource.service.ResourceSmsService;

/**
 * 短信配置服务实现类
 * <p>
 * 短信配置表服务实现类
 * </p>
 *
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class ResourceSmsServiceImpl extends BaseServiceImpl<ResourceSmsMapper, ResourceSms> implements ResourceSmsService {

    private final ResourceSmsDao resourceSmsDao;

    /**
     * 通过code查询短信配置
     *
     * @author mr.g
     * @param code 短信编码
     * @return TbResourceSms 短信配置实体
     */
    @Override
    public ResourceSms getByCode(String code) {
        return resourceSmsDao.getByCode(code);
    }
}
