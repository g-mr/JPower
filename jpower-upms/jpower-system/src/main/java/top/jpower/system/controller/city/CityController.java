package top.jpower.system.controller.city;

import cn.hutool.core.lang.tree.Tree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.entity.city.CoreCity;
import top.jpower.system.service.city.CoreCityService;
import top.jpower.system.vo.CityVO;
import top.jpower.system.vo.SelectVO;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 行政区域管理
 *
 * @author mr.g
 **/
@Tag(name = "行政区域管理")
@Validated
@RestController
@RequestMapping("/core/city")
@RequiredArgsConstructor
public class CityController extends BaseController {

    private final CoreCityService coreCityService;

    @Operation(summary = "查询下级列表")
    @GetMapping(value = "/listChild", produces = APPLICATION_JSON_VALUE)
    public R<List<SelectVO>> listChild(@Parameter(description = "父级code",required = true) @NotBlank(message = "父级CODE不可为空") @RequestParam(defaultValue = JpowerConstants.TOP_CODE) String pcode,
									   @Parameter(description = "名称") @RequestParam(required = false) String name){
        return R.data(coreCityService.listChild(pcode, name));
    }

    @Operation(summary = "新增行政区域")
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    public R<Long> add(@Validated(Validation.Create.class) @RequestBody CoreCity coreCity){
		coreCityService.add(coreCity);
		return R.data(coreCity.getId());
    }

    @Operation(summary = "修改行政区域")
    @PutMapping(value = "/update",  produces=APPLICATION_JSON_VALUE)
    public R<Long> update(@Validated(Validation.Update.class) @RequestBody CoreCity coreCity){
		coreCityService.update(coreCity);
        return R.data(coreCity.getId());
    }

    @Function(value = "保存",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_CITY",code = "SYSTEM_CITY_SAVE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "保存行政区域", description = "主键传是修改，不传是新增")
    @PostMapping(value = "/save", produces = APPLICATION_JSON_VALUE)
    public R<Long> save(@Validated(Validation.Update.class) @RequestBody CoreCity coreCity){
        return Fc.notNull(coreCity.getId()) ? update(coreCity) : add(coreCity);
    }

    @Function(value = "删除",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_CITY",code = "SYSTEM_CITY_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除行政区域")
    @DeleteMapping(value = "/delete", produces=APPLICATION_JSON_VALUE)
    public R<Boolean> delete(@Parameter(description = "主键，多个逗号分割",required = true) @NotBlank(message = "主键不可为空") @RequestParam String ids){
        return R.status(coreCityService.deleteBatch(Fc.toLongList(ids)));
    }

    @Function(value = "详情",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_CITY",code = "SYSTEM_CITY_DETAIL",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "查询行政区域详情")
    @GetMapping(value = "/get", produces=APPLICATION_JSON_VALUE)
    public R<CityVO> get(@Parameter(description = "主键",required = true) @NotNull(message = "主键不可为空") @RequestParam Long id){
        return R.data(coreCityService.getById(id));
    }

    @Function(value = "列表",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_CITY",code = "CITY_LIST",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "懒加载树形菜单")
    @GetMapping(value = "/lazyTree", produces=APPLICATION_JSON_VALUE)
    public R<List<Tree<String>>> lazyTree(@Parameter(description = "父级编码",required = true) @NotBlank(message = "上级CODE不可为空") @RequestParam String pcode){
        return R.data(coreCityService.lazyTree(pcode));
    }
}
