package com.wlcb.jpower.core.params.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.params.ParamService;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.dbs.entity.core.params.TbCoreParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ParamsController
 * @Description TODO 系统参数相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/core/param")
public class ParamsController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ParamsController.class);

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private ParamService paramService;

    /**
     * @Author 郭丁志
     * @Description //TODO 系统参数列表
     * @Date 16:57 2020-05-07
     * @Param [coreParam]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/list",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData list(TbCoreParam coreParam){

        PaginationContext.startPage();
        List<TbCoreParam> list = paramService.list(coreParam);

        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"获取成功", JSON.toJSON(new PageInfo<>(list)),true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 删除系统参数
     * @Date 17:13 2020-05-07
     * @Param [id]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE,produces="application/json")
    public ResponseData delete(String id){

        if (StringUtils.isBlank(id)){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_NULL,"id不可为空",false);
        }

        Integer count = paramService.delete(id);

        if (count > 0){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"删除成功",true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"删除失败",false);
        }

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 更新系统参数
     * @Date 17:14 2020-05-07
     * @Param [id]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/update",method = RequestMethod.PUT,produces="application/json")
    public ResponseData update(TbCoreParam coreParam){

        ResponseData responseData = BeanUtil.allFieldIsNULL(coreParam,"id",
                "updateUser");

        if (responseData.getCode() == ConstantsReturn.RECODE_NULL){
            return responseData;
        }

        Integer count = paramService.update(coreParam);

        if (count > 0){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"更新成功",true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"更新失败",false);
        }

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 新增系统参数
     * @Date 17:23 2020-05-07
     * @Param [coreParam]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces="application/json")
    public ResponseData add(TbCoreParam coreParam){

        ResponseData responseData = BeanUtil.allFieldIsNULL(coreParam,
                "createUser","code","name","value");

        if (responseData.getCode() == ConstantsReturn.RECODE_NULL){
            return responseData;
        }

        String value = paramService.selectByCode(coreParam.getCode());
        if (StringUtils.isNotBlank(value)){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该系统参数已存在",false);
        }

        Integer count = paramService.add(coreParam);

        if (count > 0){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"新增成功",true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"新增失败",false);
        }

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 立即生效一个参数
     * @Date 16:57 2020-05-07
     * @Param [code]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/takeEffect",method = RequestMethod.GET,produces="application/json")
    public ResponseData takeEffect(String code){

        if (StringUtils.isBlank(code)){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_NULL,"code不可为空",false);
        }

        String value = paramService.selectByCode(code);
        if (StringUtils.isNotBlank(value)){
            redisUtils.set(ConstantsUtils.PROPERTIES_PREFIX+code,value);
        }

        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"操作成功",true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 全部生效
     * @Date 17:29 2020-05-07
     * @Param []
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/effectAll",method = RequestMethod.GET,produces="application/json")
    public ResponseData effectAll(){

        List<TbCoreParam> params = paramService.list(new TbCoreParam());

        for (TbCoreParam param : params) {
            if (StringUtils.isNotBlank(param.getValue())){
                redisUtils.set(ConstantsUtils.PROPERTIES_PREFIX+param.getCode(),param.getValue());
            }
        }

        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"操作完成",true);
    }

}
