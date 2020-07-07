package com.wlcb.jpower.module.common.service.user.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.service.user.UserService;
import com.wlcb.jpower.module.common.utils.HttpClient;
import com.wlcb.jpower.module.common.utils.JWTUtils;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import com.wlcb.jpower.module.dbs.dao.user.mapper.UserMapper;
import com.wlcb.jpower.module.dbs.entity.user.TblUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    private String appid = "appid";
    private final String appsecret = "appsecret";
    private final String openid = "get_openid";

    private final String tokenExpired = "tokenExpired";
    private final Long tokenExpiredDefVal = 2400000L;

    /**
     * @Author 郭丁志
     * @Description //TODO 登录验证
     * @Date 23:11 2020-03-05
     * @Param [username, password]
     * @return ResponseData
     **/
    @Override
    public ResponseData login(TblUser user) {
        try {
            List<String> list = null;

            if (user.getRole() == 1) {
                list = userMapper.selectAllRole();
            } else {
                list = userMapper.selectMenuByRole(user.getRole());
            }

            JSONObject json = JSON.parseObject(JSON.toJSONString(user));
            json.put("menu",list);

            Map<String, Object> payload = new HashMap<String, Object>();
            payload.put("userId", user.getId());
            String token = JWTUtils.createJWT(JSON.toJSONString(user),payload,ParamConfig.getLong(tokenExpired,tokenExpiredDefVal));

            if (StringUtils.isBlank(token)){
                logger.error("token生成错误，token={}",token);
            }

            json.put("token",token);

            logger.info("后台用户登录成功，用户名={},id={},token={}",user.getUser(),user.getId(),token);

            return ReturnJsonUtil.printJson(200,"登录成功",json,false);
        }catch (Exception e){
            logger.error("登录出错：{}",e.getMessage());
            return ReturnJsonUtil.printJson(500,"登录失败",false);
        }
    }

    @Override
    public TblUser selectByUserName(String username) {
        return userMapper.selectByUser(username);
    }

    @Override
    public ResponseData wxLogin(String code) {

        try {
            String url = ParamConfig.getString(openid)+"?appid="+ParamConfig.getString(appid)+"&secret="+ParamConfig.getString(appsecret)+"&code="+code+"&grant_type=authorization_code";

            String detail = HttpClient.doGet(url);

            JSONObject json = JSONObject.parseObject(detail);

            if(json.containsKey("errcode")){
                return ReturnJsonUtil.printJson(json.getInteger("errcode"),json.getString("errmsg"),false);
            }

            String openid = json.getString("openid");

            TblUser user = new TblUser();
            user.setId(openid);

            Map<String, Object> payload = new HashMap<String, Object>();
            payload.put("userId", openid);
            String token = JWTUtils.createJWT(JSON.toJSONString(user),payload,ParamConfig.getLong(tokenExpired,tokenExpiredDefVal));


            JSONObject reJson = new JSONObject();
            reJson.put("openid",openid);
            reJson.put("token",token);

            logger.info("微信用户登录成功，用户名={},id={},token={}",user.getUser(),user.getId(),token);

            return ReturnJsonUtil.printJson(200,"登录成功",reJson,true);
        }catch (Exception e){
            logger.error("登录出错：{}",e.getMessage());
            return ReturnJsonUtil.printJson(500,"登录失败",false);
        }

    }

    @Override
    public TblUser selectByUserNameAndId(String id, String username) {

        TblUser user = userMapper.selectById(id);

        if (user == null){
            return null;
        }

        if (StringUtils.equals(user.getUser(),username)){
            return user;
        }

        return null;
    }

    @Override
    public Integer updatePassword(TblUser user) {
        return userMapper.updatePassword(user);
    }

    @Override
    public TblUser selectByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }
}
