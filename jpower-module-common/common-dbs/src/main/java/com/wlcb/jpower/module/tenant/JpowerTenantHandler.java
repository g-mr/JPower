package com.wlcb.jpower.module.tenant;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ShieldUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.SmartInitializingSingleton;

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
    private final List<String> tenantTableList = new ArrayList<>();
    /** 租户表 **/
    private final String TENANT_TABLE = "tb_core_tenant";

    @Override
    public Expression getTenantId() {
        return new StringValue(Fc.isBlank(ShieldUtil.getTenantCode())?TenantConstant.DEFAULT_TENANT_CODE: ShieldUtil.getTenantCode());
    }

    @Override
    public String getTenantIdColumn() {
        return properties.getColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 在表中不存在tenant_code字段的、超级用户登陆的、获取不到request的（例如：多线程、定时任务等）情况下不做多租户过滤
        return !tenantTableList.contains(tableName) || Fc.isNull(WebUtil.getRequest()) || ShieldUtil.isRoot();
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
