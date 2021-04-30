package com.wlcb.jpower.module.base.error;

import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.exception.JpowerException;
import com.wlcb.jpower.module.base.vo.ErrorReturnJson;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO 全局异常获取
 * @Author 郭丁志
 * @Date 2020-01-27 17:24
 * @Version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 系统异常处理，比如：404,500
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @SneakyThrows
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorReturnJson defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e){
        String currentPath = request.getServletPath();

        ErrorReturnJson r = new ErrorReturnJson();
        r.setMessage(e.getMessage());
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {

            if("/".equals(currentPath)){
                r.setCode(HttpStatus.OK.value());
                r.setStatus(true);
                r.setMessage(HttpStatus.OK.name());
                return r;
            }

            r.setCode(HttpStatus.NOT_FOUND.value());
            //标记返回为404错误
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }else if (e instanceof BusinessException) {
            r.setCode(HttpStatus.NOT_IMPLEMENTED.value());
            //标记返回为501错误
            response.setStatus(HttpStatus.NOT_IMPLEMENTED.value());
        }else if (e instanceof JpowerException) {
            r.setCode(((JpowerException) e).getCode());
            //标记返回为指定错误
            response.setStatus(((JpowerException) e).getCode());
        } else {
            r.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            //标记返回为500错误
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        r.setStatus(false);
        return r;
    }

}
