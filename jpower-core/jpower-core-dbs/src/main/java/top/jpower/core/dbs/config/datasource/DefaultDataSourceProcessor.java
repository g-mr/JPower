package top.jpower.core.dbs.config.datasource;

import com.mybatisflex.core.datasource.processor.DataSourceProcessor;
import com.mybatisflex.core.util.StringUtil;
import com.mybatisflex.spring.boot.MybatisFlexProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import top.jpower.core.util.utils.Fc;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 获取默认数据源
 * <p>
 *     如果存在默认数据源就取默认数据源，否则取master命名的数据源，都没有取取第一个数据源
 * </p>
 * @author mr.g
 */
@RequiredArgsConstructor
public class DefaultDataSourceProcessor implements DataSourceProcessor, Ordered {

    private static final String KEY = "@master@";
    private static final String MASTER_DATASOURCE_KEY = "master";

    private final MybatisFlexProperties mybatisFlexProperties;

    @Override
    public String process(String dataSourceKey, Object targetOrProxy, Method method, Object[] arguments) {
        if (StringUtil.noText(dataSourceKey)) {
            return null;
        }

        if (Fc.notEqualsValue(dataSourceKey, KEY)) {
            return null;
        }

        if (Fc.isNotBlank(mybatisFlexProperties.getDefaultDatasourceKey())){
            return mybatisFlexProperties.getDefaultDatasourceKey();
        }

        Map<String, Map<String, String>> datasource = mybatisFlexProperties.getDatasource();

        if (Fc.isEmpty(datasource)){
            return null;
        }

        if (datasource.containsKey(MASTER_DATASOURCE_KEY)){
            return MASTER_DATASOURCE_KEY;
        }

        return datasource.keySet().stream().findFirst().orElse(null);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
