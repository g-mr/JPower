package top.jpower.system.api.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.jpower.core.util.rsp.R;

/**
 * 系统参数Fallback
 *
 * @author mr.g
 */
@Slf4j
@Component
public class ParamsClientFallback implements ParamsClient {

    @Override
    public R<String> queryByCode(String code) {
        return R.fail("查询失败");
    }

}
