package com.wlcb.ylth.module.dbs.dao.corporate;

import com.wlcb.ylth.module.dbs.dao.BaseMapper;
import com.wlcb.ylth.module.dbs.entity.corporate.TblCsrrgCorporateReview;
import org.springframework.stereotype.Component;

/**
 * @author mr.gmac
 */
@Component("corporateReviewMapper")
public interface CorporateReviewMapper extends BaseMapper<TblCsrrgCorporateReview> {


    Integer updateStatus(String id, Integer status, String reason);
}
