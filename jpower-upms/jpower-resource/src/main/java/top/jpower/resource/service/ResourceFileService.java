package top.jpower.resource.service;


import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import top.jpower.resource.dbs.entity.ResourceFile;
import top.jpower.resource.dbs.entity.ResourceFileGroupDO;
import top.jpower.resource.pojo.MoveBO;

import java.util.List;
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

	/**
	 * 移动文件
	 *
	 * @author mr.g
	 * @param moveBO 移动参数
	 * @return Boolean 是否移动成功
	 */
	boolean move(MoveBO moveBO);

	/**
	 * 新增文件分组
	 *
	 * @author mr.g
	 * @param group 文件分组实体
	 * @return Boolean 是否新增成功
	 */
	Boolean addGroup(ResourceFileGroupDO group);

	/**
	 * 删除文件分组
	 *
	 * @author mr.g
	 * @param id 文件分组ID
	 * @return Boolean 是否删除成功
	 */
	Boolean deleteGroup(Long id);

	/**
	 * 修改文件分组
	 *
	 * @author mr.g
	 * @param group 文件分组实体
	 * @return Boolean 是否修改成功
	 */
	boolean updateGroup(ResourceFileGroupDO group);

	/**
	 * 分页查询文件分组
	 *
	 * @author mr.g
	 * @param map 查询条件
	 * @return Pg<ResourceFileGroupDO> 分页结果
	 */
	List<ResourceFileGroupDO> listGroup(Map<String, Object> map);

}
