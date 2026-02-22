package top.jpower.core.feign.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.web.common.UrlCleaner;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;

/**
 * 不做流控的URL去除
 *
 * @author 郭丁志
 */
public class UrlCleanerHandler implements UrlCleaner {

    @Override
    public String clean(String url) {
        if (Fc.equals(url,"/**")){
            return StringPool.EMPTY;
        }
        if (Fc.equals(url,"/error")){
            return StringPool.EMPTY;
        }
        return url;
    }

}
