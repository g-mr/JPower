package top.jpower.resource.controller;

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
import top.jpower.resource.dbs.entity.ResourceSms;
import top.jpower.resource.service.ResourceSmsService;
import top.jpower.resource.service.sms.SmsBuilder;
import top.jpower.resource.vo.SmsSendVO;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static top.jpower.common.constants.ServiceCodeConstants.CODE_EXIST;

/**
 * 短信配置控制器
 * <p>
 * 短信配置表前端控制器
 * </p>
 *
 * @author mr.g
 */
@Tag(name = "短信配置")
@Validated
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController extends BaseController {

    private final ResourceSmsService resourceSmsService;
    private final SmsBuilder smsBuilder;

    @Function(value = "列表",menus = {
        @Menu(client = "admin",menuCode = "SMS",code = "SMS_LIST", type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "分页查询")
	@Parameters({
			@Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
			@Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true)
	})
    @GetMapping(value = "list", produces = APPLICATION_JSON_VALUE)
    public R<Pg<ResourceSms>> list(@Ignore @RequestParam(required = false) Map<String, Object> map){
        return R.data(resourceSmsService.pg(Wrappers.getQueryWrapper(map)));
    }

    @Function(value = "编辑",menus = {
        @Menu(client = "admin", menuCode = "SMS", code = "SMS_UPDATE", type = Menu.TYPE.BTN)
    })
    @Operation(summary = "更新")
    @PutMapping(value = "/update", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> update(@Validated(Validation.Update.class) @RequestBody ResourceSms resourceSms){
        if (Fc.isNotBlank(resourceSms.getCode())){
            ResourceSms sms = resourceSmsService.getByCode(resourceSms.getCode());
            if (Fc.notNull(sms)){
                JpowerAssert.isTrue(Fc.equalsValue(sms.getId(), resourceSms.getId()), JpowerError.Business, CODE_EXIST);
            }
        }

        return R.status(resourceSmsService.updateById(resourceSms));
    }

    @Function(value = "删除",menus = {
        @Menu(client = "admin", menuCode = "SMS", code = "SMS_DEL", type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> delete(@Parameter(description = "主键，多个逗号分隔", required = true) @NotBlank(message = "主键不可为空") @RequestParam String ids){
        return R.status(resourceSmsService.removeByIds(Fc.toLongList(ids)));
    }

    @Function(value = "新增",menus = {
        @Menu(client = "admin",menuCode = "SMS",code = "SMS_ADD", type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增")
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    public R<Long> add(@Validated(Validation.Create.class) @RequestBody ResourceSms resourceSms){
        ResourceSms sms = resourceSmsService.getByCode(resourceSms.getCode());
        JpowerAssert.notTrue(Fc.notNull(sms), JpowerError.Business, CODE_EXIST);
		resourceSmsService.save(resourceSms);
        return R.data(resourceSms.getId());
    }

    @Function(value = "调试",menus = {
        @Menu(client = "admin",menuCode = "SMS",code = "SMS_TEST", type = Menu.TYPE.BTN)
    })
    @Operation(summary = "调试")
    @PostMapping(value = "/test", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> test(@NotNull(message = "请求体不可为空") @Valid @RequestBody SmsSendVO smsSendVO){
        smsBuilder.getTemplate(smsSendVO.getCodeName()).sendSingleThrow(smsSendVO.getTemplateParams(), smsSendVO.getPhone());
        return R.ok();
    }

}
