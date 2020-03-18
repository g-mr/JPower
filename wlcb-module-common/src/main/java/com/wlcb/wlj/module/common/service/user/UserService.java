package com.wlcb.wlj.module.common.service.user;

import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.dbs.entity.user.TblUser;

/**
 * @author mr.gmac
 */
public interface UserService {
    ResponseData login(TblUser user);

    TblUser selectByUserName(String username);

    ResponseData wxLogin(String code);

    TblUser selectByUserNameAndId(String id, String username);

    Integer updatePassword(TblUser user);
}
