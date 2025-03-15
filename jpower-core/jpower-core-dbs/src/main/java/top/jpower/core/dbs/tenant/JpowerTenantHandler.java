package top.jpower.core.dbs.tenant;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.SmartInitializingSingleton;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.utils.Fc;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JpowerTenantHandler
 * @Description TODO 租户实现逻辑(基于表字段)
 * @Author 郭丁志
 * @Date 2020-10-14 21:07
 * @Version 2.0
 */
@Data
@AllArgsConstructor
public class JpowerTenantHandler implements TenantLineHandler, SmartInitializingSingleton {

    private final JpowerTenantProperties properties;
    private final UserConfig userConfig;
    private final List<String> tenantTableList = new ArrayList<>();
    /** 租户表 **/
    private final String TENANT_TABLE = "tb_core_tenant";

    @Override
    public Expression getTenantId() {
        return new StringValue(userConfig.queryUser().getTenantCode());
    }

    @Override
    public String getTenantIdColumn() {
        return properties.getColumn();
    }

    /**
     * todo 超级用户新增的时候需要自己设置租户，不设置的情况下走数据库设置的默认值
     * tip: 这块想解决需要自己写租户拦截器，等以后再优化，暂时让数据库兜底
     *
     * @author mr.g
     * @param tableName
     * @return
     **/
    @Override
    public boolean ignoreTable(String tableName) {
        return !tenantTableList.contains(tableName)
                // 或者登录用户没有租户标识的  todo 这里主要考虑到定时任务等情况启动的线程没有登录信息所以可以自己控制租户，但是这样可能存在登录用户（未登录）没有租户标识的也能查所有租户数据，暂时想不到什么好的解决方案，暂时交给鉴权控制模块只要是登录用户就必须有租户标识
                || Fc.isBlank(userConfig.queryUser().getTenantCode())
                // 超级用户不需要加租户过滤
                || userConfig.queryUser().isRoot()
                ;
    }

    @Override
    public void afterSingletonsInstantiated() {
        properties.getExcludeTables().add(TENANT_TABLE);

        List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
        tableInfos.forEach(tableInfo -> {
            if (!properties.getExcludeTables().contains(tableInfo.getTableName())){
                tableInfo.getFieldList().stream()
                        .filter(tableFieldInfo -> Fc.equals(tableFieldInfo.getColumn(), properties.getColumn()))
                        .forEach(tableFieldInfo -> tenantTableList.add(tableInfo.getTableName()));
            }
        });
    }
}
