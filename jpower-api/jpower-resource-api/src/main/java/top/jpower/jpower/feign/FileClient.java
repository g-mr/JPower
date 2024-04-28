package top.jpower.jpower.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.utils.constants.AppConstant;

import java.io.File;

/**
 * FileClient
 * @Author mr.g
 **/
@FeignClient(value = AppConstant.JPOWER_RESOURCE, fallbackFactory = FileClientFallback.class, path = "/resource/file")
public interface FileClient {

    /**
     * 向服务器保存文件
     * @Author mr.g
     * @param file
     * @return ResponseData
     **/
    @PostMapping(value = "/uploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces =  MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    ResponseData uploadFile(@RequestPart("file") File file,@RequestParam("storageType") String storageType);

    /**
     * 获取文件外链
     * @author mr.g
     * @param base
     * @return
     **/
    @GetMapping(value = "/fileUrl",produces="application/json")
    ResponseData<String> fileUrl(@RequestParam("base") String base);

    /**
     * 获取文件内容
     * @Author mr.g
     * @param base
     * @return ResponseData
     **/
    @GetMapping(value = "/getFileDetail",produces="application/json")
    ResponseData<TbResourceFile> getFileDetail(@RequestParam("base") String base);
}