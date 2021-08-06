package controller;

import com.wlcb.jpower.dbs.entity.TbCoreFile;
import com.wlcb.jpower.feign.FileClient;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.DESUtil;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.operate.FileOperateBuilder;
import com.wlcb.jpower.service.CoreFileService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 文件实现
 * @Author mr.g
 **/
@ApiIgnore
@RestController
@RequestMapping("/core/file")
@AllArgsConstructor
public class FileClientController implements FileClient {

    private FileOperateBuilder operateBuilder;
    private CoreFileService coreFileService;

    @Override
    @SneakyThrows
    @PostMapping(value = "/serverUpload",produces="application/json")
    public ResponseData serverUpload(@RequestParam("file") MultipartFile file){
        TbCoreFile coreFile = operateBuilder
                .getBuilder(ConstantsEnum.FILE_STORAGE_TYPE.SERVER.getValue())
                .upload(file);
        return ReturnJsonUtil.ok("成功",coreFile.getMark());
    }

    @Override
    @SneakyThrows
    @PostMapping(value = "/fastDfsUpload",produces="application/json")
    public ResponseData fastDfsUpload(@RequestParam("file") MultipartFile file) {
        TbCoreFile coreFile = operateBuilder
                .getBuilder(ConstantsEnum.FILE_STORAGE_TYPE.FASTDFS.getValue())
                .upload(file);
        return ReturnJsonUtil.ok("成功",coreFile.getMark());
    }

    @Override
    @SneakyThrows
    @PostMapping(value = "/databaseUpload",produces="application/json")
    public ResponseData databaseUpload(@RequestParam("file") MultipartFile file) {
        TbCoreFile coreFile = operateBuilder
                .getBuilder(ConstantsEnum.FILE_STORAGE_TYPE.DATABASE.getValue())
                .upload(file);
        return ReturnJsonUtil.ok("成功",coreFile.getMark());
    }

    @Override
    @PostMapping(value = "/serverUpload",produces="application/json")
    public ResponseData<TbCoreFile> getFileDetail(@RequestParam String base) {
        String id = DESUtil.decrypt(base, ConstantsUtils.FILE_DES_KEY);
        JpowerAssert.notEmpty(id, JpowerError.Arg,"文件标识不合法");

        TbCoreFile coreFile = coreFileService.getById(id);
        coreFile.setContent(operateBuilder.getBuilder(coreFile.getStorageType()).getByte(coreFile));
        return ReturnJsonUtil.ok("成功",coreFile);
    }
}
