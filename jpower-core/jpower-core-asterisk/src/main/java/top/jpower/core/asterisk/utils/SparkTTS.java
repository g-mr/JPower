package top.jpower.core.asterisk.utils;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SparkTTS {

    public static String tts(String text){
        try {
            String result = HttpUtil.createPost("http://192.168.31.29:7860/api/voice_creation")
                    .body(JSON.toJSONString(MapBuilder.create()
                            .put("text", text)
                            .put("prompt_wav_upload", "/data/stella.wav")
                            .put("prompt_text", "宝宝们,下面给大家推荐下我们的这款电动牙刷").build()))
                    .execute().body();
            log.info("TTS返回结果,转换结果{}", result);
            JSONObject json = JSON.parseObject(result);
            return json.getString("path");
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
