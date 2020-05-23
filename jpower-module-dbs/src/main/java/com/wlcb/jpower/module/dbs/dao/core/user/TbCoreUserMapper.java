package com.wlcb.jpower.module.dbs.dao.core.user;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import org.springframework.stereotype.Component;

/**
 * @author mr.gmac
 */
@Component("tbCoreUserMapper")
public interface TbCoreUserMapper extends BaseMapper<TbCoreUser> {
}
