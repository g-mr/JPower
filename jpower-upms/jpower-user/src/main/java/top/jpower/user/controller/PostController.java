package top.jpower.user.controller;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import com.mybatisflex.core.util.UpdateEntity;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.constants.CacheNames;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.dbs.entity.TbCorePost;
import top.jpower.user.dbs.entity.CorePost;
import top.jpower.user.service.CorePostService;
import top.jpower.user.vo.PostVO;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2022-09-16 18:12
 */
@Tag(name = "岗位管理")
@RestController
@RequestMapping("/core/post")
@RequiredArgsConstructor
public class PostController extends BaseController {

    private final CorePostService postService;

    @Function(value = "岗位列表",menus = {
        @Menu(client = "admin",menuCode = "POST",code = "POST_PAGE",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "分页")
    @Parameters({
        @Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
        @Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true),
        @Parameter(name = "name", description = "岗位名称", in = ParameterIn.QUERY),
        @Parameter(name = "code", description = "岗位编码", in = ParameterIn.QUERY),
        @Parameter(name = "type_eq", description = "岗位类型 字典：POST_TYPE", in = ParameterIn.QUERY),
        @Parameter(name = "status_eq", description = "是否启用 字典：YN01", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/list", produces = "application/json")
    public R<Pg<PostVO>> list(@Ignore @RequestParam Map<String,Object> map) {
        return R.data(postService.pageVo(map));
    }

    @Function(value = "岗位下拉",menus = {
        @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "POST_SELECT",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "下拉列表")
    @GetMapping(value = "/select", produces = "application/json")
    public R<List<Map<String,String>>> select(@ApiParam("岗位名称") String name,@ApiParam("所属租户") String tenantCode) {
        return R.data(postService.listMaps(Condition.<TbCorePost>getQueryWrapper()
                        .lambda()
                        .select(TbCorePost::getId,TbCorePost::getName,TbCorePost::getCode)
                        .eq(TbCorePost::getStatus, YN01Enum.Y.getValue())
                        .eq(ShieldUtil.isRoot()&&Fc.isNotBlank(tenantCode),TbCorePost::getTenantCode,tenantCode)
                        .like(Fc.isNotBlank(name),TbCorePost::getName,name)
                        .orderByAsc(TbCorePost::getSort)));
    }

    @Function(value = "新增岗位",menus = {
            @Menu(client = "admin",menuCode = "POST",code = "POST_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增")
    @PostMapping(value = "/add", produces = "application/json")
    public R add(@Validated TbCorePost corePost) {
        corePost.setId(null);
        if (Fc.isNull(corePost.getSort())){
            corePost.setSort(0);
        }
        if (Fc.isNull(corePost.getStatus())){
            corePost.setStatus(YN01Enum.Y.getValue());
        }

        JpowerAssert.geZero(postService.count(Condition.<TbCorePost>getQueryWrapper().lambda().eq(TbCorePost::getCode,corePost.getCode())),JpowerError.Arg,"编码已存在");

        return R.status(postService.save(corePost));
    }

    @Function(value = "编辑岗位",menus = {
            @Menu(client = "admin",menuCode = "POST",code = "POST_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "编辑")
    @PutMapping(value = "/update", produces = "application/json")
    public R update(CorePost corePost) {
        JpowerAssert.notNull(corePost.getId(), JpowerError.Arg,"主键不可为空");

        TbCorePost post = postService.getOne(Condition.<TbCorePost>getQueryWrapper().lambda().eq(TbCorePost::getCode,corePost.getCode()));
        JpowerAssert.notTrue(Fc.notNull(post)&&!Fc.equalsValue(post.getId(),corePost.getId()),JpowerError.Arg,"该编码已存在");

        CacheUtil.clear(CacheNames.POST_KEY);
        return R.status(postService.updateById(UpdateEntity.ofNotNull(corePost)
                .setDescribe(corePost.getDescribe())
                .setCondition(corePost.getCondition())));
    }

    @Function(value = "删除岗位",menus = {
            @Menu(client = "admin",menuCode = "POST",code = "POST_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete", produces = "application/json")
    public R delete(@ApiParam("主键，多个逗号分割") String ids) {
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");
        CacheUtil.clear(CacheNames.POST_KEY);
        return R.status(postService.delete(Fc.toLongList(ids)));
    }

    @Function(value = "岗位详情",menus = {
            @Menu(client = "admin",menuCode = "POST",code = "POST_DETAIL",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "详情")
    @DeleteMapping(value = "/get", produces = "application/json")
    public R<TbCorePost> get(@ApiParam("主键") Long id) {
        JpowerAssert.notNull(id, JpowerError.Arg,"主键不可为空");
        return R.data(postService.getById(id));
    }

}
