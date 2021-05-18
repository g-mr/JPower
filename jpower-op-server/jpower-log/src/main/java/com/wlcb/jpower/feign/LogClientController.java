package com.wlcb.jpower.feign;

import com.wlcb.jpower.dbs.entity.TbLogError;
import com.wlcb.jpower.dbs.entity.TbLogOperate;
import com.wlcb.jpower.module.base.feign.LogClient;
import com.wlcb.jpower.module.base.model.ErrorLogDto;
import com.wlcb.jpower.module.base.model.OperateLogDto;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.service.ErrorLogService;
import com.wlcb.jpower.service.OperateLogService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author mr.g
 * @Date 2021/5/1 0001 19:32
 */
@Api(tags = "保存日志")
@ApiIgnore
@RestController
@RequestMapping("/log")
@AllArgsConstructor
public class LogClientController implements LogClient {

    private ErrorLogService errorLogService;
    private OperateLogService operateLogService;

    /**
     * 保存操作日志
     * @param operateLog
     */
    @Override
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
    @Override
    @PostMapping("/saveErrorLog")
    public ResponseData<Boolean> saveErrorLog(@RequestBody ErrorLogDto errorLog){
        TbLogError logError = BeanUtil.copyProperties(errorLog, TbLogError.class);
        return ReturnJsonUtil.status(errorLogService.save(logError));
    }
}
