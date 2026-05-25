package top.jpower.core.dbs.tenant;

import com.mybatisflex.core.tenant.TenantFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.util.utils.Fc;

/**
 * 多租户处理
 *
 * @author mr.g
 */
@Data
@Slf4j
@AllArgsConstructor
public class JpowerTenantHandler implements TenantFactory {

    private final JpowerTenantProperties properties;

    @Override
    @Deprecated
    public Object[] getTenantIds() {
        return getTenantIds(null);
    }

    @Override
    public Object[] getTenantIds(String tableName) {
        if (properties.getExcludeTables().contains(tableName) || TenantContextHolder.getTenantSkip()){
            log.debug("忽略租户过滤");
            return null;
        }
        String tenantCode = TenantContextHolder.getTenantCode();
        log.debug("当前租户为 >> {}", tenantCode);
        if (Fc.isBlank(tenantCode)) {
            // 租户为空代表不进行租户过滤
            return null;
        }
        return new Object[]{tenantCode};
    }

}
