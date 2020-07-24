package com.wlcb.jpower.module.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.*;
import java.util.Map.Entry;

/**
 * @ClassName HttpClient
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-01-29 02:20
 * @Version 1.0
 */
public class HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    /**
     * @Author 郭丁志
     * @Description //TODO get请求
     * @Date 11:34 2020-07-24
     * @Param [url]
     * @return java.lang.String
     **/
    public static String doGet(String url) {
        return doGet(url,null);
    }

    public static String doGet(String url,Map<String, Object> params) {
        return doGet(url,null,params);
    }

    public static String doGet(String url,Map<String, String> headers,Map<String, Object> params) {

        StringBuffer sb = new StringBuffer(url);
        if (params != null && params.keySet().size() > 0) {
            sb.append("?clientId=jpower");
            params.forEach((k, v) -> sb.append("&").append(k).append("=").append(v));
        }

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
            if (Fc.isNotEmpty(headers)){
                headers.forEach(httpGet::addHeader);
            }
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            Fc.closeQuietly(response);
            Fc.closeQuietly(httpClient);
        }
        return result;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 下载文件
     * @Date 01:37 2020-04-30
     * @Param [requestUrl,path]
     * @return java.lang.String
     **/
    public static void doGetDowload(String requestUrl,String path) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        byte[] buff = new byte[1024];
        int len = 0;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "APPLICATION/OCTET-STREAM;charset=UTF-8");
            conn.setRequestProperty("charset", "UTF-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setReadTimeout(80000);
            conn.connect();
            is = conn.getInputStream();

            File file = new File(path);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            while ((len = is.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }

        } catch (IOException e) {
            logger.error("文件下载失败：{}",e.getMessage());
            e.printStackTrace();
        } finally {
            Fc.closeQuietly(is);
            Fc.closeQuietly(bos);
            Fc.closeQuietly(fos);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO post请求
     * @Date 11:37 2020-07-24
     * @Param [url, paramMap]
     * @return java.lang.String
     **/
    public static String doPost(String url, Map<String, Object> paramMap) {
        return doPost(url,null,paramMap);
    }

    public static String doPost(String url, Map<String, String> headers, Map<String, Object> paramMap) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(60000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        if (Fc.isNotEmpty(headers)){
            headers.forEach(httpPost::addHeader);
        }
        // 封装post请求参数
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Entry<String, Object>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> mapEntry = iterator.next();
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            // 为httpPost设置封装好的请求参数
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage());
            }
        }
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.error("post请求失败：{}",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("post请求失败：{}",e.getMessage());
        } finally {
            // 关闭资源
            Fc.closeQuietly(httpResponse);
            Fc.closeQuietly(httpClient);
        }
        return result;
    }

    public static String requestWithCert(String strUrl, String reqBody, String cerPath, String cerPassword) {
        String UTF8 = "UTF-8";
        String resp = null;
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        InputStream certStream = null;
        try {
            URL httpUrl = new URL(strUrl);
            char[] password = cerPassword.toCharArray();
            certStream = new FileInputStream(new File(cerPath));
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), (TrustManager[])null, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpURLConnection httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);
            httpURLConnection.connect();
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(reqBody.getBytes(UTF8));
            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
            String line = null;

            while((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            resp = stringBuffer.toString();
        }catch (Exception e){
            logger.info("请求接口{}失败，error={}",strUrl,e.getMessage());
        }finally {
            Fc.closeQuietly(bufferedReader);
            Fc.closeQuietly(inputStream);
            Fc.closeQuietly(outputStream);
            Fc.closeQuietly(certStream);
        }

        return resp;
    }


    public static String doPostJson(String url, String param) {
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        //配置超时时间
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(30000).setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000).setRedirectsEnabled(true).build();

        post.setConfig(requestConfig);

        HttpEntity entity = EntityBuilder.create().setText(param).setContentType(ContentType.APPLICATION_JSON).build();
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json");

        CloseableHttpResponse execute = null;
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            execute = httpClient.execute(post);
            // 从响应对象中获取响应内容
            entity = execute.getEntity();
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            logger.error("{}接口调用失败",e.getMessage());
        } catch (IOException e) {
            logger.error("{}接口调用失败",e.getMessage());
        } finally {
            Fc.closeQuietly(execute);
            Fc.closeQuietly(httpClient);
            // 关闭资源
        }
        return result;


    }
}
