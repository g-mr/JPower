package com.wlcb.jpower.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlcb.jpower.dbs.entity.TbCoreFile;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.exception.JpowerException;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.utils.DESUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.FileUtil;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.operate.FileOperateBuilder;
import com.wlcb.jpower.service.CoreFileService;
import com.wlcb.jpower.wrapper.BaseDictWrapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FileController
 * @Description TODO 文件相关
 * @Author 郭 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@Api(tags = "文件管理")
@RestController
@RequestMapping("/core/file")
public class FileController extends BaseController {

    @Value("${jpower.downloadPath:}")
    private String downloadPath;

    @Resource
    private CoreFileService coreFileService;
    @Resource
    private FileOperateBuilder operateBuilder;

    @ApiOperation("上传文件")
    @PostMapping(value = "/upload",produces="application/json")
    public ResponseData upload(@ApiParam("文件") @RequestParam(required = false) MultipartFile file,
                               @ApiParam(value = "存储类型 字典FILE_STORAGE_TYPE",defaultValue = "SERVER") @RequestParam(required = false,defaultValue = "SERVER") String storageType){
        JpowerAssert.notTrue(file == null || file.isEmpty(),JpowerError.Arg,"文件不可为空");
        try {
            TbCoreFile coreFile = operateBuilder.getBuilder(storageType).upload(file);
            if (Fc.notNull(coreFile)){
                return ReturnJsonUtil.ok("上传成功", coreFile.getMark());
            }else {
                return ReturnJsonUtil.fail("文件保存失败");
            }
        } catch (JpowerException je){
            throw je;
        } catch (Exception e){
            e.printStackTrace();
            logger.error("文件上传失败，e={}",e.getMessage());
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_ERROR,"文件上传失败", false);
        }
    }

    @ApiOperation("下载文件")
    @GetMapping(value = "/download",produces="application/json")
    public void download(@ApiParam(value = "文件标识",required = true) @RequestParam String base){
        JpowerAssert.notEmpty(base,JpowerError.Arg,"文件标识不可为空");
        String id = DESUtil.decrypt(base,ConstantsUtils.FILE_DES_KEY);
        JpowerAssert.notEmpty(id,JpowerError.Arg,"文件标识不合法");

        TbCoreFile coreFile = coreFileService.getById(id);
        try {
            operateBuilder.getBuilder(coreFile.getStorageType()).download(coreFile);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件下载失败，e={}",e.getMessage());
            throw new BusinessException(coreFile.getName()+"文件下载失败");
        }
    }

    @ApiOperation("文件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "name",value = "文件名称",paramType = "query",required = false),
            @ApiImplicitParam(name = "storageType_eq",value = "存储位置 字典FILE_STORAGE_TYPE",paramType = "query",required = false),
            @ApiImplicitParam(name = "fileType_eq",value = "文件类型",paramType = "query",required = false),
            @ApiImplicitParam(name = "fileSize_gt",value = "文件大小最大值",paramType = "query",required = false),
            @ApiImplicitParam(name = "fileSize_lt",value = "文件大小最小值",paramType = "query",required = false),
            @ApiImplicitParam(name = "createTime_dategt",value = "上传时间最小值",paramType = "query",required = false),
            @ApiImplicitParam(name = "createTime_datelt",value = "上传时间最大值",paramType = "query",required = false)
    })
    @GetMapping(value = "/listPage",produces="application/json")
    public ResponseData<Page<TbCoreFile>> listPage(@ApiIgnore @RequestParam Map<String,Object> map){
        Page<TbCoreFile> page = coreFileService.page(PaginationContext.getMpPage(), Condition.getQueryWrapper(map,TbCoreFile.class).lambda().orderByDesc(TbCoreFile::getCreateTime));
        return ReturnJsonUtil.ok("获取成功", BaseDictWrapper.<TbCoreFile,TbCoreFile>builder().pageVo(page));
    }

    @ApiOperation("详情")
    @GetMapping(value = "/get",produces="application/json")
    public ResponseData<TbCoreFile> get(@RequestParam String id){
        JpowerAssert.notEmpty(id,JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.ok("获取成功",coreFileService.getById(id));
    }

    @ApiOperation("批量删除")
    @DeleteMapping(value = "/delete",produces="application/json")
    public ResponseData delete(@RequestParam String ids){
        JpowerAssert.notEmpty(ids,JpowerError.Arg,"主键不可为空");

        List<String> idList = Fc.toStrList(ids);

        coreFileService.listByIds(idList).forEach(tbCoreFile -> operateBuilder.getBuilder(tbCoreFile.getStorageType()).deleteFile(tbCoreFile));
        return ReturnJsonUtil.status(coreFileService.removeRealByIds(idList));
    }

    @ApiOperation("修改文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "主键",paramType = "query",required = true),
            @ApiImplicitParam(name = "name",value = "文件名称",paramType = "query",required = false),
            @ApiImplicitParam(name = "fileType",value = "文件类型",paramType = "query",required = false),
            @ApiImplicitParam(name = "note",value = "备注",paramType = "query",required = false)
    })
    @PutMapping(value = "/update",produces="application/json")
    public ResponseData update(@ApiIgnore TbCoreFile file){
        JpowerAssert.notEmpty(file.getId(),JpowerError.Arg,"主键不可为空");

        //不可修改项
        file.setContent(null);
        file.setStorageType(null);
        file.setPath(null);

        return ReturnJsonUtil.status(coreFileService.updateById(file));
    }

    @ApiOperation(value = "对导出文件进行下载",hidden = true)
    @GetMapping("/export/download")
    @Deprecated
    public void syncStart(@ApiParam("文件名称") @RequestParam String fileName){

        JpowerAssert.isTrue(FileUtil.isValidFilename(fileName), JpowerError.BUSINESS,"文件名称("+fileName+")非法，不允许下载。 ");

        File file = new File(downloadPath+File.separator+fileName);
        if (file.exists()){
            HttpServletResponse response = getResponse();
            try {
                FileUtil.download(file, response,"导出数据.xlsx");
            } catch (IOException e) {
                logger.error("下载文件出错。file={},error={}",file.getAbsolutePath(),e.getMessage());
                throw new BusinessException("下载文件出错，请联系网站管理员");
            }

            FileUtil.deleteFile(file);
        }else {
            throw new BusinessException(fileName+"文件不存在，无法下载");
        }
    }
}
