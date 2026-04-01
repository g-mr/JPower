package top.jpower.system.controller.dict;

import cn.hutool.core.lang.tree.Tree;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
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
import top.jpower.common.enums.YYZLEnum;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.system.dbs.entity.dict.CoreDict;
import top.jpower.system.dbs.entity.dict.CoreDictType;
import top.jpower.system.service.dict.CoreDictService;
import top.jpower.system.service.dict.CoreDictTypeService;
import top.jpower.system.vo.DictVO;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static top.jpower.common.constants.ServiceCodeConstants.MISS_REQUIRED_PARAMETER;
import static top.jpower.common.constants.ServiceCodeConstants.PARAMETER_ILLEGAL;
import static top.jpower.core.util.constants.JpowerConstants.I18N_KEY;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;

/**
 * 字典相关
 * 
 * @author mr.g
 */
@Tag(name = "字典管理")
@Validated
@RestController
@RequestMapping("/core/dict")
@RequiredArgsConstructor
public class DictController extends BaseController {

    private final CoreDictService coreDictService;
    private final CoreDictTypeService coreDictTypeService;


	@ApiOperationSupport(order = 102)
	@Operation(summary = "通过字典类型查询字典列表")
	@GetMapping("/dictSelect")
	public R<List<Tree<Long>>> dictSelect(@NotBlank(message = "字典类型编码不可为空") @RequestParam String dictTypeCode){
		return R.data(coreDictService.dictSelect(dictTypeCode));
	}




















