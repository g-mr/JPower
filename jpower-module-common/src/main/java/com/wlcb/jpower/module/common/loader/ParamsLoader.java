package com.wlcb.jpower.module.common.loader;

import com.wlcb.jpower.module.common.service.core.params.CoreParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName CommandLineRunnerImpl
 * @Description TODO 启动执行类
 * @Author 郭丁志
 * @Date 2020-05-18 11:02
 * @Version 1.0
 */
@Slf4j
@Component
public class ParamsLoader implements CommandLineRunner {

    @Resource
    private CoreParamService paramService;

    @Override
    public void run(String... args) throws Exception {
        log.info("系统参数初始化...");

        paramService.effectAll();

        log.info("系统参数加载完成...");
    }

}