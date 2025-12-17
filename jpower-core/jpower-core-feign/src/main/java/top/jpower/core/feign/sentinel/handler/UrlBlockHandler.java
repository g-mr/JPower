package top.jpower.core.feign.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.feign.sentinel.utils.ErrorMsg;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {
        log.error("sentinel 降级 资源名称{}", ex.getRule().getResource(), ex);

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setCharacterEncoding(StringPool.UTF_8);
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), ErrorMsg.blockException(ex));
    }
}
