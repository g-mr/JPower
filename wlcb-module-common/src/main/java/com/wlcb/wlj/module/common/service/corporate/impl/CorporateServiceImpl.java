package com.wlcb.wlj.module.common.service.corporate.impl;

import com.github.pagehelper.PageHelper;
import com.wlcb.wlj.module.common.page.PaginationContext;
import com.wlcb.wlj.module.common.service.corporate.CorporateService;
import com.wlcb.wlj.module.common.utils.UUIDUtil;
import com.wlcb.wlj.module.common.utils.constants.ConstantsEnum;
import com.wlcb.wlj.module.dbs.dao.corporate.CorporateMapper;
import com.wlcb.wlj.module.dbs.dao.corporate.CorporateReviewMapper;
import com.wlcb.wlj.module.dbs.dao.corporate.LogMapper;
import com.wlcb.wlj.module.dbs.dao.corporate.RecordMapper;
import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporate;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporateReview;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgLog;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author mr.gmac
 */
@Service("corporateService")
public class CorporateServiceImpl implements CorporateService {

    private static final Logger logger = LoggerFactory.getLogger(CorporateServiceImpl.class);

    @Autowired
    private CorporateMapper corporateMapper;
    @Autowired
    private CorporateReviewMapper corporateReviewMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private LogMapper logMapper;

    @Override
    public List<Map<String,String>> queryEnterpriseName(String name){
        return corporateMapper.selectName(name);
    }

    @Override
    public TblCsrrgCorporate selectById(String id) {
        return corporateMapper.selectById(id);
    }

    @Override
    public List<TblCsrrgCorporate> queryDetailByIdcard(String idcard) {
        return corporateMapper.selectDetailByIdcard(idcard);
    }

    @Override
    public String countCorporateByRecord(String quxian) {
        return recordMapper.countCorporate(quxian);
    }

    @Override
    public Integer countCorporateByReview(String organizationCode, String enterpriseName) {
        return corporateMapper.countCorporateByReview(organizationCode,enterpriseName);
    }

    @Override
    public Integer addCorporateReview(TblCsrrgCorporateReview corporateReview) {
        corporateReview.setReviewStats(3);
        corporateReview.setId(UUIDUtil.getUUID());
        return corporateReviewMapper.inster(corporateReview);
    }

    @Override
    public Integer updateStatus(TblCsrrgLog log,String reason) {

        Integer count = corporateReviewMapper.updateStatus(log.getKeyId(),log.getStatus(),reason);

        if(count > 0){
            //对于申请成功的企业需要加到正式的企业法人表
            if (ConstantsEnum.APPLICANT_STATUS.SUCCESS.getValue().equals(log.getStatus())){
                corporateMapper.insterByReview(log.getKeyId());
            }else{
                //防止申请成功后又改为其他状态
                corporateMapper.deleteById(log.getKeyId());
            }

            log.setId(UUIDUtil.getUUID());
            log.setTableName("tbl_csrrg_corporate");
            log.setContent("修改公司法人审核状态为："+ ConstantsEnum.APPLICANT_STATUS.getName(log.getStatus()));
            log.setStatus(null);

            logMapper.inster(log);

            logger.info("公司法人审核完成，日志记录成功，日志ID={}",log.getId());
        }

        return count;
    }

    @Override
    public PageBean<TblCsrrgCorporateReview> listPage(TblCsrrgCorporateReview corporateReview) {
        PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());
        List<TblCsrrgCorporateReview> list = corporateReviewMapper.listAll(corporateReview);
        return new PageBean<>(list);
    }

}
