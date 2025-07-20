package top.jpower.core.log.trace;

import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.core.util.StrUtil;
import org.apache.skywalking.apm.toolkit.log.logback.v1.x.LogbackPatternConverter;
import org.slf4j.MDC;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;

/**
 * 设置traceId
 *
 * @author mr.g
 * @date 2025-7-16 22:03
 */
public class TraceIdPatternConverter extends LogbackPatternConverter {

    /**
     * skywalking的前缀
     **/
    public static final String TRACING_NAME = "TID:";
    /**
     * 没有获取到skywalking的TRACEID
     **/
    public static final String TRACING_NONE = "N/A";
    /**
     * 被忽视的skywalking的TRACEID
     **/
    public static final String TRACING_IGNORE = "Ignored_Trace";

    /**
     * sleuth的traceId名称
     **/
    public static final String TRACE_ID = "traceId";
    /**
     * sleuth的spanId名称
     **/
    public static final String SPAN_ID = "spanId";

    @Override
    public String convert(ILoggingEvent event) {
        String traceId = super.convert(event);
        traceId = StrUtil.removePrefix(traceId, TRACING_NAME);
        traceId = StrUtil.trim(traceId);
        if (Fc.isBlank(traceId) || StrUtil.equalsAnyIgnoreCase(traceId, TRACING_NONE, TRACING_IGNORE)){
            // 没有拿到skywalking的traceId就获取sleuth的
            traceId = MDC.get(TRACE_ID);
        }

        // 重新设置
        MDC.put(TRACE_ID, traceId);
        return StrUtil.blankToDefault(traceId, StringPool.EMPTY);
    }

}
