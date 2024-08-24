package top.jpower.jpower.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.jpower.dbs.dao.TbResourceOssDao;
import top.jpower.jpower.dbs.dao.mapper.TbResourceOssMapper;
import top.jpower.jpower.dbs.entity.TbResourceOss;
import top.jpower.jpower.module.service.impl.BaseServiceImpl;
import top.jpower.jpower.service.ResourceOssService;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2024/4/23 3:04 PM
 */
@Service
@RequiredArgsConstructor
public class ResourceOssServiceImpl extends BaseServiceImpl<TbResourceOssMapper, TbResourceOss> implements ResourceOssService {

    private final TbResourceOssDao resourceOssDao;

    /**
     * 列表
     *
     * @return
     * @author mr.g
     **/
    @Override
    public List<Map<String, Object>> listCodeName() {
        return resourceOssDao.listCodeName();
    }
}
