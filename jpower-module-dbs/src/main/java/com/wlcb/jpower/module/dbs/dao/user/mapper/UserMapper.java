package com.wlcb.jpower.module.dbs.dao.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlcb.jpower.module.dbs.entity.user.TblUser;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("tblUserMapper")
public interface UserMapper extends BaseMapper<TblUser> {

    TblUser selectByUser(String username);

    TblUser selectByPhone(String phone);

    List<String> selectAllRole();

    List<String> selectMenuByRole(int role);

    Integer updatePassword(TblUser user);
}
