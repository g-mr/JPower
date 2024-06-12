package top.jpower.core.feign.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import top.jpower.core.util.constants.StringPool;
import top.jpower.jpower.module.common.utils.ShieldUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName OriginParserHandler
 * @Description TODO 设置流控 来源
 * @Author 郭丁志
 * @Date 2020/9/13 0013 0:09
 * @Version 1.0
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
