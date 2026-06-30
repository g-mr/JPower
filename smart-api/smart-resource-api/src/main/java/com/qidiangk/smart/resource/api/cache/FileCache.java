package com.qidiangk.smart.resource.api.cache;

import com.qidiangk.smart.common.constants.CacheNames;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.SpringUtil;
import com.qidiangk.smart.resource.api.dto.FileDTO;
import com.qidiangk.smart.resource.api.feign.FileClient;


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
