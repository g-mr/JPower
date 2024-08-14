package top.jpower.core.exception.operate;

import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.EnumUtil;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jpower.core.exception.listener.OperateLogEvent;
import top.jpower.core.exception.model.OperateLogDto;
import top.jpower.core.exception.model.UserDto;
import top.jpower.core.exception.utils.FieldCompletionUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.util.utils.WebUtil;

/**
 * @author mr.g
 * @date 2023/6/11 12:00 PM
 */
public class OperateLog {

    private final Logger log;

    private final UserDto userDto;

    private static final SimpleCache<Class<?>, OperateLog> LOG_CACHE = new SimpleCache<>();

    private OperateLog(Class<?> clazz, UserDto userDto){
        this.log = LoggerFactory.getLogger(clazz);
        this.userDto = userDto;
    }

    public static OperateLog SINGLETON(Class<?> clazz, UserDto userDto){
        return LOG_CACHE.get(clazz, ()->new OperateLog(clazz, userDto));
    }

    public void info(final OperateInfo controllerLog){
        info(controllerLog, null, null, null);
    }

    public void info(final OperateInfo controllerLog, JoinPoint joinPoint, Object rvt, Exception e){

        try {
            OperateLogDto operLog = new OperateLogDto();
            StringBuilder builder = new StringBuilder("["+controllerLog.title()+"]");
            builder.append(" 记录操作日志==> ");
            // 获取当前的用户
            if (Fc.notNull(userDto)){
                builder.append(userDto.getUserName()).append("(id=").append(userDto.getUserId()).append(")");
            }

            builder.append("请求").append(WebUtil.getRequest().getRequestURI()).append("接口;");
            if (Fc.notNull(joinPoint)){
                builder.append("是否执行成功=").append(Fc.isNull(e)).append(";");
            }

            log.info(builder.toString());

            if (controllerLog.isSaveLog()){

                operLog.setStatus(top.jpower.core.exception.annotation.OperateLog.BusinessStatus.SUCCESS.ordinal());

                if (Fc.notNull(e)){
                    operLog.setStatus(top.jpower.core.exception.annotation.OperateLog.BusinessStatus.FAIL.ordinal());
                    operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
                }

                // 设置方法名称
                String className;
                String methodName;
                if (Fc.notNull(joinPoint)){
                    className = joinPoint.getTarget().getClass().getName();
                    methodName = joinPoint.getSignature().getName();
                }else {
                    className = new Throwable().getStackTrace()[2].getClassName();;
                    methodName = new Throwable().getStackTrace()[2].getMethodName();;
                }
                operLog.setMethodClass(className);
                operLog.setMethodName(methodName);
                operLog.setReturnContent(JSON.toJSONString(rvt));
                // 设置action动作
                if (EnumUtil.equalsIgnoreCase(controllerLog.businessType(), top.jpower.core.exception.annotation.OperateLog.BusinessType.OTHER.name())
                        && Fc.isNotBlank(controllerLog.businessOther())){
                    operLog.setBusinessType(controllerLog.businessOther());
                }else {
                    operLog.setBusinessType(controllerLog.businessType().name());
                }
                // 设置标题
                operLog.setTitle(controllerLog.title());

                // 处理设置注解上的参数
                if (controllerLog.isSaveRequestData()){
                    FieldCompletionUtil.requestInfo(operLog, WebUtil.getRequest());
                }

                operLog.setRecordId(controllerLog.recordId());
                operLog.setContent(controllerLog.content());

                FieldCompletionUtil.userInfo(operLog, userDto);

                SpringUtil.publishEvent(new OperateLogEvent(operLog));
            }
        } catch (Exception exp) {
            log.error("操作日志记录异常: {}", exp.getMessage());
        }

    }

}
