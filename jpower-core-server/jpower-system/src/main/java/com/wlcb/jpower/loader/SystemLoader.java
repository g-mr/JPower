package com.wlcb.jpower.loader;

import com.wlcb.jpower.service.params.CoreParamService;
import com.wlcb.jpower.service.role.CoreRolefunctionService;
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
    private CoreRolefunctionService coreRolefunctionService;

    @Override
    public void run(String... args) {
//        try {
//            log.info("系统参数初始化...");
//            paramService.effectAll();
//            log.info("系统参数加载完成...");
//        }catch (Exception e){
//            log.warn("初始化系统参数异常：{}",e.getMessage());
//        }

        try {
            log.info("缓存匿名用户权限...");
            coreRolefunctionService.cacheAnonymous();
            log.info("缓存匿名用户权限完成...");
        }catch (Exception e){
            log.warn("初始化匿名用户权限异常：{}",e.getMessage());
        }

    }

}