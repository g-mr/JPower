package com.wlcb.jpower.feign;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileClient
 * @Author mr.g
 **/
@FeignClient(value = AppConstant.JPOWER_FILE, fallbackFactory = FileClientFallback.class, path = "/core/file")
public interface FileClient {

    /**
     * 向服务器保存文件
     * @Author mr.g
     * @param file
     * @return ResponseData
     **/
    @PostMapping(value = "/serverUpload",produces="application/json")
    ResponseData serverUpload(@RequestParam("file") MultipartFile file);

    /**
     * 向FASTDFS保存文件
     * @Author mr.g
     * @param file
     * @return ResponseData
     **/
    @PostMapping(value = "/fastDfsUpload",produces="application/json")
    ResponseData fastDfsUpload(@RequestParam("file") MultipartFile file);

    /**
     * 向数据库保存文件
     * @Author mr.g
     * @param file
     * @return ResponseData
     **/
    @PostMapping(value = "/databaseUpload",produces="application/json")
    ResponseData databaseUpload(@RequestParam("file") MultipartFile file);

    /**
     * 获取文件内容
     * @Author mr.g
     * @param base
     * @return ResponseData
     **/
    @GetMapping(value = "/getFileDetail",produces="application/json")
    ResponseData getFileDetail(@RequestParam String base);
}