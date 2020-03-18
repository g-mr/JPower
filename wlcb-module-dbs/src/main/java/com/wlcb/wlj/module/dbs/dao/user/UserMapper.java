package com.wlcb.wlj.module.dbs.dao.user;


import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.dbs.dao.BaseMapper;
import com.wlcb.wlj.module.dbs.entity.user.TblUser;
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
