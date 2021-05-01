/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
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
 * @author jiang
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
