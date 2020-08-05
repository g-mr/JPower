package com.wlcb.jpower.module.common.interceptor;

import java.util.List;

/**
 * @author 郭丁志
 * @Description //TODO 鉴权去除的URL提供的接口
 * @date 0:22 2020/8/6 0006
 */
public interface AuthExculdesUrl {

    /**
     * @author 郭丁志
     * @Description //TODO 所有需要不走鉴权的接口都需要实现该接口这个方法
     * @date 0:23 2020/8/6 0006
     * @param exculdesUrls
     * @return void
     */
    void addExculdesUrl(List<String> exculdesUrls);

}
