package com.wlcb.jpower.controller.city;

import com.wlcb.jpower.dbs.entity.city.TbCoreCity;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.mp.support.SqlKeyword;
import com.wlcb.jpower.service.city.CoreCityService;
import com.wlcb.jpower.vo.CityVo;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

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
        List<Map<String,Object>> list = coreCityService.listChild(ChainMap.init().set("pcode"+ SqlKeyword.EQUAL,pcode).set("name",name));
        return ReturnJsonUtil.ok("获取成功", list);
    }

    @ApiOperation(value = "新增行政区域",notes = "主键不可传")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add( TbCoreCity coreCity){

        JpowerAssert.notEmpty(coreCity.getCode(),JpowerError.Arg,"编号不可为空");
        JpowerAssert.notEmpty(coreCity.getName(),JpowerError.Arg,"名称不可为空");
        JpowerAssert.notTrue(Fc.isEmpty(coreCity.getRankd()),JpowerError.Arg,"城市级别不可为空");
        JpowerAssert.notEmpty(coreCity.getCityType(),JpowerError.Arg,"城市类型不可为空");

        return ReturnJsonUtil.status(coreCityService.add(coreCity));
    }

    @ApiOperation(value = "修改行政区域",notes = "主键必传")
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update( TbCoreCity coreCity){
        JpowerAssert.notEmpty(coreCity.getId(),JpowerError.Arg,"主键不可为空");

        return ReturnJsonUtil.status(coreCityService.update(coreCity));
    }

    @ApiOperation(value = "保存行政区域",notes = "主键传是修改，不传是新增")
    @PostMapping(value = "/save", produces="application/json")
    public ResponseData save( TbCoreCity coreCity){
        return Fc.isNotBlank(coreCity.getId())?update(coreCity):add(coreCity);
    }

    @ApiOperation("删除行政区域")
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData delete(@ApiParam(value = "主键，多个逗号分割",required = true) @RequestParam String ids){
        return ReturnJsonUtil.status(coreCityService.deleteBatch(Fc.toStrList(ids)));
    }

    @ApiOperation("查询行政区域详情")
    @GetMapping(value = "/get", produces="application/json")
    public ResponseData<CityVo> get(@ApiParam(value = "主键",required = true) @RequestParam String id){
        return ReturnJsonUtil.ok("成功", coreCityService.getById(id));
    }

    @ApiOperation("懒加载树形菜单")
    @RequestMapping(value = "/lazyTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Node>> lazyTree(@ApiParam(value = "父级编码",required = true) @RequestParam String pcode){
        List<Node> nodeList = coreCityService.lazyTree(pcode);
        return ReturnJsonUtil.ok("查询成功",nodeList);
    }
}
