package com.wlcb.jpower.module.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mr.g
 * @date 2021-05-26 11:18
 */
@Slf4j
public abstract class AspectBase {

    protected static ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);

    protected void doLog(String dto) {
        log.info(dto);
    }

    protected String serial(Object[] obj) {
        if (obj == null || obj.length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object item : obj) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(serial(item));
        }
        sb.insert(0, "[").append("]");
        return sb.toString();
    }

    protected String serial(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            if (obj instanceof byte[]) {
                obj = new String((byte[]) obj);
            }
            return mapper.writeValueAsString(obj);
        } catch (Exception ex) {
            log.error(String.format("%s serialize error: %s", obj.getClass().getName(), ex.toString()));
            return obj.toString();
        }
    }

}
