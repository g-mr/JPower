package top.jpower.jpower.feigh;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.jpower.feign.FileClient;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.utils.DesUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.FileUtil;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;
import top.jpower.jpower.module.common.utils.constants.ConstantsEnum;
import top.jpower.jpower.module.common.utils.constants.ConstantsUtils;
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
        String id = DesUtil.decrypt(base, ConstantsUtils.FILE_DES_KEY);
        JpowerAssert.notEmpty(id, JpowerError.Arg,"文件标识不合法");

        TbResourceFile coreFile = coreFileService.getById(Fc.toLong(id));
        return ReturnJsonUtil.data(operateBuilder
                .getBuilder(ConstantsEnum.FILE_STORAGE_TYPE.SERVER.getValue())
                .getUrl(coreFile));
    }

    @Override
    @GetMapping(value = "/getFileDetail",produces="application/json")
    public ResponseData<TbResourceFile> getFileDetail(@RequestParam String base) {
        String id = DesUtil.decrypt(base, ConstantsUtils.FILE_DES_KEY);
        JpowerAssert.notEmpty(id, JpowerError.Arg,"文件标识不合法");

        TbResourceFile coreFile = coreFileService.getById(Fc.toLong(id));
        coreFile.setContent(operateBuilder.getBuilder(coreFile.getStorageType()).getByte(coreFile));
        return ReturnJsonUtil.ok("成功",coreFile);
    }
}