    @Function(value = "字典类型树",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_TYPELIST",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "查询所有字典类型树形结构")
    @GetMapping(value = "/dictTypeTree", produces=APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> dictTypeTree(){
        return R.data(coreDictTypeService.tree());
    }

    @Function(value = "新增字典类型",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_TYPE_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增字典类型")
    @PostMapping(value = "/add", produces=APPLICATION_JSON_VALUE)
    public R<Long> add(@Validated(Validation.Create.class) @RequestBody CoreDictType dictType){
        return R.status(coreDictTypeService.addDictType(dictType), dictType.getId());
    }

    @Function(value = "修改字典类型",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_TYPE_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "更新字典类型")
    @PutMapping(value = "/update", produces=APPLICATION_JSON_VALUE)
    public R<Boolean> update(@Validated(Validation.Update.class) @RequestBody CoreDictType dictType){
        return R.status(coreDictTypeService.updateDictType(dictType));
    }

    @Function(value = "删除字典类型",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_TYPE_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除字典类型")
    @DeleteMapping(value = "/deleteDictType", produces=APPLICATION_JSON_VALUE)
    public R<Boolean> deleteDictType(@Parameter(description = "主键，多个逗号分割", required = true) @NotBlank(message = "主键不可为空") @RequestParam String ids){
        return R.status(coreDictTypeService.deleteDictType(Fc.toLongList(ids)));
    }

    @Function(value = "字典类型详情",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_TYPE_DETAIL",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "查询字典类型详情")
    @GetMapping(value = "/getDictType/{id}", produces=APPLICATION_JSON_VALUE)
    public R<CoreDictType> getDictType(@Parameter(description = "主键",required = true) @NotNull(message = "主键不可为空") @PathVariable("id") Long id){
        return R.data(coreDictTypeService.getById(id));
    }

    @Function(value = "字典列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_LIST",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "通过字典类型分页查询字典")
    @Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "dictTypeCode_eq", description = "字典类型编码", in = ParameterIn.QUERY, required = true),
		@Parameter(name = "code", description = "字典编码", in = ParameterIn.QUERY),
		@Parameter(name = "name", description = "字典名称", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/listByType", produces=APPLICATION_JSON_VALUE)
    public R<Pg<DictVO>> listByType(@Ignore @RequestParam(required = false) Map<String, Object> map) {
        JpowerAssert.notEmpty(MapUtil.getStr(map, "dictTypeCode_eq"), JpowerError.Arg,MISS_REQUIRED_PARAMETER);
		map.putIfAbsent("parentId_eq", Fc.toLong(TOP_CODE));

        return R.data(coreDictService.pageByType(map));
    }

    @Function(value = "字典子级",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_LIST_BY_PARENT",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "查询下级字典",description = "parentId不可传-1")
    @Parameters({
		@Parameter(name = "parentId_eq", description = "父级字典", in = ParameterIn.QUERY,required = true),
		@Parameter(name = "code", description = "字典编码", in = ParameterIn.QUERY),
		@Parameter(name = "name", description = "字典名称", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/listDictChildList",produces=APPLICATION_JSON_VALUE)
    public R<List<DictVO>> listDictChildList(@Ignore @RequestParam(required = false) Map<String, Object> map){
        JpowerAssert.notNull(MapUtil.getStr(map, "parentId_eq"), JpowerError.Arg, MISS_REQUIRED_PARAMETER);
        JpowerAssert.notTrue(Fc.equalsValue(MapUtil.getStr(map, "parentId_eq"), TOP_CODE), JpowerError.Arg,PARAMETER_ILLEGAL);

        return R.data(coreDictService.listByType(map));
    }

    @Function(value = "保存字典",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_SAVE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "保存或者新增字典", description = "不传ID就是新增，传ID就是修改")
    @PostMapping(value = "/saveDict", produces = APPLICATION_JSON_VALUE)
    public R<Long> saveDict(@Validated @RequestBody CoreDict dict){
        return R.data(coreDictService.saveDict(dict));
    }

    @Function(value = "停用字典",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_STOP",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "停用字典")
    @PostMapping(value = "/stopDict/{id}",produces=APPLICATION_JSON_VALUE)
    public R<Boolean> stopDict(@Parameter(description = "主键", required = true) @NotNull(message = "主键不可为空") @PathVariable("id") Long id){
        return R.status(coreDictService.stopDict(id));
    }

    @Function(value = "删除字典",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除字典")
    @DeleteMapping(value = "/deleteDict", produces=APPLICATION_JSON_VALUE)
    public R<Boolean> deleteDict(@Parameter(description = "主键，多个逗号分割",required = true) @NotBlank(message = "主键不可为空") @RequestParam String ids){
        return R.status(coreDictService.removeByIds(Fc.toLongList(ids)));
    }

    @Function(value = "字典详情",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DICT",code = "SYSTEM_DICT_DETAIL",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "查询字典详情")
    @GetMapping(value = "/getDict/{id}", produces=APPLICATION_JSON_VALUE)
    public R<CoreDict> getDict(@Parameter(description = "字典ID",required = true) @NotNull(message = "主键不可为空") @PathVariable("id") Long id){
        return R.data(coreDictService.getById(id));
    }

    @ApiOperationSupport(order = 101)
    @Operation(summary = "根据字典类型查询树形字典")
    @GetMapping(value = "/treeDict/{dictTypeCode}", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> treeDict(@Parameter(description = "字典类型编码") @NotBlank(message = "字典类型编码不可为空") @PathVariable("dictTypeCode") String dictTypeCode){
        return R.data(coreDictService.tree(dictTypeCode));
    }

	@ApiOperationSupport(order = 100)
	@Operation(summary = "通过字典类型查询字典列表")
	@GetMapping("/getDictListByType")
	public R<List<DictVO>> getDictListByType(@RequestParam Map<String, Object> map){
		JpowerAssert.notEmpty(MapUtil.getStr(map, "dictTypeCode_eq"), JpowerError.Arg, MISS_REQUIRED_PARAMETER);
		map.putIfAbsent("parentId_eq", Fc.toLong(TOP_CODE));
		//只查询未停用的
		map.put("isStop_eq", YN01Enum.N.getValue());
		//查询的语言
		map.put("locale_eq", Fc.toStr(getRequest().getHeader(I18N_KEY), YYZLEnum.CHINA.getValue()));

		return R.data(coreDictService.listByType(map));
	}
}
