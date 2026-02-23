package top.jpower.user.controller;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.enums.YN01Enum;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.user.dbs.entity.CorePost;
import top.jpower.user.service.CorePostService;
import top.jpower.user.vo.PostSelectVO;
import top.jpower.user.vo.PostVO;

import java.util.List;
import java.util.Map;

/**
 * 岗位Controller
 *
 * @author mr.g
 * @date 2022-09-16 18:12
 */
@Tag(name = "岗位管理")
@RestController
@RequestMapping("/core/post")
@RequiredArgsConstructor
@Validated
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
    public R<Pg<PostVO>> list(@Ignore @RequestParam(required = false) Map<String,Object> map) {
        return R.data(postService.pageVo(map));
    }

    @Function(value = "岗位下拉",menus = {
        @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "POST_SELECT",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "下拉列表")
    @GetMapping(value = "/select", produces = "application/json")
    public R<List<PostSelectVO>> select(@Parameter(description = "岗位名称") @RequestParam(required = false) String name) {
        return R.data(postService.listSelect(name));
    }

    @Function(value = "新增岗位",menus = {
        @Menu(client = "admin",menuCode = "POST",code = "POST_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增")
    @PostMapping(value = "/add", produces = "application/json")
    public R<Long> add(@RequestBody @Validated(Validation.Create.class) CorePost corePost) {
        if (Fc.isNull(corePost.getSort())){
            corePost.setSort(0);
        }
        if (Fc.isNull(corePost.getStatus())){
            corePost.setStatus(YN01Enum.Y.getValue());
        }

        return R.data(postService.createPost(corePost));
    }

    @Function(value = "编辑岗位",menus = {
        @Menu(client = "admin",menuCode = "POST",code = "POST_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "编辑")
    @PutMapping(value = "/update", produces = "application/json")
    public R<Long> update(@NotNull(message = "岗位信息不能为空") @Validated(Validation.Update.class) @RequestBody CorePost corePost) {
        return R.data(postService.editById(corePost));
    }

    @Function(value = "删除岗位",menus = {
        @Menu(client = "admin",menuCode = "POST",code = "POST_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete", produces = "application/json")
    public R<Boolean> delete(@Parameter(description = "主键，多个逗号分割") @NotBlank(message = "主键不可为空") @RequestParam String ids) {
        return R.status(postService.deleteInIds(Fc.toLongList(ids)));
    }

    @Function(value = "岗位详情",menus = {
        @Menu(client = "admin",menuCode = "POST",code = "POST_DETAIL",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "详情")
    @GetMapping(value = "/get/{id}", produces = "application/json")
    public R<CorePost> get(@Parameter(description = "主键") @NotNull(message = "主键不可为空") @PathVariable("id") Long id) {
        return R.data(postService.getById(id));
    }

}
