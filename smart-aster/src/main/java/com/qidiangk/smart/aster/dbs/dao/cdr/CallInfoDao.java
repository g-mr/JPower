package com.qidiangk.smart.aster.dbs.dao.cdr;


import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.aster.dbs.dao.cdr.mapper.CallInfoMapper;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallInfoDO;

@Repository
public class CallInfoDao extends JpowerServiceImpl<CallInfoMapper, CallInfoDO> {
}
