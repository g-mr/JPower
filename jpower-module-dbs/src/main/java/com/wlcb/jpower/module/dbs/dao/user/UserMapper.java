package com.wlcb.jpower.module.dbs.dao.user;


import com.wlcb.jpower.module.dbs.entity.user.TblUser;
import com.wlcb.jpower.module.dbs.dao.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("tblUserMapper")
public interface UserMapper extends BaseMapper<TblUser> {

    TblUser selectByUser(String username);

    List<String> selectAllRole();

    List<String> selectMenuByRole(int role);

    Integer updatePassword(TblUser user);
}
