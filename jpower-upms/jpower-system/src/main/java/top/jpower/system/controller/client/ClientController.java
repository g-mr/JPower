package top.jpower.system.controller.client;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.entity.client.CoreClient;
import top.jpower.system.service.client.CoreClientService;
import top.jpower.system.vo.SelectVO;

import java.util.List;
import java.util.Map;

/**
 * 客户端管理
 * 
 * @author mr.g
 */
@Tag(name = "客户端管理")
@Slf4j
@Validated
@RestController
@RequestMapping("/core/client")
@RequiredArgsConstructor
public class ClientController extends BaseController {

    private final CoreClientService coreClientService;

    /**
     * 保存或者更新客户端信息
     * 
     * @author mr.g
     * @param coreClient 客户端信息
     * @return R 操作结果
     */
    @Function(value = "保存",menus = {
		@Menu(name = "编辑",client = "admin",menuCode = "SYSTEM_CLIENT",code = "SYSTEM_CLIENT_SAVE",type = Menu.TYPE.BTN),
		@Menu(name = "新增",client = "admin",menuCode = "SYSTEM_CLIENT",code = "SYSTEM_CLIENT_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "保存或者更新客户端信息")
    @PostMapping("save")
    public R<Long> save(@Validated @RequestBody CoreClient coreClient){
        return R.data(coreClientService.createOrUpdate(coreClient));
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_CLIENT",code = "SYSTEM_CLIENT_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除客户端")
    @DeleteMapping("delete")
    public R<Boolean> delete(@Parameter(description = "主键，多个逗号分割",required = true) @NotBlank(message = "客户端主键不可为空") @RequestParam String ids){
        CacheUtil.clear(CacheNames.CLIENT_KEY);
        return R.status(coreClientService.removeByIds(Fc.toLongList(ids)));
    }

    @Function(value = "列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_CLIENT",code = "CLIENT_LIST",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "分页查询客户端列表")
    @Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "name", description = "客户端名称", in = ParameterIn.QUERY),
		@Parameter(name = "clientCode", description = "客户端编码", in = ParameterIn.QUERY)
    })
    @GetMapping("list")
    public R<Pg<CoreClient>> list(@Ignore @RequestParam Map<String,Object> map){
        return R.data(coreClientService.page(map));
    }

    @Function(value = "客户端下拉",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "FUNCTION_CLIENT_SELECT",type = Menu.TYPE.INTERFACE),
		@Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_SELECT_URL",code = "ROLE_CLIENT_SELECT",type = Menu.TYPE.INTERFACE),
		@Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE",code = "DATASCOPE_CLIENT_SELECT",type = Menu.TYPE.INTERFACE),
		@Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_CLIENT_SELECT",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "下拉客户端列表")
    @GetMapping("selectList")
    public R<List<SelectVO>> selectList() {
        return R.data(coreClientService.select());
    }
}
