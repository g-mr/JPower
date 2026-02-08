package top.jpower.core.exception.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.core.exception.enums.constants.LogConstant;
import top.jpower.core.exception.model.ErrorLogDto;
import top.jpower.core.exception.model.OperateLogDto;
import top.jpower.core.feign.config.DynamicFeignConfig;
import top.jpower.core.util.rsp.R;

import static top.jpower.core.feign.config.DynamicFeignConfig.SERVICE_PARAM_NAME;

@FeignClient(
        name = LogConstant.JPOWER_LOG,
        contextId = "logTraceClient",
        configuration = DynamicFeignConfig.class,
        path = "/log"
)
public interface LogTraceClient {

    @PostMapping("/saveOperateLog")
    R<Long> saveOperateLog(
            @RequestParam(SERVICE_PARAM_NAME) String serviceName,
            @RequestBody OperateLogDto operateLog
    );

    @PostMapping("/saveErrorLog")
    R<Long> saveErrorLog(
            @RequestParam(SERVICE_PARAM_NAME) String serviceName,
            @RequestBody ErrorLogDto errorLogDto
    );

}
