package com.wlcb.jpower.module.base.feign;

import com.wlcb.jpower.module.base.model.ErrorLogDto;
import com.wlcb.jpower.module.base.model.OperateLogDto;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Feign接口类
 *
 * @author mr.g
 */
@Slf4j
@Service
@AllArgsConstructor
public class LogClient {

	private RestTemplate restTemplate;

	/**
	 * 保存操作日志
	 * @param operateLog
	 * @return
	 */
	public void saveOperateLog(OperateLogDto operateLog){
		try {
			ResponseData responseData = restTemplate.postForObject("http://"+ AppConstant.JPOWER_LOG+"/log/saveOperateLog",operateLog,ResponseData.class);
			if (Fc.isNull(responseData) || !responseData.isStatus()){
				log.error("操作日志保存失败={}",responseData);
			}
		} catch (Exception e){
			log.error("操作日志保存失败={}", e.getMessage());
		}
	}

	/**
	 * 保存错误日志
	 * @author mr.g
	 * @param errorLog
	 * @return com.wlcb.jpower.module.base.vo.ResponseData<java.lang.Boolean>
	 */
	public void saveErrorLog(ErrorLogDto errorLog){
		if (Fc.equalsValue(errorLog.getServerName(),AppConstant.JPOWER_LOG) &&
			Fc.equalsValue(errorLog.getMethodClass(),"com.wlcb.jpower.feign.LogClientController") &&
			Fc.equalsValue(errorLog.getMethodName(),"saveErrorLog") &&
			Fc.equalsValue(errorLog.getUrl(),"/log/saveErrorLog")){
			log.error("保存错误日志接口错误={}",errorLog);
			return;
		}

		try{
			ResponseData responseData = restTemplate.postForObject("http://"+ AppConstant.JPOWER_LOG+"/log/saveErrorLog",errorLog,ResponseData.class);
			if (Fc.isNull(responseData) || !responseData.isStatus()){
				log.error("操作日志保存失败={}",responseData);
			}
		}catch (Exception e){
			log.error("操作日志保存失败={}", e.getMessage());
		}
	}
}
