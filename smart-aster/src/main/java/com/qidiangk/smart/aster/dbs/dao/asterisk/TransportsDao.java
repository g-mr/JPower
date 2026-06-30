package com.qidiangk.smart.aster.dbs.dao.asterisk;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.aster.dbs.dao.asterisk.mapper.TransportsMapper;
import com.qidiangk.smart.aster.dbs.entity.asterisk.TransportsDO;

/**
 * @author mr.g
 */
@Repository
public class TransportsDao extends JpowerServiceImpl<TransportsMapper, TransportsDO> {
}
