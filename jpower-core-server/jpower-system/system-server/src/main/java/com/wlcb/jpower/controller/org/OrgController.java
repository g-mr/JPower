package com.wlcb.jpower.controller.org;

import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.service.org.CoreOrgService;
import com.wlcb.jpower.vo.OrgVo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

    @ApiOperation("分页懒加载组织机构树形列表")
    @RequestMapping(value = "/listLazyByParent",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData<PageInfo<OrgVo>> listLazyByParent(TbCoreOrg coreOrg){

        if (StringUtils.isBlank(coreOrg.getParentId())){
            coreOrg.setParentId("-1");
        }

        PaginationContext.startPage();
        PageInfo<OrgVo> pageInfo = new PageInfo<>(coreOrgService.listLazyByParent(coreOrg));
        return ReturnJsonUtil.ok("获取成功", pageInfo);
    }

    @ApiOperation(value = "新增一个组织机构",notes = "无需传主键")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreOrg coreOrg){

        BeanUtil.allFieldIsNULL(coreOrg,
                "name","code");

        TbCoreOrg org = coreOrgService.selectOrgByCode(coreOrg.getCode());
        if (org != null){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该组织机构已存在", false);
        }

        if (StringUtils.isBlank(coreOrg.getParentId())){
            coreOrg.setParentId("-1");
            coreOrg.setAncestorId("-1");
        }else {
            coreOrg.setAncestorId(coreOrg.getParentId().concat(StringPool.COMMA).concat(coreOrgService.getById(coreOrg.getParentId()).getAncestorId()));
        }
        Boolean is = coreOrgService.add(coreOrg);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"新增成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"新增失败", false);
        }
    }

    @ApiOperation("删除组织机构")
    @RequestMapping(value = "/deleteStatus",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData deleteStatus(@ApiParam(value = "主键 多个逗号分割",required = true) @RequestParam String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");

        Integer c = coreOrgService.listOrgByPids(ids);
        if (c > 0){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"您选中的组织机构存在下级机构，请先删除下级机构", false);
        }

        Boolean is = coreOrgService.removeByIds(Fc.toStrList(ids));

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"删除成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"删除失败", false);
        }
    }

    @ApiOperation("修改组织机构信息")
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreOrg coreOrg){

        JpowerAssert.notEmpty(coreOrg.getId(), JpowerError.Arg,"id不可为空");

        if (StringUtils.isNotBlank(coreOrg.getCode())){
            TbCoreOrg org = coreOrgService.selectOrgByCode(coreOrg.getCode());
            if (org != null && !StringUtils.equals(org.getId(),coreOrg.getId())){
                return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该组织机构编码已存在", false);
            }
        }

        Boolean is = coreOrgService.update(coreOrg);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"修改成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"修改失败", false);
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
