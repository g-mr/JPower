package top.jpower.core.boot.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import top.jpower.core.util.utils.DateUtil;
import top.jpower.core.util.utils.Fc;

import java.io.IOException;
import java.util.Date;

/**
 * @author mr.g
 * @date 2023/11/22 12:06 PM
 */
public class DateJacksonConverter extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        Date targetDate = null;
        String originDate = p.getText();
        if (Fc.isNotBlank(originDate)) {
            try {
                long longDate = Long.valueOf(originDate.trim());
                targetDate = new Date(longDate);
            } catch (NumberFormatException e) {
                targetDate = DateUtil.parse(originDate);
            }
        }

        return targetDate;
    }

    @Override
    public Class<?> handledType() {
        return Date.class;
    }
}