package top.jpower.jpower.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import top.jpower.core.utils.utils.ReturnJsonUtil;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.jpower.module.base.vo.ResponseData;

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
            public ResponseData uploadFile(File file, String storageType) {
                return ReturnJsonUtil.fail("上传服务器失败");
            }

            /**
             * 获取文件外链
             *
             * @param base
             * @return
             * @author mr.g
             **/
            @Override
            public ResponseData<String> fileUrl(String base) {
                return ReturnJsonUtil.fail("获取文件外链失败");
            }

            @Override
            public ResponseData<TbResourceFile> getFileDetail(String base) {
                return ReturnJsonUtil.fail("获取文件详情失败");
            }
        };
    }
}
