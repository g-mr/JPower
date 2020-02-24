package com.wlcb.web.tiwen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName SpringBootStartApplication
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-02-24 18:41
 * @Version 1.0
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.wlcb.web.tiwen.controller","com.wlcb.module.common.page","com.wlcb.module.common.service","com.wlcb.module.dbs.dao","com.wlcb.module.base"})
@MapperScan(basePackages = "com.wlcb.module.dbs.dao")
public class SpringBootStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStartApplication.class, args);
    }

}
