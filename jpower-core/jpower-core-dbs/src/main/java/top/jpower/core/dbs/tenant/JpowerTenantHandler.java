package top.jpower.core.dbs.tenant;

import com.mybatisflex.core.tenant.TenantFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.utils.Fc;

import java.util.ArrayList;
import java.util.List;

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
    private final List<String> tenantTableList = new ArrayList<>();
    /** 租户表 **/
    private final String TENANT_TABLE = "tb_core_tenant";

    @Override
    public Object[] getTenantIds() {
        return getTenantIds(null);
    }

    @Override
    public Object[] getTenantIds(String tableName) {
        if (!tenantTableList.contains(tableName)
                || Fc.isNull(userConfig.queryUser())
                // 或者登录用户没有租户标识的  todo 这里主要考虑到定时任务等情况启动的线程没有登录信息所以可以自己控制租户，但是这样可能存在登录用户（未登录）没有租户标识的也能查所有租户数据，暂时想不到什么好的解决方案，暂时交给鉴权控制模块只要是登录用户就必须有租户标识
                || Fc.isBlank(userConfig.queryUser().getTenantCode())
                // 超级用户不需要加租户过滤
                || userConfig.queryUser().isRoot()){
            return null;
        }
        return new Object[]{userConfig.queryUser().getTenantCode()};
    }

}
