package com.wlcb.jpower.controller.function;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlcb.jpower.dbs.entity.function.TbCoreDataScope;
import com.wlcb.jpower.dbs.entity.role.TbCoreRoleData;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.role.CoreDataScopeService;
import com.wlcb.jpower.service.role.CoreRoleDataService;
import com.wlcb.jpower.service.role.CoreRoleService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author ding
 * @description 数据权限入口
 * @date 2020-11-03 15:49
 */
@Api(tags = "数据权限管理")
@RestController
@RequestMapping("/core/dataScope")
@AllArgsConstructor
public class DataScopeController {

    private CoreDataScopeService dataScopeService;
    private CoreRoleService roleService;
    private CoreRoleDataService roleDataService;

    @ApiOperation(value = "新增",notes = "主键ID不可传")
    @PostMapping(value = "/add",produces="application/json")
    public ResponseData add(TbCoreDataScope dataScope){

        JpowerAssert.notEmpty(dataScope.getMenuId(), JpowerError.Arg,"菜单ID不可为空");
        JpowerAssert.notEmpty(dataScope.getScopeCode(), JpowerError.Arg,"编号不可为空");
        JpowerAssert.notEmpty(dataScope.getScopeName(), JpowerError.Arg,"名称不可为空");
        JpowerAssert.notEmpty(dataScope.getScopeClass(), JpowerError.Arg,"权限类名不可为空");
        if (Fc.isBlank(dataScope.getScopeColumn())){
            dataScope.setScopeColumn(StringPool.ASTERISK);
        }
        if(Fc.isEmpty(dataScope.getScopeType())){
            dataScope.setScopeType(ConstantsEnum.DATA_SCOPE_TYPE.ALL.getValue());
        }
        if(Fc.isEmpty(dataScope.getAllRole())){
            dataScope.setScopeType(ConstantsEnum.YN01.N.getValue());
        }
        if (Fc.equals(dataScope.getScopeType(),ConstantsEnum.DATA_SCOPE_TYPE.CUSTOM.getValue())){
            JpowerAssert.notEmpty(dataScope.getScopeValue(), JpowerError.Arg,"数据权限值域不可为空");
        }

        CacheUtil.clear(CacheNames.DATASCOPE_REDIS_CACHE,Boolean.FALSE);
        return ReturnJsonUtil.status(dataScopeService.save(dataScope));
    }

    @ApiOperation("修改")
    @PutMapping(value = "/update",produces="application/json")
    public ResponseData update(TbCoreDataScope dataScope){
        JpowerAssert.notEmpty(dataScope.getId(), JpowerError.Arg,"主键不可为空");

        TbCoreDataScope coreDataScope = dataScopeService.getOne(Condition.<TbCoreDataScope>getQueryWrapper().lambda()
                .eq(TbCoreDataScope::getScopeCode,dataScope.getScopeCode()));
        if (Fc.notNull(coreDataScope) && !Fc.equalsValue(coreDataScope.getId(),dataScope.getId())){
            return ReturnJsonUtil.fail("编号已经存在");
        }

        CacheUtil.clear(CacheNames.DATASCOPE_REDIS_CACHE,Boolean.FALSE);
        return ReturnJsonUtil.status(dataScopeService.updateById(dataScope));
    }

    @ApiOperation("删除")
    @DeleteMapping(value = "/delete",produces="application/json")
    public ResponseData delete(@ApiParam(value = "主键",required = true) @RequestParam String id){
        JpowerAssert.notEmpty(id, JpowerError.Arg,"主键不可为空");
        CacheUtil.clear(CacheNames.DATASCOPE_REDIS_CACHE,Boolean.FALSE);
        return ReturnJsonUtil.status(dataScopeService.removeRealById(id));
    }

    @ApiOperation("详情")
    @GetMapping(value = "/queryById",produces="application/json")
    public ResponseData<TbCoreDataScope> queryById(@ApiParam(value = "主键",required = true) @RequestParam String id){
        JpowerAssert.notEmpty(id, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.ok("查询成功",dataScopeService.getById(id));
    }

    @ApiOperation("分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId_eq",value = "菜单ID",required = true,paramType = "query"),
            @ApiImplicitParam(name = "scopeCode",value = "权限编号",paramType = "query"),
            @ApiImplicitParam(name = "scopeName",value = "权限名称",paramType = "query"),
            @ApiImplicitParam(name = "scopeType_eq",value = "权限类型 字典DATA_SCOPE_TYPE",dataType="integer",paramType = "query"),
            @ApiImplicitParam(name = "allRole_eq",value = "是否所有角色都执行",dataType="integer",paramType = "query")
    })
    @GetMapping(value = "/listPage",produces="application/json")
    public ResponseData<Page<TbCoreDataScope>> listPage(@ApiIgnore @RequestParam Map<String,Object> map){
        JpowerAssert.notEmpty(Fc.toStr(map.get("menuId_eq")), JpowerError.Arg,"菜单ID不可为空");
        return ReturnJsonUtil.ok("查询成功",dataScopeService.page(PaginationContext.getMpPage(),Condition.getQueryWrapper(map,TbCoreDataScope.class)));
    }

    @ApiOperation("通过菜单ID查询列表")
    @GetMapping(value = "/listByMenuId",produces="application/json")
    public ResponseData<List<TbCoreDataScope>> listByMenuId(@ApiParam(value = "菜单ID",required = true) @RequestParam String menuId){
        JpowerAssert.notEmpty(menuId, JpowerError.Arg,"菜单ID不可为空");
        return ReturnJsonUtil.ok("查询成功",dataScopeService.list(Condition.<TbCoreDataScope>getQueryWrapper().lambda()
                .eq(TbCoreDataScope::getMenuId,menuId)));
    }

    @ApiOperation("通过角色ID查询所有数据权限ID")
    @GetMapping(value = "/listIdByRoleId",produces="application/json")
    public ResponseData<List<String>> listIdByRoleId(@ApiParam(value = "角色ID 多个逗号分割",required = true) @RequestParam String roleIds){
        JpowerAssert.notEmpty(roleIds, JpowerError.Arg,"角色ID不可为空");
        return ReturnJsonUtil.ok("查询成功",roleDataService.listObjs(Condition.<TbCoreRoleData>getQueryWrapper().lambda()
                .select(TbCoreRoleData::getDataId)
                .in(TbCoreRoleData::getRoleId,roleIds),Fc::toStr));
    }

    @ApiOperation("角色赋权")
    @PostMapping(value = "/roleDataScope",produces="application/json")
    public ResponseData roleDataScope(@ApiParam(value = "角色主键",required = true) @RequestParam String roleId,
                                      @ApiParam("数据权限主键,多个逗号分割") @RequestParam(required = false) String dataIds){
        JpowerAssert.notEmpty(roleId, JpowerError.Arg,"角色主键不可为空");
        JpowerAssert.notNull(roleService.getById(roleId), JpowerError.Parser,"该角色找不到");

        CacheUtil.clear(CacheNames.DATASCOPE_REDIS_CACHE,Boolean.FALSE);
        return ReturnJsonUtil.status(dataScopeService.roleDataScope(roleId,dataIds));
    }
}
