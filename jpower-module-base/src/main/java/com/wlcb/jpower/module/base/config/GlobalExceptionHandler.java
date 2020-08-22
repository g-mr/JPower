package com.wlcb.jpower.module.base.config;

import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.exception.JpowerException;
import com.wlcb.jpower.module.base.vo.ErrorReturnJson;
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
    public ErrorReturnJson defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        String currentPath = request.getServletPath();

        ErrorReturnJson r = new ErrorReturnJson();
        r.setMessage(e.getMessage());
//        r.setMessage("系统异常，请联系管理员");
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {

            if("/".equals(currentPath)){
                r.setCode(0);
                r.setStatus(true);
                r.setMessage("ok");
                return r;
            }

            r.setCode(404);
        }else if (e instanceof BusinessException) {
            r.setCode(501);
//            logger.error("{}", e);
        }else if (e instanceof JpowerException) {
            r.setCode(((JpowerException) e).getCode());
//            logger.error("{}", e);
        } else {
            r.setCode(500);
//            logger.error("message={},！！！{}", e.getMessage() , e);
        }
        r.setStatus(false);
        return r;
    }

}
