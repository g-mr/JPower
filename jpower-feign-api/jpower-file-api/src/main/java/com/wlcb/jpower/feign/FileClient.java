package com.wlcb.jpower.feign;

import com.wlcb.jpower.dbs.entity.TbCoreFile;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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
    @PostMapping(value = "/serverUpload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces =  MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    ResponseData serverUpload(@RequestPart("file") MultipartFile file);

    /**
     * 向FASTDFS保存文件
     * @Author mr.g
     * @param file
     * @return ResponseData
     **/
    @PostMapping(value = "/fastDfsUpload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces =  MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    ResponseData fastDfsUpload(@RequestPart("file") MultipartFile file);

    /**
     * 向数据库保存文件
     * @Author mr.g
     * @param file
     * @return ResponseData
     **/
    @PostMapping(value = "/databaseUpload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces =  MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    ResponseData databaseUpload(@RequestPart("file") MultipartFile file);

    /**
     * 获取文件内容
     * @Author mr.g
     * @param base
     * @return ResponseData
     **/
    @GetMapping(value = "/getFileDetail",produces="application/json")
    ResponseData<TbCoreFile> getFileDetail(@RequestParam String base);
}