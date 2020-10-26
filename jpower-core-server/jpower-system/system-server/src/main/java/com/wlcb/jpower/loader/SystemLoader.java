package com.wlcb.jpower.loader;

import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.service.dict.CoreDictTypeService;
import com.wlcb.jpower.service.params.CoreParamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @ClassName CommandLineRunnerImpl
 * @Description TODO 启动执行类
 * @Author 郭丁志
 * @Date 2020-05-18 11:02
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class SystemLoader implements CommandLineRunner {

    private CoreParamService paramService;
    private CoreDictTypeService dictTypeService;
    private RedisUtil redisUtil;

    @Override
    public void run(String... args) throws Exception {
        log.warn("系统参数初始化...");
        paramService.effectAll();
        log.warn("系统参数加载完成...");
    }

}