package top.jpower.core.exception.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.utils.ExceptionUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.exception.enums.constants.LogConstant;
import top.jpower.core.exception.model.ErrorLogDto;
import top.jpower.core.exception.model.OperateLogDto;

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
	void saveOperateLog(OperateLogDto operateLog);

	/**
	 * 保存错误日志
	 * @author mr.g
	 * @param errorLog
	 * @return top.jpower.jpower.module.base.vo.ResponseData<java.lang.Boolean>
	 */
	void saveErrorLog(ErrorLogDto errorLog);

}
