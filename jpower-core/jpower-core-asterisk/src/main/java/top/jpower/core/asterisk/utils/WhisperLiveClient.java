package top.jpower.core.asterisk.utils;//
//package cn.teninfor.framework.ivr.utils;
//
//import cn.hutool.core.map.MapBuilder;
//import cn.hutool.core.text.UnicodeUtil;
//import cn.hutool.core.thread.ThreadUtil;
//import cn.hutool.core.util.IdUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.teninfor.framework.common.jpower.Fc;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Slf4j
//public class WhisperLiveClient extends WebSocketClient implements AsrClient {
//
//    public WhisperLiveClient(String serverUri) throws URISyntaxException {
//        super(new URI(serverUri));
//    }
//
//    private boolean recording = false;
//    private Map<Double, String> listPoints = new HashMap<>();
//    private boolean stop = true;
//
//    @Override
//    public void send(byte[] message){
//        if (!stop && recording){
//            byte[] packet = ByteToAudioByte.bytesToBytes(message, message.length);
//            super.send(packet);
//
//            // 等待ASR解析;本来是应该*1000,但是我想让他快,所以只乘以10
//            double deltaSleep = ((double) message.length / 8000) * 1000;
//            ThreadUtil.sleep((long)deltaSleep);
//        }
//    }
//
//    @Override
//    public void onOpen(ServerHandshake handshakedata) {
//        System.out.println("Connected to WhisperLive server");
//
//        super.send(JSON.toJSONString(MapBuilder.create()
//                .put("uid", IdUtil.nanoId())
//                .put("language", "zh")
//                .put("task", "transcribe")
//                .put("use_vad", false)
//                .put("vad_parameters", MapBuilder.create().put("onset", 0.5).build())
//                .build()));
//    }
//
//    @Override
//    public void onMessage(String message) {
////        System.out.println(message);
//
//        JSONObject json = JSONObject.parseObject(message);
//        if (json.containsKey("message") && StrUtil.equals(json.getString("message"), "SERVER_READY")){
//            recording = true;
//        } else if (json.containsKey("segments")){
//            System.out.println(message);
//
//            JSONArray segments = json.getJSONArray("segments");
//
//            // 只有最后一个为完成则代表一句话完成
////            if (JSON.parseObject(JSON.toJSONString(segments.get(segments.size()-1))).getBoolean("completed")){
//                for (Object obj: segments) {
//                    JSONObject jb = JSON.parseObject(JSON.toJSONString(obj));
//                    if (jb.getBoolean("completed")){
//                        Double jbPoint = jb.getDouble("start") + jb.getDouble("end");
//                        String text = UnicodeUtil.toString(jb.getString("text"));
//                        log.info("识别到用户话语:{}", text);
//                        listPoints.put(jbPoint, text);
//                    }
//                }
////            }
//
//
//        } else {
//            System.out.println("Transcription: " + message);
//        }
//
//    }
//
//    @Override
//    public void onClose(int code, String reason, boolean remote) {
//        System.out.println("Connection closed");
//        this.recording = false;
//    }
//
//    @Override
//    public void onError(Exception ex) {
//        ex.printStackTrace();
//    }
//
//    /**
//     * 收音
//     * @return 结果
//     */
//    @Override
//    public String radio() {
//        this.listPoints = new HashMap<>();
//        if (this.isClosed()){
//            this.reconnect();
//        }
//        this.stop = false;
//        while (Fc.isEmpty(listPoints)){
//            if (this.isOpen()){
//                // 收音中
//                ThreadUtil.sleep(100);
//            }
//        }
//        this.stop = true;
//        String user = listPoints.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByKey())
//                .map(Map.Entry::getValue)
//                .collect(Collectors.joining(""));
//        log.info("用户说:{}", user);
//        return user;
//    }
//}
