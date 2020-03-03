package com.wlcb.wlj.module.dbs.dao.corporate;

import com.wlcb.wlj.module.dbs.dao.BaseMapper;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporate;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgRecord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Component("corporateMapper")
public interface CorporateMapper extends BaseMapper<TblCsrrgCorporate> {

    List<Map<String,String>> selectName(String name);

    TblCsrrgCorporate selectDetailByLegal(TblCsrrgRecord record);

    List<TblCsrrgCorporate> selectDetailByIdcard(String idcard);

    /**
     * @Author 郭丁志
     * @Description //TODO 查询有多少企业关联了联系人
     * @Date 22:06 2020-03-02
     * @Param []
     * @return java.lang.Integer
     **/
    Integer countCorporate();

    Integer countCorporateByReview(String organizationCode, String enterpriseName);

    Integer insterByReview(String id);
}
