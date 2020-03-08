package com.wlcb.wlj.module.common.utils;

import com.alibaba.fastjson.JSON;
import com.wlcb.wlj.module.common.utils.cache.LoginTokenCache;
import com.wlcb.wlj.module.dbs.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
    //登录token过期时间
    private static Long tokenExpired = 15 * 60 * 1000L;

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
    public static boolean parseJWT(HttpServletRequest request, HttpServletResponse response) {
        String currentPath = request.getServletPath();

        String jwt = request.getHeader("Authorization");

        if (jwt == null) {
            logger.warn("未登陆或登陆超时！");
        } else {

            Claims c = null;
            try{
                c = JWTUtils.parseJWT(jwt);
            }catch (Exception e){
                logger.warn("未登陆或登陆超时！");
                e.printStackTrace();
                return false;
            }

            if(c != null){

                String userId = (String) c.get("userId");
                if (StringUtils.isNotBlank(userId)){
                    User user = JSON.parseObject(c.getSubject(), User.class);
                    if (user!=null){
                        Date expDate = c.getExpiration();

                        long time = expDate.getTime() - System.currentTimeMillis();

                        //过期时间小于10分钟则获取新的token
                        if(time <= refToken){

                            //判断当前userID是否生成了新的token,如果已经生成则不再生成
                            if (LoginTokenCache.get(userId) == null || !StringUtils.equals(LoginTokenCache.get(userId),jwt)){
                                try {
                                    Map<String, Object> payload = new HashMap<String, Object>();
                                    payload.put("userId", user.getId());
                                    String token = JWTUtils.createJWT(JSON.toJSONString(user),payload,tokenExpired);

                                    //记录该用户当前token已经刷新了
                                    LoginTokenCache.put(userId,jwt);

                                    response.setHeader("token",token);

                                    logger.info("{}用户已刷新，用户id={},旧token={},新token={}",user.getUser(),user.getId(),jwt,token);
                                } catch (Exception e) {
                                    logger.error("刷新token出错：{}",e.getMessage());
                                }
                            }

                        }

                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {

//        User user = new User();
//        user.setId("23213");
//        user.setUser("sdwdqqdq");
//
//        Map<String, Object> payload = new HashMap<String, Object>();
//        payload.put("userId", "21321312312");
//        String token = JWTUtils.createJWT(JSON.toJSONString(user),payload,400000L);
//        System.out.println(token);

        Claims c = JWTUtils.parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOlwiMjMyMTNcIixcInJvbGVcIjowLFwic3RhdHVzXCI6MCxcInVzZXJcIjpcInNkd2RxcWRxXCJ9IiwiZXhwIjoxNTgzNDI1NjgzLCJ1c2VySWQiOiIyMTMyMTMxMjMxMiIsImlhdCI6MTU4MzQyNTI4MywianRpIjoiand0TG9naW4ifQ.kJNKLQw5NY-IOTUyhaYppb_g5k_NcttsdZv");
        System.out.println(c);
    }
}
