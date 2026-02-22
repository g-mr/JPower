package top.jpower.core.dbs.tenant;

import com.mybatisflex.core.tenant.TenantFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.utils.Fc;

/**
 * 多租户处理
 *
 * @author mr.g
 * @date 2020-10-14 20:54
 */
@Data
@AllArgsConstructor
public class JpowerTenantHandler implements TenantFactory {

    private final JpowerTenantProperties properties;
    private final UserConfig userConfig;

    @Override
    @Deprecated
    public Object[] getTenantIds() {
        return getTenantIds(null);
    }

    @Override
    public Object[] getTenantIds(String tableName) {
		// todo 等把鉴权换成springsecurity后这里改成通过TransmittableThreadLocal获取当前租户
        if (!properties.getExcludeTables().contains(tableName)
                || Fc.isNull(userConfig.queryUser())
                // 或者登录用户没有租户标识的
                || Fc.isBlank(userConfig.queryUser().getTenantCode())
                // 超级用户不需要加租户过滤
                || userConfig.queryUser().isRoot()){
            return null;
        }
        return new Object[]{userConfig.queryUser().getTenantCode()};
    }

}
