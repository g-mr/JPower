package top.jpower.system.api.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import top.jpower.core.util.rsp.R;

/**
 * 字典Fallback
 *
 * @author mr.g
 */
@Slf4j
@Component
public class DictClientFallback implements FallbackFactory<DictClient> {

	@Override
	public DictClient create(Throwable cause) {
		return dictTypeCode -> {
			log.error("查询字典失败", cause);
			return R.fail("查询失败");
		};
	}
}
