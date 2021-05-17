package com.wlcb.jpower.module.base.feign;

import com.wlcb.jpower.module.base.model.ErrorLogDto;
import com.wlcb.jpower.module.base.model.OperateLogDto;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 日志fallback
 *
 * @author mr.g
 */
@Slf4j
@Component
public class LogClientFallback implements LogClient {

	@Override
	public ResponseData<Boolean> saveOperateLog(OperateLogDto log) {
		return ReturnJsonUtil.fail("operate log send fail...");
	}

	@Override
	public ResponseData<Boolean> saveErrorLog(ErrorLogDto errorLog) {
		return ReturnJsonUtil.fail("error log send fail...");
	}
}
