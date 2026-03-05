package top.jpower.log.controller.api;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.exception.model.ErrorLogDTO;
import top.jpower.core.exception.model.OperateLogDTO;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.log.dbs.entity.LogError;
import top.jpower.log.dbs.entity.LogOperate;
import top.jpower.log.service.ErrorLogService;
import top.jpower.log.service.OperateLogService;

/**
 * @author mr.g
 */
@Hidden
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogClientController{

    private final ErrorLogService errorLogService;
    private final OperateLogService operateLogService;

    /**
     * 保存操作日志
     * @param operateLog 操作日志
     */
    @PostMapping("/saveOperateLog")
    public R<Boolean> saveOperateLog(@RequestBody OperateLogDTO operateLog){
        LogOperate logOperate = BeanUtil.copyProperties(operateLog, LogOperate.class);
        return R.status(operateLogService.save(logOperate));
    }

    /**
     * 保存错误日志
     * @author mr.g
     * @param errorLog 错误日志
     */
    @PostMapping("/saveErrorLog")
    public R<Boolean> saveErrorLog(@RequestBody ErrorLogDTO errorLog){
        LogError logError = BeanUtil.copyProperties(errorLog, LogError.class);
        return R.status(errorLogService.save(logError));
    }
}
