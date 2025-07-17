package top.jpower.core.log.trace;

import lombok.NoArgsConstructor;
import org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout;

/**
 * traceId设置
 *
 * @author mr.g
 * @date 2025-7-16 21:53
 * @description
 */
@NoArgsConstructor
public class TraceIdLayout extends TraceIdPatternLogbackLayout {

    static {
        defaultConverterMap.put("traceId", TraceIdPatternConverter.class.getName());
    }

}
