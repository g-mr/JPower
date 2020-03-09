package com.wlcb.wlj.module.common.service.tiwen.impl;

import com.github.pagehelper.PageHelper;
import com.wlcb.wlj.module.common.page.PaginationContext;
import com.wlcb.wlj.module.common.service.tiwen.SuppliesService;
import com.wlcb.wlj.module.common.utils.UUIDUtil;
import com.wlcb.wlj.module.dbs.dao.tiwen.TblSuppliesMapper;
import com.wlcb.wlj.module.dbs.dao.tiwen.UserMapper;
import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.tiwen.TblSupplies;
import com.wlcb.wlj.module.dbs.entity.tiwen.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("suppliesService")
public class SuppliesServiceImpl implements SuppliesService {

    @Autowired
    private TblSuppliesMapper suppliesMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageBean<Map<String, Object>> listPage(User user, TblSupplies supplies){
        PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());
        List<Map<String, Object>> list = suppliesMapper.listDetail(user, supplies);
        return new PageBean<>(list);
    }

    @Override
    public Integer add(User user, TblSupplies supplies) {
        Integer count = userMapper.inster(user);

        supplies.setId(UUIDUtil.getUUID());
        supplies.setUpdateUser(user.getName());
        return suppliesMapper.inster(supplies);
    }

    @Override
    public Integer update(TblSupplies supplies) {
        return suppliesMapper.updateByPrimaryKey(supplies);
    }

    @Override
    public Integer delete(String id) {
        return suppliesMapper.deleteById(id);
    }

}
