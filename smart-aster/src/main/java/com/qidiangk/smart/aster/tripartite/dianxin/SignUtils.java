package com.qidiangk.smart.aster.tripartite.dianxin;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
public class SignUtils {

    // 4小时缓存
    private static final Cache<String, String> SIGN_CACHE = CacheUtil.newTimedCache(1000 * 60 * 60 * 4);
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private static final List DEFAULT_HEADERS = Arrays.asList(
            HttpHeaders.HOST,
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.CONTENT_LENGTH,
            "Content-Md5");

    /**
     * 生成签名
     * @param signPrefix  签名前缀
     * @param appKey  应用密钥
     * @param method  HTTP请求方法
     * @param path    HTTP请求uri
     * @param params  HTTP请求参数
     * @param headers HTTP请求headers
     * @return 生成的签名
     */
    private static String genSignature(String signPrefix,String appKey, HttpMethod method, String path,
                                      Map<String, String> params, Map headers,String signedHeaders) {

        // 格式化基础签名信息
        String signingKey;
        try {
            // 生成签名密钥
            signingKey = generateHmacSHA256Hex(signPrefix, appKey);
        } catch (Exception e) {
            log.error("{signPrefix}/{X-APP-ID}/{region}/{timestamp}/{expirationPeriodInSeconds}签名错误", e);
            throw new RuntimeException(e);
        }
        log.info("appKey:[{}] signingKey:[{}]",appKey,signingKey);
        // 获取规范化的请求字符串
        String canonicalRequest = getCanonicalRequest(method, path, params, headers,signedHeaders);
        log.info("请求内容签名信息 canonicalRequest: {}",canonicalRequest);
        String signature;
        try {
            // 生成最终签名
            signature = generateHmacSHA256Hex(canonicalRequest, signingKey);
        } catch (Exception e) {
            log.error("canonicalRequest 签名错误", e);
            throw new RuntimeException(e);
        }
        return signature;
    }

    /**
     * 获取规范化的请求字符串
     *
     * @param method  HTTP请求方法
     * @param path    HTTP请求uri
     * @param params  HTTP请求参数
     * @param headers HTTP请求headers
     * @param signedHeaders 签名头
     * @return 规范化的请求字符串
     */
    private static String getCanonicalRequest(HttpMethod method, String path, Map<String, String> params,
                                              Map headers, String signedHeaders) {
        // 构建规范化的请求字符串
        StringBuilder canonicalRequest = new StringBuilder();
        canonicalRequest.append(method).append("\n");
        String canonicalURI;
        try {
            // 规范化请求URI
            canonicalURI = URLEncoder.encode(path, "utf-8").replaceAll("%2F", "/");
        } catch (UnsupportedEncodingException e) {
            log.error("url encode error", e);
            throw new RuntimeException(e);
        }
        canonicalRequest.append(canonicalURI).append("\n");

        // 获取规范化查询字符串
        canonicalRequest.append(getCanonicalQueryString(params)).append("\n");

        // 获取规范化请求头
        String canonicalHeaders = getCanonicalHeaders(headers,signedHeaders);
        canonicalRequest.append(canonicalHeaders);
        return canonicalRequest.toString();
    }

    /**
     * 获取规范化的查询字符串
     *
     * @param paramMap 查询参数映射
     * @return 规范化的查询字符串
     */
    private static String getCanonicalQueryString(Map<String, String> paramMap) {
        List params = new ArrayList();

        for (Map.Entry<String, String> param : paramMap.entrySet()) {
            String key = param.getKey();
            String value = param.getValue();
            try {
                params.add(URLEncoder.encode(key, "UTF-8") + "=" + (StrUtil.isNotBlank(value) ? URLEncoder.encode(value, "UTF-8") : ""));
            } catch (UnsupportedEncodingException e) {
                log.error("params encode error", e);
                throw new RuntimeException(e);
            }
        }
        // 按字母顺序排序查询参数
//        params.sort(String.CASE_INSENSITIVE_ORDER);
        Collections.sort(params);
        return StrUtil.join( "&",params);
    }

