package top.jpower.core.exception.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import top.jpower.core.exception.enums.constants.LogConstant;
import top.jpower.core.exception.model.ErrorLogDto;
import top.jpower.core.exception.model.OperateLogDto;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.utils.ExceptionUtil;
import top.jpower.core.util.utils.Fc;

/**
 * 日志默认处理器，去调用jpower的接口
 *
 * @author mr.g
 */
@Slf4j
@RequiredArgsConstructor
public class RestLogClient implements LogClient {

    private final RestTemplate restTemplate;

    /**
     * 保存操作日志
     *
     * @author mr.g
     * @param operateLog 操作日志
     */
    @Override
    public void saveOperateLog(OperateLogDto operateLog) {
        try {
            ResponseData responseData = restTemplate.postForObject("http://"+ LogConstant.getInstance().getJpowerLog()+"/log/saveOperateLog", operateLog,ResponseData.class);
            if (Fc.isNull(responseData) || !responseData.isStatus()){
                log.error("操作日志保存失败={}",responseData);
            }
        }catch (Exception e){
            log.error("操作日志保存失败={}", ExceptionUtil.getStackTraceAsString(e));
        }
    }

    /**
     * 保存错误日志
     *
     * @author mr.g
     * @param errorLog 错误日志
     */
    @Override
    public void saveErrorLog(ErrorLogDto errorLog) {
        if (!isSaveError(errorLog)){
            log.error("保存错误日志接口错误={}",errorLog);
            return;
        }

        try {
            ResponseData responseData = restTemplate.postForObject("http://"+ LogConstant.getInstance().getJpowerLog()+"/log/saveErrorLog", errorLog,ResponseData.class);
            if (Fc.isNull(responseData) || !responseData.isStatus()){
                log.error("错误日志保存失败={}",responseData);
            }
        }catch (Exception e){
            log.error("错误日志保存失败={}", ExceptionUtil.getStackTraceAsString(e));
        }
    }

    public boolean isSaveError(ErrorLogDto errorLog){
        if (Fc.equalsValue(errorLog.getServerName(), LogConstant.getInstance().getJpowerLog()) &&
                Fc.equalsValue(errorLog.getMethodClass(),"top.jpower.jpower.feign.LogClientController") &&
                Fc.equalsValue(errorLog.getMethodName(),"saveErrorLog") &&
                Fc.equalsValue(errorLog.getUrl(),"/log/saveErrorLog")){
            return false;
        }
        return true;
    }
}
