package com.wlcb.jpower.web.controller.core.params;

import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.core.params.CoreParamService;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.dbs.entity.core.params.TbCoreParam;
import com.wlcb.jpower.module.mp.support.Condition;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@AllArgsConstructor
public class ParamsController extends BaseController {

    private RedisUtils redisUtils;
    private CoreParamService paramService;

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
        return ReturnJsonUtil.ok("获取成功", new PageInfo<>(list));
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
        JpowerAssert.notEmpty(id, JpowerError.Arg,"id不可为空");
        return ReturnJsonUtil.status(paramService.delete(id));
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
        JpowerAssert.notEmpty(coreParam.getId(), JpowerError.Arg,"id不可为空");
        return ReturnJsonUtil.status(paramService.update(coreParam));
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
        JpowerAssert.notEmpty(coreParam.getCode(), JpowerError.Arg,"编号值不可为空");
        JpowerAssert.notEmpty(coreParam.getName(), JpowerError.Arg,"参数名称不可为空");
        JpowerAssert.notEmpty(coreParam.getValue(), JpowerError.Arg,"参数值不可为空");

        JpowerAssert.isEmpty(paramService.selectByCode(coreParam.getCode()), JpowerError.BUSINESS,"该系统参数已存在");

        return ReturnJsonUtil.status(paramService.save(coreParam));
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
        JpowerAssert.notEmpty(code, JpowerError.Arg,"编号值不可为空");

        TbCoreParam param = paramService.getOne(Condition.<TbCoreParam>getQueryWrapper().lambda().eq(TbCoreParam::getCode,code));
        JpowerAssert.notTrue(Fc.equals(param.getIsEffect(), ConstantsEnum.YN01.N.getValue()), JpowerError.BUSINESS,"该参数无法立即生效，请重启项目");

        if (Fc.isNotEmpty(param) && Fc.isNotBlank(param.getValue())){
            redisUtils.set(CacheNames.PARAMS_REDIS_KEY+code,param.getValue());
        }else {
            if (Fc.isNull(param)){
                return ReturnJsonUtil.fail("该参数不存在");
            }
        }
        return ReturnJsonUtil.ok("操作成功");
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 全部生效
     * @Date 17:29 2020-05-07
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/effectAll",method = RequestMethod.GET,produces="application/json")
    public ResponseData effectAll(){
        paramService.effectAll();
        return ReturnJsonUtil.ok("操作完成");
    }

}
