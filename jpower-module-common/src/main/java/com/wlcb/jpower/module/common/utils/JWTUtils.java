package com.wlcb.jpower.module.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import com.wlcb.jpower.module.dbs.entity.user.TblUser;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mr.gmac
 */
public class JWTUtils {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    //多少时间内要刷新token
    private static Long refToken = 10 * 60 * 1000L;
    //续期token过期时间
    public static final String tokenExpired = "tokenExpired";
    public static final Long tokenExpiredDefVal = 2400000L;

    /**
     * 加密密文
     */
    public static final String JWT_SECRET = "woyebuzhidaoxiediansha";
    public static final String JWT_ID = "jwtLogin";

    public static String createJWT(String subject, Map<String, Object> claims,Long millis) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        long nowMillis = System.currentTimeMillis();// 生成JWT的时间
        Date now = new Date(nowMillis);
        SecretKey key = generalKey();
        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims) // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(JWT_ID) // 设置jti(JWT
                // ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now) // iat: jwt的签发时间
                .setSubject(subject) // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, key);// 设置签名使用的签名算法和签名使用的秘钥

        if (millis > 0) {
            long expMillis = nowMillis + millis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp); // 设置过期时间
        }
        // 就开始压缩为xxxxxxxxxxxxxx.xxxxxxxxxxxxxxx.xxxxxxxxxxxxx这样的jwt
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    private static Claims parseJWT(String jwt) {
        SecretKey key = generalKey(); // 签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser() // 得到DefaultJwtParser
                .setSigningKey(key) // 设置签名的秘钥
                .parseClaimsJws(jwt).getBody();// 设置需要解析的jwt
        return claims;
    }

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.decodeBase64(JWT_SECRET);// 本地的密码解码
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥，使用
        return key;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断是否用登录权限
     * @Date 00:11 2020-03-06
     * @Param [jwt]
     * @return io.jsonwebtoken.Claims
     **/
    @Deprecated
    public static boolean parseJWT(HttpServletRequest request, HttpServletResponse response) {
        String currentPath = request.getServletPath();

        String jwt = request.getHeader("Authorization");
        if (StringUtils.isBlank(jwt)){
            Cookie[] cookies = request.getCookies();
            if (cookies!=null){
                for (Cookie cookie : cookies) {
                    if (StringUtils.equals(cookie.getName(),"Authorization")){
                        jwt = cookie.getValue();
                        logger.info("通过cookie获取到token",cookie.getValue());
                        break;
                    }
                }
            }
        }


        logger.info("{}已进入鉴权拦截器,token={}",currentPath,jwt);

        if (StringUtils.isBlank(jwt)) {
            logger.info("未登陆或登陆超时！jwt是空的,请求地址={}",currentPath);
        } else {

            JSONObject jsonObject = parsingJwt(jwt);
            if (jsonObject.getBoolean("status")){

                if(StringUtils.isNotBlank(request.getMethod()) && "POST|PUT|DELETE".contains(request.getMethod())){
                    //表明是后台的请求
                    JSONObject user = jsonObject.getJSONObject("user");
                    if (StringUtils.isNotBlank(user.getString("user"))){
                        String userId = jsonObject.getString("userId");
                        logger.info("ID={}的用户请求了{}接口，参数={}",userId,currentPath,JSON.toJSONString(request.getParameterMap()));
                    }
                }

                if (jsonObject.containsKey("token")){
                    response.setHeader("token",jsonObject.getString("token"));
                }

                return true;
            }
        }
        return false;
    }

    public static JSONObject parsingJwt(String jwt){

        JSONObject json = new JSONObject();
        json.put("code",401);
        json.put("status",false);
        json.put("msg","解析失败");


        Claims c = null;
        try{
            c = JWTUtils.parseJWT(jwt);
        }catch (ExpiredJwtException e){
            c = e.getClaims();
            JSONObject user = JSONObject.parseObject(c.getSubject());
            String type = StringUtils.isBlank(user.getString("user"))?"微信":"后台";
            logger.info("{}{}用户登陆超时请重新登录！用户ID={},token={},error={}",user.getString("user"),type,(String) c.get("userId"),jwt,e.getMessage());
            json.put("msg","用户登陆超时请重新登录！用户ID="+user.getString("user"));
            return json;
        }catch (Exception e){
            logger.error("JWT解析出错！error={},token={}",e.getMessage(),jwt);
            json.put("msg","JWT解析出错！token="+e.getMessage());
            return json;
        }

        if(c != null){

            String userId = (String) c.get("userId");
            if (StringUtils.isNotBlank(userId)){
                JSONObject user = JSONObject.parseObject(c.getSubject());
                logger.info("{}用户token已解析，用户信息={}",userId,user.toJSONString());
                if (user!=null){
                    Date expDate = c.getExpiration();

                    long time = expDate.getTime() - System.currentTimeMillis();

                    //过期时间小于10分钟则获取新的token
                    if(time <= refToken){

                        try {
                            Map<String, Object> payload = new HashMap<String, Object>();
                            payload.put("userId", user.getString("id"));
                            String token = JWTUtils.createJWT(JSON.toJSONString(user),payload, ParamConfig.getLong(tokenExpired,tokenExpiredDefVal));

                            if (StringUtils.isBlank(token)){
                                logger.error("token生成错误，token={}",token);
                            }

                            json.put("token",token);

                            logger.info("{}用户已刷新，用户id={},旧token={},新token={}",user.getString("user"),user.getString("id"),jwt,token);
                        } catch (Exception e) {
                            logger.error("刷新token出错：{}",e.getMessage());
                        }
                    }

                    json.put("code",200);
                    json.put("status",true);
                    json.put("msg","解析成功");
                    json.put("userId",userId);
                    json.put("user",user);
                    return json;
                }else {
                    logger.info("未登陆或登陆超时！未解析到用户信息");
                    json.put("msg","未登陆或登陆超时！未解析到用户信息");
                }
            }else {
                logger.info("未登陆或登陆超时！未解析到userId");
                json.put("msg","未登陆或登陆超时！未解析到userId");
            }
        }
        return json;
    }

    public static void main(String[] args) throws Exception {

        TblUser user = new TblUser();
        user.setId("23213");
        user.setUser("sdwdqqdq");
//
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("userId", "21321312312");
        String token = JWTUtils.createJWT(JSON.toJSONString(user),payload,400000L);
        System.out.println(token);

//        Claims c = JWTUtils.parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOlwiMjMyMTNcIixcInJvbGVcIjowLFwic3RhdHVzXCI6MCxcInVzZXJcIjpcInNkd2RxcWRxXCJ9IiwiZXhwIjoxNTgzNDI1NjgzLCJ1c2VySWQiOiIyMTMyMTMxMjMxMiIsImlhdCI6MTU4MzQyNTI4MywianRpIjoiand0TG9naW4ifQ.kJNKLQw5NY-IOTUyhaYppb_g5k_NcttsdZv");
//        System.out.println(c);
//        e.getClaims().getExpiration();

    }
}
