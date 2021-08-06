package com.wlcb.jpower.cache;

import com.wlcb.jpower.dbs.entity.TbCoreFile;
import com.wlcb.jpower.feign.FileClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.SpringUtil;


/**
 * @ClassName UserCache
 * @Description TODO 用户缓存
 * @Author 郭丁志
 * @Version 1.0
 */
public class FileCache {

    private static FileClient fileClient;

    static {
        fileClient = SpringUtil.getBean(FileClient.class);
    }

    public static TbCoreFile getFileDetail(String base) {
        return CacheUtil.get(CacheNames.FILE_REDIS_CACHE,CacheNames.FILE_BASE_KEY, base,() -> {
            ResponseData<TbCoreFile> responseData = fileClient.getFileDetail(base);
            return responseData.getData();
        });
    }
}
