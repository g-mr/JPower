package top.jpower.jpower.controller.org;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import top.jpower.jpower.dbs.entity.org.TbCoreOrg;
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
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;
import top.jpower.jpower.module.common.utils.constants.JpowerConstants;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.org.CoreOrgService;
import top.jpower.jpower.vo.OrgVo;
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

    @Function(value = "下级部门",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORGCHILDER_LIST",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("懒加载组织机构树形列表")
    @GetMapping(value = "/listLazyByParent",produces="application/json")
    public ResponseData<List<OrgVo>> listLazyByParent(TbCoreOrg coreOrg){
        coreOrg.setParentId(Fc.isNotBlank(coreOrg.getParentId())?coreOrg.getParentId():JpowerConstants.TOP_CODE);
        return ReturnJsonUtil.ok("获取成功", coreOrgService.listLazyByParent(coreOrg));
    }

    @Function(value = "树形列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORG_TREELIST",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("分页懒加载组织机构树形列表")
    @GetMapping(value = "/listLazy",produces="application/json")
    public ResponseData<Pg<OrgVo>> listLazy(TbCoreOrg coreOrg){
        List<OrgVo> list = coreOrgService.listLazyByParent(coreOrg);
        List<OrgVo> pageList = ListUtil.page(PaginationContext.getPageNum()-1,PaginationContext.getPageSize(),list);
        return ReturnJsonUtil.data(new Pg<>(list.size(),pageList));
    }

    @Function(value = "新增",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "ORG_CHILD_ADD",type = Menu.TYPE.BTN),
            @Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORG_ADD",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "新增一个组织机构",notes = "无需传主键(id)")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreOrg coreOrg){
        JpowerAssert.notEmpty(coreOrg.getName(),JpowerError.Arg,"名称不可为空");
        JpowerAssert.notEmpty(coreOrg.getCode(),JpowerError.Arg,"编码不可为空");

        Boolean is = coreOrgService.add(coreOrg);

        if (is){
            CacheUtil.clear(CacheNames.ORG_KEY, coreOrg.getTenantCode());
            return ReturnJsonUtil.ok("新增成功",coreOrg.getId());
        }else {
            return ReturnJsonUtil.fail("新增失败");
        }
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORG_DELETE",type = Menu.TYPE.BTN)
    })
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
            CacheUtil.clear(CacheNames.ORG_KEY, tenants.toArray(new String[tenants.size()]));
            return ReturnJsonUtil.ok("删除成功");
        }else {
            return ReturnJsonUtil.fail("删除失败");
        }
    }

    @Function(value = "编辑",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORG_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "修改组织机构信息")
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreOrg coreOrg){
        JpowerAssert.notEmpty(coreOrg.getId(), JpowerError.Arg,"id不可为空");

        Boolean is = coreOrgService.update(coreOrg);

        if (is){
            CacheUtil.clear(CacheNames.ORG_KEY,coreOrg.getTenantCode());
            return ReturnJsonUtil.ok("修改成功");
        }else {
            return ReturnJsonUtil.fail("修改失败");
        }
    }

    @Function(value = "树形部门",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORG_TREE",type = Menu.TYPE.INTERFACE),
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "SYSTEM_USER_ORG",type = Menu.TYPE.INTERFACE),
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_ORG",type = Menu.TYPE.INTERFACE)
    })
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
            @ApiImplicitParam(name = "type",value = "机构类型 字典ORG_TYPE",paramType = "query"),
    })
    @RequestMapping(value = "/tree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Tree<String>>> tree(@ApiIgnore @RequestParam Map<String,Object> coreOrg){
        List<Tree<String>> list = coreOrgService.tree(coreOrg);
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
            @ApiImplicitParam(name = "type",value = "机构类型 字典ORG_TYPE",paramType = "query"),

    })
    @RequestMapping(value = "/lazyTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Tree<String>>> lazyTree(@ApiParam(value = "父级ID",defaultValue = JpowerConstants.TOP_CODE,required = true) @RequestParam(defaultValue = JpowerConstants.TOP_CODE)  String parentId,
                                 @ApiIgnore @RequestParam Map<String,Object> coreOrg){
        coreOrg.remove("parentId");
        List<Tree<String>> list = coreOrgService.tree(parentId,coreOrg);
        return ReturnJsonUtil.ok("查询成功",list);
    }

}
