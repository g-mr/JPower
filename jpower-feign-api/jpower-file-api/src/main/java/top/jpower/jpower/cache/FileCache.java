package top.jpower.jpower.cache;

import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.jpower.feign.FileClient;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.utils.CacheUtil;
import top.jpower.jpower.module.common.utils.SpringUtil;


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

    public static TbResourceFile getFileDetail(String base) {
        return CacheUtil.get(CacheNames.FILE_KEY,CacheNames.FILE_BASE_KEY, base,() -> {
            ResponseData<TbResourceFile> responseData = fileClient.getFileDetail(base);
            return responseData.getData();
        });
    }
}
