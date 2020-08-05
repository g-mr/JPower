package com.wlcb.jpower;

import com.wlcb.jpower.module.base.JpowerApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName SpringBootStartApplication
 * @Description TODO 同步模块入口
 * @Author 郭丁志
 * @Date 2020-02-24 18:41
 * @Version 1.0
 */
@SpringBootApplication
@EnableTransactionManagement
public class SyncStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run("sync",SyncStartApplication.class, args);
    }

}
