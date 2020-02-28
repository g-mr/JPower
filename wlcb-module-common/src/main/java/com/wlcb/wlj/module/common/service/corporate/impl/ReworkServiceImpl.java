package com.wlcb.wlj.module.common.service.corporate.impl;

import com.github.pagehelper.PageHelper;
import com.wlcb.wlj.module.common.page.PaginationContext;
import com.wlcb.wlj.module.common.service.corporate.ReworkService;
import com.wlcb.wlj.module.common.utils.UUIDUtil;
import com.wlcb.wlj.module.common.utils.constants.ConstantsEnum;
import com.wlcb.wlj.module.dbs.dao.corporate.LogMapper;
import com.wlcb.wlj.module.dbs.dao.corporate.ReworkMapper;
import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgLog;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgRework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mr.gmac
 */
@Service("reworkService")
public class ReworkServiceImpl implements ReworkService {

    private static final Logger logger = LoggerFactory.getLogger(ReworkServiceImpl.class);

    @Autowired
    private ReworkMapper reworkMapper;
    @Autowired
    private LogMapper logMapper;

    /**
     * @Author 郭丁志
     * @Description //TODO 分页查询复工记录
     * @Date 23:02 2020-02-28
     * @Param [corporateId]
     * @return PageBean<TblCsrrgRecord>
     **/
    @Override
    public PageBean<TblCsrrgRework> listPage(String corporateId) {
        PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());
        List<TblCsrrgRework> list = reworkMapper.listAll(corporateId);
        return new PageBean<>(list);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 查询复工详情
     * @Date 23:12 2020-02-28
     * @Param [id]
     * @return TblCsrrgRework
     **/
    @Override
    public TblCsrrgRework detail(String id) {
        return reworkMapper.selectById(id);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 更新审核状态，并记录日志
     * @Date 23:24 2020-02-28
     * @Param [log, refuseReason]
     * @return java.lang.Integer
     **/
    @Override
    public Integer updateReworkStatus(TblCsrrgLog log, String refuseReason) {
        TblCsrrgRework rework = new TblCsrrgRework();
        rework.setId(log.getKeyId());
        rework.setReviewStats(log.getStatus());
        rework.setUpdateUser(log.getName());
        rework.setRefuseReason(refuseReason);

        Integer count = reworkMapper.updateByPrimaryKeySelective(rework);

        if(count > 0){
            log.setStatus(null);
            log.setId(UUIDUtil.getUUID());
            log.setTableName("tbl_csrrg_rework");
            log.setContent("修改复工审核状态为："+ ConstantsEnum.APPLICANT_STATUS.getName(rework.getReviewStats()));

            logMapper.inster(log);

            logger.info("复工审核完成，日志记录成功，日志ID={}",log.getId());
        }

        return count;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 新增复工申请
     * @Date 23:51 2020-02-28
     * @Param [rework]
     * @return java.lang.Integer
     **/
    @Override
    public Integer addRework(TblCsrrgRework rework) {
        rework.setId(UUIDUtil.getUUID());
        rework.setReviewStats(3);
        rework.setRefuseReason(null);
        return reworkMapper.insterSelective(rework);
    }
}
