package com.wlcb.wlj.module.common.service.tiwen;

import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.tiwen.TblSupplies;
import com.wlcb.wlj.module.dbs.entity.tiwen.User;

import java.util.Map;

public interface SuppliesService {

    PageBean<Map<String, Object>> listPage(User user, TblSupplies supplies);

    Integer add(User user, TblSupplies supplies);

    Integer update(TblSupplies supplies);

    Integer delete(String id);
}
