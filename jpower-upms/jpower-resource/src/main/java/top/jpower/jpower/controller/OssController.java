package top.jpower.jpower.controller;

import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.validated.UpdateGroup;
import top.jpower.jpower.dbs.entity.TbResourceOss;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.jpower.operate.FileOperateBuilder;
import top.jpower.jpower.service.ResourceOssService;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2024/4/23 2:57 PM
 */
@Api(tags = "对象存储")
@RestController
@RequestMapping("/oss")
@RequiredArgsConstructor
public class OssController extends BaseController {

    private final ResourceOssService ossService;
    private final FileOperateBuilder operateBuilder;

    @Function(value = "列表",menus = {
        @Menu(client = "admin",menuCode = "OSS",code = "OSS_LIST", type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("分页查询")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataTypeClass = Integer.class,required = true),
        @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataTypeClass = Integer.class,required = true)
    })
    @GetMapping(value = "list", produces = "application/json")
    public ResponseData<Pg<TbResourceOss>> list(@ApiIgnore @RequestParam Map<String, Object> map){
        return ReturnJsonUtil.data(ossService.page(PaginationContext.getMpPage(), Condition.getQueryWrapper(map, TbResourceOss.class)));
    }

    @Function(value = "新增",menus = {
        @Menu(client = "admin",menuCode = "OSS",code = "OSS_ADD", type = Menu.TYPE.BTN)
    })
    @ApiOperation("新增")
    @PostMapping(value = "add", produces = "application/json")
    public ResponseData add(@Validated @RequestBody TbResourceOss resourceOss){
        resourceOss.setCode(IdUtil.nanoId(6));
        return ReturnJsonUtil.status(ossService.save(resourceOss));
    }

    @Function(value = "更新",menus = {
        @Menu(client = "admin",menuCode = "OSS",code = "OSS_UPDATE", type = Menu.TYPE.BTN)
    })
    @ApiOperation("更新")
    @PutMapping(value = "update", produces = "application/json")
    public ResponseData update(@Validated({Default.class, UpdateGroup.class}) @RequestBody TbResourceOss resourceOss){
        TbResourceOss oss = ossService.getById(resourceOss.getId());
        JpowerAssert.notNull(oss, JpowerError.NotFind, "对象存储");
        resourceOss.setCode(oss.getCode());
        operateBuilder.removeBuilder(oss.getCode());
        return ReturnJsonUtil.status(ossService.updateAllById(resourceOss));
    }

    @Function(value = "删除",menus = {
        @Menu(client = "admin",menuCode = "OSS",code = "OSS_DELETE", type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除")
    @DeleteMapping(value = "delete/{ids}", produces = "application/json")
    public ResponseData delete(@PathVariable("ids") String ids){
        List<TbResourceOss> list = ossService.listByIds(Fc.toStrList(ids));
        list.forEach(oss-> operateBuilder.removeBuilder(oss.getCode()));
        return ReturnJsonUtil.status(ossService.removeByIds(Fc.toStrList(ids)));
    }

}
