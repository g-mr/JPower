package com.wlcb.jpower.controller.params;

import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.dbs.entity.params.TbCoreParam;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.params.CoreParamService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@Api(tags = "系统参数管理")
@RestController
@RequestMapping("/core/param")
@AllArgsConstructor
public class ParamsController extends BaseController {

    private CoreParamService paramService;

    @ApiOperation("系统参数分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "code",value = "编码",paramType = "query"),
            @ApiImplicitParam(name = "name",value = "名称",paramType = "query"),
            @ApiImplicitParam(name = "value",value = "值",paramType = "query")
    })
    @GetMapping(value = "/list" , produces="application/json")
    public ResponseData<PageInfo<TbCoreParam>> list(@ApiIgnore @RequestParam Map<String,Object> coreParam){
        PaginationContext.startPage();
        List<TbCoreParam> list = paramService.list(Condition.getQueryWrapper(coreParam,TbCoreParam.class).lambda().orderByDesc(TbCoreParam::getCreateTime));
        return ReturnJsonUtil.ok("获取成功", new PageInfo<>(list));
    }

    @ApiOperation("删除系统参数")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE,produces="application/json")
    public ResponseData delete(@ApiParam(value = "主键",required = true) @RequestParam String ids){
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");
        return ReturnJsonUtil.status(paramService.deletes(ids));
    }

    @ApiOperation("修改系统参数")
    @RequestMapping(value = "/update",method = RequestMethod.PUT,produces="application/json")
    public ResponseData update(TbCoreParam coreParam){
        JpowerAssert.notEmpty(coreParam.getId(), JpowerError.Arg,"id不可为空");
        return ReturnJsonUtil.status(paramService.update(coreParam));
    }

    @ApiOperation(value = "新增系统参数",notes = "新增不用传主键")
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces="application/json")
    public ResponseData add(TbCoreParam coreParam){
        JpowerAssert.notEmpty(coreParam.getCode(), JpowerError.Arg,"编号值不可为空");
        JpowerAssert.notEmpty(coreParam.getName(), JpowerError.Arg,"参数名称不可为空");
        JpowerAssert.notEmpty(coreParam.getValue(), JpowerError.Arg,"参数值不可为空");

        JpowerAssert.isEmpty(paramService.selectByCode(coreParam.getCode()), JpowerError.BUSINESS,"该系统参数已存在");

        return ReturnJsonUtil.status(paramService.save(coreParam));
    }

    @ApiOperation(value = "通过Id获取参数详情")
    @RequestMapping(value = "/queryById",method = RequestMethod.GET,produces="application/json")
    public ResponseData<TbCoreParam> queryById(@ApiParam("主键ID") @RequestParam String id){
        JpowerAssert.notEmpty(id, JpowerError.Arg,"ID不可为空");
        return ReturnJsonUtil.ok("查询成功",paramService.getById(id));
    }

    @ApiOperation(value = "立即生效一个参数")
    @RequestMapping(value = "/takeEffect",method = RequestMethod.GET,produces="application/json")
    public ResponseData<String> takeEffect(@ApiParam(value = "编码",required = true) @RequestParam String code){
        JpowerAssert.notEmpty(code, JpowerError.Arg,"编号值不可为空");

        TbCoreParam param = paramService.getOne(Condition.<TbCoreParam>getQueryWrapper().lambda().eq(TbCoreParam::getCode,code));
        JpowerAssert.notTrue(Fc.equals(param.getIsEffect(), ConstantsEnum.YN01.N.getValue()), JpowerError.BUSINESS,"该参数无法立即生效，请重启项目");

        if (Fc.isNotEmpty(param) && Fc.isNotBlank(param.getValue())){
            CacheUtil.put(CacheNames.PARAMS_REDIS_CACHE,CacheNames.PARAMS_REDIS_CODE_KEY,param.getCode(),param.getValue(),Boolean.FALSE);
        }else {
            if (Fc.isNull(param)){
                return ReturnJsonUtil.fail("该参数不存在");
            }
        }
        return ReturnJsonUtil.ok("操作成功");
    }

    @ApiOperation(value = "全部生效")
    @RequestMapping(value = "/effectAll",method = RequestMethod.GET,produces="application/json")
    public ResponseData<String> effectAll(){
        paramService.effectAll();
        return ReturnJsonUtil.ok("操作完成");
    }

}
