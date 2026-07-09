package top.jpower.resource.controller;

import cn.hutool.core.util.IdUtil;
import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.resource.dbs.entity.ResourceOss;
import top.jpower.resource.service.ResourceOssService;
import top.jpower.resource.service.file.FileOperateBuilder;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static top.jpower.common.constants.ServiceCodeConstants.NOT_FOUND_OSS;

/**
 * OSS配置管理
 * 
 * @author mr.g
 */
@Tag(name = "对象存储")
@Validated
@RestController
@RequestMapping("/oss")
@RequiredArgsConstructor
public class OssController extends BaseController {

    private final ResourceOssService ossService;
    private final FileOperateBuilder operateBuilder;

    @Function(value = "列表",menus = {
        @Menu(client = "admin",menuCode = "OSS",code = "OSS_LIST", type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "分页查询")
    @Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true)
    })
    @GetMapping(value = "list", produces = APPLICATION_JSON_VALUE)
    public R<Pg<ResourceOss>> list(@Ignore @RequestParam(required = false) Map<String, Object> map){
        return R.data(ossService.pg(Wrappers.getQueryWrapper(map)));
    }

    @Function(value = "新增",menus = {
        @Menu(client = "admin",menuCode = "OSS",code = "OSS_ADD", type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增")
    @PostMapping(value = "add", produces = APPLICATION_JSON_VALUE)
    public R<Long> add(@Validated(Validation.Create.class) @RequestBody ResourceOss resourceOss){
        resourceOss.setCode(IdUtil.nanoId(6));
		ossService.save(resourceOss);
        return R.data(resourceOss.getId());
    }

    @Function(value = "默认",menus = {
            @Menu(client = "admin",menuCode = "OSS",code = "OSS_DEFAULT", type = Menu.TYPE.BTN)
    })
    @Operation(summary = "默认")
    @PostMapping(value = "default/{id}", produces = APPLICATION_JSON_VALUE)
    public R<Void> setDefault(@PathVariable("id") Long id){
        return R.status(false);
    }

    @Function(value = "更新",menus = {
        @Menu(client = "admin",menuCode = "OSS",code = "OSS_UPDATE", type = Menu.TYPE.BTN)
    })
    @Operation(summary = "更新")
    @PutMapping(value = "update", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> update(@Validated({Validation.Update.class}) @RequestBody ResourceOss resourceOss){
        ResourceOss oss = ossService.getById(resourceOss.getId());
        JpowerAssert.notNull(oss, JpowerError.NotFind, NOT_FOUND_OSS);
		
		// code 无法修改
        resourceOss.setCode(oss.getCode());
        operateBuilder.removeBuilder(oss.getCode());
        return R.status(ossService.updateById(resourceOss));
    }

    @Function(value = "删除",menus = {
        @Menu(client = "admin",menuCode = "OSS",code = "OSS_DELETE", type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除")
    @DeleteMapping(value = "delete/{ids}", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> delete(@Parameter(description = "主键，多个用逗号分隔") @NotBlank(message = "主键不可为空") @PathVariable("ids") String ids){
        List<ResourceOss> list = ossService.listByIds(Fc.toLongList(ids));
        list.forEach(oss-> operateBuilder.removeBuilder(oss.getCode()));
        return R.status(ossService.removeByIds(Fc.toLongList(ids)));
    }

}
