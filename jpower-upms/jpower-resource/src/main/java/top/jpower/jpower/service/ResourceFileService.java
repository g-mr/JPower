package top.jpower.jpower.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.core.dbs.service.BaseService;

import java.util.Map;

/**
 * @author mr.gmac
 */
public interface ResourceFileService extends BaseService<TbResourceFile> {

    /**
     * @Author 郭丁志
     * @Description //TODO 新增一个文件
     * @Date 16:41 2020-07-20
     * @Param [coreFile]
     * @return java.lang.Boolean
     **/
    Boolean add(TbResourceFile coreFile);

    TbResourceFile getById(Long id);

    /**
     * @Author 郭丁志
     * @Description //TODO 获取文件路径
     * @Date 17:18 2020-07-20
     * @Param [id]
     * @return top.jpower.jpower.module.dbs.entity.core.file.TbCoreFile
     **/
    String getPathById(Long id);

    Page<TbResourceFile> listPage(Map<String, Object> map);
}
