package top.jpower.core.exception.handler;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.jpower.core.exception.config.UserConfig;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.listener.ErrorLogEvent;
import top.jpower.core.exception.model.ErrorLogDto;
import top.jpower.core.exception.utils.FieldCompletionUtil;
import top.jpower.core.exception.vo.ErrorReturnJson;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常获取
 *
 * @author mr.g
 **/
@Slf4j
@RestControllerAdvice
public class JpowerExceptionHandler {

    @Autowired(required = false)
    private UserConfig userConfig;

    private static final String ROOT_PACKAGE;

    static {
        ROOT_PACKAGE = ClassUtil.getPackage(ClassUtil.getMainClass());
    }

    /**
     * 404 NOT_FOUND
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorReturnJson handlerNotFoundException(NoHandlerFoundException e) {
        String currentPath = WebUtil.getRequest().getServletPath();
        ErrorReturnJson r = new ErrorReturnJson();
        r.setMessage(e.getMessage());
        if(StringPool.SLASH.equals(currentPath)){
            r.setCode(HttpStatus.OK.value());
            r.setStatus(true);
            r.setMessage(HttpStatus.OK.name());
            return r;
        }
        r.setCode(HttpStatus.NOT_FOUND.value());
        r.setStatus(false);
        return r;
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public ErrorReturnJson methodArgumentNotValidHandler(BindException e){

        ErrorReturnJson r = new ErrorReturnJson();
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (Fc.notNull(fieldError)){
            r.setMessage(fieldError.getDefaultMessage());
        }
        r.setCode(JpowerError.Arg.getCode());
        r.setStatus(false);

        return r;
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public void handleException(AsyncRequestTimeoutException e) {
        log.error("CControlAdvice.handleException ex={}", e.getMessage());
        ErrorReturnJson r = new ErrorReturnJson();
        r.setCode(500);
        r.setStatus(false);
        r.setMessage(e.getLocalizedMessage());
    }

    /**
     * 系统异常处理，比如：404,500
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ErrorReturnJson defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e){

        ErrorReturnJson r = new ErrorReturnJson();
        r.setMessage(ExceptionUtil.unwrap(e).getMessage());
        if (e instanceof BusinessException) {
            r.setCode(HttpStatus.NOT_IMPLEMENTED.value());
        }else if (e instanceof JpowerException) {
            r.setCode(((JpowerException) e).getCode());
        } else {
            r.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            //标记返回为500错误
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

            createLog(request, e);
            log.error("运行异常,异常信息===>>{}{}", StringPool.NEWLINE, ExceptionUtil.getStackTraceAsString(e));

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
        if (Fc.notNull(userConfig)){
            FieldCompletionUtil.userInfo(errorLog, userConfig.queryUser());
        }

        StackTraceElement element = getStackTrace(e.getStackTrace());

        errorLog.setMessage(e.getMessage());
        errorLog.setMethodClass(element.getClassName());
        errorLog.setMethodName(element.getMethodName());
        errorLog.setLineNumber(element.getLineNumber());
        errorLog.setError(ExceptionUtil.getStackTraceAsString(e));
        errorLog.setExceptionName(e.getClass().getName());
        SpringUtil.publishEvent(new ErrorLogEvent(errorLog));
    }

    private StackTraceElement getStackTrace(StackTraceElement[] elements){
        for (StackTraceElement element : elements) {
            if (StrUtil.startWith(element.getClassName(), ROOT_PACKAGE)){
                return element;
            }
        }
        return elements[0];
    }

}
