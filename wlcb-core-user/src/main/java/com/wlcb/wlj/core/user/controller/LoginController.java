package com.wlcb.wlj.core.user.controller;

import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.common.service.user.UserService;
import com.wlcb.wlj.module.common.utils.ReturnJsonUtil;
import com.wlcb.wlj.module.dbs.entity.user.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName LoginController
 * @Description TODO 登录相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST,produces="application/json")
    public ResponseData login(String username,String password, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return ReturnJsonUtil.printJson(406,"用户名或者密码不可为空",false);
        }

        User user = userService.selectByUserName(username);

        if (user == null){
            //用户名空则返回
            return ReturnJsonUtil.printJson(400,"用户名不存在",false);
        }

        if (!password.equals(user.getPassword().toUpperCase())) {
            //密码错误直接返回
            return ReturnJsonUtil.printJson(300,"密码错误",false);
        }

        user.setPassword(null);
        return userService.login(user);
    }

    @RequestMapping(value = "/wxlogin",method = RequestMethod.POST,produces="application/json")
    public ResponseData wxLogin(String code, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(code)){
            return ReturnJsonUtil.printJson(406,"code不可为空",false);
        }

        return userService.wxLogin(code);
    }
}
