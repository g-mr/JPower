package com.wlcb.ylth.module.common.service.corporate.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wlcb.ylth.module.common.page.PaginationContext;
import com.wlcb.ylth.module.common.service.corporate.CorporateService;
import com.wlcb.ylth.module.common.utils.UUIDUtil;
import com.wlcb.ylth.module.common.utils.constants.ConstantsEnum;
import com.wlcb.ylth.module.dbs.dao.corporate.*;
import com.wlcb.ylth.module.dbs.entity.base.PageBean;
import com.wlcb.ylth.module.dbs.entity.corporate.TblCsrrgCorporate;
import com.wlcb.ylth.module.dbs.entity.corporate.TblCsrrgCorporateKakou;
import com.wlcb.ylth.module.dbs.entity.corporate.TblCsrrgCorporateReview;
import com.wlcb.ylth.module.dbs.entity.corporate.TblCsrrgLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    private CorporateKakouMapper corporateKakouMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private ReworkMapper reworkMapper;
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
    public Integer updateStatus(TblCsrrgLog log, String reason) {

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

    @Override
    public String countCorporateByRework(String quxian) {
        return reworkMapper.countCorporateByRework(quxian);
    }

    @Override
    public PageBean<Map<String, String>> listKakou(TblCsrrgCorporateKakou kakou) {
        PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());
        List<Map<String, String>> list = corporateKakouMapper.listKakou(kakou);
        return new PageBean<>(list);
    }

    @Override
    public Integer addKakou(TblCsrrgCorporateKakou kakou) {
        kakou.setId(UUIDUtil.getUUID());
        kakou.setReviewStatus(ConstantsEnum.APPLICANT_STATUS.REVIEW.getValue());
        return corporateKakouMapper.inster(kakou);
    }

    @Override
    public Integer selectKakouById(String kakouId) {
        return corporateKakouMapper.selectKakouById(kakouId);
    }

    @Override
    public Integer updateKakouStatus(TblCsrrgLog log,String refuseReason) {
        TblCsrrgCorporateKakou kakou = new TblCsrrgCorporateKakou();
        kakou.setId(log.getKeyId());
        kakou.setReviewStatus(log.getStatus());
        kakou.setUpdateUser(log.getName());
        kakou.setRefuseReason(refuseReason);

        Integer count = corporateKakouMapper.updateByPrimaryKeySelective(kakou);

        if(count > 0){
            log.setStatus(null);
            log.setId(UUIDUtil.getUUID());
            log.setTableName("tbl_csrrg_corporate_kakou");
            log.setContent("修改企业卡口审核状态为："+ ConstantsEnum.APPLICANT_STATUS.getName(kakou.getReviewStatus()));

            logMapper.inster(log);

            logger.info("企业卡口审核完成，日志记录成功，日志ID={}",log.getId());
        }

        return count;
    }

    @Override
    public Integer deleteKakou(String id) {
        TblCsrrgCorporateKakou kakou = new TblCsrrgCorporateKakou();
        kakou.setId(id);
        kakou.setStatus(0);
        return corporateKakouMapper.updateByPrimaryKeySelective(kakou);
    }

    @Override
    public PageInfo<TblCsrrgCorporate> listCorporate(TblCsrrgCorporate corporate) {
        PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());
        List<TblCsrrgCorporate> csrrgCorporates = corporateMapper.listAll(corporate);
        return new PageInfo<>(csrrgCorporates);
    }

    @Override
    public Integer updateCorporate(TblCsrrgCorporate corporate) {

        TblCsrrgCorporateReview csrrgCorporateReview = corporateReviewMapper.selectById(corporate.getId());
        if (csrrgCorporateReview != null){
            csrrgCorporateReview = JSON.parseObject(JSON.toJSONString(corporate),TblCsrrgCorporateReview.class);
            corporateReviewMapper.updateByPrimaryKeySelective(csrrgCorporateReview);
        }
        Integer count = corporateMapper.updateByPrimaryKeySelective(corporate);

        return count;
    }

    @Override
    public TblCsrrgCorporateKakou selectKakouByCorporateId(String corporateId) {
        return corporateKakouMapper.selectKakouByCorporateId(corporateId);
    }

}
