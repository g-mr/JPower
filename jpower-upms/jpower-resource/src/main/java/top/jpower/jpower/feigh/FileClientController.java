package top.jpower.jpower.feigh;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.common.enums.FileStorageTypeEnum;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.DesUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.FileUtil;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.jpower.feign.FileClient;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.jpower.operate.FileOperateBuilder;
import top.jpower.jpower.service.ResourceFileService;

import java.io.File;

/**
 * 文件实现
 * @Author mr.g
 **/
@ApiIgnore
@Api(hidden = true)
@RestController
@RequestMapping("/resource/file")
@AllArgsConstructor
public class FileClientController implements FileClient {

    private FileOperateBuilder operateBuilder;
    private ResourceFileService coreFileService;

    @Override
    @PostMapping(value = "/uploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces =  MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseData uploadFile(@RequestParam("file") File file,@RequestParam("storageType") String storageType){
        TbResourceFile coreFile = operateBuilder
                .getBuilder(storageType)
                .upload(FileUtil.readBytes(file), file.getName(), file.length());
        return ReturnJsonUtil.ok("成功",coreFile.getMark());
    }

    @Override
    @GetMapping(value = "/fileUrl",produces="application/json")
    public ResponseData<String> fileUrl(@RequestParam String base){
        String id = DesUtil.decrypt(base, DefaultValConstants.FILE_DES_KEY);
        JpowerAssert.notEmpty(id, JpowerError.Arg,"文件标识不合法");

        TbResourceFile coreFile = coreFileService.getById(Fc.toLong(id));
        return ReturnJsonUtil.data(operateBuilder
                .getBuilder(FileStorageTypeEnum.SERVER.getValue())
                .getUrl(coreFile));
    }

    @Override
    @GetMapping(value = "/getFileDetail",produces="application/json")
    public ResponseData<TbResourceFile> getFileDetail(@RequestParam String base) {
        String id = DesUtil.decrypt(base, DefaultValConstants.FILE_DES_KEY);
        JpowerAssert.notEmpty(id, JpowerError.Arg,"文件标识不合法");

        TbResourceFile coreFile = coreFileService.getById(Fc.toLong(id));
        coreFile.setContent(operateBuilder.getBuilder(coreFile.getStorageType()).getByte(coreFile));
        return ReturnJsonUtil.ok("成功",coreFile);
    }
}
