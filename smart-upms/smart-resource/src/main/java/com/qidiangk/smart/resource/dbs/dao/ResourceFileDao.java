package com.qidiangk.smart.resource.dbs.dao;

import com.mybatisflex.core.util.UpdateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.GuavaCache;
import top.jpower.core.util.utils.MapUtil;
import com.qidiangk.smart.resource.dbs.dao.mapper.ResourceFileMapper;
import com.qidiangk.smart.resource.dbs.entity.ResourceFile;
import com.qidiangk.smart.resource.dbs.entity.ResourceOss;
import com.qidiangk.smart.resource.pojo.MoveBO;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 文件管理
 *
 * @author mr.g
 */
@Repository
@RequiredArgsConstructor
public class ResourceFileDao extends JpowerServiceImpl<ResourceFileMapper, ResourceFile> {

    private final ResourceOssDao resourceOssDao;
    private final GuavaCache<ResourceOss> cache = GuavaCache.getInstance(5L, TimeUnit.MINUTES);

    public Pg<ResourceFile> listPage(Map<String, Object> map) {
		Pg<ResourceFile> page = getMapper().page(PaginationContext.page(), Wrappers.getQueryWrapper(map).orderBy(ResourceFile::getCreateTime).desc());
        page.getList().forEach(f->{
            if (!MapUtil.containsKey(f.getParams(), "storageType") || Fc.isBlank(MapUtil.getStr(f.getParams(), "storageType"))){
                Map<String, Object> mapParams = f.getParams();
                if (Fc.isNull(mapParams)){
                    mapParams = new HashMap<>(1);
                }
                ResourceOss resourceOss = cache.isExist(f.getStorageType()) ? cache.get(f.getStorageType())
                        : resourceOssDao.getByCode(f.getStorageType());
                if (Fc.notNull(resourceOss)){
                    mapParams.put("storageType", resourceOss.getName());
                    f.setParams(mapParams);
                    cache.put(f.getStorageType(), resourceOss);
                }
            }
        });
        return page;
    }

	public byte[] getContentById(Long id) {
		return super.getObjAs(Wrappers.getQueryWrapper()
				.select(ResourceFile::getContent)
				.eq(ResourceFile::getId, id), byte[].class);
	}

	public ResourceFile detailFile(Long id) {
		return super.getOne(Wrappers.getQueryWrapper()
				.select(ResourceFile::getPath,ResourceFile::getContent,ResourceFile::getName,ResourceFile::getStorageType)
				.eq(ResourceFile::getId,id));
	}

	/**
	 * 移动文件
	 *
	 * @param moveBO 移动参数
	 * @return 是否成功
	 */
	public boolean move(MoveBO moveBO) {
		return super.update(UpdateEntity.of(ResourceFile.class).setGroupId(moveBO.getGroupId()), Wrappers.getQueryWrapper().in(ResourceFile::getId, moveBO.getIds()));
	}

	/**
	 * 移除分组
	 *
	 * @param groupId 分组ID
	 */
	public void moveUnGroup(Long groupId) {
		super.update(UpdateEntity.of(ResourceFile.class).setGroupId(null), Wrappers.getQueryWrapper().eq(ResourceFile::getGroupId, groupId));
	}
}
