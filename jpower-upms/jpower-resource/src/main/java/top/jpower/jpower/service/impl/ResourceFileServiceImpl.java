package top.jpower.jpower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.dbs.dao.TbResourceFileDao;
import top.jpower.jpower.dbs.dao.mapper.TbResourceFileMapper;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.service.ResourceFileService;

import java.util.Map;

/**
 * @author mr.gmac
 */
@Service
public class ResourceFileServiceImpl extends BaseServiceImpl<TbResourceFileMapper, TbResourceFile> implements ResourceFileService {

    @Autowired
    private TbResourceFileDao coreFileDao;


    @Override
    public Boolean add(TbResourceFile coreFile) {
        return coreFileDao.save(coreFile);
    }

    @Override
    public TbResourceFile getById(Long id) {
        return coreFileDao.getById(id);
    }

    @Override
    public String getPathById(Long id) {
        return coreFileDao.getObj(new QueryWrapper<TbResourceFile>().lambda().select(TbResourceFile::getPath).eq(TbResourceFile::getId, id), Fc::toStr);
    }

    @Override
    public Page<TbResourceFile> listPage(Map<String, Object> map) {
        return coreFileDao.listPage(map);
    }
}
