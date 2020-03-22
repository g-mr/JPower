package com.wlcb.wlj.module.common.service.pay;

import com.github.pagehelper.PageInfo;
import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.dbs.entity.pay.TblWecharRed;

public interface PayService {

    ResponseData paymentChange(TblWecharRed wecharRed);

    ResponseData paymentChangeInfo(TblWecharRed wecharRed);

    TblWecharRed selectDetailByOrderNum(String orderNum);

    PageInfo<TblWecharRed> redList(TblWecharRed wecharRed);

    ResponseData redEnvelope(TblWecharRed wecharRed, String wishing, String actName);

    ResponseData getRedInfo(TblWecharRed wecharRed);
}
