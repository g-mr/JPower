package com.qidiangk.smart.resource.controller;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.qidiangk.smart.common.constants.CacheNames;
import com.qidiangk.smart.common.constants.DefaultValConstants;
import com.qidiangk.smart.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.DesUtil;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.resource.dbs.entity.ResourceFile;
import com.qidiangk.smart.resource.dbs.entity.ResourceFileGroupDO;
import com.qidiangk.smart.resource.pojo.MoveBO;
import com.qidiangk.smart.resource.service.ResourceFileService;
import com.qidiangk.smart.resource.service.ResourceOssService;
import com.qidiangk.smart.resource.service.file.FileOperateBuilder;
import com.qidiangk.smart.system.api.cache.dict.DictCache;
import com.qidiangk.smart.system.api.dto.SelectDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static com.qidiangk.smart.common.constants.ServiceCodeConstants.FILE_ID_NOT_LEGAL;
import static com.qidiangk.smart.common.constants.ServiceCodeConstants.NOT_FOUND_FILE_INFO;

/**
 * 文件管理控制器
 * <p>
 * 提供文件上传、下载、管理等功能
 * </p>
 *
 * @author mr.g
 */
@Slf4j
@Tag(name = "文件管理")
@Validated
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController extends BaseController {

    private final ResourceFileService coreFileService;
    private final FileOperateBuilder operateBuilder;
    private final ResourceOssService ossService;

    @Operation(summary = "上传文件")
    @PostMapping(value = "/upload", produces = APPLICATION_JSON_VALUE)
    public R<Long> upload(@Parameter(description = "文件", required = true) @NotNull(message = "文件不可为空") @RequestParam MultipartFile file,
						  @Parameter(description = "存储类型 字典:FILE_STORAGE_TYPE", example = "SERVER") @RequestParam(required = false, defaultValue = "SERVER") String storageType,
						  @Parameter(description = "文件分组ID") @RequestParam(required = false) Long groupId) throws IOException {
		ResourceFile coreFile = operateBuilder.getBuilder(storageType).upload(file.getBytes(), file.getOriginalFilename(), file.getSize(), groupId);
		CacheUtil.clear(CacheNames.FILE_KEY);
		return R.data(coreFile.getId());
    }

	/**
	 * 下载文件。
	 * <p>
	 * 客户端需将上传接口返回的文件 ID 使用 DES 密钥加密后作为参数传递，
	 * 加密密钥为 {@link DefaultValConstants#FILE_DES_KEY}。
	 * </p>
	 *
	 * @param base 加密后的文件标识（由原始文件 ID 经 DES 加密得到），不能为 {@code null} 或空字符串。
	 * @throws IOException              如果文件读取过程中发生 I/O 错误。
	 */
    @Operation(summary = "下载文件")
    @GetMapping(value = "/download/{base}", produces=APPLICATION_JSON_VALUE)
    public void download(@Parameter(description = "文件标识",required = true) @NotBlank(message = "文件标识不可为空") @PathVariable("base") String base) throws IOException {
		String id;
        try {
			id = DesUtil.decrypt(base, DefaultValConstants.FILE_DES_KEY);
		} catch (Exception e) {
			id = base;
		}
        JpowerAssert.notEmpty(id,JpowerError.Arg,FILE_ID_NOT_LEGAL);

        ResourceFile coreFile = coreFileService.detailFile(id);
        JpowerAssert.notNull(coreFile,JpowerError.NotFind,NOT_FOUND_FILE_INFO);

		operateBuilder.getBuilder(coreFile.getStorageType()).download(coreFile);
    }

    @Operation(summary = "获取文件外链")
    @GetMapping(value = "/url/{base}",produces=APPLICATION_JSON_VALUE)
    public R<String> url(@Parameter(description = "文件标识",required = true) @NotBlank(message = "文件标识不可为空") @PathVariable("base") String base){
		String id;
		try {
			id = DesUtil.decrypt(base, DefaultValConstants.FILE_DES_KEY);
		} catch (Exception e) {
			id = base;
		}
        JpowerAssert.notEmpty(id,JpowerError.Arg,FILE_ID_NOT_LEGAL);

		ResourceFile coreFile = coreFileService.detailFile(id);
        JpowerAssert.notNull(coreFile,JpowerError.NotFind, NOT_FOUND_FILE_INFO);

        return R.data(operateBuilder.getBuilder(coreFile.getStorageType()).getUrl(coreFile));
    }

    @Function(value = "文件列表",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "SYSTEM_FILE_LIST",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "文件列表")
    @Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "name", description = "文件名称", in = ParameterIn.QUERY),
		@Parameter(name = "groupId_eq", description = "分组ID", in = ParameterIn.QUERY),
		@Parameter(name = "groupId_null", description = "查询未分组文件，只要有这个参数不管有没有值都会生效", in = ParameterIn.QUERY),
		@Parameter(name = "storageType_eq", description = "存储位置 字典FILE_STORAGE_TYPE", in = ParameterIn.QUERY),
		@Parameter(name = "fileType_eq", description = "文件类型", in = ParameterIn.QUERY),
		@Parameter(name = "fileSize_gt", description = "文件大小最大值", in = ParameterIn.QUERY),
		@Parameter(name = "fileSize_lt", description = "文件大小最小值", in = ParameterIn.QUERY),
		@Parameter(name = "createTime_dategt", description = "上传时间最小值", in = ParameterIn.QUERY),
		@Parameter(name = "createTime_datelt", description = "上传时间最大值", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/listPage", produces=APPLICATION_JSON_VALUE)
    public R<Pg<ResourceFile>> listPage(@Ignore @RequestParam(required = false) Map<String,Object> map){
        return R.data(coreFileService.listPage(map));
    }

    @Function(value = "文件详情",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "SYSTEM_FILE_DETAIL",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "详情")
    @GetMapping(value = "/get",produces=APPLICATION_JSON_VALUE)
    public R<ResourceFile> get(@NotNull(message = "主键不可为空") @RequestParam Long id){
        return R.data(coreFileService.getById(id));
    }

    @Function(value = "批量删除",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "SYSTEM_FILE_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "批量删除")
    @DeleteMapping(value = "/delete",produces=APPLICATION_JSON_VALUE)
    public R<Boolean> delete(@NotBlank(message = "主键不可为空") @RequestParam String ids){
        List<Long> idList = Fc.toLongList(ids);

        coreFileService.listByIds(idList).forEach(tbCoreFile -> operateBuilder.getBuilder(tbCoreFile.getStorageType()).deleteFile(tbCoreFile));
        CacheUtil.clear(CacheNames.FILE_KEY);
        return R.status(coreFileService.removeRealByIds(idList));
    }

    @Function(value = "修改文件",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "SYSTEM_FILE_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "修改文件")
    @PutMapping(value = "/update",produces=APPLICATION_JSON_VALUE)
    public R<Boolean> update(@Valid @RequestBody ResourceFile file){
        return R.status(coreFileService.updateById(file));
    }

    @Function(value = "上传类型",menus = {
        @Menu(client = "admin",menuCode = "SYSTEM_FILE",btnCode = "SYSTEM_FILE_ADD",code = "FILE_STORAGE_TYPE",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "上传类型")
    @GetMapping(value = "/storageType",produces=APPLICATION_JSON_VALUE)
    public R<List<SelectDTO>> storageType(){
        List<SelectDTO> list = DictCache.getDictByType("FILE_STORAGE_TYPE");
        list.addAll(ossService.listCodeName());
        return R.data(list);
    }

	@Function(value = "移动文件",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "FILE_MOVE",type = Menu.TYPE.BTN)
	})
	@Operation(summary = "移动文件")
	@PostMapping(value = "/move", produces = APPLICATION_JSON_VALUE)
	public R<Boolean> move(@Valid @RequestBody MoveBO moveBO) {
		return R.status(coreFileService.move(moveBO));
	}

	@Function(value = "文件分组列表",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "FILE_GROUP_LIST",type = Menu.TYPE.INTERFACE)
	})
	@Operation(summary = "文件分组列表")
	@GetMapping(value = "/group/list", produces = APPLICATION_JSON_VALUE)
	public R<List<ResourceFileGroupDO>> groupListPage(@Ignore @RequestParam(required = false) Map<String, Object> map) {
		return R.data(coreFileService.listGroup(map));
	}

	@Function(value = "新增文件分组",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "FILE_GROUP_ADD",type = Menu.TYPE.BTN)
	})
	@Operation(summary = "新增文件分组")
	@PostMapping(value = "/group/add", produces = APPLICATION_JSON_VALUE)
	public R<Boolean> groupAdd(@Validated(Validation.Create.class) @RequestBody ResourceFileGroupDO group) {
		return R.status(coreFileService.addGroup(group));
	}

	@Function(value = "修改文件分组",menus = {
			@Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "FILE_GROUP_UPDATE",type = Menu.TYPE.BTN)
	})
	@Operation(summary = "修改文件分组")
	@PutMapping(value = "/group/update", produces = APPLICATION_JSON_VALUE)
	public R<Boolean> groupUpdate(@Validated(Validation.Update.class) @RequestBody ResourceFileGroupDO group) {
		return R.status(coreFileService.updateGroup(group));
	}

	@Function(value = "删除文件分组",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FILE",code = "FILE_GROUP_DELETE",type = Menu.TYPE.BTN)
	})
	@Operation(summary = "删除文件分组")
	@DeleteMapping(value = "/group/delete/{id}", produces = APPLICATION_JSON_VALUE)
	public R<Boolean> groupDelete(@NotNull(message = "主键不可为空") @PathVariable("id") Long id) {
		return R.status(coreFileService.deleteGroup(id));
	}

}
