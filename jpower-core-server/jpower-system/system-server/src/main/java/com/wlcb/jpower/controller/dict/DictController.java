package com.wlcb.jpower.controller.dict;

import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDict;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDictType;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.dict.CoreDictService;
import com.wlcb.jpower.service.dict.CoreDictTypeService;
import com.wlcb.jpower.vo.DictVo;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @ClassName DictController
 * @Description TODO 字典相关
 * @Author 郭丁志
 * @Date 2020/7/26 0026 17:52
 * @Version 1.0
 */
@Api(tags = "字典管理")
@RestController
@RequestMapping("/core/dict")
@AllArgsConstructor
public class DictController {

    private CoreDictService coreDictService;
    private CoreDictTypeService coreDictTypeService;

    @GetMapping("/test")
    @GlobalTransactional
    public ResponseData<String> test(@RequestParam Integer t) throws InterruptedException {

        TbCoreDictType dict = new TbCoreDictType();
        dict.setDictTypeName("测试");
        dict.setDictTypeCode("ttt");
        coreDictTypeService.save(dict);

        if (t > 10){
            throw new RuntimeException("抛个异常");
        }

//        coreDictTypeService.test(t);

        return ReturnJsonUtil.ok("查询成功");
    }

    @ApiOperation("查询所有字典类型树形结构")
    @RequestMapping(value = "/dictTypeTree",method = RequestMethod.GET,produces="application/json")
    public ResponseData<List<Node>> dictTypeTree(){
        List<Node> list = coreDictTypeService.tree();
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @ApiOperation(value = "新增字典类型")
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces="application/json")
    public ResponseData add(TbCoreDictType dictType){
        JpowerAssert.notEmpty(dictType.getDictTypeCode(), JpowerError.Arg,"字典类型编号不可为空");
        JpowerAssert.notEmpty(dictType.getDictTypeName(), JpowerError.Arg,"字典类型名称不可为空");

        return ReturnJsonUtil.status(coreDictTypeService.addDictType(dictType));
    }

