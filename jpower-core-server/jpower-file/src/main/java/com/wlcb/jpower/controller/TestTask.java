package com.wlcb.jpower.controller;

import com.wlcb.jpower.annotation.JpowerDelayTask;
import com.wlcb.jpower.dbs.dao.TbCoreFileDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author mr.g
 * @date 2023/7/6 5:36 PM
 */
@Service
@AllArgsConstructor
public class TestTask {

    private TbCoreFileDao fileDao;

    @JpowerDelayTask(name = "订单任务")
    public void test(String id){
        System.out.println("数量:"+fileDao.count());
        System.out.println(id);
    }

}
