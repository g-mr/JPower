package com.wlcb.jpower.core.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.core.login.utils.RSAUtil;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.service.core.user.CoreUserService;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginController
 * @Description TODO 登录相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@EnableAutoConfiguration
@RestController
public class LoginController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private CoreUserService coreUserService;
    @Resource
    private RedisUtils redisUtils;

    /**
     * @author 郭丁志
     * @Description //TODO 用户登陆
     * @date 0:35 2020/6/10 0010
     * @param loginId
     * @param passWord
     * @param md5Key
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces="application/json")
    public ResponseData login(String loginId,String passWord,@RequestParam(name = "key1")String md5Key) throws Exception {

        //解密
        byte[] enUserName = RSAUtil.hexStringToBytes(loginId);
        byte[] enPassWord = RSAUtil.hexStringToBytes(passWord);

        if (enUserName == null || enPassWord == null){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_NULL,"用户名或者密码不可为空",false);
        }

        byte[] deUserName = RSAUtil.decrypt(RSAUtil.getPrivateKey(), enUserName);
        byte[] dePassWord = RSAUtil.decrypt(RSAUtil.getPrivateKey(), enPassWord);

        String password = new StringBuilder(new String(dePassWord, "UTF-8")).reverse().toString();
        String username = URLDecoder.decode(new StringBuilder(new String(deUserName)).reverse().toString(), "UTF-8");

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_NULL,"用户名或者密码不可为空",false);
        }
        //对MD5进行加密
        password = MD5.parseStrToMd5U32(password);

        //防篡改校验
        String keyBeforeMd5 = username + password + "wlcbPortal";
        String keyAfterMd5 = MD5.parseStrToMd5U32(keyBeforeMd5);
        if(StringUtils.isBlank(md5Key) || !keyAfterMd5.equals(md5Key)) {
            logger.info("检测到信息被篡改，不可登录～～～用户名={}",username);
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_NOTFOUND,"用户名不存在",false);
        }

//        TblUser user = userService.selectByUserName(username);
        TbCoreUser user = coreUserService.selectUserLoginId(username);

        if (user == null){
            //用户名空则返回
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_NOTFOUND,"用户名不存在",false);
        }

        if (!password.equals(user.getPassword().toUpperCase())) {
            //密码错误直接返回
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"密码错误",false);
        }

        //用户未激活
        if (user.getActivationStatus() != 1){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该用户未激活，请先激活",false);
        }

        user.setLastLoginTime(new Date());
        user.setLoginCount(user.getLoginCount()+1);
        coreUserService.updateLoginInfo(user);

        user.setPassword(null);
        user.setIsSysUser(0);
//        return userService.login(user);
        return coreUserService.createToken(user);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 发送登录验证码
     * @Date 17:10 2020-04-30
     * @Param [phone]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/loginVercode",method = RequestMethod.GET,produces="application/json")
    public ResponseData loginVercode(String phone) {

        if (StringUtils.isBlank(phone) || !StrUtil.isPhone(phone)){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"手机号不合法",false);
        }

        if (redisUtils.getExpire(phone+"-login",TimeUnit.MINUTES) >= 4){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该验证码已经发送，请一分钟后重试",false);
        }

        TbCoreUser user = coreUserService.selectByPhone(phone);

        if (user == null){
            //用户名空则返回
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_NOTFOUND,"手机号不存在",false);
        }


        String code = RandomStringUtils.randomNumeric(6);

//        String content = "【乌丽吉】您的登录验证码:" + code + "，如非本人操作，请忽略本短信！";

        JSONObject json = SmsAliyun.send(phone,"乌丽吉","code");

        if (json.getInteger("status") == 1){
            redisUtils.set(phone+"-login",code,5L, TimeUnit.MINUTES);
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,user.getLoginId()+"的验证码发送成功",true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"验证码发送失败",false);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 手机号验证码登录
     * @Date 17:10 2020-04-30
     * @Param [request]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/phoneLogin",method = RequestMethod.POST,produces="application/json")
    public ResponseData phoneLogin(HttpServletRequest request) throws Exception {

        String md5Key = request.getParameter("key1");

        //解密
        String phone = request.getParameter("phone");
        String vercode = request.getParameter("vercode");

        if (StringUtils.isBlank(phone) || StringUtils.isBlank(vercode)){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_NULL,"手机号或者验证码不可为空",false);
        }

        //防篡改校验
        String keyBeforeMd5 = phone + vercode + "wlcbPortal";
        String keyAfterMd5 = MD5.parseStrToMd5U32(keyBeforeMd5);
        if(null == md5Key || "".equals(md5Key) || !keyAfterMd5.equals(md5Key)) {
            logger.info("检测到信息被篡改，不可登录～～～手机号={}",phone);
            return ReturnJsonUtil.printJson(400,"用户不存在",false);
        }

        TbCoreUser user = coreUserService.selectByPhone(phone);

        if (user == null){
            //用户名空则返回
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_NOTFOUND,"用户不存在",false);
        }

        if (!vercode.equals(redisUtils.get(phone+"-login"))) {
            //密码错误直接返回
            return ReturnJsonUtil.printJson(300,"验证码错误或过期",false);
        }

        user.setPassword(null);
        return coreUserService.createToken(user);

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 修改密码
     * @Date 10:40 2020-07-03
     * @Param [request, response]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST,produces="application/json")
    public ResponseData updatePassword(HttpServletRequest request) throws Exception {

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

        TbCoreUser user = coreUserService.selectByUserNameAndId(id,username);

        if (user == null){
            //用户名空则返回
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_NOTFOUND,"用户不存在",false);
        }

        if (!oldPassWord.equals(user.getPassword().toUpperCase())) {
            //密码错误直接返回
            return ReturnJsonUtil.printJson(300,"旧密码错误",false);
        }

        Boolean is = coreUserService.updateUserPassword(user.getId(),newPassWord);
        if (is){
            return ReturnJsonUtil.printJson(200,"修改成功",true);
        }else {
            return ReturnJsonUtil.printJson(400,"修改失败",false);
        }

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
