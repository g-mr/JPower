package top.jpower.core.exception.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.exception.enums.constants.LogConstant;
import top.jpower.core.exception.feign.LogTraceClient;
import top.jpower.core.exception.model.ErrorLogDTO;
import top.jpower.core.exception.model.OperateLogDTO;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.ExceptionUtil;
import top.jpower.core.util.utils.Fc;

/**
 * 日志处理器，去调用jpower的接口
 *
 * @author mr.g
 */
@Slf4j
@RequiredArgsConstructor
public class FeignLogClient implements LogClient {

    private final LogTraceClient logTraceClient;

    /**
     * 保存操作日志
     *
     * @author mr.g
     * @param operateLog 操作日志
     */
    @Override
    public void saveOperateLog(OperateLogDTO operateLog) {
        try {
            R<Long> r = logTraceClient.saveOperateLog(LogConstant.getInstance().getJpowerLog(), operateLog);
            if (Fc.isNull(r) || !r.isStatus()){
                log.error("操作日志保存失败={}",r);
            }
        }catch (Exception e){
            log.error("操作日志保存失败={}", ExceptionUtil.getMessage(e));
        }
    }

    /**
     * 保存错误日志
     *
     * @author mr.g
     * @param errorLog 错误日志
     */
    @Override
    public void saveErrorLog(ErrorLogDTO errorLog) {
        if (!isSaveError(errorLog)){
            log.error("保存错误日志接口错误={}",errorLog);
            return;
        }

        try {
            R<Long> r = logTraceClient.saveErrorLog(LogConstant.getInstance().getJpowerLog(), errorLog);
            if (Fc.isNull(r) || !r.isStatus()){
                log.error("错误日志保存失败={}", r);
            }
        }catch (Exception e){
            log.error("错误日志保存失败={}", ExceptionUtil.getMessage(e));
        }
    }

    public boolean isSaveError(ErrorLogDTO errorLog){
        if (Fc.equalsValue(errorLog.getServerName(), LogConstant.getInstance().getJpowerLog()) &&
                Fc.equalsValue(errorLog.getUrl(),"/log/saveErrorLog")){
            return false;
        }
        return true;
    }
}
