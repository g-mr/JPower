package com.wlcb.wlj.module.common.service.corporate;

import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporate;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporateReview;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgLog;

import java.util.List;
import java.util.Map;

public interface CorporateService {

    List<Map<String,String>> queryEnterpriseName(String name);

    TblCsrrgCorporate selectById(String id);

    List<TblCsrrgCorporate> queryDetailByIdcard(String idcard);

    /**
     * @Author 郭丁志
     * @Description //TODO 查询有多少企业关联的了联系人
     * @Date 22:06 2020-03-02
     * @Param []
     * @return java.lang.Integer
     **/
    String countCorporateByRecord(String quxian);

    Integer countCorporateByReview(String organizationCode, String enterpriseName);

    Integer addCorporateReview(TblCsrrgCorporateReview corporateReview);

    Integer updateStatus(TblCsrrgLog log, String reason);

    PageBean<TblCsrrgCorporateReview> listPage(TblCsrrgCorporateReview corporateReview);

    String countCorporateByRework(String quxian);
}
