package top.jpower.jpower.controller;

import cn.hutool.core.lang.Validator;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.jpower.config.sms.SmsBuilder;
import top.jpower.jpower.dbs.entity.TbResourceSms;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.base.vo.Pg;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.controller.BaseController;
import top.jpower.jpower.module.common.page.PaginationContext;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.ResourceSmsService;

import java.util.Map;

/**
 * <p>
 * 短信配置表 前端控制器
 * </p>
 *
 * @author mr.g
 * @since 2024-03-04
 */
@Api(tags = "短信配置")
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class ResourceSmsController extends BaseController {

    private final ResourceSmsService resourceSmsService;
    private final SmsBuilder smsBuilder;

    @Function(value = "列表",menus = {
        @Menu(client = "admin",menuCode = "SMS",code = "SMS_LIST", type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("分页查询")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataTypeClass = Integer.class,required = true),
        @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataTypeClass = Integer.class,required = true)
    })
    @GetMapping(value = "list", produces = "application/json")
    public ResponseData<Pg<TbResourceSms>> list(@ApiIgnore @RequestParam Map<String, Object> map){
        return ReturnJsonUtil.data(resourceSmsService.page(PaginationContext.getMpPage(), Condition.getQueryWrapper(map,TbResourceSms.class)));
    }

    @Function(value = "编辑",menus = {
        @Menu(client = "admin", menuCode = "SMS", code = "SMS_UPDATE", type = Menu.TYPE.BTN)
    })
    @ApiOperation("更新")
    @PutMapping(value = "/update", produces = "application/json")
    public ResponseData update(@RequestBody TbResourceSms resourceSms){
        JpowerAssert.notNull(resourceSms.getId(), JpowerError.Arg,"主键不可为空");
        if (Fc.isNotBlank(resourceSms.getCode())){
            TbResourceSms sms = resourceSmsService.getByCode(resourceSms.getCode());
            if (Fc.notNull(sms)){
                JpowerAssert.isTrue(Fc.equalsValue(sms.getId(), resourceSms.getId()), JpowerError.Business, "该编码已存在");
            }
        }

        return ReturnJsonUtil.status(resourceSmsService.updateById(resourceSms));
    }

    @Function(value = "删除",menus = {
        @Menu(client = "admin", menuCode = "SMS", code = "SMS_DEL", type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除")
    @DeleteMapping(value = "/delete", produces = "application/json")
    public ResponseData delete(@ApiParam("主键，多个逗号分隔") @RequestParam String ids){
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.status(resourceSmsService.removeByIds(Fc.toLongList(ids)));
    }

    @Function(value = "新增",menus = {
        @Menu(client = "admin",menuCode = "SMS",code = "SMS_ADD", type = Menu.TYPE.BTN)
    })
    @ApiOperation("新增")
    @PostMapping(value = "/add", produces = "application/json")
    public ResponseData add(@Validated @RequestBody TbResourceSms resourceSms){
        resourceSms.setId(null);

        TbResourceSms sms = resourceSmsService.getByCode(resourceSms.getCode());
        JpowerAssert.notTrue(Fc.notNull(sms), JpowerError.Business, "该编码已存在");

        return ReturnJsonUtil.status(resourceSmsService.save(resourceSms));
    }

    @Function(value = "调试",menus = {
        @Menu(client = "admin",menuCode = "SMS",code = "SMS_TEST", type = Menu.TYPE.BTN)
    })
    @ApiOperation("调试")
    @PostMapping(value = "/test", produces = "application/json")
    public ResponseData test(@RequestBody Map<String, String> map){
        JpowerAssert.notEmpty(map.get("phone"), JpowerError.Arg, "手机号 不能为空");
        JpowerAssert.isTrue(Validator.isMobile(map.get("phone")), JpowerError.Arg, "手机号 不合法");

        String code = map.remove("code");
        String phone = map.remove("phone");

        smsBuilder.getTemplate(code).sendSingleThrow(map, phone);
        return ReturnJsonUtil.status(true);
    }

}
