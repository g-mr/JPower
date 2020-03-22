package com.wlcb.wlj.module.dbs.dao.pay;

import com.wlcb.wlj.module.dbs.dao.BaseMapper;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporateKakou;
import com.wlcb.wlj.module.dbs.entity.pay.TblWecharRed;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Component("tblWecharRedMapper")
public interface TblWecharRedMapper extends BaseMapper<TblWecharRed> {


    TblWecharRed selectByOrderNum(String orderNum);

    Integer updateByOrderNum(TblWecharRed wecharRed);
}
