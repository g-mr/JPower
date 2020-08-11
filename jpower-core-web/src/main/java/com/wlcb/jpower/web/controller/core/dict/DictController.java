package com.wlcb.jpower.web.controller.core.dict;

import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.core.dict.CoreDictService;
import com.wlcb.jpower.module.common.service.core.dict.CoreDictTypeService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDictType;
import com.wlcb.jpower.module.mp.support.Condition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DictController
 * @Description TODO 字典相关
 * @Author 郭丁志
 * @Date 2020/7/26 0026 17:52
 * @Version 1.0
 */
@RestController
@RequestMapping("/core/dict")
public class DictController {

    @Resource
    private CoreDictService coreDictService;
    @Resource
    private CoreDictTypeService coreDictTypeService;

    /**
     * @author 郭丁志
     * @Description //TODO 查询所有字典类型树形结构
     * @date 18:03 2020/7/26 0026
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/dictTypeTree",method = RequestMethod.GET,produces="application/json")
    public ResponseData dictTypeTree(){
        List<Node> list = coreDictTypeService.tree();
        return ReturnJsonUtil.ok("查询成功",list);
    }

    /**
     * @author 郭丁志
     * @Description //TODO 保存字典类型
     * @date 19:19 2020/7/26 0026
     * @param dictType
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/saveDictType",method = RequestMethod.POST,produces="application/json")
    public ResponseData saveDictType(TbCoreDictType dictType){
        if (Fc.isBlank(dictType.getId())){
            JpowerAssert.notEmpty(dictType.getDictTypeCode(), JpowerError.Arg,"字典类型编号不可为空");
            JpowerAssert.notEmpty(dictType.getDictTypeName(), JpowerError.Arg,"字典类型名称不可为空");
        }

        return ReturnJsonUtil.status(coreDictTypeService.saveDictType(dictType));
    }

    /**
     * @author 郭丁志
     * @Description //TODO 删除字典类型
     * @date 19:22 2020/7/26 0026
     * @param ids
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/deleteDictType",method = RequestMethod.DELETE,produces="application/json")
    public ResponseData deleteDictType(String ids){
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.status(coreDictTypeService.deleteDictType(Fc.toStrList(ids)));
    }

    /**
     * @author 郭丁志
     * @Description //TODO 查询字典详情
     * @date 20:12 2020/7/26 0026
     * @param id
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/getDictType",method = RequestMethod.GET,produces="application/json")
    public ResponseData getDictType(String id){
        JpowerAssert.notEmpty(id, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.ok("查询成功", coreDictTypeService.getById(id));
    }

    /**
     * @author 郭丁志
     * @Description //TODO 通过字典类型查询字典
     * @date 20:42 2020/7/26 0026
     * @param dict
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/listByType",method = RequestMethod.GET,produces="application/json")
    public ResponseData listByType(String typeCode ,@RequestParam Map<String,Object> dict){
        dict.remove("typeCode");
        JpowerAssert.notEmpty(typeCode, JpowerError.Arg,"字典类型不可为空");
        PaginationContext.startPage();
        List<TbCoreDict> list = coreDictService.list(Condition.getQueryWrapper(dict,TbCoreDict.class).lambda().eq(TbCoreDict::getDictTypeCode,typeCode).orderByAsc(TbCoreDict::getSortNum));
        return ReturnJsonUtil.ok("查询成功", new PageInfo<>(list));
    }

    /**
     * @author 郭丁志
     * @Description //TODO 保存或者新增字典
     * @date 20:53 2020/7/26 0026
     * @param dict
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
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

    /**
     * @author 郭丁志
     * @Description //TODO 删除字典
     * @date 21:03 2020/7/26 0026
     * @param ids
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/deleteDict",method = RequestMethod.DELETE,produces="application/json")
    public ResponseData deleteDict(String ids){
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"字典ID不可为空");
        return ReturnJsonUtil.status(coreDictService.removeRealByIds(Fc.toStrList(ids)));
    }

    /**
     * @author 郭丁志
     * @Description //TODO 查询字典详情
     * @date 21:07 2020/7/26 0026
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/getDict",method = RequestMethod.GET,produces="application/json")
    public ResponseData getDict(TbCoreDict dict){
        return ReturnJsonUtil.ok("查询成功",coreDictService.getOne(Condition.getQueryWrapper(dict),false));
    }

}
