package com.wlcb.jpower.controller.function;

import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dbs.entity.function.TbCoreTopMenu;
import com.wlcb.jpower.module.annotation.Function;
import com.wlcb.jpower.module.annotation.Menu;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.Pg;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.client.CoreClientService;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.service.role.CoreMenuService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.wlcb.jpower.module.common.utils.constants.JpowerConstants.TOP_CODE;

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
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_ADD")
    })
    @ApiOperation("新增菜单")
    @PostMapping(value = "/add",produces="application/json")
    public ResponseData add(TbCoreTopMenu topMenu){
        topMenu.setId(null);
        JpowerAssert.notEmpty(topMenu.getCode(), JpowerError.Arg,"编号不可为空");
        JpowerAssert.notEmpty(topMenu.getName(), JpowerError.Arg,"名称不可为空");
        JpowerAssert.notEmpty(topMenu.getClientId(), JpowerError.Arg,"客户端ID不可为空");

        long c = menuService.count(Condition.<TbCoreTopMenu>getQueryWrapper().lambda().eq(TbCoreTopMenu::getCode,topMenu.getCode()));
        JpowerAssert.geZero(c,JpowerError.Business, "菜单编号不可重复");

        if (Fc.isNull(topMenu.getStatus())){
            topMenu.setStatus(ConstantsEnum.YN01.Y.getValue());
        }

        if (Fc.isNull(topMenu.getSortNum())){
            topMenu.setSortNum(1);
        }

        return ReturnJsonUtil.status(menuService.save(topMenu));
    }

    @Function(value = "更新菜单",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_UPDATE")
    })
    @ApiOperation("更新菜单")
    @PutMapping(value = "/update",produces="application/json")
    public ResponseData update(TbCoreTopMenu topMenu){
        JpowerAssert.notEmpty(topMenu.getId(), JpowerError.Arg,"主键不可为空");

        long c = menuService.count(Condition.<TbCoreTopMenu>getQueryWrapper().lambda().eq(TbCoreTopMenu::getCode,topMenu.getCode()).ne(TbCoreTopMenu::getId,topMenu.getId()));
        JpowerAssert.geZero(c,JpowerError.Business, "菜单编号不可重复");

        return ReturnJsonUtil.status(menuService.updateById(topMenu));
    }

    @Function(value = "删除菜单",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_DELETE")
    })
    @ApiOperation("删除菜单")
    @DeleteMapping(value = "/delete",produces="application/json")
    public ResponseData delete(String id){
        JpowerAssert.notEmpty(id, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.status(menuService.removeById(id));
    }

    @Function(value = "菜单列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_LIST")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataTypeClass = Integer.class,required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataTypeClass = Integer.class,required = true),
            @ApiImplicitParam(name = "code",value = "菜单编号",paramType = "query",dataTypeClass = String.class),
            @ApiImplicitParam(name = "name",value = "菜单名称",paramType = "query",dataTypeClass = String.class),
            @ApiImplicitParam(name = "status",value = "状态 字典：YN01",paramType = "query",dataTypeClass = String.class)
    })
    @ApiOperation("删除菜单")
    @GetMapping(value = "/list",produces="application/json")
    public ResponseData<Pg<TbCoreTopMenu>> list(@ApiIgnore @RequestParam Map<String,Object> map){
        return ReturnJsonUtil.data(menuService.page(PaginationContext.getMpPage(), Condition.getQueryWrapper(map,TbCoreTopMenu.class)));
    }

    @Function(value = "顶部菜单",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "ROLE_TOPMENU")
    })
    @ApiOperation("顶部菜单启用列表")
    @GetMapping(value = "/listName",produces="application/json")
    public ResponseData<List<Map<String,Object>>> listName(){

        List<TbCoreTopMenu> menuList = menuService.list(Condition.<TbCoreTopMenu>getQueryWrapper().lambda()
                .eq(TbCoreTopMenu::getStatus, ConstantsEnum.YN01.Y.getValue()));

        List<TbCoreClient> coreClients = clientService.list();

        List<Map<String,Object>> list = new ArrayList<>();
        coreClients.forEach(client -> {
            Map<String,Object> map = ChainMap.<String,Object>create().put("name",client.getName()).put("id",client.getId()).build();

            List<Map<String,String>> menuMapList = new ArrayList<>();
            menuList.stream().filter(topMenu -> Fc.equalsValue(topMenu.getClientId(),client.getId())).forEach(topMenu -> {
                Map<String,String> menuMap = ChainMap.<String,String>create().put("name",topMenu.getName()).put("id",topMenu.getId()).build();
                menuMapList.add(menuMap);
            });
            map.put("children",menuMapList);
            map.put("hasChildren",Fc.isNotEmpty(menuMapList));

            list.add(map);
        });

        return ReturnJsonUtil.data(list);
    }

    @Function(value = "关联一级菜单ID",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_FUNCTION_ID")
    })
    @ApiOperation("顶部菜单关联的左侧第一级菜单ID")
    @GetMapping(value = "/listFunctionId",produces="application/json")
    public ResponseData<List<String>> listFunctionId(@ApiParam(value = "顶部菜单ID",required = true) String menuId){
        JpowerAssert.notEmpty(menuId,JpowerError.Arg,"顶部菜单ID不可为空");
        return ReturnJsonUtil.data(menuService.listFunctionId(menuId));
    }

    @Function(value = "一级菜单",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_FUNCTION")
    })
    @ApiOperation("一级菜单列表")
    @GetMapping(value = "/listFunction",produces="application/json")
    public ResponseData<List<Map<String,Object>>> listFunction(@ApiParam(value = "客户端ID",required = true) String clientId){
        JpowerAssert.notEmpty(clientId,JpowerError.Arg,"客户端ID不可为空");

        return ReturnJsonUtil.data(functionService.listMaps(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                        .select(TbCoreFunction::getId,TbCoreFunction::getFunctionName)
                        .eq(TbCoreFunction::getParentId, TOP_CODE)
                        .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue())
                        .eq(TbCoreFunction::getClientId,clientId)));
    }

    @Function(value = "保存一级菜单",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_FUNCTION_SAVE")
    })
    @ApiOperation("设置顶部菜单关联的一级菜单")
    @PostMapping(value = "/saveFunction",produces="application/json")
    public ResponseData saveFunction(@ApiParam(value = "顶部菜单ID",required = true) String menuId,@ApiParam(value = "功能ID，多个逗号分割") String functionIds){
        JpowerAssert.notEmpty(menuId,JpowerError.Arg,"顶部菜单ID不可为空");

        return ReturnJsonUtil.status(menuService.saveFunction(menuId,Fc.toStrList(functionIds)));
    }

    @ApiOperation("获取当前登录用户的顶级菜单")
    @GetMapping(value = "/roleMenu",produces="application/json")
    public ResponseData<List<Map<String,Object>>> roleFunction(){
        return ReturnJsonUtil.data(menuService.roleMenu());
    }
}
