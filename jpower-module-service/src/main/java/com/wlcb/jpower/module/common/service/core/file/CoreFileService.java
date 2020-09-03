package com.wlcb.jpower.module.common.service.core.file;


import com.wlcb.jpower.module.common.service.BaseService;
import com.wlcb.jpower.module.dbs.entity.core.file.TbCoreFile;

/**
 * @author mr.gmac
 */
public interface CoreFileService extends BaseService<TbCoreFile> {

    /**
     * @Author 郭丁志
     * @Description //TODO 新增一个文件
     * @Date 16:41 2020-07-20
     * @Param [coreFile]
     * @return java.lang.Boolean
     **/
    Boolean add(TbCoreFile coreFile);

    TbCoreFile getById(String id);

    /**
     * @Author 郭丁志
     * @Description //TODO 获取文件路径
     * @Date 17:18 2020-07-20
     * @Param [id]
     * @return com.wlcb.jpower.module.dbs.entity.core.file.TbCoreFile
     **/
    String getPathById(String id);
}
