package com.wlcb.wlj.module.dbs.dao.corporate;

import com.wlcb.wlj.module.dbs.dao.BaseMapper;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgRecord;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("recordMapper")
public interface RecordMapper extends BaseMapper<TblCsrrgRecord> {

    @Override
    List<TblCsrrgRecord> listAll(TblCsrrgRecord record);

    Integer selectCountByCidAndOid(String openid, String corporateId);

    /**
     * @Author 郭丁志
     * @Description //TODO 查询该联系对应该公司申请成功和申请中的数量
     * @Date 15:58 2020-03-03
     * @Param [record]
     * @return java.lang.Integer
     **/
    Integer selectAppCountByStatus(TblCsrrgRecord record);

    String countCorporate(String quxian);
}
