package com.wlcb.wlj.module.common.service.corporate.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.common.page.PaginationContext;
import com.wlcb.wlj.module.common.service.corporate.RecordService;
import com.wlcb.wlj.module.common.utils.ReturnJsonUtil;
import com.wlcb.wlj.module.common.utils.UUIDUtil;
import com.wlcb.wlj.module.common.utils.constants.ConstantsEnum;
import com.wlcb.wlj.module.dbs.dao.corporate.CorporateMapper;
import com.wlcb.wlj.module.dbs.dao.corporate.LogMapper;
import com.wlcb.wlj.module.dbs.dao.corporate.RecordMapper;
import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporate;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgLog;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mr.gmac
 */
@Service("recordService")
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private CorporateMapper corporateMapper;
    @Autowired
    private LogMapper logMapper;

    @Override
    public PageInfo<TblCsrrgRecord> listPage(TblCsrrgRecord record) {
        PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());
        List<TblCsrrgRecord> list = recordMapper.listAll(record);

        return new PageInfo<>(list);
    }

    @Override
    public ResponseData addRecord(TblCsrrgRecord record) {

        String msg = "提交成功";
        record.setApplicantStatus(ConstantsEnum.APPLICANT_STATUS.REVIEW.getValue());

        //判断该联系人是否已经申请了该公司
        Integer i = recordMapper.selectAppCountByStatus(record);
        if (i > 0){
            return ReturnJsonUtil.printJson(2,"已经提交过申请，不可重复提交",false);
        }

        if (StringUtils.isBlank(record.getFilePath())){
            //如果文件为空则去判断是否是法人，是则直接关联，不是返回提示
            TblCsrrgCorporate csrrgCorporate = corporateMapper.selectDetailByLegal(record);
            if (csrrgCorporate == null){
                return ReturnJsonUtil.printJson(1,"您不是该企业的法人，请上传法人授权委托书",false);
            }

            record.setCorporateId(csrrgCorporate.getId());
            record.setApplicantStatus(ConstantsEnum.APPLICANT_STATUS.MATCH.getValue());
            msg = ConstantsEnum.APPLICANT_STATUS.MATCH.getName();
        }else {
            Integer c = corporateMapper.countZhengfuUserByLidcard(record.getCorporateId());
            if (c <= 0){
                return ReturnJsonUtil.printJson(3,"该公司法人未注册市民卡，请先通知法人注册市民卡",false);
            }
        }

        record.setId(UUIDUtil.getUUID());
        Integer count = recordMapper.insterSelective(record);

        if (count>0){
            return ReturnJsonUtil.printJson(0,msg,true);
        }else {
            return ReturnJsonUtil.printJson(-1,"提交失败",false);
        }
    }

    @Override
    public TblCsrrgRecord detail(String id) {
        return recordMapper.selectById(id);
    }

    @Override
    public Integer updateRecordStatus(TblCsrrgLog recordLog, String failReason) {

        TblCsrrgRecord record = new TblCsrrgRecord();
        record.setId(recordLog.getKeyId());
        record.setApplicantStatus(recordLog.getStatus());
        record.setUpdateUser(recordLog.getName());
        record.setFailReason(failReason);

        Integer count = recordMapper.updateByPrimaryKeySelective(record);

        if(count > 0){
            recordLog.setStatus(null);
            recordLog.setId(UUIDUtil.getUUID());
            recordLog.setTableName("tbl_csrrg_record");
            recordLog.setContent("修改申请记录状态为："+ConstantsEnum.APPLICANT_STATUS.getName(record.getApplicantStatus()));

            logMapper.inster(recordLog);
        }

        return count;
    }

    @Override
    public Integer selectCountByCidAndOid(String openid, String corporateId) {
        return recordMapper.selectCountByCidAndOid(openid,corporateId);
    }

}
