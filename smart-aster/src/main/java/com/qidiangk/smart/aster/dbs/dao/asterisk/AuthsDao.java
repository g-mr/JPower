package com.qidiangk.smart.aster.dbs.dao.asterisk;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.aster.dbs.dao.asterisk.mapper.AuthsMapper;
import com.qidiangk.smart.aster.dbs.entity.asterisk.AuthsDO;

/**
 *  Mapper
 *
 * @author 芋道源码
 */
@Repository
public class AuthsDao extends JpowerServiceImpl<AuthsMapper, AuthsDO> {
}
