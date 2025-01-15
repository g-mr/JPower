package top.jpower.jpower.module.tenant;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.SmartInitializingSingleton;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.WebUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JpowerTenantHandler
 * @Description TODO 租户实现逻辑(基于表字段)
 * @Author 郭丁志
 * @Date 2020-10-14 21:07
 * @Version 2.0
 */
@AllArgsConstructor
public class JpowerTenantHandler implements TenantLineHandler, SmartInitializingSingleton {

    private final JpowerTenantProperties properties;
    private final UserConfig userConfig;
    private final List<String> tenantTableList = new ArrayList<>();
    /** 租户表 **/
    private final String TENANT_TABLE = "tb_core_tenant";

    @Override
    public Expression getTenantId() {
        return new StringValue(Fc.isBlank(userConfig.queryUser().getTenantCode())?TenantConstant.DEFAULT_TENANT_CODE: userConfig.queryUser().getTenantCode());
    }

    @Override
    public String getTenantIdColumn() {
        return properties.getColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 在表中不存在租户字段的
        return !tenantTableList.contains(tableName)
                // 获取不到request的（例如：多线程、定时任务等）情况下不做多租户过滤
                || Fc.isNull(WebUtil.getRequest())
                // 超级用户不需要加租户过滤
                || userConfig.queryUser().isRoot()
                // 或者登录用户没有租户标识的
                || Fc.isBlank(userConfig.queryUser().getTenantCode())
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
