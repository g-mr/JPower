package top.jpower.jpower.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import top.jpower.core.util.rsp.R;
import top.jpower.jpower.dto.FileDTO;

import java.io.File;

/**
 * FILE 熔断
 * @Author mr.g
 **/
@Component
@Slf4j
public class FileClientFallback implements FallbackFactory<FileClient> {
    @Override
    public FileClient create(Throwable cause) {
        return new FileClient() {

            @Override
            public R<Boolean> uploadFile(File file, String storageType) {
                return R.fail("上传服务器失败");
            }

            /**
             * 获取文件外链
             *
             * @param base
             * @return
             * @author mr.g
             **/
            @Override
            public R<String> fileUrl(String base) {
                return R.fail("获取文件外链失败");
            }

            @Override
            public R<FileDTO> getFileDetail(String base) {
                return R.fail("获取文件详情失败");
            }
        };
    }
}
