package com.qidiangk.smart.aster.dbs.dao.asterisk;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.aster.dbs.dao.asterisk.mapper.ContactsMapper;
import com.qidiangk.smart.aster.dbs.entity.asterisk.ContactsDO;

@Repository
public class ContactsDao extends JpowerServiceImpl<ContactsMapper, ContactsDO> {
}