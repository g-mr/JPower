package top.jpower.core.exception.client;

import top.jpower.core.exception.model.ErrorLogDTO;
import top.jpower.core.exception.model.OperateLogDTO;

/**
 * Feign接口类
 *
 * @author mr.g
 */
public interface LogClient {

	/**
	 * 保存操作日志
	 * @param operateLog
	 * @return
	 */
	void saveOperateLog(OperateLogDTO operateLog);

	/**
	 * 保存错误日志
	 * @author mr.g
	 * @param errorLog
	 * @return top.jpower.jpower.module.base.vo.ResponseData<java.lang.Boolean>
	 */
	void saveErrorLog(ErrorLogDTO errorLog);

}
