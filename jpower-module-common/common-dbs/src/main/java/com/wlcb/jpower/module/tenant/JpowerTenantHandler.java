package com.wlcb.jpower.module.tenant;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
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
 * @Version 1.0
 */
@AllArgsConstructor
public class JpowerTenantHandler implements TenantHandler, SmartInitializingSingleton {

    private final JpowerTenantProperties properties;
    private final List<String> tenantTableList = new ArrayList<>();

    @Override
    public Expression getTenantId(boolean select) {
        return new StringValue(Fc.isBlank(SecureUtil.getTenantCode())?TenantConstant.DEFAULT_TENANT_ID:SecureUtil.getTenantCode());
    }

    @Override
    public String getTenantIdColumn() {
        return properties.getColumn();
    }

    @Override
    public boolean doTableFilter(String tableName) {
        return !tenantTableList.contains(tableName) || SecureUtil.isRoot();
    }

    @Override
    public void afterSingletonsInstantiated() {
        List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
        tableInfos.forEach(tableInfo -> {
            if (!properties.getExcludeTables().contains(tableInfo.getTableName())){
                for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
                    if (Fc.equals(tableFieldInfo.getColumn(),properties.getColumn())){
                        tenantTableList.add(tableInfo.getTableName());
                        break;
                    }
                }
            }
        });
    }
}
