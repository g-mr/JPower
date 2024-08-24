package top.jpower.jpower.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.dbs.entity.TbCorePost;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.utils.CacheUtil;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.CorePostService;
import top.jpower.jpower.vo.PostVo;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2022-09-16 18:12
 */
@Api(tags = "岗位管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/core/post")
public class PostController extends BaseController {

    private final CorePostService postService;

    @Function(value = "岗位列表",menus = {
            @Menu(client = "admin",menuCode = "POST",code = "POST_PAGE",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation(value = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", defaultValue = "1", paramType = "query", dataTypeClass = Integer.class, required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页长度", defaultValue = "10", paramType = "query", dataTypeClass = Integer.class, required = true),
            @ApiImplicitParam(name = "name", value = "岗位名称", paramType = "query", required = false),
            @ApiImplicitParam(name = "code", value = "岗位编码", paramType = "query", required = false),
            @ApiImplicitParam(name = "type_eq", value = "岗位类型 字典：POST_TYPE", paramType = "query", required = false),
            @ApiImplicitParam(name = "status_eq", value = "是否启用 字典：YN01", paramType = "query", required = false)
    })
    @GetMapping(value = "/list", produces = "application/json")
    public ResponseData<Pg<PostVo>> list(@ApiIgnore @RequestParam Map<String,Object> map) {
        return ReturnJsonUtil.data(postService.pageVo(map));
    }

    @Function(value = "岗位下拉",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "POST_SELECT",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation(value = "下拉列表")
    @GetMapping(value = "/select", produces = "application/json")
    public ResponseData<List<Map<String,String>>> select(@ApiParam("岗位名称") String name,@ApiParam("所属租户") String tenantCode) {
        return ReturnJsonUtil.data(postService.listMaps(Condition.<TbCorePost>getQueryWrapper()
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
    @ApiOperation(value = "新增")
    @PostMapping(value = "/add", produces = "application/json")
    public ResponseData add(@Validated TbCorePost corePost) {
        corePost.setId(null);
        if (Fc.isNull(corePost.getSort())){
            corePost.setSort(0);
        }
        if (Fc.isNull(corePost.getStatus())){
            corePost.setStatus(YN01Enum.Y.getValue());
        }

        JpowerAssert.geZero(postService.count(Condition.<TbCorePost>getQueryWrapper().lambda().eq(TbCorePost::getCode,corePost.getCode())),JpowerError.Arg,"编码已存在");

        return ReturnJsonUtil.status(postService.save(corePost));
    }

    @Function(value = "编辑岗位",menus = {
            @Menu(client = "admin",menuCode = "POST",code = "POST_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "编辑")
    @PutMapping(value = "/update", produces = "application/json")
    public ResponseData update(TbCorePost corePost) {
        JpowerAssert.notNull(corePost.getId(), JpowerError.Arg,"主键不可为空");

        TbCorePost post = postService.getOne(Condition.<TbCorePost>getQueryWrapper().lambda().eq(TbCorePost::getCode,corePost.getCode()));
        JpowerAssert.notTrue(Fc.notNull(post)&&!Fc.equalsValue(post.getId(),corePost.getId()),JpowerError.Arg,"该编码已存在");

        CacheUtil.clear(CacheNames.POST_KEY);
        return ReturnJsonUtil.status(postService.updateById(corePost));
    }

    @Function(value = "删除岗位",menus = {
            @Menu(client = "admin",menuCode = "POST",code = "POST_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete", produces = "application/json")
    public ResponseData delete(@ApiParam("主键，多个逗号分割") String ids) {
        JpowerAssert.notEmpty(ids, JpowerError.Arg,"主键不可为空");
        CacheUtil.clear(CacheNames.POST_KEY);
        return ReturnJsonUtil.status(postService.delete(Fc.toLongList(ids)));
    }

    @Function(value = "岗位详情",menus = {
            @Menu(client = "admin",menuCode = "POST",code = "POST_DETAIL",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "详情")
    @DeleteMapping(value = "/get", produces = "application/json")
    public ResponseData<TbCorePost> get(@ApiParam("主键") Long id) {
        JpowerAssert.notNull(id, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.data(postService.getById(id));
    }

}
