package top.jpower.jpower.controller;


import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.jpower.dbs.entity.TbResourceSms;
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
@RequestMapping("/rou/resourceSms")
@RequiredArgsConstructor
public class ResourceSmsController extends BaseController {

    private final ResourceSmsService resourceSmsService;

    @ApiOperation("分页查询")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataTypeClass = Integer.class,required = true),
        @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataTypeClass = Integer.class,required = true)
    })
    @GetMapping(value = "list", produces = "application/json")
    public ResponseData<Pg<TbResourceSms>> list(@ApiIgnore @RequestParam Map<String, Object> map){
        return ReturnJsonUtil.data(resourceSmsService.page(PaginationContext.getMpPage(), Condition.getQueryWrapper(map,TbResourceSms.class)));
    }

    @ApiOperation("更新")
    @PutMapping(value = "/update", produces = "application/json")
    public ResponseData update(@RequestBody TbResourceSms resourceSms){
        JpowerAssert.notNull(resourceSms.getId(), JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.status(resourceSmsService.updateById(resourceSms));
    }

    @ApiOperation("删除")
    @DeleteMapping(value = "/delete", produces = "application/json")
    public ResponseData delete(@ApiParam("主键，多个逗号分隔") @RequestParam String ids){
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.status(resourceSmsService.removeByIds(Fc.toLongList(ids)));
    }

    @ApiOperation("新增")
    @PostMapping(value = "/add", produces = "application/json")
    public ResponseData add(@Validated @RequestBody TbResourceSms resourceSms){
        resourceSms.setId(null);

        boolean is = resourceSmsService.exists(Condition.<TbResourceSms>getQueryWrapper().lambda().eq(TbResourceSms::getCode, resourceSms.getCode()));
        JpowerAssert.notTrue(is, JpowerError.Business, "该编码已存在");

        return ReturnJsonUtil.status(resourceSmsService.save(resourceSms));
    }

}
