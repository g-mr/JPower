package top.jpower.system.api.feign;

import org.springframework.stereotype.Component;
import top.jpower.core.util.rsp.R;

import java.util.List;
import java.util.Map;

/**
 * 字典Fallback
 *
 * @author mr.g
 */
@Component
public class DictClientFallback implements DictClient {

    @Override
    public R<List<Map<String,Object>>> queryDictByType(String dictTypeCode) {
        return R.fail("查询失败");
    }
}
