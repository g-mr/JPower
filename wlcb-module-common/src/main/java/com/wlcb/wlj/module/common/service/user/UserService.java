package com.wlcb.wlj.module.common.service.user;

import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.dbs.entity.user.User;

/**
 * @author mr.gmac
 */
public interface UserService {
    ResponseData login(User user);

    User selectByUserName(String username);
}
