package com.wlcb.jpower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.dbs.dao.TbCoreFileDao;
import com.wlcb.jpower.dbs.dao.mapper.TbCoreFileMapper;
import com.wlcb.jpower.dbs.entity.TbCoreFile;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.service.CoreFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

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
