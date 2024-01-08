package top.jpower.jpower.module.common.jackson;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import top.jpower.jpower.module.common.utils.DateUtil;
import top.jpower.jpower.module.common.utils.Fc;

import java.io.IOException;
import java.util.Date;

/**
 * @author mr.g
 * @date 2023/11/22 12:06 PM
 */
public class Long2StringJacksonConverter extends JsonSerializer<Long> {

    /**
     * Long类型达到长度则转换
     **/
    private final int LONG_MAX_LENGTH = 16;

    @Override
    public void serialize(Long text, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String val = Fc.toStr(text);
        if (val.length() >= LONG_MAX_LENGTH){
            jsonGenerator.writeString(val);
        } else {
            jsonGenerator.writeNumber(text);
        }
    }

}