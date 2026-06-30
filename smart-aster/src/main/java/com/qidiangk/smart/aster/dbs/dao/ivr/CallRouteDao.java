package com.qidiangk.smart.aster.dbs.dao.ivr;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.aster.dbs.dao.ivr.mapper.CallRouteMapper;
import com.qidiangk.smart.aster.dbs.entity.ivr.CallRouteDO;

/**
 * @author mr.g
 */
@Repository
public class CallRouteDao extends JpowerServiceImpl<CallRouteMapper, CallRouteDO> {
}
