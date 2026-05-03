package top.jpower.core.dbs.tenant;

import com.mybatisflex.core.tenant.TenantFactory;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 多租户处理
 *
 * @author mr.g
 */
@Data
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
            return null;
        }
        return new Object[]{TenantContextHolder.getTenantCode()};
    }

}
