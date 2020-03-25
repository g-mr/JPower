package com.wlcb.ylth.module.common.service.tiwen;

import com.wlcb.ylth.module.dbs.entity.base.PageBean;
import com.wlcb.ylth.module.dbs.entity.tiwen.User;
import com.wlcb.ylth.module.dbs.entity.tiwen.Wend;

import java.util.Map;

public interface TiwenService {

    PageBean<Map<String, Object>> listPage(User user, Wend wend, String startTime, String endTime);

    Integer add(User user,Wend wend);

    Map<String, Object> getByNew(String openid);
}
