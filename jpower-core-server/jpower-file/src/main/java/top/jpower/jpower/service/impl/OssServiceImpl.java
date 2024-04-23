package top.jpower.jpower.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.jpower.dbs.dao.TbResourceOssDao;
import top.jpower.jpower.dbs.dao.mapper.TbResourceOssMapper;
import top.jpower.jpower.dbs.entity.TbResourceOss;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.service.OssService;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2024/4/23 3:04 PM
 */
@Service
@RequiredArgsConstructor
public class OssServiceImpl extends BaseServiceImpl<TbResourceOssMapper, TbResourceOss> implements OssService {

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
