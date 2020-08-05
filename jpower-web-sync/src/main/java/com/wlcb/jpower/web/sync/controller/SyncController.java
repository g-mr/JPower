package com.wlcb.jpower.web.sync.controller;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.service.sync.SyncService;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.dbs.entity.sync.ImportInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName TiwenController
 * @Description TODO 体温调用入口Controller
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("sync")
public class SyncController {

    @Resource
    private SyncService syncService;

    @RequestMapping(value = "/syncStart",method = RequestMethod.POST,produces="application/json")
    public ResponseData syncStart(HttpServletRequest request, HttpServletResponse response, ImportInfo importInfo){

        if (importInfo.getFile() ==null || importInfo.getFile().isEmpty()){
            return ReturnJsonUtil.printJson(-1,"文件不可为空",false);
        }


        String suffixName=importInfo.getFile().getOriginalFilename().substring(importInfo.getFile().getOriginalFilename().lastIndexOf(".") + 1);

        if (!StringUtils.equalsIgnoreCase("csv",suffixName)){
            return ReturnJsonUtil.printJson(-1,"文件类型不符，请上传csv文件类型",false);
        }

        return syncService.sync(importInfo);
    }

}
