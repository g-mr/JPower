package com.wlcb.jpower.module.common.service.core.file.impl;

import com.wlcb.jpower.module.common.service.core.file.CoreFileService;
import com.wlcb.jpower.module.dbs.dao.core.file.TbCoreFileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mr.gmac
 */
@Service("coreFileService")
public class CoreFileServiceImpl implements CoreFileService {

    @Autowired
    private TbCoreFileDao coreFileDao;

}
