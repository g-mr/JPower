package top.jpower.jpower.dbs.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.GuavaCache;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.jpower.dbs.dao.mapper.TbResourceFileMapper;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.jpower.dbs.entity.TbResourceOss;
import top.jpower.jpower.module.page.PaginationContext;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;
import top.jpower.jpower.module.mp.support.Condition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TbCoreParamsDao
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-03 13:30
 * @Version 1.0
 */
@Repository
@RequiredArgsConstructor
public class TbResourceFileDao extends JpowerServiceImpl<TbResourceFileMapper, TbResourceFile> {

    private final TbResourceOssDao resourceOssDao;
    private final GuavaCache<TbResourceOss> cache = GuavaCache.getInstance(5L, TimeUnit.MINUTES);

    public Page<TbResourceFile> listPage(Map<String, Object> map) {
        Page<TbResourceFile> page = super.page(PaginationContext.getMpPage(), Condition.getQueryWrapper(map,TbResourceFile.class).lambda().orderByDesc(TbResourceFile::getCreateTime));
        page.getRecords().forEach(f->{
            if (!MapUtil.containsKey(f.getParams(), "storageType") || Fc.isBlank(MapUtil.getStr(f.getParams(), "storageType"))){
                Map<String, Object> mapParams = f.getParams();
                if (Fc.isNull(mapParams)){
                    mapParams = new HashMap<>(1);
                }
                TbResourceOss resourceOss = cache.isExist(f.getStorageType()) ? cache.get(f.getStorageType())
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
}
