package top.jpower.system.api.feign;

import org.springframework.stereotype.Component;
import top.jpower.core.util.rsp.R;
import top.jpower.system.api.dto.SelectDTO;

import java.util.List;

/**
 * 字典Fallback
 *
 * @author mr.g
 */
@Component
public class DictClientFallback implements DictClient {

    @Override
    public R<List<SelectDTO>> queryDictByType(String dictTypeCode) {
        return R.fail("查询失败");
    }
}
