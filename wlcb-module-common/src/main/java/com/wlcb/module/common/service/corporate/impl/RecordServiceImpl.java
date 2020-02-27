package com.wlcb.module.common.service.corporate.impl;

import com.github.pagehelper.PageHelper;
import com.wlcb.module.base.vo.ResponseData;
import com.wlcb.module.common.page.PaginationContext;
import com.wlcb.module.common.service.corporate.RecordService;
import com.wlcb.module.common.utils.ReturnJsonUtil;
import com.wlcb.module.common.utils.UUIDUtil;
import com.wlcb.module.common.utils.constants.ConstantsEnum;
import com.wlcb.module.dbs.dao.corporate.CorporateMapper;
import com.wlcb.module.dbs.dao.corporate.RecordLogMapper;
import com.wlcb.module.dbs.dao.corporate.RecordMapper;
import com.wlcb.module.dbs.entity.base.PageBean;
import com.wlcb.module.dbs.entity.corporate.TblCsrrgCorporate;
import com.wlcb.module.dbs.entity.corporate.TblCsrrgRecord;
import com.wlcb.module.dbs.entity.corporate.TblCsrrgRecordLog;
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
    private RecordLogMapper recordLogMapper;

    @Override
    public PageBean<TblCsrrgRecord> listPage(TblCsrrgRecord record) {
        PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());
        List<TblCsrrgRecord> list = recordMapper.listAll(record);
        return new PageBean<>(list);

    }

    @Override
    public ResponseData addRecord(TblCsrrgRecord record) {

        String msg = "提交成功";
        record.setApplicantStatus(ConstantsEnum.APPLICANT_STATUS.REVIEW.getValue());

        if (StringUtils.isBlank(record.getFilePath())){
            TblCsrrgCorporate csrrgCorporate = corporateMapper.selectDetailByLegal(record);
            if (csrrgCorporate == null){
                return ReturnJsonUtil.printJson(1,"您不是该企业的法人，请上传法人授权委托书",false);
            }

            record.setCorporateId(csrrgCorporate.getId());
            record.setApplicantStatus(ConstantsEnum.APPLICANT_STATUS.MATCH.getValue());
            msg = ConstantsEnum.APPLICANT_STATUS.MATCH.getName();
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
    public Integer updateRecordStatus(TblCsrrgRecordLog recordLog,String failReason) {

        TblCsrrgRecord record = new TblCsrrgRecord();
        record.setId(recordLog.getRecordId());
        record.setApplicantStatus(recordLog.getStatus());
        record.setUpdateUser(recordLog.getName());
        record.setFailReason(failReason);

        Integer count = recordMapper.updateByPrimaryKeySelective(record);

        if(count > 0){
            recordLog.setStatus(null);
            recordLog.setId(UUIDUtil.getUUID());
            recordLog.setContent("修改状态为："+ConstantsEnum.APPLICANT_STATUS.getName(record.getApplicantStatus()));

            recordLogMapper.inster(recordLog);
        }

        return count;
    }
}
