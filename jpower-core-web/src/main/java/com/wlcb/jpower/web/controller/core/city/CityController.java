package com.wlcb.jpower.web.controller.core.city;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.core.city.CoreCityService;
import com.wlcb.jpower.module.common.service.core.user.CoreFunctionService;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity;
import com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleController
 * @Description TODO 菜单相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 *
 */
@RestController
@RequestMapping("/core/city")
public class CityController extends BaseController {

    @Resource
    private CoreCityService coreCityService;

    /**
     * @Author 郭丁志
     * @Description //TODO 根据父节点查询子节点菜单
     * @Date 15:13 2020-05-20
     * @Param [code 默认为-1]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/listChild",method = {RequestMethod.GET},produces="application/json")
    public ResponseData listChild(@RequestParam(defaultValue = JpowerConstants.TOP_CODE) String pcode, String name){

        List<Map<String,Object>> list = coreCityService.listChild(ChainMap.init().set("pcode_eq",pcode).set("name",name));
        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"获取成功", JSON.toJSON(list),true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 查询行政区域列表
     * @Date 11:15 2020-07-23
     * @Param [coreCity]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/listPage",method = {RequestMethod.GET},produces="application/json")
    public ResponseData listPage( TbCoreCity coreCity){
        coreCity.setPcode(Fc.isNotBlank(coreCity.getPcode())?coreCity.getPcode():JpowerConstants.TOP_CODE);

        PaginationContext.startPage();
        List<TbCoreCity> list = coreCityService.list(coreCity);
        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"获取成功", JSON.toJSON(new PageInfo<>(list)),true);
    }

}
