package com.wlcb.jpower.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author mr.g
 * @Date 2021/4/4 0004 22:03
 */
@AllArgsConstructor
public class HttpInfoHandler {

    private JSONObject methodsInfo;
    private JSONObject definitions;

    public List<String> getMethodTypes() {
        List<String> list = new ArrayList<>();
        Iterator<String> keys = methodsInfo.keySet().iterator();
        while (keys.hasNext()) {
            list.add(keys.next());
        }
        return list;
    }
}
