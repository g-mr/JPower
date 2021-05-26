package com.wlcb.jpower.service.impl;

import com.wlcb.jpower.dbs.dao.LogOperateDao;
import com.wlcb.jpower.dbs.dao.mapper.LogOperateMapper;
import com.wlcb.jpower.dbs.entity.TbLogOperate;
import com.wlcb.jpower.module.base.model.OperateLogDto;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.service.OperateLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author mr.g
 * @Date 2021/4/19 0019 19:15
 */
@Service
@AllArgsConstructor
public class OperateLogServiceImpl extends BaseServiceImpl<LogOperateMapper, TbLogOperate> implements OperateLogService {

    private LogOperateDao operateDao;

    @Override
    public void saveOperateLog(OperateLogDto operateLog) {
        TbLogOperate logOperate = BeanUtil.copyProperties(operateLog, TbLogOperate.class);
        operateDao.save(logOperate);
    }
}
