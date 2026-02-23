package top.jpower.resource.api.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import top.jpower.core.util.rsp.R;
import top.jpower.resource.api.dto.FileDTO;

import java.io.File;

/**
 * FILE 熔断
 * @author mr.g
 **/
@Component
@Slf4j
public class FileClientFallback implements FallbackFactory<FileClient> {
    @Override
    public FileClient create(Throwable cause) {
        return new FileClient() {

            @Override
            public R<Long> uploadFile(File file, String storageType) {
                return R.fail("上传服务器失败");
            }

            /**
             * 获取文件外链
             *
             * @param id
             * @author mr.g
             **/
            @Override
            public R<String> fileUrl(Long id) {
                return R.fail("获取文件外链失败");
            }

            @Override
            public R<FileDTO> getFileDetail(Long id) {
                return R.fail("获取文件详情失败");
            }
        };
    }
}
