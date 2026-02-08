package top.jpower.jpower.feign;

import org.springframework.stereotype.Component;
import top.jpower.core.util.rsp.R;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ParamsClientFallback
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-09-01 15:31
 * @Version 1.0
 */
@Component
public class DictClientFallback implements DictClient {

    @Override
    public R<List<Map<String,Object>>> queryDictByType(String dictTypeCode) {
        return R.fail("查询失败");
    }
}
