package top.jpower.resource.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.util.rsp.R;
import top.jpower.resource.api.dto.FileDTO;

import java.io.File;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 文件客户端
 * 
 * @author mr.g
 **/
@FeignClient(value = AppConstant.JPOWER_RESOURCE, fallbackFactory = FileClientFallback.class, path = "/feign/resource/file")
public interface FileClient {

    /**
     * 向服务器保存文件
     * @author mr.g
     * @param file
     * @return R
     **/
    @PostMapping(value = "/uploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces =  MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    R<Long> uploadFile(@RequestPart("file") File file,@RequestParam("storageType") String storageType);

    /**
     * 获取文件外链
     * @author mr.g
     * @param id
     * @return
     **/
    @GetMapping(value = "/fileUrl",produces=APPLICATION_JSON_VALUE)
    R<String> fileUrl(@RequestParam("base") Long id);

    /**
     * 获取文件内容
     * @author mr.g
     * @param id
     * @return R
     **/
    @GetMapping(value = "/getFileDetail",produces=APPLICATION_JSON_VALUE)
    R<FileDTO> getFileDetail(@RequestParam("base") Long id);
}