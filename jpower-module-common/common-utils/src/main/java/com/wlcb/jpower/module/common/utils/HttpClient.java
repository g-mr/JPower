package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
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
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName HttpClient
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-01-29 02:20
 * @Version 1.0
 */
@Slf4j
public class HttpClient {

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
     * @return java.lang.String
     * @Author 郭丁志
     * @Description //TODO get请求
     * @Date 11:34 2020-07-24
     * @Param [url]
     **/
    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, null, params);
    }

    public static String doGet(String url, Map<String, String> headers, Map<String, Object> params) {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        try {

            URIBuilder builder = new URIBuilder(url);
            builder.setParameter("clientCode", AppConstant.JPOWER);
            if (params != null && params.keySet().size() > 0) {
                params.forEach((param, value) -> builder.setParameter(param, Fc.toStr(value)));
            }

            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();

            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(builder.build());
            // 设置请求头信息，鉴权
            if (Fc.isNotEmpty(headers)) {
                headers.forEach(httpGet::addHeader);
            }
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig());

            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            // 关闭资源
            Fc.closeQuietly(response);
            Fc.closeQuietly(httpClient);
        }
        return result;
    }

    /**
     * @return java.lang.String
     * @Author 郭丁志
     * @Description //TODO 下载文件
     * @Date 01:37 2020-04-30
     * @Param [requestUrl, path]
     **/
    public static File doGetDowload(String requestUrl, String path) {
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

            return file;
        } catch (IOException e) {
            log.error("文件下载失败：{}", ExceptionsUtil.getStackTraceAsString(e));
        } finally {
            Fc.closeQuietly(is);
            Fc.closeQuietly(bos);
            Fc.closeQuietly(fos);
        }

        return null;
    }

    /**
     * @return java.lang.String
     * @Author 郭丁志
     * @Description //TODO post请求
     * @Date 11:37 2020-07-24
     * @Param [url, paramMap]
     **/
    public static String doPost(String url, Map<String, Object> paramMap) {
        return doPost(url, null, paramMap);
    }

    public static String doPost(String url, Map<String, String> headers, Map<String, Object> paramMap) {
        return doEntityRequest(new HttpPost(url),headers,paramMap);
    }


    public static String doPut(String url) {
        return doPut(url, null);
    }

    public static String doPut(String url, Map<String, Object> params) {
        return doPut(url, null, params);
    }

    /**
     * @return java.lang.String
     * @Author 郭丁志
     * @Description //TODO PUT请求
     * @Date 22:15 2020-08-28
     * @Param [url, headers, paramMap]
     **/
    public static String doPut(String url, Map<String, String> headers, Map<String, Object> paramMap) {
        return doEntityRequest(new HttpPut(url),headers,paramMap);
    }

    /**
     * body请求
     * @Author mr.g
     * @param http
     * @param headers
     * @param paramMap
     * @return java.lang.String
     **/
    private static String doEntityRequest(HttpEntityEnclosingRequestBase http, Map<String, String> headers, Map<String, Object> paramMap) {
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 为httpPost实例设置配置
        http.setConfig(requestConfig());
        // 设置请求头
        http.addHeader("Content-Type", "application/x-www-form-urlencoded");
        if (Fc.isNotEmpty(headers)) {
            headers.forEach(http::addHeader);
        }
        // 封装请求参数
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();

            paramMap.forEach((k, v) -> nvps.add(new BasicNameValuePair(k, Fc.toStr(v))));

            // 为httpPost设置封装好的请求参数
            try {
                http.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
        }
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(http);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            log.error("请求失败：{}", ExceptionsUtil.getStackTraceAsString(e));
        } finally {
            // 关闭资源
            Fc.closeQuietly(httpResponse);
            Fc.closeQuietly(httpClient);
        }
        return result;
    }

    /**
     * https请求
     * @Author mr.g
     * @param strUrl 请求地址
     * @param reqBody body
     * @param cerPath 密钥
     * @param cerPassword 密钥密码
     * @return java.lang.String
     **/
    public static String requestWithCert(String strUrl, String reqBody, String cerPath, String cerPassword) {
        String UTF8 = "UTF-8";
        String resp = null;
        StringBuilder builder = new StringBuilder();
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
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
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

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            resp = builder.toString();
        } catch (Exception e) {
            log.info("请求接口{}失败，error={}", strUrl, ExceptionsUtil.getStackTraceAsString(e));
        } finally {
            Fc.closeQuietly(bufferedReader);
            Fc.closeQuietly(inputStream);
            Fc.closeQuietly(outputStream);
            Fc.closeQuietly(certStream);
        }

        return resp;
    }


    public static String doPostJson(String url, String param) {
        return doPostJson(url,null,param);
    }

    public static String doPostJson(String url, Map<String, String> headers, String param) {
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        post.setConfig(requestConfig());

        HttpEntity entity = EntityBuilder.create().setText(param).setContentType(ContentType.APPLICATION_JSON).build();
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json");
        if (Fc.isNotEmpty(headers)) {
            headers.forEach(post::addHeader);
        }
        CloseableHttpResponse execute = null;
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            execute = httpClient.execute(post);
            // 从响应对象中获取响应内容
            entity = execute.getEntity();
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            log.error("{}接口调用失败=>{}",url, ExceptionsUtil.getStackTraceAsString(e));
        } finally {
            Fc.closeQuietly(execute);
            Fc.closeQuietly(httpClient);
            // 关闭资源
        }
        return result;
    }

    /**
     * 单文件上传
     * @param file 文件
     * @param url 路径
     * @param fileName 文件字段名
     * @param paramMap 参数
     * @return
     */
    public static String uploadFile(File file, String url, String fileName, Map<String, String> paramMap) {
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            //把文件转换成流对象FileBody
            FileBody fundFileBin = new FileBody(file);
            //设置传输参数
            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
            multipartEntity.addPart(fileName, fundFileBin);
            //文件以外的参数
            Set<String> keySet = paramMap.keySet();
            for (String key : keySet) {
                multipartEntity.addPart(key, new StringBody(paramMap.get(key), ContentType.create("multipart/form-data", Consts.UTF_8)));
            }


            HttpEntity reqEntity = multipartEntity.build();
            httpPost.setEntity(reqEntity);

            //发起请求   并返回请求的响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                // httpClient对象执行post请求,并返回响应参数对象
                response = httpClient.execute(httpPost);
                // 从响应对象中获取响应内容
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
            } catch (Exception e) {
                log.error("{}接口调用失败=>{}",url, ExceptionsUtil.getStackTraceAsString(e));
            } finally {
                response.close();
                // 关闭资源
            }
        } catch (IOException e) {
            log.error("{}接口调用失败=>{}",url, ExceptionsUtil.getStackTraceAsString(e));
        } finally {
            Fc.closeQuietly(httpClient);
            // 关闭资源
        }
        return result;
    }

    public static String uploadFile(byte[] bytes, String url, String paramFileName, String fileName, String json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;
        HttpPost httpPost = null;
        HttpEntity httpEntity = null;

        try {
            //服务器地址
            httpPost = new HttpPost(url);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
            mEntityBuilder.setCharset(Charset.forName("utf-8"));
            mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            mEntityBuilder.addBinaryBody(paramFileName, bytes, ContentType.MULTIPART_FORM_DATA,fileName);
            ContentType contentType = ContentType.create("application/json", "utf-8");
            StringBody stringBody = new StringBody(json,contentType);
            mEntityBuilder.addPart("objectJsonStr", stringBody);

            httpPost.setEntity(mEntityBuilder.build());
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                httpEntity = response.getEntity();
                result =EntityUtils.toString(httpEntity);
            }
        } catch (IOException e) {
            log.error(ExceptionsUtil.getStackTraceAsString(e));
        } finally {
            // 释放资源
            try {
                if( null != httpEntity ) {
                    EntityUtils.consume(httpEntity);
                }
                Fc.closeQuietly(response);
                Fc.closeQuietly(httpClient);
                if( null != httpPost ){
                    httpPost.releaseConnection();
                }
            } catch (IOException e) {
                log.error(ExceptionsUtil.getStackTraceAsString(e));
            }
        }
        return result;
    }

}
