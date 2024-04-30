package top.jpower.jpower.module.base.feign;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import top.jpower.core.utils.utils.ExceptionUtil;
import top.jpower.core.utils.utils.Fc;
import top.jpower.core.utils.utils.SpringUtil;
import top.jpower.jpower.module.base.constants.LogConstant;
import top.jpower.jpower.module.base.model.ErrorLogDto;
import top.jpower.jpower.module.base.model.OperateLogDto;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.deploy.props.JpowerProperties;

/**
 * Feign接口类
 *
 * @author mr.g
 */
@Slf4j
@RequiredArgsConstructor
public class LogClient {

	private static volatile LogClient INSTANCE = null;

	private final JpowerProperties.SERVER server;

	public static LogClient getInstance(JpowerProperties.SERVER server) {
		if (INSTANCE == null) {
			synchronized (LogClient.class) {
				if (INSTANCE == null) {
					INSTANCE = new LogClient(server);

				}
			}
		}
		return INSTANCE;
	}

	/**
	 * 保存操作日志
	 * @param operateLog
	 * @return
	 */
	public void saveOperateLog(OperateLogDto operateLog){
		try {
			if (server == JpowerProperties.SERVER.BOOT){
				try {
					int count = SpringUtil.getBean(JdbcTemplate.class).update("insert into tb_log_operate" +
							"(id,server_name,server_ip,server_host,env,url,method,method_class,method_name,param,oper_ip,oper_name,oper_user_type,client_code,title,business_type,return_content,status,error_msg,record_id,content) " +
							"values " +
							"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
							Fc.randomSnowFlakeId(),operateLog.getServerName(),operateLog.getServerIp(),operateLog.getServerHost(),operateLog.getEnv(),operateLog.getUrl(),operateLog.getMethod(),operateLog.getMethodClass(),operateLog.getMethodName(),operateLog.getParam(),operateLog.getOperIp(),operateLog.getOperName(),operateLog.getOperUserType(),operateLog.getClientCode(),operateLog.getTitle(),operateLog.getBusinessType(),operateLog.getReturnContent(),operateLog.getStatus(),operateLog.getErrorMsg(),operateLog.getRecordId(),operateLog.getContent());
					if (count <= 0){
						log.error("操作日志保存失败={}",count);
					}
				}catch (Exception e){
					log.error("操作日志保存失败={}", ExceptionUtil.getStackTraceAsString(e));
				}

			}else {
				try {
					ResponseData responseData = SpringUtil.getBean(RestTemplate.class).postForObject("http://"+ LogConstant.getInstance().getJpowerLog()+"/log/saveOperateLog",operateLog,ResponseData.class);
					if (Fc.isNull(responseData) || !responseData.isStatus()){
						log.error("操作日志保存失败={}",responseData);
					}
				}catch (Exception e){
					log.error("操作日志保存失败={}", ExceptionUtil.getStackTraceAsString(e));
				}
			}
		} catch (Exception e){
			log.error("操作日志保存失败={}", e.getMessage());
		}
	}

	/**
	 * 保存错误日志
	 * @author mr.g
	 * @param errorLog
	 * @return top.jpower.jpower.module.base.vo.ResponseData<java.lang.Boolean>
	 */
	public void saveErrorLog(ErrorLogDto errorLog){
		if (Fc.equalsValue(errorLog.getServerName(), LogConstant.getInstance().getJpowerLog()) &&
			Fc.equalsValue(errorLog.getMethodClass(),"top.jpower.jpower.feign.LogClientController") &&
			Fc.equalsValue(errorLog.getMethodName(),"saveErrorLog") &&
			Fc.equalsValue(errorLog.getUrl(),"/log/saveErrorLog")){
			log.error("保存错误日志接口错误={}",errorLog);
			return;
		}

		try{
			if (server == JpowerProperties.SERVER.BOOT){
				try {
					int count = SpringUtil.getBean(JdbcTemplate.class).update("insert into tb_log_error" +
									"(id,server_name,server_ip,server_host,env,url,method,method_class,method_name,param,oper_ip,oper_name,oper_user_type,client_code,error,line_number,exception_name,message) " +
									"values " +
									"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
							Fc.randomSnowFlakeId(),errorLog.getServerName(),errorLog.getServerIp(),errorLog.getServerHost(),errorLog.getEnv(),errorLog.getUrl(),errorLog.getMethod(),errorLog.getMethodClass(),errorLog.getMethodName(),errorLog.getParam(),errorLog.getOperIp(),errorLog.getOperName(),errorLog.getOperUserType(),errorLog.getClientCode(),errorLog.getError(),errorLog.getLineNumber(),errorLog.getExceptionName(),errorLog.getMessage());
					if (count <= 0){
						log.error("错误日志保存失败={}",count);
					}
				}catch (Exception e){
					log.error("错误日志保存失败={}", ExceptionUtil.getStackTraceAsString(e));
				}
			}else {
				try {
					ResponseData responseData = SpringUtil.getBean(RestTemplate.class).postForObject("http://"+ LogConstant.getInstance().getJpowerLog()+"/log/saveErrorLog",errorLog,ResponseData.class);
					if (Fc.isNull(responseData) || !responseData.isStatus()){
						log.error("错误日志保存失败={}",responseData);
					}
				}catch (Exception e){
					log.error("错误日志保存失败={}", ExceptionUtil.getStackTraceAsString(e));
				}
			}
		} catch (Exception e){
			log.error("错误日志保存失败={}", e.getMessage());
		}
	}
}
