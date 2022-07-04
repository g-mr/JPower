package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Http工具类
 *
 * @author mr.g
 **/
@Slf4j
public class HttpClient {

    /**
     * 设置http超时时间
     *
     * @author mr.g
     * @return org.apache.http.client.config.RequestConfig
     **/
    private static RequestConfig requestConfig(){
        return RequestConfig.custom()
                // 连接主机服务超时时间
                .setConnectTimeout(35000)
                // 请求超时时间
                .setConnectionRequestTimeout(35000)
                // 数据读取超时时间
                .setSocketTimeout(60000)
                .build();
    }

    /**
     * get请求
     *
     * @author mr.g
     * @param url 请求地址
     * @return 请求结果
     **/
    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * get请求
     *
     * @author mr.g
     * @param url 请求地址
     * @param params 请求参数
     * @return 请求结果
     **/
    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, null, params);
    }

    /**
     * get请求
     *
     * @author mr.g
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数
     * @return 请求结果
     **/
    @SneakyThrows({IOException.class,URISyntaxException.class})
    public static String doGet(String url, Map<String, String> headers, Map<String, Object> params) {

        URIBuilder builder = new URIBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            params.forEach((param, value) -> builder.setParameter(param, Fc.toStr(value)));
        }

        // 通过址默认配置创建一个httpClient实例
        @Cleanup CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建httpGet远程连接实例
        HttpGet httpGet = new HttpGet(builder.build());
        // 设置请求头信息，鉴权
        if (Fc.isNotEmpty(headers)) {
            headers.forEach(httpGet::addHeader);
        }
        // 为httpGet实例设置配置
        httpGet.setConfig(requestConfig());

        // 执行get请求得到返回对象
        @Cleanup CloseableHttpResponse response = httpClient.execute(httpGet);
        // 通过返回对象获取返回数据
        HttpEntity entity = response.getEntity();
        // 通过EntityUtils中的toString方法将结果转换为字符串
        return EntityUtils.toString(entity);
    }

    /**
     * 文件下载
     *
     * @author mr.g
     * @param url 请求地址
     * @param path 文件保存路径
     * @return 文件
     **/
    @SneakyThrows(IOException.class)
    public static File doGetDowload(String url, String path) {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("Content-Type", "APPLICATION/OCTET-STREAM;charset=UTF-8");
        conn.setRequestProperty("charset", CharsetKit.UTF_8);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setReadTimeout(80000);
        conn.connect();

        File file = new File(path);
        byte[] buff = new byte[1024];
        int len;

        @Cleanup InputStream is = conn.getInputStream();
        @Cleanup FileOutputStream fos = new FileOutputStream(file);
        @Cleanup BufferedOutputStream bos = new BufferedOutputStream(fos);
        while ((len = is.read(buff)) != -1) {
            bos.write(buff, 0, len);
        }

        return file;
    }

    /**
     * post请求
     *
     * @author mr.g
     * @param url 请求地址
     * @param params 请求参数
     * @return 请求结果
     **/
    public static String doPost(String url, Map<String, Object> params) {
        return doPost(url, null, params);
    }

    /**
     * post请求
     *
     * @author mr.g
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数
     * @return 请求结果
     **/
    public static String doPost(String url, Map<String, String> headers, Map<String, Object> params) {
        return doEntityRequest(new HttpPost(url),headers,params);
    }

    /**
     * put请求
     *
     * @author mr.g
     * @param url 请求地址
     * @return 请求结果
     **/
    public static String doPut(String url) {
        return doPut(url, null);
    }

    /**
     * put请求
     *
     * @author mr.g
     * @param url 请求地址
     * @param params 请求参数
     * @return 请求结果
     **/
    public static String doPut(String url, Map<String, Object> params) {
        return doPut(url, null, params);
    }

    /**
     * put请求
     *
     * @author mr.g
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数
     * @return 请求结果
     **/
    public static String doPut(String url, Map<String, String> headers, Map<String, Object> params) {
        return doEntityRequest(new HttpPut(url),headers,params);
    }

    /**
     * from请求
     *
     * @author mr.g
     * @param http HTTP
     * @param headers 请求头
     * @param params 请求参数
     * @return 请求结果
     **/
    @SneakyThrows(IOException.class)
    private static String doEntityRequest(HttpEntityEnclosingRequestBase http, Map<String, String> headers, Map<String, Object> params) {
        // 为httpPost实例设置配置
        http.setConfig(requestConfig());
        // 设置请求头
        http.addHeader("Content-Type", "application/x-www-form-urlencoded");
        if (Fc.isNotEmpty(headers)) {
            headers.forEach(http::addHeader);
        }
        // 封装请求参数
        if (Fc.isNotEmpty(params)) {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            params.forEach((k, v) -> nameValuePairs.add(new BasicNameValuePair(k, Fc.toStr(v))));
            // 为httpPost设置封装好的请求参数
            http.setEntity(new UrlEncodedFormEntity(nameValuePairs, CharsetKit.UTF_8));
        }

        // 创建httpClient实例
        @Cleanup CloseableHttpClient httpClient = HttpClients.createDefault();

        // httpClient对象执行post请求,并返回响应参数对象
        @Cleanup CloseableHttpResponse httpResponse = httpClient.execute(http);
        // 从响应对象中获取响应内容
        return EntityUtils.toString(httpResponse.getEntity());
    }

    /**
     * https请求（POST）
     *
     * @author mr.g
     * @param url 请求地址
     * @param body body
     * @param cerPath 密钥文件路径
     * @param cerPassword 密钥密码
     * @return 请求结果
     **/
    public static String requestWithCert(String url, String body, String cerPath, String cerPassword) {
        return requestWithCert("POST",url, body, cerPath, cerPassword);
    }

    /**
     * https请求（POST）
     *
     * @author mr.g
     * @param httpType 请求类型
     * @param url 请求地址
     * @param body body
     * @param cerPath 密钥文件路径
     * @param cerPassword 密钥密码
     * @return 请求结果
     **/
    @SneakyThrows({IOException.class, KeyStoreException.class, KeyManagementException.class , NoSuchAlgorithmException.class,UnrecoverableKeyException.class, CertificateException.class})
    public static String requestWithCert(String httpType,String url, String body, String cerPath, String cerPassword) {
        StringBuilder builder = new StringBuilder();

        URL httpUrl = new URL(url);
        char[] password = cerPassword.toCharArray();
        @Cleanup InputStream certStream = Files.newInputStream(new File(cerPath).toPath());
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(certStream, password);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpURLConnection httpConnection = (HttpURLConnection) httpUrl.openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setRequestMethod(httpType);
        httpConnection.setConnectTimeout(3000);
        httpConnection.setReadTimeout(3000);
        httpConnection.connect();
        @Cleanup OutputStream outputStream = httpConnection.getOutputStream();
        outputStream.write(body.getBytes(CharsetKit.UTF_8));
        @Cleanup InputStream inputStream = httpConnection.getInputStream();
        @Cleanup BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, CharsetKit.UTF_8));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    /**
     * post请求（body参数）
     *
     * @author mr.g
     * @param url 请求地址
     * @param param 请求参数
     * @return 请求结果
     **/
    public static String doPostJson(String url, String param) {
        return doPostJson(url,null,param);
    }

    /**
     * post请求（body参数）
     *
     * @author mr.g
     * @param url 请求地址
     * @param headers 请求头
     * @param param 请求参数
     * @return 请求结果
     **/
    @SneakyThrows(IOException.class)
    public static String doPostJson(String url, Map<String, String> headers, String param) {
        @Cleanup("releaseConnection") HttpPost post = new HttpPost(url);

        post.setConfig(requestConfig());

        HttpEntity entity = EntityBuilder.create().setText(param).setContentType(ContentType.APPLICATION_JSON).build();
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json");
        if (Fc.isNotEmpty(headers)) {
            headers.forEach(post::addHeader);
        }

        @Cleanup CloseableHttpClient httpClient = HttpClients.createDefault();
        // httpClient对象执行post请求,并返回响应参数对象
        @Cleanup CloseableHttpResponse execute = httpClient.execute(post);
        // 从响应对象中获取响应内容
        return EntityUtils.toString(execute.getEntity());
    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @param url 路径
     * @param fileName 文件字段名
     * @param params from参数
     * @return 请求结果
     */
    @SneakyThrows(IOException.class)
    public static String uploadFile(File file, String url, String fileName, Map<String, String> params) {
        @Cleanup("releaseConnection") HttpPost httpPost = new HttpPost(url);
        //把文件转换成流对象FileBody
        FileBody fundFileBin = new FileBody(file);
        //设置传输参数
        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
        multipartEntity.addPart(fileName, fundFileBin);
        //文件以外的参数
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            multipartEntity.addPart(key, new StringBody(params.get(key), ContentType.create("multipart/form-data", Consts.UTF_8)));
        }

        HttpEntity reqEntity = multipartEntity.build();
        httpPost.setEntity(reqEntity);

        @Cleanup CloseableHttpClient httpClient = HttpClients.createDefault();
        //发起请求   并返回请求的响应
        @Cleanup CloseableHttpResponse response = httpClient.execute(httpPost);
        // 从响应对象中获取响应内容
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * 数据流上传
     *
     * @author mr.g
     * @param bytes 数据流
     * @param url 请求地址
     * @param paramFileName 文件字段名
     * @param fileName 文件名
     * @param json body参数
     * @return 请求结果
     **/
    @SneakyThrows(IOException.class)
    public static String uploadFile(byte[] bytes, String url, String paramFileName, String fileName, String json) {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setCharset(Consts.UTF_8);
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.addBinaryBody(paramFileName, bytes, ContentType.MULTIPART_FORM_DATA,fileName);
        ContentType contentType = ContentType.create("application/json", "utf-8");
        StringBody stringBody = new StringBody(json,contentType);
        multipartEntityBuilder.addPart("objectJsonStr", stringBody);

        @Cleanup("releaseConnection") HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(multipartEntityBuilder.build());

        @Cleanup CloseableHttpClient httpClient = HttpClients.createDefault();
        @Cleanup CloseableHttpResponse response = httpClient.execute(httpPost);
        return EntityUtils.toString(response.getEntity());
    }

}
