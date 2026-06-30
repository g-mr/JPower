package com.qidiangk.smart.aster.dbs.dao.cdr;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.aster.dbs.dao.cdr.mapper.CallTransferInfoMapper;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallTransferInfoDO;

/**
 * @author mr.g
 */
@Repository
public class CallTransferInfoDao extends JpowerServiceImpl<CallTransferInfoMapper, CallTransferInfoDO> {
}
