package top.jpower.jpower.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.BusinessException;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.util.constants.ReturnConstants;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.DesUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.FileUtil;
import top.jpower.jpower.cache.dict.DictCache;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.utils.CacheUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.operate.FileOperateBuilder;
import top.jpower.jpower.service.ResourceFileService;
import top.jpower.jpower.service.ResourceOssService;

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
@Slf4j
@Api(tags = "文件管理")
@RestController
@RequestMapping("/resource/file")
public class FileController extends BaseController {

    @Value("${jpower.downloadPath:}")
    private String downloadPath;

    @Resource
    private ResourceFileService coreFileService;
    @Resource
    private FileOperateBuilder operateBuilder;
    @Resource
    private ResourceOssService ossService;

    @ApiOperation("上传文件")
    @PostMapping(value = "/upload",produces="application/json")
    public ResponseData upload(@ApiParam("文件") @RequestParam(required = false) MultipartFile file,
                               @ApiParam(value = "存储类型 字典FILE_STORAGE_TYPE",defaultValue = "SERVER") @RequestParam(required = false,defaultValue = "SERVER") String storageType){
        JpowerAssert.notTrue(file == null || file.isEmpty(),JpowerError.Arg,"文件不可为空");
        try {
            TbResourceFile coreFile = operateBuilder.getBuilder(storageType).upload(file.getBytes(), file.getOriginalFilename(), file.getSize());
            if (Fc.notNull(coreFile)){
                CacheUtil.clear(CacheNames.FILE_KEY);
                return ReturnJsonUtil.ok("上传成功", coreFile.getMark());
            }else {
                return ReturnJsonUtil.fail("文件保存失败");
            }
        } catch (JpowerException je){
            throw je;
        } catch (Exception e){
            e.printStackTrace();
            log.error("文件上传失败，e={}",e.getMessage());
            return ReturnJsonUtil.print(ReturnConstants.RECODE_ERROR,"文件上传失败", false);
        }
    }

    @ApiOperation("下载文件")
    @GetMapping(value = "/download/{base}",produces="application/json")
    public void download(@ApiParam(value = "文件标识",required = true) @PathVariable("base") String base){
        JpowerAssert.notEmpty(base,JpowerError.Arg,"文件标识不可为空");
        String id = DesUtil.decrypt(base, DefaultValConstants.FILE_DES_KEY);
        JpowerAssert.notEmpty(id,JpowerError.Arg,"文件标识不合法");

        TbResourceFile coreFile = coreFileService.getOne(Condition.<TbResourceFile>getQueryWrapper().lambda()
                .select(TbResourceFile::getPath,TbResourceFile::getContent,TbResourceFile::getName,TbResourceFile::getStorageType)
                .eq(TbResourceFile::getId,id));
        JpowerAssert.notNull(coreFile,JpowerError.Unknown,"未查到文件数据");

        try {
            operateBuilder.getBuilder(coreFile.getStorageType()).download(coreFile);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件下载失败，e={}",e.getMessage());
            throw new BusinessException(coreFile.getName()+"文件下载失败");
        }
    }

    @ApiOperation("获取文件外链")
    @GetMapping(value = "/url/{base}",produces="application/json")
    public ResponseData<String> url(@ApiParam(value = "文件标识",required = true) @PathVariable("base") String base){
        JpowerAssert.notEmpty(base, JpowerError.Arg, "文件标识不可为空");
        String id = DesUtil.decrypt(base, DefaultValConstants.FILE_DES_KEY);
        JpowerAssert.notEmpty(id,JpowerError.Arg,"文件标识不合法");

        TbResourceFile coreFile = coreFileService.getOne(Condition.<TbResourceFile>getQueryWrapper().lambda()
                .select(TbResourceFile::getPath,TbResourceFile::getContent,TbResourceFile::getName,TbResourceFile::getStorageType)
                .eq(TbResourceFile::getId,id));
        JpowerAssert.notNull(coreFile,JpowerError.Unknown,"未查到文件数据");

        return ReturnJsonUtil.data(operateBuilder.getBuilder(coreFile.getStorageType()).getUrl(coreFile));
    }