    @ApiOperation(value = "更新字典类型")
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces="application/json")
    public ResponseData update(TbCoreDictType dictType){
        if (Fc.isBlank(dictType.getId())){
            JpowerAssert.notEmpty(dictType.getDictTypeCode(), JpowerError.Arg,"字典类型编号不可为空");
            JpowerAssert.notEmpty(dictType.getDictTypeName(), JpowerError.Arg,"字典类型名称不可为空");
        }

        return ReturnJsonUtil.status(coreDictTypeService.updateDictType(dictType));
    }

    @ApiOperation("删除字典类型")
    @RequestMapping(value = "/deleteDictType",method = RequestMethod.DELETE,produces="application/json")
    public ResponseData deleteDictType(@ApiParam(value = "主键，多个逗号分割",required = true) @RequestParam String ids){
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.status(coreDictTypeService.deleteDictType(Fc.toStrList(ids)));
    }

    @ApiOperation("查询字典类型详情")
    @RequestMapping(value = "/getDictType",method = RequestMethod.GET,produces="application/json")
    public ResponseData<TbCoreDictType> getDictType(@ApiParam(value = "主键",required = true) @RequestParam String id){
        JpowerAssert.notEmpty(id, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.ok("查询成功", coreDictTypeService.getById(id));
    }

    @ApiOperation("通过字典类型分页查询字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "dictTypeCode",value = "字典类型编码",paramType = "query",required = true),
            @ApiImplicitParam(name = "code",value = "字典编码",paramType = "query"),
            @ApiImplicitParam(name = "name",value = "字典名称",paramType = "query")
    })
    @RequestMapping(value = "/listByType",method = RequestMethod.GET,produces="application/json")
    public ResponseData<PageInfo<DictVo>> listByType(@ApiIgnore TbCoreDict dict){
        JpowerAssert.notEmpty(dict.getDictTypeCode(), JpowerError.Arg,"字典类型不可为空");

        PaginationContext.startPage();
        List<DictVo> list = coreDictService.listByType(dict);
        return ReturnJsonUtil.ok("查询成功", new PageInfo<>(list));
    }

    @ApiOperation(value = "查询下级字典",notes = "不可传-1")
    @RequestMapping(value = "/listDictChildList",method = RequestMethod.GET,produces="application/json")
    public ResponseData<List<DictVo>> listDictChildList(@ApiParam(value = "父级字典id",required = true) @RequestParam String parentId){
        JpowerAssert.notEmpty(parentId, JpowerError.Arg,"父级字典id不可为空");
        JpowerAssert.notTrue(Fc.equals(parentId, JpowerConstants.TOP_CODE), JpowerError.Arg,"父级字典id不可为-1");

        TbCoreDict dict = new TbCoreDict();
        dict.setParentId(parentId);

        List<DictVo> list = coreDictService.listByType(dict);
        return ReturnJsonUtil.ok("查询成功", list);
    }

    @ApiOperation("保存或者新增字典")
    @RequestMapping(value = "/saveDict",method = RequestMethod.POST,produces="application/json")
    public ResponseData saveDict(TbCoreDict dict){
        if (Fc.isBlank(dict.getId())){
            JpowerAssert.notEmpty(dict.getDictTypeCode(), JpowerError.Arg,"字典类型编号不可为空");
            JpowerAssert.notEmpty(dict.getCode(), JpowerError.Arg,"字典编号不可为空");
            JpowerAssert.notEmpty(dict.getName(), JpowerError.Arg,"字典名称不可为空");
        }

        if(coreDictTypeService.count(Condition.<TbCoreDictType>getQueryWrapper().lambda().eq(TbCoreDictType::getDictTypeCode,dict.getDictTypeCode())) <= 0){
            return ReturnJsonUtil.notFind("字典类型不存在");
        }

        return ReturnJsonUtil.status(coreDictService.saveDict(dict));
    }

    @ApiOperation("删除字典")
    @RequestMapping(value = "/deleteDict",method = RequestMethod.DELETE,produces="application/json")
    public ResponseData deleteDict(@ApiParam(value = "主键，多个逗号分割",required = true) @RequestParam String ids){
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"字典ID不可为空");

        if(coreDictService.count(Condition.<TbCoreDict>getQueryWrapper()
                .lambda()
                .in(TbCoreDict::getParentId,Fc.toStrList(ids))) > 0){
            return ReturnJsonUtil.notFind("请先删除下级字典");
        }

        List<String> list = Fc.toStrList(ids);
        List<TbCoreDict> dicts = coreDictService.listByIds(list);
        if (!Fc.isNull(dicts) && dicts.size() > 0){
            dicts.forEach(ls -> CacheUtil.evict(CacheNames.DICT_REDIS_CACHE,CacheNames.DICT_REDIS_TYPE_MAP_KEY,ls.getDictTypeCode()));
        }

        return ReturnJsonUtil.status(coreDictService.removeRealByIds(list));
    }

    @ApiOperation("查询字典详情")
    @RequestMapping(value = "/getDict",method = RequestMethod.GET,produces="application/json")
    public ResponseData<TbCoreDict> getDict(@ApiParam(value = "字典ID",required = true) @RequestParam(required = true) String id){
        JpowerAssert.notEmpty(id, JpowerError.Arg,"字典ID不可为空");
        return ReturnJsonUtil.ok("查询成功",coreDictService.getById(id));
    }

    @ApiOperation("通过字典类型查询字典列表")
    @RequestMapping(value = "/getDictListByType",method = RequestMethod.GET,produces="application/json")
    public ResponseData<List<TbCoreDict>> getDictListByType(@ApiParam(value = "字典类型编码",required = true) @RequestParam String dictType){
        JpowerAssert.notEmpty(dictType, JpowerError.Arg,"字典类型不可为空");
        return ReturnJsonUtil.ok("查询成功",coreDictService.list(Condition.<TbCoreDict>getQueryWrapper().lambda()
                .eq(TbCoreDict::getDictTypeCode,dictType)));
    }

}
