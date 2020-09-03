package com.wlcb.jpower.module.common.service.core.file.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.file.CoreFileService;
import com.wlcb.jpower.module.dbs.dao.core.file.TbCoreFileDao;
import com.wlcb.jpower.module.dbs.dao.core.file.mapper.TbCoreFileMapper;
import com.wlcb.jpower.module.dbs.entity.core.file.TbCoreFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author mr.gmac
 */
@Service("coreFileService")
public class CoreFileServiceImpl extends BaseServiceImpl<TbCoreFileMapper,TbCoreFile> implements CoreFileService {

    @Autowired
    private TbCoreFileDao coreFileDao;

    @Override
    public Boolean add(TbCoreFile coreFile) {
        return coreFileDao.save(coreFile);
    }

    @Override
    public TbCoreFile getById(String id) {
        return coreFileDao.getById(id);
    }

    @Override
    public String getPathById(String id) {
        return coreFileDao.getObj(new QueryWrapper<TbCoreFile>().lambda().select(TbCoreFile::getPath).eq(TbCoreFile::getId, id), new Function<Object, String>() {
            @Override
            public String apply(Object o) {
                return (String) o;
            }
        });
    }
}
