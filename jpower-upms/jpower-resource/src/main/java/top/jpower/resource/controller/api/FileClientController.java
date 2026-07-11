package top.jpower.resource.controller.api;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.FileUtil;
import top.jpower.resource.api.dto.FileDTO;
import top.jpower.resource.api.feign.FileClient;
import top.jpower.resource.dbs.entity.ResourceFile;
import top.jpower.resource.service.ResourceFileService;
import top.jpower.resource.service.file.FileOperateBuilder;

import java.io.File;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 文件API客户端控制器
 * <p>
 * 提供文件相关的API接口实现
 * </p>
 *
 * @author mr.g
 */
@Hidden
@RestController
@RequestMapping("/feign/resource/file")
@RequiredArgsConstructor
public class FileClientController implements FileClient {

    private final FileOperateBuilder operateBuilder;
    private final ResourceFileService coreFileService;

    @Override
    @PostMapping(value = "/uploadFileByStorage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces =  MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public R<Long> uploadFile(@RequestParam("file") File file,@RequestParam("storageType") String storageType){
        ResourceFile coreFile = operateBuilder
                .getBuilder(storageType)
                .upload(FileUtil.readBytes(file), file.getName(), file.length(), null);
        return R.data(coreFile.getId());
    }

    @Override
    @PostMapping(value = "/uploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces =  MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public R<Long> uploadFile(@RequestParam("file") File file){
        ResourceFile coreFile = operateBuilder
                .getBuilder(null)
                .upload(FileUtil.readBytes(file), file.getName(), file.length(), null);
        return R.data(coreFile.getId());
    }

    @Override
    @GetMapping(value = "/fileUrl",produces=APPLICATION_JSON_VALUE)
    public R<String> fileUrl(@RequestParam Long id){

        ResourceFile coreFile = coreFileService.getById(id);
        return R.data(operateBuilder
                .getBuilder(coreFile.getStorageType())
                .getUrl(coreFile));
    }

    @Override
    @GetMapping(value = "/getFileDetail",produces=APPLICATION_JSON_VALUE)
    public R<FileDTO> getFileDetail(@RequestParam Long id) {
        ResourceFile coreFile = coreFileService.getById(id);
		FileDTO fileDTO = BeanUtil.copyProperties(coreFile, FileDTO.class);
		fileDTO.setContent(operateBuilder.getBuilder(coreFile.getStorageType()).getByte(coreFile));
        return R.data(fileDTO);
    }
}
