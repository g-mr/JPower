package com.wlcb.web.tiwen.controller;

import com.wlcb.module.base.vo.ResponseData;
import com.wlcb.module.common.service.TiwenService;
import com.wlcb.module.common.utils.ReturnJsonUtil;
import com.wlcb.module.dbs.entity.base.PageBean;
import com.wlcb.module.dbs.entity.tiwen.User;
import com.wlcb.module.dbs.entity.tiwen.Wend;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName TiwenController
 * @Description TODO 体温调用入口Controller
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("tiwen")
public class TiwenController {

    private static final Logger logger = LoggerFactory.getLogger(TiwenController.class);

    @Resource
    private TiwenService tiwenService;

    @RequestMapping(value = "/list",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData list(User user, Wend wend, @Param("startTime") String startTime,@Param("endTime") String endTime, HttpServletRequest request, HttpServletResponse response){

        PageBean<Map<String, Object>> pageBean = tiwenService.listPage(user,wend,startTime,endTime);

        return ReturnJsonUtil.printJson(0,"查询成功",pageBean,true);
    }

    @RequestMapping(value = "/add",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData add(User user, Wend wend, HttpServletRequest request, HttpServletResponse response){

        Integer count = tiwenService.add(user,wend);

        if (count > 0){
            return ReturnJsonUtil.printJson(0,"新增成功",true);
        }else {
            return ReturnJsonUtil.printJson(-1,"新增失败",false);
        }
    }


    @RequestMapping(value = "/getByNew",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData getByNew(String openid, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(openid)){
            return ReturnJsonUtil.printJson(-1,"参数不合法",true);
        }

        Map<String,Object> map = tiwenService.getByNew(openid);

        if (map != null){
            return ReturnJsonUtil.printJson(0,"查询成功",map,true);
        }else{
            return ReturnJsonUtil.printJson(0,"查无此人体温",true);
        }
    }

}
