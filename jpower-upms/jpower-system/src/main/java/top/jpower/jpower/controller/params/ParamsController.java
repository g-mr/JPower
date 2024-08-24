package top.jpower.jpower.controller.params;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.dbs.entity.params.TbCoreParam;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.common.utils.CacheUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.module.page.PaginationContext;
import top.jpower.jpower.service.params.CoreParamService;

import java.util.List;
import java.util.Map;

import static top.jpower.jpower.module.common.cache.CacheNames.PARAM_KEY;


@Api(tags = "系统参数管理")
@RestController
@RequestMapping("/core/param")
@AllArgsConstructor
public class ParamsController extends BaseController {

    private CoreParamService paramService;

    @Function(value = "列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_PARAMS",code = "PARAM_LIST",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("系统参数分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "code",value = "编码",paramType = "query"),
            @ApiImplicitParam(name = "name",value = "名称",paramType = "query"),
            @ApiImplicitParam(name = "value",value = "值",paramType = "query")
    })
    @GetMapping(value = "/list" , produces="application/json")
    public ResponseData<Pg<TbCoreParam>> list(@ApiIgnore @RequestParam Map<String,Object> coreParam){
        PaginationContext.startPage();
        List<TbCoreParam> list = paramService.list(Condition.getQueryWrapper(coreParam,TbCoreParam.class).lambda().orderByDesc(TbCoreParam::getCreateTime));
        return ReturnJsonUtil.ok("获取成功", new PageInfo<>(list));
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_PARAMS",code = "SYSTEM_PARAMS_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除系统参数")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE,produces="application/json")
    public ResponseData delete(@ApiParam(value = "主键",required = true) @RequestParam String ids){
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");

        CacheUtil.clear(PARAM_KEY, Boolean.FALSE);
        return ReturnJsonUtil.status(paramService.removeByIds(Fc.toLongList(ids)));
    }

    @Function(value = "编辑",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_PARAMS",code = "SYSTEM_PARAMS_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("修改系统参数")
    @RequestMapping(value = "/update",method = RequestMethod.PUT,produces="application/json")
    public ResponseData update(TbCoreParam coreParam){
        JpowerAssert.notNull(coreParam.getId(), JpowerError.Arg,"id不可为空");
        CacheUtil.clear(PARAM_KEY, Boolean.FALSE);
        return ReturnJsonUtil.status(paramService.updateById(coreParam));
    }

    @Function(value = "新增",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_PARAMS",code = "SYSTEM_PARAMS_ADD",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "新增系统参数",notes = "新增不用传主键")
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces="application/json")
    public ResponseData add(TbCoreParam coreParam){
        JpowerAssert.notEmpty(coreParam.getCode(), JpowerError.Arg,"编号值不可为空");
        JpowerAssert.notEmpty(coreParam.getName(), JpowerError.Arg,"参数名称不可为空");
        JpowerAssert.notEmpty(coreParam.getValue(), JpowerError.Arg,"参数值不可为空");

        JpowerAssert.isEmpty(paramService.selectByCode(coreParam.getCode()), JpowerError.Business,"该系统参数已存在");
        CacheUtil.clear(PARAM_KEY, Boolean.FALSE);
        return ReturnJsonUtil.status(paramService.save(coreParam));
    }

    @Function(value = "详情",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_PARAMS",code = "SYSTEM_PARAMS_DETAIL",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "通过Id获取参数详情")
    @RequestMapping(value = "/queryById",method = RequestMethod.GET,produces="application/json")
    public ResponseData<TbCoreParam> queryById(@ApiParam("主键ID") @RequestParam Long id){
        JpowerAssert.notNull(id, JpowerError.Arg,"ID不可为空");
        return ReturnJsonUtil.data(paramService.getById(id));
    }

}
