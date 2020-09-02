package com.wlcb.jpower.dbs.dao;


import com.wlcb.jpower.dbs.dao.mapper.TbCoreUserMapper;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @author mr.gmac
 */
@Repository
public class TbCoreUserDao extends JpowerServiceImpl<TbCoreUserMapper, TbCoreUser> {
}
