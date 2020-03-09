package com.wlcb.wlj.module.common.service.tiwen.impl;

import com.github.pagehelper.PageHelper;
import com.wlcb.wlj.module.common.page.PaginationContext;
import com.wlcb.wlj.module.common.service.tiwen.TiwenService;
import com.wlcb.wlj.module.common.utils.UUIDUtil;
import com.wlcb.wlj.module.dbs.dao.tiwen.UserMapper;
import com.wlcb.wlj.module.dbs.dao.tiwen.WendMapper;
import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.tiwen.User;
import com.wlcb.wlj.module.dbs.entity.tiwen.Wend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("tiwenService")
public class TiwenServiceImpl implements TiwenService {

    @Autowired
    private WendMapper tiwenMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageBean<Map<String, Object>> listPage(User user, Wend wend, String startTime, String endTime) {
        PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());
        List<Map<String, Object>> list = tiwenMapper.listDetail(user, wend, startTime,endTime);
        return new PageBean<>(list);
    }

    @Override
    public Integer add(User user,Wend wend) {

        Integer count = userMapper.inster(user);

        wend.setUuid(UUIDUtil.getUUID());
        wend.setInsertAt(new Date());
        return tiwenMapper.inster(wend);
    }

    @Override
    public Map<String, Object> getByNew(String openid) {
        return tiwenMapper.queryNew(openid);
    }
}
