package com.wlcb.jpower.controller.city;

import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.dbs.entity.city.TbCoreCity;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.mp.support.SqlKeyword;
import com.wlcb.jpower.service.city.CoreCityService;
import com.wlcb.jpower.wrapper.DictWrapper;
import io.swagger.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = "行政区域管理")
@RestController
@RequestMapping("/core/city")
public class CityController extends BaseController {

    @Resource
    private CoreCityService coreCityService;

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

    @ApiOperation("分页查询行政区域列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pcode",value = "父级编码",defaultValue = JpowerConstants.TOP_CODE,required = true,paramType = "query")
    })
    @RequestMapping(value = "/listPage",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<PageInfo<TbCoreCity>> listPage(TbCoreCity coreCity){
        coreCity.setPcode(Fc.isNotBlank(coreCity.getPcode())?coreCity.getPcode():JpowerConstants.TOP_CODE);

        PaginationContext.startPage();
        List<TbCoreCity> list = coreCityService.list(coreCity);

        return ReturnJsonUtil.ok("获取成功", new PageInfo<>(DictWrapper.dict(list,TbCoreCity.class)));
    }

    @ApiOperation(value = "新增行政区域",notes = "主键不可传")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add( TbCoreCity coreCity){

        JpowerAssert.notEmpty(coreCity.getCode(),JpowerError.Arg,"编号不可为空");
        JpowerAssert.notEmpty(coreCity.getName(),JpowerError.Arg,"名称不可为空");
        JpowerAssert.notTrue(Fc.isEmpty(coreCity.getRankd()),JpowerError.Arg,"城市级别不可为空");
        JpowerAssert.notEmpty(coreCity.getCityType(),JpowerError.Arg,"城市类型不可为空");

        Boolean is = coreCityService.add(coreCity);
        return ReturnJsonUtil.status(is);
    }

    @ApiOperation(value = "修改行政区域",notes = "主键必传")
    @Caching(evict = {@CacheEvict(value= CacheNames.CITY_PARENT_REDIS_KEY, key = "#coreCity.pcode"),
            @CacheEvict(value= {CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY}, allEntries = true)})
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update( TbCoreCity coreCity){
        JpowerAssert.notEmpty(coreCity.getId(),JpowerError.Arg,"主键不可为空");
        JpowerAssert.notEmpty(coreCity.getPcode(),JpowerError.Arg,"父级编码不可为空");

        if (Fc.isNotBlank(coreCity.getCode())){
            TbCoreCity city = coreCityService.getById(coreCity.getId());
            JpowerAssert.isTrue(Fc.equals(city.getCode(),coreCity.getCode()),JpowerError.Arg,"编码不可修改");
        }

        return ReturnJsonUtil.status(coreCityService.updateById(coreCity));
    }

    @ApiOperation("删除行政区域")
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData delete(@ApiParam(value = "主键，多个逗号分割",required = true) @RequestParam String ids){
        return ReturnJsonUtil.status(coreCityService.deleteBatch(Fc.toStrList(ids)));
    }

    @ApiOperation("懒加载树形菜单")
    @RequestMapping(value = "/lazyTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Node>> lazyTree(@ApiParam(value = "父级编码",required = true) @RequestParam String pcode){
        List<Node> nodeList = coreCityService.lazyTree(pcode);
        return ReturnJsonUtil.ok("查询成功",nodeList);
    }
}
