package top.jpower.jpower.controller.dict;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.core.utils.constants.ConstantsEnum;
import top.jpower.core.utils.utils.Fc;
import top.jpower.core.utils.utils.ReturnJsonUtil;
import top.jpower.jpower.dbs.entity.dict.TbCoreDict;
import top.jpower.jpower.dbs.entity.dict.TbCoreDictType;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.base.vo.Pg;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.controller.BaseController;
import top.jpower.jpower.module.common.page.PaginationContext;
import top.jpower.jpower.module.common.utils.CacheUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.dict.CoreDictService;
import top.jpower.jpower.service.dict.CoreDictTypeService;
import top.jpower.jpower.vo.DictVo;

import java.util.List;

import static top.jpower.core.utils.constants.ConstantsUtils.I18N_KEY;
import static top.jpower.core.utils.constants.JpowerConstants.TOP_CODE;

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
public class DictController extends BaseController {

    private CoreDictService coreDictService;
    private CoreDictTypeService coreDictTypeService;

    @Function(value = "字典类型树",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_TYPELIST",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("查询所有字典类型树形结构")
    @RequestMapping(value = "/dictTypeTree",method = RequestMethod.GET,produces="application/json")
    public ResponseData<List<Tree<Long>>> dictTypeTree(){
        return ReturnJsonUtil.data(coreDictTypeService.tree());
    }

    @Function(value = "新增字典类型",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_TYPE_ADD",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "新增字典类型")
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces="application/json")
    public ResponseData<Long> add(TbCoreDictType dictType){
        JpowerAssert.notEmpty(dictType.getDictTypeCode(), JpowerError.Arg,"字典类型编号不可为空");
        JpowerAssert.notEmpty(dictType.getDictTypeName(), JpowerError.Arg,"字典类型名称不可为空");

        CacheUtil.clear(CacheNames.DICT_KEY);
        return ReturnJsonUtil.status(coreDictTypeService.addDictType(dictType), dictType.getId());
    }

    @Function(value = "修改字典类型",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_TYPE_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "更新字典类型")
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces="application/json")
    public ResponseData update(TbCoreDictType dictType){
        JpowerAssert.notNull(dictType.getId(), JpowerError.Arg,"字典类型主键不可为空");
        CacheUtil.clear(CacheNames.DICT_KEY);
        return ReturnJsonUtil.status(coreDictTypeService.updateDictType(dictType));
    }

