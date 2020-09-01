package com.wlcb.jpower.module.support;

import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DictResult
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/9/1 0001 22:30
 * @Version 1.0
 */
@Configuration
public interface DictResult {

    List<Map<String,Object>> queryDictList(List<String> dictTypeNames);

}
