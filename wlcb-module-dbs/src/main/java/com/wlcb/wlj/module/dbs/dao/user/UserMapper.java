package com.wlcb.wlj.module.dbs.dao.user;


import com.wlcb.wlj.module.dbs.dao.BaseMapper;
import com.wlcb.wlj.module.dbs.entity.user.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author mr.gmac
 */
@Component("tblUserMapper")
public interface UserMapper extends BaseMapper<User> {

    User selectByUser(String username);

    List<String> selectAllRole();

    List<String> selectMenuByRole(int role);
}
