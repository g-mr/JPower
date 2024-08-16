package top.jpower.core.exception.client;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import top.jpower.core.exception.model.ErrorLogDto;
import top.jpower.core.exception.model.OperateLogDto;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;

/**
 * 日志默认处理器，保存到数据库
 *
 * @author mr.g
 */
@Slf4j
@RequiredArgsConstructor
public class JdbcLogClient implements LogClient {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 保存操作日志SQL
     **/
    private static final String OPERATE_LOG_SQL = "insert into tb_log_operate(id,server_name,server_ip,server_host,env,url,method,method_class,method_name,param,oper_ip,oper_name,oper_user_type,client_code,title,business_type,return_content,status,error_msg,record_id,content) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    /**
     * 保存错误日志SQL
     **/
    private static final String ERROR_LOG_SQL = "insert into tb_log_error(id,server_name,server_ip,server_host,env,url,method,method_class,method_name,param,oper_ip,oper_name,oper_user_type,client_code,error,line_number,exception_name,message) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    /**
     * 保存操作日志
     *
     * @param operateLog 操作日志
     * @return
     */
    @Override
    @SneakyThrows(Exception.class)
    public void saveOperateLog(OperateLogDto operateLog) {
        int count = jdbcTemplate.update(OPERATE_LOG_SQL,
                Fc.randomSnowFlakeId(),operateLog.getServerName(),operateLog.getServerIp(),operateLog.getServerHost(),operateLog.getEnv(),operateLog.getUrl(),operateLog.getMethod(),operateLog.getMethodClass(),operateLog.getMethodName(),operateLog.getParam(),operateLog.getOperIp(),operateLog.getOperName(),operateLog.getOperUserType(),operateLog.getClientCode(),operateLog.getTitle(),operateLog.getBusinessType(),operateLog.getReturnContent(),operateLog.getStatus(),operateLog.getErrorMsg(),operateLog.getRecordId(),operateLog.getContent());
        if (count <= 0){
            log.error("操作日志保存失败={}",count);
        }
    }

    /**
     * 保存错误日志
     *
     * @param errorLog 错误日志
     * @author mr.g
     */
    @Override
    @SneakyThrows(Exception.class)
    public void saveErrorLog(ErrorLogDto errorLog) {
        int count = jdbcTemplate.update(ERROR_LOG_SQL,
                Fc.randomSnowFlakeId(),errorLog.getServerName(),errorLog.getServerIp(),errorLog.getServerHost(),errorLog.getEnv(),errorLog.getUrl(),errorLog.getMethod(),errorLog.getMethodClass(),errorLog.getMethodName(),errorLog.getParam(),errorLog.getOperIp(),errorLog.getOperName(),errorLog.getOperUserType(),errorLog.getClientCode(),errorLog.getError(),errorLog.getLineNumber(),errorLog.getExceptionName(),errorLog.getMessage());
        if (count <= 0){
            log.error("错误日志保存失败={}",count);
        }
    }
}
