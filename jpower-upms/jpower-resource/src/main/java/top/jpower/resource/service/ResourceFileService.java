package top.jpower.resource.service;


import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import top.jpower.resource.dbs.entity.ResourceFile;

import java.util.Map;

/**
 * 文件服务接口
 * <p>
 * 提供文件管理的相关服务方法
 * </p>
 *
 * @author mr.g
 */
public interface ResourceFileService extends BaseService<ResourceFile> {

    /**
     * 新增一个文件
     *
     * @author mr.g
     * @param file 文件实体
     * @return Boolean 是否新增成功
     */
    Boolean add(ResourceFile file);

    ResourceFile getById(Long id);

	Pg<ResourceFile> listPage(Map<String, Object> map);

	/**
	 * 根据ID获取文件内容
	 *
	 * @author mr.g
	 * @param id 文件ID
	 * @return byte[] 文件内容
	 */
	byte[] getContentById(Long id);

	ResourceFile detailFile(String id);

	@Override
	boolean updateById(ResourceFile file);
}
