package com.wlcb.ylth.module.dbs.dao.pay;

import com.wlcb.ylth.module.dbs.dao.BaseMapper;
import com.wlcb.ylth.module.dbs.entity.pay.TblWecharRed;
import org.springframework.stereotype.Component;

/**
 * @author mr.gmac
 */
@Component("tblWecharRedMapper")
public interface TblWecharRedMapper extends BaseMapper<TblWecharRed> {


    TblWecharRed selectByOrderNum(String orderNum);

    Integer updateByOrderNum(TblWecharRed wecharRed);
}
