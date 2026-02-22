package top.jpower.core.feign.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.util.constants.StringPool;

/**
 * 设置流控 来源
 *
 * @author mr.g
 */
public class OriginParserHandler implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        try {
            return ShieldUtil.getClientCodeFromHeader();
        }catch (Exception e){
            return StringPool.EMPTY;
        }

    }
}
