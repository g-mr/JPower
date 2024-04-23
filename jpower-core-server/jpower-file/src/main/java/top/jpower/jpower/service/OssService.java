package top.jpower.jpower.service;

import top.jpower.jpower.dbs.entity.TbResourceOss;
import top.jpower.jpower.module.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2024/4/23 3:03 PM
 */
public interface OssService extends BaseService<TbResourceOss> {

    /**
     * 列表
     *
     * @author mr.g
     * @param
     * @return
     **/
    List<Map<String, Object>> listCodeName();

}
