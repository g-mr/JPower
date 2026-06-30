package com.qidiangk.smart.aster.tripartite.ali;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONPath;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.AsrResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Slf4j
public class AliHttpAsrClient extends AliToken implements AsrClient {

    @Override
    public AsrResult process(PipedInputStream pipedInput) {
        Thread main = Thread.currentThread();
        AtomicReference<Consumer<String>> dataReference = new AtomicReference<>(null);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            String url = "https://nls-gateway-cn-shanghai.aliyuncs.com/stream/v1/asr";
            url = url + "?appkey=" + aliProperty.getAppKey();
            url = url + "&format=wav";
            url = url + "&sample_rate=8000";
//        if (downloads){
//            url = url + "&audio_address="+FILE_URL+filename;
//        }

            HttpRequest request = HttpRequest.post(url).contentType("application/octet-stream").header("X-NLS-Token", getToken());
            // 读取流
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
                byte[] buffer = new byte[1024];

                while (pipedInput.read(buffer) > 0 && !main.isInterrupted()) {
                    outputStream.writeBytes(buffer);
                }
                outputStream.flush();

                request.body(outputStream.toByteArray());
            } catch (IOException e) {
                log.error("录音流读取异常==>>{}", ExceptionUtil.stacktraceToString(e));
            }

            String result = request.execute().body();
            log.info("识别结果={}", result);
            String str = JSONPath.read(result, "result").toString();
            if (dataReference.get() != null){
                dataReference.get().accept(str);
            }
            return str;
        });
        return new AsrResult(future, new AtomicBoolean(false), dataReference, new AtomicReference<>(null));
    }

    @Override
    public void close() {

    }

}
