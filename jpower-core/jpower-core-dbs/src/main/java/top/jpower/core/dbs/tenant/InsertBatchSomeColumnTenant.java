package top.jpower.core.dbs.tenant;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import top.jpower.core.util.utils.ExceptionUtil;
import top.jpower.core.util.utils.Fc;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2023/11/19 14:23
 * @description
 */
@AllArgsConstructor
@Slf4j
public class InsertBatchSomeColumnTenant implements InnerInterceptor {

    private TenantLineHandler tenantLineHandler;

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) {
        if (SqlCommandType.INSERT != ms.getSqlCommandType()) {
            return;
        }
        String methodName = ms.getId().substring(ms.getId().lastIndexOf(StringPool.DOT) + 1);
        if ("insertBatchSomeColumn".equals(methodName)) {
            if (parameter instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) parameter;
                Object object = map.getOrDefault("list", null);

                if (object instanceof List && Fc.isNotEmpty(object)){
                    doTenantValue((List) object);
                }
            }
        }
    }

    protected void doTenantValue(List list) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(list.get(0).getClass());
        if (tableInfo == null) {
            return;
        }

        if (tenantLineHandler.ignoreTable(tableInfo.getTableName())) {
            // 过滤退出执行
            return;
        }

        Field field = tableInfo.getFieldList().stream().filter(fieldInfo->Fc.equalsValue(fieldInfo.getColumn(), tenantLineHandler.getTenantIdColumn())).findFirst().map(TableFieldInfo::getField).orElse(null);

        if (Fc.notNull(field)){
            list.forEach(ob->{
                try {

                    Object val = field.get(ob);
                    if (Fc.isNull(val)){
                        field.set(ob, new StringValue(tenantLineHandler.getTenantId().toString()).getValue());
                    }

                } catch (IllegalAccessException e){
                    log.error("insertBatchSomeColumn tenant value set error:{}", ExceptionUtil.getStackTraceAsString(e));
                }
            });
        }
    }

}
