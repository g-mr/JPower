package com.qidiangk.smart.aster.tripartite.ali;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.caller.CallerUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.agi.fastagi.support.AgiSupport;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.AsrResult;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.tripartite.property.AliProperty;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 阿里多说话人录音文件ASR识别
 * <br />
 * <a href="https://help.aliyun.com/zh/isi/developer-reference/api-reference-2?spm=a2c4g.11186623.help-menu-30413.d_3_2_2_0.77e72479XjdW3S&scm=20140722.H_90727._.OR_help-T_cn~zh-V_1">接口文档</a>
 */
@Slf4j
public class AliFileRoleAsrClient extends AliToken implements AsrClient {

    private final IAcsClient client;
    private final CommonRequest sendRequest;
    private final CommonRequest resultRequest;
    private final String fileDir;
    private final String fileLink;

    public AliFileRoleAsrClient() {
        AliProperty.AsrFileRole asrFileRole = aliProperty.getAsrFileRole();
        if (Fc.isBlank(asrFileRole.getFileDir())) {
            throw new RuntimeException("请配置人工录音文件存储目录");
        }
        fileDir = StrUtil.appendIfMissing(asrFileRole.getFileDir(), File.separator);
        if (!FileUtil.exist(fileDir)){
            FileUtil.mkdir(fileDir);
        }

        DefaultProfile profile = DefaultProfile.getProfile(asrFileRole.getRegionId(), aliProperty.getAccessKeyId(), aliProperty.getAccessKeySecret());
        client = new DefaultAcsClient(profile);

        // 创建提交请求 设置请求参数
        sendRequest = new CommonRequest();
        sendRequest.setSysDomain("filetrans."+asrFileRole.getRegionId()+".aliyuncs.com");
        sendRequest.setSysVersion(asrFileRole.getVersion());
        sendRequest.setSysAction("SubmitTask");          // 设置action，固定值。
        sendRequest.setSysProduct("nls-filetrans");      // 设置产品名称，固定值。
        sendRequest.setSysMethod(MethodType.POST);
        //当aliyun-java-sdk-core 版本为4.6.0及以上时，请取消该行注释
        sendRequest.setHttpContentType(FormatType.JSON);
        JSONObject task = JSON.parseObject(JSON.toJSONString(asrFileRole));
        task.put("appkey", aliProperty.getAppKey());
        task.put("version", "4.0");
        if (Fc.isBlank(asrFileRole.getFileDomain())){
            throw new RuntimeException("请配置文件下载地址");
        }
        fileLink = StrUtil.appendIfMissing(asrFileRole.getFileDomain(), "/");
        sendRequest.putBodyParameter("Task", task.toJSONString());

        // 设置结果查询请求 设置请求参数
        resultRequest = new CommonRequest();
        resultRequest.setSysDomain("filetrans."+asrFileRole.getRegionId()+".aliyuncs.com");
        resultRequest.setSysVersion(asrFileRole.getVersion());
        resultRequest.setSysAction("GetTaskResult");           // 设置action，固定值。
        resultRequest.setSysProduct("nls-filetrans");          // 设置产品名称，固定值。
        resultRequest.setSysMethod(MethodType.GET);            // 设置为GET方式的请求。
    }