    @Function(value = "删除字典类型",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_TYPE_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除字典类型")
    @DeleteMapping(value = "/deleteDictType",produces="application/json")
    public ResponseData deleteDictType(@ApiParam(value = "主键，多个逗号分割",required = true) @RequestParam String ids){
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");
        CacheUtil.clear(CacheNames.DICT_KEY);
        return ReturnJsonUtil.status(coreDictTypeService.deleteDictType(Fc.toLongList(ids)));
    }

    @Function(value = "字典类型详情",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_TYPE_DETAIL",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("查询字典类型详情")
    @RequestMapping(value = "/getDictType",method = RequestMethod.GET,produces="application/json")
    public ResponseData<TbCoreDictType> getDictType(@ApiParam(value = "主键",required = true) @RequestParam Long id){
        JpowerAssert.notNull(id, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.ok("查询成功", coreDictTypeService.getById(id));
    }

    @Function(value = "字典列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_LIST",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("通过字典类型分页查询字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "dictTypeCode",value = "字典类型编码",paramType = "query",required = true),
            @ApiImplicitParam(name = "code",value = "字典编码",paramType = "query"),
            @ApiImplicitParam(name = "name",value = "字典名称",paramType = "query")
    })
    @RequestMapping(value = "/listByType",method = RequestMethod.GET,produces="application/json")
    public ResponseData<Pg<DictVo>> listByType(@ApiIgnore TbCoreDict dict){
        JpowerAssert.notEmpty(dict.getDictTypeCode(), JpowerError.Arg,"字典类型不可为空");
        if (Fc.isNull(dict.getParentId())){
            dict.setParentId(Fc.toLong(TOP_CODE));
        }

        PaginationContext.startPage();
        List<DictVo> list = coreDictService.listByType(dict);
        return ReturnJsonUtil.data(new PageInfo<>(list));
    }

    @Function(value = "字典子级",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_LIST_BY_PARENT",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation(value = "查询下级字典",notes = "parentId不可传-1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId",value = "父级字典",paramType = "query",required = true),
            @ApiImplicitParam(name = "code",value = "字典编码",paramType = "query"),
            @ApiImplicitParam(name = "name",value = "字典名称",paramType = "query")
    })
    @GetMapping(value = "/listDictChildList",produces="application/json")
    public ResponseData<List<DictVo>> listDictChildList(@ApiIgnore TbCoreDict dict){
        JpowerAssert.notNull(dict.getParentId(), JpowerError.Arg,"父级字典id不可为空");
        JpowerAssert.notTrue(Fc.equalsValue(dict.getParentId(), TOP_CODE), JpowerError.Arg,"父级字典id不可为-1");

        List<DictVo> list = coreDictService.listByType(dict);
        return ReturnJsonUtil.ok("查询成功", list);
    }

    @Function(value = "保存字典",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_SAVE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("保存或者新增字典")
    @RequestMapping(value = "/saveDict",method = RequestMethod.POST,produces="application/json")
    public ResponseData saveDict(TbCoreDict dict){
        if (Fc.isNull(dict.getId())){
            JpowerAssert.notEmpty(dict.getDictTypeCode(), JpowerError.Arg,"字典类型编号不可为空");
            JpowerAssert.notEmpty(dict.getCode(), JpowerError.Arg,"字典编号不可为空");
            JpowerAssert.notEmpty(dict.getName(), JpowerError.Arg,"字典名称不可为空");
        }else {
            //防止用户A在更新时，用户B做了删除操作
            if (Fc.isNull(coreDictService.getById(dict.getId()))){
                return ReturnJsonUtil.fail("该数据不存在");
            }
        }

        if (Fc.isNotBlank(dict.getDictTypeCode())){
            if(coreDictTypeService.count(Condition.<TbCoreDictType>getQueryWrapper()
                    .lambda()
                    .eq(TbCoreDictType::getDictTypeCode,dict.getDictTypeCode())) <= 0){
                return ReturnJsonUtil.notFind("字典类型不存在");
            }
        }

        CacheUtil.clear(CacheNames.DICT_KEY);
        return ReturnJsonUtil.status(coreDictService.saveDict(dict));
    }

    @Function(value = "停用字典",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_STOP",type = Menu.TYPE.BTN)
    })
    @ApiOperation("停用字典")
    @PostMapping(value = "/stopDict",produces="application/json")
    public ResponseData stopDict(@ApiParam("字典主键，多个逗号分割") String ids){
        JpowerAssert.notEmpty(ids,JpowerError.Arg,"字典主键不可为空");

        long count = coreDictService.count(Condition.<TbCoreDict>getQueryWrapper().lambda().eq(TbCoreDict::getIsStop, ConstantsEnum.YN.N.getValue()).in(TbCoreDict::getParentId,Fc.toLongList(ids)));
        JpowerAssert.geZero(count,JpowerError.Business,"存在启用的下级字典，不可停用");

        CacheUtil.clear(CacheNames.DICT_KEY);
        return ReturnJsonUtil.status(coreDictService.update(Wrappers.<TbCoreDict>lambdaUpdate().set(TbCoreDict::getIsStop, ConstantsEnum.YN.Y.getValue()).in(TbCoreDict::getId,Fc.toLongList(ids))));
    }

    @Function(value = "删除字典",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除字典")
    @RequestMapping(value = "/deleteDict",method = RequestMethod.DELETE,produces="application/json")
    public ResponseData deleteDict(@ApiParam(value = "主键，多个逗号分割",required = true) @RequestParam String ids){
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"字典ID不可为空");
        List<Long> list = Fc.toLongList(ids);

        if(coreDictService.count(Condition.<TbCoreDict>getQueryWrapper()
                .lambda()
                .in(TbCoreDict::getParentId,list)) > 0){
            return ReturnJsonUtil.fail("请先删除下级字典");
        }

        CacheUtil.clear(CacheNames.DICT_KEY);
        return ReturnJsonUtil.status(coreDictService.removeRealByIds(list));
    }

    @Function(value = "字典详情",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_DETAIL",type = Menu.TYPE.BTN)
    })
    @ApiOperation("查询字典详情")
    @RequestMapping(value = "/getDict",method = RequestMethod.GET,produces="application/json")
    public ResponseData<TbCoreDict> getDict(@ApiParam(value = "字典ID",required = true) @RequestParam(required = false) Long id){
        JpowerAssert.notNull(id, JpowerError.Arg,"字典ID不可为空");
        return ReturnJsonUtil.data(coreDictService.getById(id));
    }

    @ApiOperationSupport(order = 100)
    @ApiOperation("通过字典类型查询字典列表")
    @GetMapping("/getDictListByType")
    public ResponseData<List<DictVo>> getDictListByType(TbCoreDict dict){
        JpowerAssert.notEmpty(dict.getDictTypeCode(), JpowerError.Arg,"字典类型不可为空");
        if (Fc.isNull(dict.getParentId())){
            dict.setParentId(Fc.toLong(TOP_CODE));
        }

        //只查询未停用的
        dict.setIsStop(ConstantsEnum.YN.N.getValue());
        //查询的语言
        dict.setLocale(Fc.toStr(getRequest().getHeader(I18N_KEY), ConstantsEnum.YYZL.CHINA.getValue()));

        return ReturnJsonUtil.data(coreDictService.listByType(dict));
    }

    @ApiOperationSupport(order = 101)
    @ApiOperation(value = "根据字典类型查询树形字典")
    @GetMapping(value = "/treeDict",produces="application/json")
    public ResponseData<List<Tree<Long>>> treeDict(@ApiParam("字典类型编码") String dictTypeCode){
        JpowerAssert.notEmpty(dictTypeCode, JpowerError.Arg,"字典类型编码不可为空");

        return ReturnJsonUtil.data(coreDictService.tree(Condition.getLambdaTreeWrapper(TbCoreDict.class,TbCoreDict::getId,TbCoreDict::getParentId)
                .eq(TbCoreDict::getDictTypeCode,dictTypeCode)));
    }
}
