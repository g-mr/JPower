package com.qidiangk.smart.resource.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.qidiangk.smart.common.constants.CacheNames;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.resource.dbs.dao.ResourceFileDao;
import com.qidiangk.smart.resource.dbs.dao.ResourceFileGroupDao;
import com.qidiangk.smart.resource.dbs.dao.mapper.ResourceFileMapper;
import com.qidiangk.smart.resource.dbs.entity.ResourceFile;
import com.qidiangk.smart.resource.dbs.entity.ResourceFileGroupDO;
import com.qidiangk.smart.resource.pojo.MoveBO;
import com.qidiangk.smart.resource.service.ResourceFileService;

import java.util.List;
import java.util.Map;

/**
 * 文件服务实现类
 * <p>
 * 提供文件管理服务的具体实现
 * </p>
 *
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class ResourceFileServiceImpl extends BaseServiceImpl<ResourceFileMapper, ResourceFile> implements ResourceFileService {

    private final ResourceFileDao coreFileDao;
    private final ResourceFileGroupDao fileGroupDao;


    @Override
    public Boolean add(ResourceFile coreFile) {
        return coreFileDao.save(coreFile);
    }

    @Override
    public ResourceFile getById(Long id) {
        return coreFileDao.getById(id);
    }

    @Override
    public Pg<ResourceFile> listPage(Map<String, Object> map) {
        return coreFileDao.listPage(map);
    }

	@Override
	public byte[] getContentById(Long id) {
		return coreFileDao.getContentById(id);
	}

	@Override
	public ResourceFile detailFile(String id) {
		return coreFileDao.detailFile(Fc.toLong(id));
	}

	@Override
	public boolean updateById(ResourceFile file) {
		//不可修改项
		file.setContent(null);
		file.setStorageType(null);
		file.setPath(null);

		CacheUtil.clear(CacheNames.FILE_KEY);
		return coreFileDao.updateById(file);
	}

	@Override
	public boolean move(MoveBO moveBO) {
		return coreFileDao.move(moveBO);
	}

	@Override
	public Boolean addGroup(ResourceFileGroupDO group) {
		return fileGroupDao.save(group);
	}

	@Override
	public Boolean deleteGroup(Long id) {
		coreFileDao.moveUnGroup(id);
		return fileGroupDao.removeById(id);
	}

	@Override
	public boolean updateGroup(ResourceFileGroupDO group) {
		return fileGroupDao.updateById(group);
	}

	@Override
	public List<ResourceFileGroupDO> listGroup(Map<String, Object> map) {
		return fileGroupDao.list(map);
	}
}
