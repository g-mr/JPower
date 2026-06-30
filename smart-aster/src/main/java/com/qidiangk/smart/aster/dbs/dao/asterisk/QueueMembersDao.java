package com.qidiangk.smart.aster.dbs.dao.asterisk;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.aster.dbs.dao.asterisk.mapper.QueueMembersMapper;
import com.qidiangk.smart.aster.dbs.entity.asterisk.QueueMembersDO;

@Repository
public class QueueMembersDao extends JpowerServiceImpl<QueueMembersMapper, QueueMembersDO> {
}
