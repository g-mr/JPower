package com.wlcb.jpower;

import com.wlcb.jpower.module.common.deploy.JpowerApplication;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName AdminStartApplication
 * @Description TODO swagger启动入口
 * @Author 郭丁志
 * @Date 2020-02-24 18:41
 * @Version 1.0
 */
@SpringBootApplication
public class DocStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_DOC,DocStartApplication.class,args);
    }

}
