package top.jpower.jpower.feign;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.jpower.dbs.entity.TbLogError;
import top.jpower.jpower.dbs.entity.TbLogOperate;
import top.jpower.jpower.module.base.model.ErrorLogDto;
import top.jpower.jpower.module.base.model.OperateLogDto;
import top.jpower.jpower.service.ErrorLogService;
import top.jpower.jpower.service.OperateLogService;

/**
 * @Author mr.g
 * @Date 2021/5/1 0001 19:32
 */
@Api(tags = "保存日志")
@ApiIgnore
@RestController
@RequestMapping("/log")
@AllArgsConstructor
public class LogClientController{

    private ErrorLogService errorLogService;
    private OperateLogService operateLogService;

    /**
     * 保存操作日志
     * @param operateLog
     */
    @PostMapping("/saveOperateLog")
    public ResponseData<Boolean> saveOperateLog(@RequestBody OperateLogDto operateLog){
        TbLogOperate logOperate = BeanUtil.copyProperties(operateLog, TbLogOperate.class);
        return ReturnJsonUtil.status(operateLogService.save(logOperate));
    }

    /**
     * 保存错误日志
     * @author mr.g
     * @param errorLog
     */
    @PostMapping("/saveErrorLog")
    public ResponseData<Boolean> saveErrorLog(@RequestBody ErrorLogDto errorLog){
        TbLogError logError = BeanUtil.copyProperties(errorLog, TbLogError.class);
        return ReturnJsonUtil.status(errorLogService.save(logError));
    }
}