    /**
     * 获取规范化的请求头
     * @param headerMap 请求头映射
     * @param signedHeaders 签名头
     * @return 规范化的请求头字符串
     */
    private static String getCanonicalHeaders(Map<String, String> headerMap, String signedHeaders) {
        if (headerMap.isEmpty()) {
            return "";
        }
        // 将请求头键名转换为小写
        Map<String, String> lowerCaseHeaderMap = new HashMap();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            lowerCaseHeaderMap.put(entry.getKey().toLowerCase(), entry.getValue());
        }

        List<String> headerList = new ArrayList();
        List<String> headerNames;
        if (StrUtil.isNotBlank(signedHeaders)) {
            headerNames = StrUtil.split(signedHeaders,";");
        } else {
            headerNames = DEFAULT_HEADERS;
        }
        for (String headerName : headerNames) {
            String lowerCaseHeaderName = headerName.trim().toLowerCase();
            String headerValue = lowerCaseHeaderMap.get(lowerCaseHeaderName);
            if (StrUtil.isNotBlank(headerValue)) {
                try {
                    headerList.add(lowerCaseHeaderName + ":" + StrUtil.trim(URLEncoder.encode(headerValue, "UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    log.error("headers encode error", e);
                    throw new RuntimeException(e);
                }
            }
        }
        // 按字母顺序排序请求头
//        headerList.sort(String.CASE_INSENSITIVE_ORDER);
        Collections.sort(headerList);
        return StrUtil.join("\n",headerList );
    }

    /**
     * 生成一个 HmacSHA256 哈希值。
     *
     * @param data 要进行哈希处理的数据。
     * @param key  用于哈希处理的密钥。
     * @return 生成的 HmacSHA256 哈希值，以十六进制编码表示。
     * @throws Exception 如果哈希处理失败，则抛出异常。
     */
    private static String generateHmacSHA256Hex(String data, String key) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        return bytesToHex(rawHmac);
    }

    /**
     * 将字节数组转换为十六进制字符串。
     *
     * @param bytes 要转换的字节数组。
     * @return 字节数组的十六进制表示。
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String get(String appId, String appKey, String path) {
        return SIGN_CACHE.get(StrUtil.concat(true, appId, appKey, path), () -> {
            //特此声明里边的值需要根据需求进行替换
            //时间戳
            long l = System.currentTimeMillis() / 1000L;
            //这里因为没有引入Request 所以模拟header的map
            Map<String, String> headerMap = new HashMap<>();
            //这里根据文档里的描述，这里需要根据实际需求进行修改（推荐使用 x-app-id）
            String signedheaders = "x-app-id";
            //如果signedheaders  有多个那么这里需要都塞进去  同样的 当你使用request的请求的时候 你的signedheaders里的key在header都需要具备
            headerMap.put("X-APP-ID", appId);
            //根据文档进行替换,所请求服务资源所在的区域，可使用所在省份缩写（见本文后面的省份列表）
            String region="NM";
            String basic = "teleai-cloud-auth-v1/"+appId+"/"+region+"/"+l+"/180000";
            //这里是接口的param  param指的是 路径后缀 使用?后面的参数 同样的如果使用request的话 使用了url拼接？key=value 那么加密必须使用
            //这里模拟的是不具备？的情况所以塞了一个空的map
            Map<String, String> params = new HashMap<>();
            String sign = SignUtils.genSignature(
                    basic,
                    //X-APP-KEY,对应账号信息中的key值
                    appKey,
                    //请求的发送方式   POST GET 等 websocket 是GET，需要与算法的请求方式保持一致
                    HttpMethod.GET,
                    //请求路径的path
                    path,
                    params,
                    headerMap,
                    signedheaders);

            //生成的就是 Authorization 注意生成的有斜杠进行拼接
            return String.format("%s/%s/%s",basic,signedheaders,sign);
        });
    }
}