    @Function(value = "文件列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "SYSTEM_FILE_LIST",type = Menu.TYPE.INTERFACE)
    })
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
    public ResponseData<Pg<TbResourceFile>> listPage(@ApiIgnore @RequestParam Map<String,Object> map){
        return ReturnJsonUtil.ok("获取成功", coreFileService.listPage(map));
    }

    @Function(value = "文件详情",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "SYSTEM_FILE_DETAIL",type = Menu.TYPE.BTN)
    })
    @ApiOperation("详情")
    @GetMapping(value = "/get",produces="application/json")
    public ResponseData<TbResourceFile> get(@RequestParam Long id){
        JpowerAssert.notNull(id,JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.ok("获取成功",coreFileService.getById(id));
    }

    @Function(value = "批量删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "SYSTEM_FILE_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("批量删除")
    @DeleteMapping(value = "/delete",produces="application/json")
    public ResponseData delete(@RequestParam String ids){
        JpowerAssert.notEmpty(ids,JpowerError.Arg,"主键不可为空");

        List<Long> idList = Fc.toLongList(ids);

        coreFileService.listByIds(idList).forEach(tbCoreFile -> operateBuilder.getBuilder(tbCoreFile.getStorageType()).deleteFile(tbCoreFile));
        CacheUtil.clear(CacheNames.FILE_KEY);
        return ReturnJsonUtil.status(coreFileService.removeRealByIds(idList));
    }

    @Function(value = "修改文件",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "SYSTEM_FILE_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("修改文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "主键",paramType = "query",required = true),
            @ApiImplicitParam(name = "name",value = "文件名称",paramType = "query",required = false),
            @ApiImplicitParam(name = "fileType",value = "文件类型",paramType = "query",required = false),
            @ApiImplicitParam(name = "note",value = "备注",paramType = "query",required = false)
    })
    @PutMapping(value = "/update",produces="application/json")
    public ResponseData update(@ApiIgnore TbResourceFile file){
        JpowerAssert.notNull(file.getId(),JpowerError.Arg,"主键不可为空");

        //不可修改项
        file.setContent(null);
        file.setStorageType(null);
        file.setPath(null);

        CacheUtil.clear(CacheNames.FILE_KEY);
        return ReturnJsonUtil.status(coreFileService.updateById(file));
    }

    @Function(value = "上传类型",menus = {
        @Menu(client = "admin",menuCode = "SYSTEM_FILE",btnCode = "SYSTEM_FILE_ADD",code = "FILE_STORAGE_TYPE",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("上传类型")
    @GetMapping(value = "/storageType",produces="application/json")
    public ResponseData storageType(){
        List<Map<String, Object>> list = DictCache.getDictByType("FILE_STORAGE_TYPE");
        list.addAll(ossService.listCodeName());
        return ReturnJsonUtil.data(list);
    }

    @ApiOperation(value = "对导出文件进行下载",hidden = true)
    @GetMapping("/export/download")
    @Deprecated
    public void syncStart(@ApiParam("文件名称") @RequestParam String fileName){

        JpowerAssert.isTrue(FileUtil.isValidFilename(fileName), JpowerError.Business,"文件名称("+fileName+")非法，不允许下载。 ");

        File file = new File(downloadPath+File.separator+fileName);
        if (file.exists()){
            HttpServletResponse response = getResponse();
            try {
                FileUtil.download(file, response,"导出数据.xlsx");
            } catch (IOException e) {
                log.error("下载文件出错。file={},error={}",file.getAbsolutePath(),e.getMessage());
                throw new BusinessException("下载文件出错，请联系网站管理员");
            }

            FileUtil.deleteFile(file);
        }else {
            throw new BusinessException(fileName+"文件不存在，无法下载");
        }
    }
}
