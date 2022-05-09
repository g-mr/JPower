package com.wlcb.jpower.dbs.dao.mapper;

import com.wlcb.jpower.dbs.entity.TbLogError;
import com.wlcb.jpower.module.annotation.NoSqlLog;
import com.wlcb.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;
import org.springframework.stereotype.Component;

/**
 * @Author mr.g
 * @Date 2021/5/1 0001 19:39
 */
@Component
public interface LogErrorMapper extends JpowerBaseMapper<TbLogError> {

    /**
     * 插入一条记录
     *
     * @param entity 实体对象
     */
    @Override
    @NoSqlLog
    int insert(TbLogError entity);

}
