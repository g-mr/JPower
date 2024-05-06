package top.jpower.jpower.controller.function;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.common.enums.FunctionTypeEnum;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.jpower.dbs.entity.client.TbCoreClient;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.dbs.entity.function.TbCoreTopMenu;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.common.controller.BaseController;
import top.jpower.jpower.module.common.page.PaginationContext;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.client.CoreClientService;
import top.jpower.jpower.service.role.CoreFunctionService;
import top.jpower.jpower.service.role.CoreMenuService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;

/**
 * 顶级菜单接口
 *
 * @author mr.g
 * @date 2022/10/23 23:22
 */
@Api(tags = "顶部菜单管理")
@RestController
@RequestMapping("/core/menu")
@RequiredArgsConstructor
public class TopMenuController extends BaseController {

    private final CoreMenuService menuService;
    private final CoreFunctionService functionService;
    private final CoreClientService clientService;

    @Function(value = "新增菜单",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_ADD",type = Menu.TYPE.BTN)
    })
    @ApiOperation("新增菜单")
    @PostMapping(value = "/add",produces="application/json")
    public ResponseData add(TbCoreTopMenu topMenu){
        topMenu.setId(null);
        JpowerAssert.notEmpty(topMenu.getCode(), JpowerError.Arg,"编号不可为空");
        JpowerAssert.notEmpty(topMenu.getName(), JpowerError.Arg,"名称不可为空");
        JpowerAssert.notNull(topMenu.getClientId(), JpowerError.Arg,"客户端ID不可为空");

        long c = menuService.count(Condition.<TbCoreTopMenu>getQueryWrapper().lambda().eq(TbCoreTopMenu::getCode,topMenu.getCode()));
        JpowerAssert.geZero(c,JpowerError.Business, "菜单编号不可重复");

        if (Fc.isNull(topMenu.getStatus())){
            topMenu.setStatus(YN01Enum.Y.getValue());
        }

        if (Fc.isNull(topMenu.getSortNum())){
            topMenu.setSortNum(1);
        }

        return ReturnJsonUtil.status(menuService.save(topMenu));
    }

    @Function(value = "更新菜单",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("更新菜单")
    @PutMapping(value = "/update",produces="application/json")
    public ResponseData update(TbCoreTopMenu topMenu){
        JpowerAssert.notNull(topMenu.getId(), JpowerError.Arg,"主键不可为空");

        long c = menuService.count(Condition.<TbCoreTopMenu>getQueryWrapper().lambda().eq(TbCoreTopMenu::getCode,topMenu.getCode()).ne(TbCoreTopMenu::getId,topMenu.getId()));
        JpowerAssert.geZero(c,JpowerError.Business, "菜单编号不可重复");

        return ReturnJsonUtil.status(menuService.updateAllById(topMenu));
    }

    @Function(value = "菜单开关",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_SWITCH",type = Menu.TYPE.BTN)
    })
    @ApiOperation("菜单开关")
    @PutMapping(value = "/switch",produces="application/json")
    public ResponseData statusSwitch(@ApiParam(value = "主键",required = true) Long id,@ApiParam(value = "开关状态",required = true) Integer status){
        JpowerAssert.notNull(id, JpowerError.Arg,"主键不可为空");
        JpowerAssert.notNull(status, JpowerError.Arg,"开关状态不可为空");

        JpowerAssert.isTrue(YN01Enum.isExist(status), JpowerError.Arg,"开关状态值不合法");

        return ReturnJsonUtil.status(menuService.update(Wrappers.<TbCoreTopMenu>lambdaUpdate()
                .set(TbCoreTopMenu::getStatus,status)
                .eq(TbCoreTopMenu::getId,id)));
    }

    @Function(value = "删除菜单",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除菜单")
    @DeleteMapping(value = "/delete",produces="application/json")
    public ResponseData delete(String ids){
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.status(menuService.removeByIds(Fc.toLongList(ids)));
    }

    @Function(value = "菜单列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_LIST",type = Menu.TYPE.INTERFACE)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataTypeClass = Integer.class,required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataTypeClass = Integer.class,required = true),
            @ApiImplicitParam(name = "code",value = "菜单编号",paramType = "query",dataTypeClass = String.class),
            @ApiImplicitParam(name = "name",value = "菜单名称",paramType = "query",dataTypeClass = String.class),
            @ApiImplicitParam(name = "status",value = "状态 字典：YN01",paramType = "query",dataTypeClass = String.class)
    })
    @ApiOperation("菜单列表")
    @GetMapping(value = "/list",produces="application/json")
    public ResponseData<Pg<TbCoreTopMenu>> list(@ApiIgnore @RequestParam Map<String,Object> map){
        return ReturnJsonUtil.data(menuService.page(PaginationContext.getMpPage(), Condition.getQueryWrapper(map,TbCoreTopMenu.class)));
    }

    @Function(value = "客户端顶部菜单树",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_DATASCOPE_LIST",code = "ROLE_CLIENT_TOPMENU",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("客户端顶部菜单树")
    @GetMapping(value = "/listName",produces="application/json")
    public ResponseData<List<Map<String,Object>>> listName(){

        List<Map<String, Object>> menuList = menuService.selectList(null);

        List<TbCoreClient> coreClients = clientService.list();

        List<Map<String,Object>> list = new ArrayList<>();
        coreClients.forEach(client -> {
            Map<String,Object> map = ChainMap.<String,Object>create().put("name",client.getName()).put("id",client.getId()).build();

            map.put("children",menuList.stream().filter(topMenu -> NumberUtil.equals(MapUtil.getLong(topMenu, "client_id"),client.getId())));
            map.put("hasChildren",Fc.isNotEmpty(map.get("children")));

            list.add(map);
        });

        return ReturnJsonUtil.data(list);
    }

    @Function(value = "关联一级菜单ID",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_FUNCTION_ID",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("顶部菜单关联的左侧第一级菜单ID")
    @GetMapping(value = "/listFunctionId",produces="application/json")
    public ResponseData<List<Long>> listFunctionId(@ApiParam(value = "顶部菜单ID",required = true) Long menuId){
        JpowerAssert.notNull(menuId,JpowerError.Arg,"顶部菜单ID不可为空");
        return ReturnJsonUtil.data(menuService.listFunctionId(menuId));
    }

    @Function(value = "关联菜单",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_FUNCTION",type = Menu.TYPE.BTN)
    })
    @ApiOperation("一级菜单列表")
    @GetMapping(value = "/listFunction",produces="application/json")
    public ResponseData<List<Map<String,Object>>> listFunction(@ApiParam(value = "客户端ID",required = true) Long clientId){
        JpowerAssert.notNull(clientId,JpowerError.Arg,"客户端ID不可为空");

        return ReturnJsonUtil.data(functionService.listMaps(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                        .select(TbCoreFunction::getId,TbCoreFunction::getFunctionName)
                        .eq(TbCoreFunction::getParentId, Fc.toLong(TOP_CODE))
                        .eq(TbCoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue())
                        .eq(TbCoreFunction::getClientId,clientId)));
    }

    @Function(value = "保存一级菜单",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_FUNCTION_SAVE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("设置顶部菜单关联的一级菜单")
    @PostMapping(value = "/saveFunction",produces="application/json")
    public ResponseData saveFunction(@ApiParam(value = "顶部菜单ID",required = true) Long menuId,@ApiParam(value = "功能ID，多个逗号分割") String functionIds){
        JpowerAssert.notNull(menuId,JpowerError.Arg,"顶部菜单ID不可为空");

        return ReturnJsonUtil.status(menuService.saveFunction(menuId,Fc.toLongList(functionIds)));
    }

    @ApiOperation("获取当前登录用户的顶级菜单")
    @GetMapping(value = "/roleMenu",produces="application/json")
    public ResponseData<List<Map<String,Object>>> roleMenu(){
        return ReturnJsonUtil.data(menuService.roleMenu());
    }

    @Function(value = "顶级菜单选项",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "FUNCTION_TOPMENU_SELECT",type = Menu.TYPE.INTERFACE),
            @Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE",code = "DATASCOPE_TOPMENU_SELECT",type = Menu.TYPE.INTERFACE),
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_SELECT_URL",code = "ROLE_TOPMENU",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation(value = "获取顶级菜单下拉框",notes = "只获取当前用户的权限")
    @GetMapping(value = "/select",produces="application/json")
    public ResponseData<List<Map<String,Object>>> select(@ApiParam(value = "客户端ID",required = true) Long clientId){

        JpowerAssert.notNull(clientId,JpowerError.Arg,"客户端ID不可为空");

        return ReturnJsonUtil.data(menuService.selectList(clientId));
    }
}
