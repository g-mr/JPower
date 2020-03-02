package com.wlcb.wlj.module.common.service.corporate;

import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporate;

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
    Integer countCorporateByRecord();

}
