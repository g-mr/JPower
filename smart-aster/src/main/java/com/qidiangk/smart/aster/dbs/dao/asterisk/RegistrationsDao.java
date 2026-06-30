package com.qidiangk.smart.aster.dbs.dao.asterisk;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.aster.dbs.dao.asterisk.mapper.RegistrationsMapper;
import com.qidiangk.smart.aster.dbs.entity.asterisk.RegistrationsDO;

@Repository
public class RegistrationsDao extends JpowerServiceImpl<RegistrationsMapper, RegistrationsDO> {
}
