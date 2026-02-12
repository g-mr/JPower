package top.jpower.system.api.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.jpower.core.util.rsp.R;

/**
 * @ClassName ParamsClientFallback
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-09-01 15:31
 * @Version 1.0
 */
@Slf4j
@Component
public class ParamsClientFallback implements ParamsClient {

    @Override
    public R<String> queryByCode(String code) {
        return R.fail("查询失败");
    }

}
