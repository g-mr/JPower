package com.wlcb.jpower.core.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.core.login.utils.RSAUtil;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.service.user.UserService;
import com.wlcb.jpower.module.common.utils.JWTUtils;
import com.wlcb.jpower.module.common.utils.MD5;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.dbs.entity.user.TblUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * @ClassName LoginController
 * @Description TODO 登录相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@EnableAutoConfiguration
@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST,produces="application/json")
    public ResponseData login(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String md5Key = request.getParameter("key1");

        //解密
        byte[] enUserName = RSAUtil.hexStringToBytes(request.getParameter("userName"));
        byte[] enPassWord = RSAUtil.hexStringToBytes(request.getParameter("passWord"));

        if (enUserName == null || enPassWord == null){
            return ReturnJsonUtil.printJson(406,"用户名或者密码不可为空",false);
        }

        byte[] deUserName = RSAUtil.decrypt(RSAUtil.getPrivateKey(), enUserName);
        byte[] dePassWord = RSAUtil.decrypt(RSAUtil.getPrivateKey(), enPassWord);

        String username = URLDecoder.decode(new StringBuilder(new String(deUserName)).reverse().toString(), "UTF-8");
        String password = new StringBuilder(new String(dePassWord, "UTF-8")).reverse().toString();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return ReturnJsonUtil.printJson(406,"用户名或者密码不可为空",false);
        }

        //防篡改校验
        String keyBeforeMd5 = username + password + "wlcbPortal";
        String keyAfterMd5 = MD5.parseStrToMd5U32(keyBeforeMd5);
        if(null == md5Key || "".equals(md5Key) || !keyAfterMd5.equals(md5Key)) {
            logger.info("检测到信息被篡改，不可登录～～～用户名={}",username);
            return ReturnJsonUtil.printJson(400,"用户名不存在",false);
        }

        TblUser user = userService.selectByUserName(username);

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


    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST,produces="application/json")
    public ResponseData updatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String md5Key = request.getParameter("key1");

        //解密
        String id = request.getParameter("id");
        byte[] enUserName = RSAUtil.hexStringToBytes(request.getParameter("userName"));
        byte[] enOldPassWord = RSAUtil.hexStringToBytes(request.getParameter("oldPassWord"));
        byte[] enNewPassWord = RSAUtil.hexStringToBytes(request.getParameter("newPassWord"));

        if (StringUtils.isBlank(id) || enUserName == null|| enOldPassWord == null || enNewPassWord == null){
            return ReturnJsonUtil.printJson(406,"用户名信息不可为空",false);
        }

        byte[] deUserName = RSAUtil.decrypt(RSAUtil.getPrivateKey(), enUserName);
        byte[] deOldPassWord = RSAUtil.decrypt(RSAUtil.getPrivateKey(), enOldPassWord);
        byte[] deNewPassWord = RSAUtil.decrypt(RSAUtil.getPrivateKey(), enNewPassWord);

        String username = URLDecoder.decode(new StringBuilder(new String(deUserName)).reverse().toString(), "UTF-8");
        String oldPassWord = new StringBuilder(new String(deOldPassWord, "UTF-8")).reverse().toString();
        String newPassWord = new StringBuilder(new String(deNewPassWord, "UTF-8")).reverse().toString();

        if (StringUtils.isBlank(username)|| StringUtils.isBlank(oldPassWord)|| StringUtils.isBlank(newPassWord)){
            return ReturnJsonUtil.printJson(406,"用户名信息不可为空",false);
        }

        //防篡改校验
        String keyBeforeMd5 = id + username + oldPassWord+ newPassWord + "wlcbPortal";
        String keyAfterMd5 = MD5.parseStrToMd5U32(keyBeforeMd5);
        if(null == md5Key || "".equals(md5Key) || !keyAfterMd5.equals(md5Key)) {
            logger.info("检测到信息被篡改，不可修改密码～～～用户名={}",username);
            return ReturnJsonUtil.printJson(400,"用户不存在",false);
        }

        TblUser user = userService.selectByUserNameAndId(id,username);

        if (user == null){
            //用户名空则返回
            return ReturnJsonUtil.printJson(400,"用户不存在",false);
        }

        if (!oldPassWord.equals(user.getPassword().toUpperCase())) {
            //密码错误直接返回
            return ReturnJsonUtil.printJson(300,"旧密码错误",false);
        }

        user.setPassword(newPassWord);
        Integer count = userService.updatePassword(user);
        if (count > 0){
            return ReturnJsonUtil.printJson(200,"修改成功",true);
        }else {
            return ReturnJsonUtil.printJson(400,"修改失败",false);
        }

    }

    @RequestMapping(value = "/wxlogin",method = RequestMethod.POST,produces="application/json")
    public ResponseData wxLogin(String code, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(code)){
            return ReturnJsonUtil.printJson(406,"code不可为空",false);
        }

        return userService.wxLogin(code);
    }

    @RequestMapping(value = "/parseJwt",method = RequestMethod.POST,produces="application/json")
    public ResponseData parseJwt(String token, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(token)){
            return ReturnJsonUtil.printJson(406,"token不可为空",false);
        }

        JSONObject jsonObject = JWTUtils.parsingJwt(token);

        return ReturnJsonUtil.printJson(jsonObject.getInteger("code"),jsonObject.getString("msg"),jsonObject.getString("token"),jsonObject.getBoolean("status"));
    }

}
