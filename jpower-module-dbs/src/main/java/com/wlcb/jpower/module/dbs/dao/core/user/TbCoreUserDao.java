package com.wlcb.jpower.module.dbs.dao.core.user;


import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreUserMapper;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import org.springframework.stereotype.Repository;

/**
 * @author mr.gmac
 */
@Repository
public class TbCoreUserDao extends JpowerServiceImpl<TbCoreUserMapper, TbCoreUser> {
}
