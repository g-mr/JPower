package top.jpower.jpower.controller.city;

import cn.hutool.core.lang.tree.Tree;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.dbs.entity.city.TbCoreCity;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.controller.BaseController;
import top.jpower.jpower.module.mp.support.WrapperKeyword;
import top.jpower.jpower.service.city.CoreCityService;
import top.jpower.jpower.vo.CityVo;

import java.util.List;
import java.util.Map;

@Api(tags = "行政区域管理")
@RestController
@RequestMapping("/core/city")
@RequiredArgsConstructor
public class CityController extends BaseController {

    private final CoreCityService coreCityService;

    @ApiOperation("查询下级列表")
    @ApiResponses({
            @ApiResponse(code = 200,message = "ResponseData => 'data':[{'code':'string','name':'string'}]")
    })
    @Cacheable(value = CacheNames.CITY_PARENT_CODE_REDIS_KEY,key = "#pcode.concat(#name==null?'':name)")
    @RequestMapping(value = "/listChild",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Map<String,Object>>> listChild(@ApiParam(value = "父级code",required = true) @RequestParam(defaultValue = JpowerConstants.TOP_CODE) String pcode,
                                                            @ApiParam(value = "名称") @RequestParam(required = false) String name){
        List<Map<String,Object>> list = coreCityService.listChild(ChainMap.<String, Object>create().put("pcode"+ WrapperKeyword.EQ.getSuffixKeyword(),pcode).put("name",name).build());
        return ReturnJsonUtil.ok("获取成功", list);
    }

    @ApiOperation(value = "新增行政区域",notes = "主键不可传")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData<Long> add( TbCoreCity coreCity){

        JpowerAssert.notEmpty(coreCity.getCode(),JpowerError.Arg,"编号不可为空");
        JpowerAssert.notEmpty(coreCity.getName(),JpowerError.Arg,"名称不可为空");
        JpowerAssert.notTrue(Fc.isEmpty(coreCity.getRankd()),JpowerError.Arg,"城市级别不可为空");
        JpowerAssert.notEmpty(coreCity.getCityType(),JpowerError.Arg,"城市类型不可为空");

        if (coreCityService.add(coreCity)){
            return ReturnJsonUtil.ok("新增成功", coreCity.getId());
        }else {
            return ReturnJsonUtil.fail("新增失败");
        }
    }

    @ApiOperation(value = "修改行政区域",notes = "主键必传")
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update( TbCoreCity coreCity){
        JpowerAssert.notNull(coreCity.getId(),JpowerError.Arg,"主键不可为空");

        return ReturnJsonUtil.status(coreCityService.update(coreCity));
    }

    @Function(value = "保存",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_CITY",code = "SYSTEM_CITY_SAVE",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "保存行政区域",notes = "主键传是修改，不传是新增")
    @PostMapping(value = "/save", produces="application/json")
    public ResponseData save( TbCoreCity coreCity){
        return Fc.notNull(coreCity.getId())?update(coreCity):add(coreCity);
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_CITY",code = "SYSTEM_CITY_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除行政区域")
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData delete(@ApiParam(value = "主键，多个逗号分割",required = true) @RequestParam String ids){
        return ReturnJsonUtil.status(coreCityService.deleteBatch(Fc.toLongList(ids)));
    }

    @Function(value = "详情",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_CITY",code = "SYSTEM_CITY_DETAIL",type = Menu.TYPE.BTN)
    })
    @ApiOperation("查询行政区域详情")
    @GetMapping(value = "/get", produces="application/json")
    public ResponseData<CityVo> get(@ApiParam(value = "主键",required = true) @RequestParam Long id){
        return ReturnJsonUtil.ok("成功", coreCityService.getById(id));
    }

    @Function(value = "列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_CITY",code = "CITY_LIST",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("懒加载树形菜单")
    @RequestMapping(value = "/lazyTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Tree<String>>> lazyTree(@ApiParam(value = "父级编码",required = true) @RequestParam String pcode){
        List<Tree<String>> nodeList = coreCityService.lazyTree(pcode);
        return ReturnJsonUtil.ok("查询成功",nodeList);
    }
}
