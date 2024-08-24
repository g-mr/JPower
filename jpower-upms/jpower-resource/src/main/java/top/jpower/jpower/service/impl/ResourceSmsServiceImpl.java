package top.jpower.jpower.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.jpower.dbs.dao.TbResourceSmsDao;
import top.jpower.jpower.dbs.dao.mapper.TbResourceSmsMapper;
import top.jpower.jpower.dbs.entity.TbResourceSms;
import top.jpower.jpower.module.service.impl.BaseServiceImpl;
import top.jpower.jpower.service.ResourceSmsService;

/**
 * <p>
 * 短信配置表 服务实现类
 * </p>
 *
 * @author mr.g
 * @since 2024-03-04
 */
@Service
@RequiredArgsConstructor
public class ResourceSmsServiceImpl extends BaseServiceImpl<TbResourceSmsMapper, TbResourceSms> implements ResourceSmsService {

    private final TbResourceSmsDao resourceSmsDao;

    /**
     * 通过code查询
     *
     * @param code
     * @return
     * @author mr.g
     **/
    @Override
    public TbResourceSms getByCode(String code) {
        return resourceSmsDao.getByCode(code);
    }
}
