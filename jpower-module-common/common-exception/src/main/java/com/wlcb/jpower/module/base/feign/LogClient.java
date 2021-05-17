package com.wlcb.jpower.module.base.feign;

import com.wlcb.jpower.module.base.model.ErrorLogDto;
import com.wlcb.jpower.module.base.model.OperateLogDto;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign接口类
 *
 * @author mr.g
 */
@FeignClient(value = AppConstant.JPOWER_LOG, fallback = LogClientFallback.class, path = "/log")
public interface LogClient {

	/**
	 * 保存操作日志
	 * @param log
	 * @return
	 */
	@PostMapping("/saveOperateLog")
	ResponseData<Boolean> saveOperateLog(@RequestBody OperateLogDto log);

	/**
	 * 保存错误日志
	 * @author mr.g
	 * @param errorLog
	 * @return com.wlcb.jpower.module.base.vo.ResponseData<java.lang.Boolean>
	 */
	@PostMapping("/saveErrorLog")
	ResponseData<Boolean> saveErrorLog(ErrorLogDto errorLog);
}
