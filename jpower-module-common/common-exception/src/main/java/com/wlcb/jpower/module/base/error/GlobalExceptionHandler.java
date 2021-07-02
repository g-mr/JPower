package com.wlcb.jpower.module.base.error;

import cn.hutool.core.util.StrUtil;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.exception.JpowerException;
import com.wlcb.jpower.module.base.listener.ErrorLogEvent;
import com.wlcb.jpower.module.base.model.ErrorLogDto;
import com.wlcb.jpower.module.base.utils.FieldCompletionUtil;
import com.wlcb.jpower.module.base.vo.ErrorReturnJson;
import com.wlcb.jpower.module.common.utils.ExceptionsUtil;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.dbs.config.LoginUserContext;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO 全局异常获取
 * @Author 郭丁志
 * @Date 2020-01-27 17:24
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final String JPOWER_PACKAGE = "com.wlcb.jpower";

    /**
     * 404-NOT_FOUND
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorReturnJson handlerNotFoundException(NoHandlerFoundException e)
    {String currentPath = WebUtil.getRequest().getServletPath();
        ErrorReturnJson r = new ErrorReturnJson();
        r.setMessage(e.getMessage());
        if("/".equals(currentPath)){
            r.setCode(HttpStatus.OK.value());
            r.setStatus(true);
            r.setMessage(HttpStatus.OK.name());
            return r;
        }
        r.setCode(HttpStatus.NOT_FOUND.value());
        r.setStatus(false);
        return r;
    }

    /**
     * 系统异常处理，比如：404,500
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @SneakyThrows
    @ExceptionHandler(value = Exception.class)
    public ErrorReturnJson defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e){

        ErrorReturnJson r = new ErrorReturnJson();
        r.setMessage(e.getMessage());
        if (e instanceof BusinessException) {
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

            createLog(request,e);
        }
        r.setStatus(false);
        return r;
    }

    /**
     * 错误事件发布
     * @author mr.g
     * @return void
     */
    private void createLog(HttpServletRequest request, Exception e) {
        ErrorLogDto errorLog = new ErrorLogDto();

        FieldCompletionUtil.requestInfo(errorLog, request);
        FieldCompletionUtil.userInfo(errorLog,LoginUserContext.get());

        StackTraceElement element = getStackTrace(e.getStackTrace());

        errorLog.setMessage(e.getMessage());
        errorLog.setMethodClass(element.getClassName());
        errorLog.setMethodName(element.getMethodName());
        errorLog.setLineNumber(element.getLineNumber());
        errorLog.setError(ExceptionsUtil.getStackTraceAsString(e));
        errorLog.setExceptionName(e.getClass().getName());
        SpringUtil.publishEvent(new ErrorLogEvent(errorLog));
    }

    private StackTraceElement getStackTrace(StackTraceElement[] elements){
        for (StackTraceElement element : elements) {
            if (StrUtil.startWith(element.getClassName(),JPOWER_PACKAGE)){
                return element;
            }
        }
        return elements[0];
    }
}
