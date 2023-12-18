package top.jpower.jpower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jpower.jpower.dbs.dao.TbCoreFileDao;
import top.jpower.jpower.dbs.dao.mapper.TbCoreFileMapper;
import top.jpower.jpower.dbs.entity.TbCoreFile;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.service.CoreFileService;

/**
 * @author mr.gmac
 */
@Service("coreFileService")
public class CoreFileServiceImpl extends BaseServiceImpl<TbCoreFileMapper, TbCoreFile> implements CoreFileService {

    @Autowired
    private TbCoreFileDao coreFileDao;


    @Override
    public Boolean add(TbCoreFile coreFile) {
        return coreFileDao.save(coreFile);
    }

    @Override
    public TbCoreFile getById(Long id) {
        return coreFileDao.getById(id);
    }

    @Override
    public String getPathById(Long id) {
        return coreFileDao.getObj(new QueryWrapper<TbCoreFile>().lambda().select(TbCoreFile::getPath).eq(TbCoreFile::getId, id), Fc::toStr);
    }
}
