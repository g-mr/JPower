package top.jpower.core.feign.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import top.jpower.core.feign.sentinel.utils.ErrorMsg;
import top.jpower.core.util.constants.StringPool;

/**
 * @ClassName JpowerUrlBlockHandler
 * @Description TODO 限流降级后出返回结果
 * @Author 郭丁志
 * @Date 2020/9/12 0012 21:41
 * @Version 1.0
 */
@Slf4j
public class UrlBlockHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, String resourceName, BlockException ex) throws Exception {
        // TODO 回头需要看下resourceName和ex.getRule().getResource()是否一致
        log.error("sentinel 降级 资源名称{}", ex.getRule().getResource(), ex);

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setCharacterEncoding(StringPool.UTF_8);
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), ErrorMsg.blockException(ex));
    }
}
