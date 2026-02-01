package top.jpower.core.boot.transaction;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName ClientProperties
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 22:22
 * @Version 1.0
 */
@Data
@ConfigurationProperties("jpower.transaction")
public class TransactionProperties {

    /** 是否开事务 **/
    private Boolean auto = true;

    /** 事务方法扫描配置 todo 如果不继承dbs会报错,回头需要解决 **/
    private String execution = "execution(* top.jpower.core.dbs.service.BaseService+.*(..))";
}
