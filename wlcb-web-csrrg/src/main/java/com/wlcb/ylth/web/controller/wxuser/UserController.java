package com.wlcb.ylth.web.controller.wxuser;

import com.wlcb.ylth.module.base.vo.ResponseData;
import com.wlcb.ylth.module.common.service.user.WxUserService;
import com.wlcb.ylth.module.common.utils.ReturnJsonUtil;
import com.wlcb.ylth.module.dbs.entity.user.TblZhengwuUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName PayController
 * @Description TODO 红包入口
 * @Author 郭丁志
 * @Date 2020-03-20 01:13
 * @Version 1.0
 */
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private WxUserService wxUserService;

    @RequestMapping(value = "/wxuser", method = RequestMethod.GET)
    public ResponseData wxuser(@RequestHeader(value = "openid", required = true) String openid,
                         @RequestParam(value = "kid", required = false) Integer kid) {

        TblZhengwuUser zhengwuUser = wxUserService.selectWxuser(openid, kid);

        if (zhengwuUser != null){
            return ReturnJsonUtil.printJson(200,"获取用户信息成功",zhengwuUser,true);
        }
        return ReturnJsonUtil.printJson(400,"没有找到该用户的信息",false);
    }
}
