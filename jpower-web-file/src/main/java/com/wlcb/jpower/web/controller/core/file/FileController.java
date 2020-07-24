package com.wlcb.jpower.web.controller.core.file;

import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.service.core.file.CoreFileService;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.dbs.entity.core.file.TbCoreFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName RoleController
 * @Description TODO 菜单相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 *
 */
@RestController
@RequestMapping("/core/file")
public class FileController extends BaseController {

    @Resource
    private CoreFileService coreFileService;

    /**
     * @Author 郭丁志
     * @Description //TODO 上传文件
     * @Date 15:59 2020-07-20
     * @Param [file]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/upload",method = {RequestMethod.POST},produces="application/json")
    public ResponseData upload(MultipartFile file){

        JpowerAssert.notTrue(file == null || file.isEmpty(),JpowerError.Arg,"文件不可为空");

        try {
            String path = MultipartFileUtil.saveFile(file,fileParentPath + File.separator +"file");

            File saveFile = new File(fileParentPath + File.separator +"file"+path);

            TbCoreFile coreFile = new TbCoreFile();
            coreFile.setPath("file" + File.separator +path);
            coreFile.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            coreFile.setFileSize(file.getSize());
            coreFile.setName(saveFile.getName());
            coreFile.setId(UUIDUtil.getUUID());

            Boolean is = coreFileService.add(coreFile);

            if (is){
                return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"获取成功", DESUtil.encrypt(coreFile.getId(), ConstantsUtils.FILE_DES_KEY),true);
            }else {
                FileUtil.deleteFile(saveFile);
                return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"文件保存失败", false);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("文件上传失败，e={}",e.getMessage());
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_ERROR,"文件上传失败", false);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 下载文件
     * @Date 17:08 2020-07-20
     * @Param [base]
     * @return void
     **/
    @RequestMapping(value = "/download",method = {RequestMethod.GET},produces="application/json")
    public void download(String base){
        JpowerAssert.notEmpty(base,JpowerError.Arg,"文件标识不可为空");

        String id = DESUtil.decrypt(base,ConstantsUtils.FILE_DES_KEY);

        JpowerAssert.notEmpty(id,JpowerError.Arg,"文件标识不合法");

        String path = coreFileService.getPathById(id);
        if(StringUtils.isBlank(path)){
            throw new BusinessException("文件不存在，无法下载");
        }

        File file = new File(fileParentPath+File.separator+path);
        if (!file.exists()){
            throw new BusinessException(file.getName()+"文件不存在，无法下载");
        }

        try {
            Integer is = FileUtil.download(file,getResponse(),file.getName());
            if (is != 0){
                throw new BusinessException(file.getName()+"文件下载失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件下载失败，e={}",e.getMessage());
            throw new BusinessException(file.getName()+"文件下载失败");
        }

    }
}
