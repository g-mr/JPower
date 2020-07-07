package com.wlcb.jpower.module.dbs.dao.user;

import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.module.dbs.dao.user.mapper.UserMapper;
import com.wlcb.jpower.module.dbs.entity.user.TblUser;
import org.springframework.stereotype.Repository;

/**
 * @ClassName SyncDao
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-03 15:19
 * @Version 1.0
 */
@Repository
public class UserDao extends JpowerServiceImpl<UserMapper, TblUser> {
}
