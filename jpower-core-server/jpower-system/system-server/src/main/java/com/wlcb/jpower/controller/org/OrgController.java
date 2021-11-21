package com.wlcb.jpower.controller.org;

import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.org.CoreOrgService;
import com.wlcb.jpower.vo.OrgVo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@Api(tags = "组织机构管理")
@RestController
@AllArgsConstructor
@RequestMapping("/core/org")
public class OrgController extends BaseController {

    private CoreOrgService coreOrgService;

    @ApiOperation("懒加载组织机构树形列表")
    @GetMapping(value = "/listLazyByParent",produces="application/json")
    public ResponseData<List<OrgVo>> listLazyByParent(TbCoreOrg coreOrg){
        return ReturnJsonUtil.ok("获取成功", coreOrgService.listLazyByParent(coreOrg));
    }

    @ApiOperation("分页懒加载组织机构树形列表")
    @GetMapping(value = "/listLazy",produces="application/json")
    public ResponseData<PageInfo<OrgVo>> listLazy(TbCoreOrg coreOrg){
        PaginationContext.startPage();
        PageInfo<OrgVo> pageInfo = new PageInfo<>(coreOrgService.listLazyByParent(coreOrg));
        return ReturnJsonUtil.ok("获取成功", pageInfo);
    }

    @ApiOperation(value = "新增一个组织机构",notes = "无需传主键(id)")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreOrg coreOrg){
        JpowerAssert.notEmpty(coreOrg.getName(),JpowerError.Arg,"名称不可为空");
        JpowerAssert.notEmpty(coreOrg.getCode(),JpowerError.Arg,"编码不可为空");

        Boolean is = coreOrgService.add(coreOrg);

        if (is){
            CacheUtil.clear(CacheNames.SYSTEM_REDIS_CACHE,coreOrg.getTenantCode());
            return ReturnJsonUtil.ok("新增成功");
        }else {
            return ReturnJsonUtil.fail("新增失败");
        }
    }

    @ApiOperation("删除组织机构")
    @RequestMapping(value = "/deleteStatus",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData deleteStatus(@ApiParam(value = "主键 多个逗号分割",required = true) @RequestParam String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");

        long c = coreOrgService.listOrgByPids(ids);
        if (c > 0){
            return ReturnJsonUtil.busFail("您选中的组织机构存在下级机构，请先删除下级机构");
        }


        List<String> tenants = coreOrgService.listObjs(Condition.<TbCoreOrg>getQueryWrapper().lambda().select(TbCoreOrg::getTenantCode).in(TbCoreOrg::getId,Fc.toStrList(ids)),Fc::toStr);
        Boolean is = coreOrgService.removeByIds(Fc.toStrList(ids));

        if (is){
            CacheUtil.clear(CacheNames.SYSTEM_REDIS_CACHE, tenants.toArray(new String[tenants.size()]));
            return ReturnJsonUtil.ok("删除成功");
        }else {
            return ReturnJsonUtil.fail("删除失败");
        }
    }

    @ApiOperation(value = "修改组织机构信息")
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreOrg coreOrg){
        JpowerAssert.notEmpty(coreOrg.getId(), JpowerError.Arg,"id不可为空");

        Boolean is = coreOrgService.update(coreOrg);

        if (is){
            CacheUtil.clear(CacheNames.SYSTEM_REDIS_CACHE,coreOrg.getTenantCode());
            return ReturnJsonUtil.ok("修改成功");
        }else {
            return ReturnJsonUtil.fail("修改失败");
        }
    }

    @ApiOperation("加载组织机构树形菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "编码",paramType = "query"),
            @ApiImplicitParam(name = "name",value = "名称",paramType = "query"),
            @ApiImplicitParam(name = "headName",value = "领导人名字",paramType = "query"),
            @ApiImplicitParam(name = "headPhone",value = "领导人电话",paramType = "query"),
            @ApiImplicitParam(name = "headEmail",value = "领导人邮箱",paramType = "query"),
            @ApiImplicitParam(name = "contactName",value = "联系人名字",paramType = "query"),
            @ApiImplicitParam(name = "contactPhone",value = "联系人电话",paramType = "query"),
            @ApiImplicitParam(name = "contactEmail",value = "联系人邮箱",paramType = "query"),
            @ApiImplicitParam(name = "address",value = "地址",paramType = "query"),
            @ApiImplicitParam(name = "isVirtual",value = "是否虚拟机构 字典YN01",paramType = "query"),
    })
    @RequestMapping(value = "/tree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Node>> tree(@ApiIgnore @RequestParam Map<String,Object> coreOrg){
        List<Node> list = coreOrgService.tree(coreOrg);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @ApiOperation("懒加载组织机构树形菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "编码",paramType = "query"),
            @ApiImplicitParam(name = "name",value = "名称",paramType = "query"),
            @ApiImplicitParam(name = "headName",value = "领导人名字",paramType = "query"),
            @ApiImplicitParam(name = "headPhone",value = "领导人电话",paramType = "query"),
            @ApiImplicitParam(name = "headEmail",value = "领导人邮箱",paramType = "query"),
            @ApiImplicitParam(name = "contactName",value = "联系人名字",paramType = "query"),
            @ApiImplicitParam(name = "contactPhone",value = "联系人电话",paramType = "query"),
            @ApiImplicitParam(name = "contactEmail",value = "联系人邮箱",paramType = "query"),
            @ApiImplicitParam(name = "address",value = "地址",paramType = "query"),
            @ApiImplicitParam(name = "isVirtual",value = "是否虚拟机构 字典YN01",paramType = "query"),

    })
    @RequestMapping(value = "/lazyTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Node>> lazyTree(@ApiParam(value = "父级ID",defaultValue = JpowerConstants.TOP_CODE,required = true) @RequestParam(defaultValue = JpowerConstants.TOP_CODE)  String parentId,
                                 @ApiIgnore @RequestParam Map<String,Object> coreOrg){
        coreOrg.remove("parentId");
        List<Node> list = coreOrgService.tree(parentId,coreOrg);
        return ReturnJsonUtil.ok("查询成功",list);
    }

}
