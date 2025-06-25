package top.jpower.core.exception.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.core.exception.model.ErrorLogDto;
import top.jpower.core.exception.model.OperateLogDto;
import top.jpower.core.feign.AutoFeignClientsRegistrar;
import top.jpower.core.feign.config.DynamicFeignConfig;
import top.jpower.core.util.rsp.ResponseData;

import static top.jpower.core.feign.config.DynamicFeignConfig.SERVICE_PARAM_NAME;

@FeignClient(
        configuration = DynamicFeignConfig.class,
        path = "/log"
)
@Import(AutoFeignClientsRegistrar.class)
public interface LogTraceClient {

    @PostMapping("/saveOperateLog")
    ResponseData saveOperateLog(
            @RequestParam(SERVICE_PARAM_NAME) String serviceName,
            @RequestBody OperateLogDto operateLog
    );

    @PostMapping("/saveErrorLog")
    ResponseData saveErrorLog(
            @RequestParam(SERVICE_PARAM_NAME) String serviceName,
            @RequestBody ErrorLogDto errorLogDto
    );

}
