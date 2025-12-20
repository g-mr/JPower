package top.jpower.core.log.trace;

import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.core.util.StrUtil;
import lombok.NoArgsConstructor;
import net.logstash.logback.composite.AbstractFieldJsonProvider;
import net.logstash.logback.composite.FieldNamesAware;
import net.logstash.logback.composite.JsonWritingUtils;
import net.logstash.logback.fieldnames.LogstashFieldNames;
import tools.jackson.core.JsonGenerator;
import top.jpower.core.util.utils.Fc;

import java.util.Map;

import static top.jpower.core.log.trace.TraceIdPatternConverter.TRACE_ID;
import static top.jpower.core.log.trace.TraceIdPatternConverter.TRACING_IGNORE;
import static top.jpower.core.log.trace.TraceIdPatternConverter.TRACING_NONE;

/**
 * @author mr.g
 * @date 2025-7-27 19:02
 * @description
 */
@NoArgsConstructor
public class TraceIdJsonProvider extends AbstractFieldJsonProvider<ILoggingEvent> implements FieldNamesAware<LogstashFieldNames> {

    public void writeTo(JsonGenerator generator, ILoggingEvent event) {


        String tracingId = this.getTracingId(event);
        tracingId = StrUtil.trim(tracingId);
        if (Fc.isBlank(tracingId) || StrUtil.equalsAnyIgnoreCase(tracingId, TRACING_NONE, TRACING_IGNORE)){
            // 没有拿到skywalking的traceId就获取sleuth的
            tracingId = event.getMDCPropertyMap().get(TRACE_ID);
        }
        JsonWritingUtils.writeStringField(generator, this.getFieldName(), tracingId);
    }

    public void setFieldNames(LogstashFieldNames fieldNames) {
        this.setFieldName(TRACE_ID);
    }

    public String getTracingId(ILoggingEvent event) {
        Map<String, String> map = event.getLoggerContextVO().getPropertyMap();
        return map.get("TID");
    }

}