    @Override
    public synchronized AsrResult process(PipedInputStream pipedInput) {

        AtomicBoolean started = new AtomicBoolean(false);
        AtomicReference<Consumer<String>> dataReference = new AtomicReference<>(null);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            File file = new File(fileDir + IdUtil.getSnowflakeNextId() + ".wav");
            try {
                byte[] buffer = new byte[3200];
                int len;

                if (CallerUtil.isCalledBy(AgiSupport.class)){
                    throw new RuntimeException("文件识别暂不支持实时输出ASR............");
                }

                while ((len = pipedInput.read(buffer)) > 0) {
                    FileUtil.writeBytes(buffer, file, 0, len, true);
                }
            } catch (IOException e) {
                log.error("录音流读取异常==>>{}", e.getMessage());
            }

            // 设置文件下载地址
            JSONObject task = JSONObject.parseObject(sendRequest.getSysBodyParameters().get("Task"));
            task.put("file_link", fileLink+file.getName());
            sendRequest.getSysBodyParameters().put("Task", task.toJSONString());

            try {
                // 提交任务
                CommonResponse postResponse = client.getCommonResponse(sendRequest);
                if (postResponse.getHttpStatus() == 200) {
                    JSONObject result = JSONObject.parseObject(postResponse.getData());
                    String statusText = result.getString("StatusText");
                    if (Fc.equalsValue("SUCCESS", statusText)) {
                        log.info("录音文件识别请求成功响应==>>{}", result.toJSONString());
                        String taskId = result.getString("TaskId");

                        // 还差实现结果获取
                        resultRequest.putQueryParameter("TaskId", taskId);
                        while (true) {
                            CommonResponse getResponse = client.getCommonResponse(resultRequest);
                            if (getResponse.getHttpStatus() == 200) {

                                JSONObject resultJson = JSONObject.parseObject(getResponse.getData());
                                String status = resultJson.getString("StatusText");
                                if (StrUtil.equalsAnyIgnoreCase(status, "SUCCESS", "SUCCESS_WITH_NO_VALID_FRAGMENT")) {
                                    started.set(true);

                                    if (Fc.equalsValue("SUCCESS_WITH_NO_VALID_FRAGMENT", status)){
                                        return StrUtil.EMPTY;
                                    }

                                    JSONArray sentences = resultJson.getJSONObject("Result").getJSONArray("Sentences");

                                    if (Fc.notNull(dataReference.get())){
                                        sentences.forEach(sentence -> {
                                            String text = ((JSONObject) sentence).getString("Text");
                                            dataReference.get().accept(text);
                                        });
                                    }

                                    if (CallerUtil.isCalledBy(AgiSupport.class)){
                                        if (sentences.getJSONObject(0).containsKey("SpeakerId") && sentences.stream().map(node -> ((JSONObject) node)).map(node -> node.getString("SpeakerId")).distinct().count() > 1){
                                            return sentences.getJSONObject(sentences.size()-1).getString("Text");
                                        } else {
                                            return sentences.stream().map(node -> ((JSONObject) node)).map(node -> node.getString("Text")).collect(Collectors.joining(StrPool.LF));
                                        }
                                    } else {
                                        return sentences.stream().map(node -> ((JSONObject) node)).map(node -> {
                                            return StrUtil.blankToDefault(node.getString("SpeakerId"), "1") + StrPool.COLON + node.getString("Text");
                                        }).collect(Collectors.joining(StrPool.LF));
                                    }
                                } if (StrUtil.equalsAnyIgnoreCase(status, "RUNNING", "QUEUEING")) {
                                    // 继续轮询
                                    ThreadUtil.safeSleep(3000);
                                } else {
                                    throw new RuntimeException("[Ali]录音文件识别结果失败[状态={"+status+"}]===>>"+ JSONObject.toJSONString(getResponse));
                                }

                            } else {
                                throw new RuntimeException("[Ali]录音文件识别结果获取失败响应===>>"+ JSONObject.toJSONString(getResponse));
                            }
                        }


                    } else {
                        throw new RuntimeException("[Ali]录音文件识别请求失败： " + result.toJSONString());
                    }
                } else {
                    throw new RuntimeException("[Ali]录音文件识别请求失败响应===>>"+ JSONObject.toJSONString(postResponse));
                }
            } catch (ClientException e) {
                throw new RuntimeException(e);
            } finally {
                FileUtil.del(file);
            }

        });

        return new AsrResult(future, started, dataReference, new AtomicReference<>(null));
    }

    @Override
    public void close() {

    }

}
