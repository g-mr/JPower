package com.wlcb.jpower.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.StringPool;

/**
 * @ClassName UrlCleanerHandler
 * @Description TODO 不做流控的URL去除
 * @Author 郭丁志
 * @Date 2020/9/13 0013 0:17
 * @Version 1.0
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
