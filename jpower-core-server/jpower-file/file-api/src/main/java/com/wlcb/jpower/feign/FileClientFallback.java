package com.wlcb.jpower.feign;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
            public ResponseData serverUpload(MultipartFile file) {
                return ReturnJsonUtil.fail("上传服务器失败");
            }

            @Override
            public ResponseData fastDfsUpload(MultipartFile file) {
                return ReturnJsonUtil.fail("上传FASTDFS失败");
            }

            @Override
            public ResponseData databaseUpload(MultipartFile file) {
                return ReturnJsonUtil.fail("上传数据库失败");
            }

            @Override
            public ResponseData getFileDetail(String base) {
                return ReturnJsonUtil.fail("获取文件详情失败");
            }
        };
    }
}
