package com.wlcb.jpower;

import com.wlcb.jpower.module.common.deploy.JpowerApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName SpringBootStartApplication
 * @Description TODO 文件管理入口
 * @Author 郭丁志
 * @Date 2020-02-24 18:41
 * @Version 1.0
 */
@SpringBootApplication
@EnableTransactionManagement
public class FileStartApplication {
    public static void main(String[] args) {
        JpowerApplication.run("admin-file",FileStartApplication.class,args);
    }

}
