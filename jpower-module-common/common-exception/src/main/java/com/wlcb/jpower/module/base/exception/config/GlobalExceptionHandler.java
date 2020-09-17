package com.wlcb.jpower.module.base.exception.config;

import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.exception.JpowerException;
import com.wlcb.jpower.module.base.vo.ErrorReturnJson;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO
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
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorReturnJson defaultErrorHandler(HttpServletRequest request, Exception e) {
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
        }else if (e instanceof BusinessException) {
            r.setCode(HttpStatus.NOT_IMPLEMENTED.value());
        }else if (e instanceof JpowerException) {
            r.setCode(((JpowerException) e).getCode());
        } else {
            r.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        r.setStatus(false);

        return r;
    }

}
