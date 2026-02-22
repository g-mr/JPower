package top.jpower.core.boot.transaction;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 事务配置
 *
 * @author mr.g
 */
@Data
@ConfigurationProperties("jpower.transaction")
public class TransactionProperties {

    /** 是否开事务 **/
    private Boolean auto = true;

    /** 事务方法扫描配置 **/
    private String execution = "execution(* top.jpower.core.dbs.service.BaseService+.*(..))";
}
