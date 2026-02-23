package top.jpower.resource.api.cache;

import top.jpower.common.constants.CacheNames;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.resource.api.dto.FileDTO;
import top.jpower.resource.api.feign.FileClient;


/**
 * 文件缓存
 *
 * @author mr.g
 */
public class FileCache {

    private static final FileClient FILE_CLIENT;

    static {
        FILE_CLIENT = SpringUtil.getBean(FileClient.class);
    }

    public static FileDTO getFileDetail(Long id) {
        return CacheUtil.get(CacheNames.FILE_KEY,CacheNames.FILE_BASE_KEY, id,() -> {
            R<FileDTO> r = FILE_CLIENT.getFileDetail(id);
            return r.getData();
        });
    }
}
