package top.jpower.system.controller.org;

import cn.hutool.core.lang.tree.Tree;
import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.constants.CacheNames;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.entity.org.CoreOrg;
import top.jpower.system.service.org.CoreOrgService;
import top.jpower.system.vo.OrgVo;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static top.jpower.common.constants.ServiceCodeConstants.DELETE_ORG_EXIST_CHILD;

/**
 * 组织机构管理
 *
 * @author mr.g
 */
@Tag(name = "组织机构管理")
@Validated
@RestController
@RequestMapping("/core/org")
@RequiredArgsConstructor
public class OrgController extends BaseController {

    private final CoreOrgService coreOrgService;

    @Function(value = "下级部门",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORGCHILDER_LIST",type = Menu.TYPE.INTERFACE)
    })
	@Parameters({
		@Parameter(name = "parentId_eq", description = "父级ID", in = ParameterIn.QUERY, required = true),
		@Parameter(name = "code_eq", description = "编码", in = ParameterIn.QUERY),
		@Parameter(name = "type_eq", description = "类型 字典：ORG_TYPE", in = ParameterIn.QUERY),
		@Parameter(name = "name", description = "名称", in = ParameterIn.QUERY),
		@Parameter(name = "headName", description = "领导人名字", in = ParameterIn.QUERY),
		@Parameter(name = "headPhone", description = "领导人电话", in = ParameterIn.QUERY),
		@Parameter(name = "headEmail", description = "领导人邮箱", in = ParameterIn.QUERY),
		@Parameter(name = "contactName", description = "联系人名字", in = ParameterIn.QUERY),
		@Parameter(name = "contactPhone", description = "联系人电话", in = ParameterIn.QUERY),
		@Parameter(name = "contactEmail", description = "联系人邮箱", in = ParameterIn.QUERY)
	})
    @Operation(summary = "懒加载组织机构树形列表")
    @GetMapping(value = "/listLazyByParent",produces = APPLICATION_JSON_VALUE)
    public R<List<OrgVo>> listLazyByParent(@Ignore @RequestParam Map<String, Object> map) {
        return R.data(coreOrgService.listLazyByParent(map));
    }

    @Function(value = "树形列表",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORG_TREELIST",type = Menu.TYPE.INTERFACE)
    })
	@Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "code_eq", description = "编码", in = ParameterIn.QUERY),
		@Parameter(name = "type_eq", description = "类型 字典：ORG_TYPE", in = ParameterIn.QUERY),
		@Parameter(name = "name", description = "名称", in = ParameterIn.QUERY),
		@Parameter(name = "headName", description = "领导人名字", in = ParameterIn.QUERY),
		@Parameter(name = "headPhone", description = "领导人电话", in = ParameterIn.QUERY),
		@Parameter(name = "headEmail", description = "领导人邮箱", in = ParameterIn.QUERY),
		@Parameter(name = "contactName", description = "联系人名字", in = ParameterIn.QUERY),
		@Parameter(name = "contactPhone", description = "联系人电话", in = ParameterIn.QUERY),
		@Parameter(name = "contactEmail", description = "联系人邮箱", in = ParameterIn.QUERY)
	})
    @Operation(summary = "分页懒加载组织机构树形列表")
    @GetMapping(value = "/listLazy",produces = APPLICATION_JSON_VALUE)
    public R<Pg<OrgVo>> listLazy(@Ignore @RequestParam Map<String, Object> map) {
        return R.data(coreOrgService.pageTop(map));
    }

    @Function(value = "新增",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "ORG_CHILD_ADD",type = Menu.TYPE.BTN),
		@Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORG_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增一个组织机构")
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    public R<Long> add(@Validated(Validation.Create.class) @RequestBody CoreOrg coreOrg){
		return R.data(coreOrgService.create(coreOrg));
    }

    @Function(value = "删除",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORG_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除组织机构")
    @DeleteMapping(value = "/delete", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> deleteStatus(@Parameter(description = "主键 多个逗号分割",required = true) @NotBlank(message = "ids不可为空") @RequestParam String ids){
        JpowerAssert.geZero(coreOrgService.countByParentids(Fc.toLongList(ids)), JpowerError.Business, DELETE_ORG_EXIST_CHILD);
        if (coreOrgService.removeByIds(Fc.toLongList(ids))){
            CacheUtil.clear(CacheNames.ORG_KEY);
            return R.data(true);
        }else {
            return R.fail();
        }
    }

    @Function(value = "编辑",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORG_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "修改组织机构信息")
    @PutMapping(value = "/update", produces=APPLICATION_JSON_VALUE)
    public R<Boolean> update(@Validated(Validation.Update.class) @RequestBody CoreOrg coreOrg){
        JpowerAssert.notNull(coreOrg.getId(), JpowerError.Arg,"id不可为空");
		return R.data(coreOrgService.update(coreOrg));
    }

    @Function(value = "树形部门",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ORG",code = "SYSTEM_ORG_TREE",type = Menu.TYPE.INTERFACE),
		@Menu(client = "admin",menuCode = "SYSTEM_USER",code = "SYSTEM_USER_ORG",type = Menu.TYPE.INTERFACE),
		@Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_DATASCOPE_LIST",code = "SYSTEM_ROLE_ORG",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "加载组织机构树形菜单")
    @Parameters({
		@Parameter(name = "code",description = "编码",in = ParameterIn.QUERY),
		@Parameter(name = "name",description = "名称", in = ParameterIn.QUERY),
		@Parameter(name = "headName",description = "领导人名字", in = ParameterIn.QUERY),
		@Parameter(name = "headPhone",description = "领导人电话", in = ParameterIn.QUERY),
		@Parameter(name = "headEmail",description = "领导人邮箱", in = ParameterIn.QUERY),
		@Parameter(name = "contactName",description = "联系人名字", in = ParameterIn.QUERY),
		@Parameter(name = "contactPhone",description = "联系人电话", in = ParameterIn.QUERY),
		@Parameter(name = "contactEmail",description = "联系人邮箱", in = ParameterIn.QUERY),
		@Parameter(name = "address",description = "地址", in = ParameterIn.QUERY),
		@Parameter(name = "type_eq",description = "机构类型 字典ORG_TYPE", in = ParameterIn.QUERY),
    })
    @GetMapping(value = "/tree", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> tree(@Ignore @RequestParam(required = false) Map<String,Object> map){
        return R.data(coreOrgService.tree(map));
    }

    @Operation(summary = "懒加载组织机构树形菜单")
    @Parameters({
		@Parameter(name = "code", description = "编码", in = ParameterIn.QUERY),
		@Parameter(name = "name", description = "名称", in = ParameterIn.QUERY),
		@Parameter(name = "headName", description = "领导人名字", in = ParameterIn.QUERY),
		@Parameter(name = "headPhone", description = "领导人电话", in = ParameterIn.QUERY),
		@Parameter(name = "headEmail", description = "领导人邮箱", in = ParameterIn.QUERY),
		@Parameter(name = "contactName", description = "联系人名字", in = ParameterIn.QUERY),
		@Parameter(name = "contactPhone", description = "联系人电话", in = ParameterIn.QUERY),
		@Parameter(name = "contactEmail", description = "联系人邮箱", in = ParameterIn.QUERY),
		@Parameter(name = "address", description = "地址", in = ParameterIn.QUERY),
		@Parameter(name = "type_eq", description = "机构类型 字典ORG_TYPE", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/lazyTree/{parentId}", produces=APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> lazyTree(@Ignore @RequestParam Map<String,Object> map,
										@Parameter(description = "父级编码", example = JpowerConstants.TOP_CODE, required = true) @PathVariable("parentId") Long parentId){
        return R.data(coreOrgService.tree(parentId, map));
    }

}
