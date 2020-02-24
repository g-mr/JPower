package com.wlcb.module.common.service;

import com.wlcb.module.dbs.entity.base.PageBean;
import com.wlcb.module.dbs.entity.tiwen.User;
import com.wlcb.module.dbs.entity.tiwen.Wend;

import java.util.List;
import java.util.Map;

public interface TiwenService {

    PageBean<Map<String, Object>> listPage(User user, Wend wend, String startTime, String endTime);

    Integer add(User user,Wend wend);

    Map<String, Object> getByNew(String openid);
}
