package com.qidiangk.smart.aster.dbs.dao.ivr.mapper;

import com.mybatisflex.core.FlexConsts;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import top.jpower.core.dbs.config.annotation.NoSqlLog;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import com.qidiangk.smart.aster.dbs.entity.ivr.OutTaskDO;

import java.util.List;

/**
 * @author mr.g
 */
@Mapper
public interface OutTaskMapper extends JpowerBaseMapper<OutTaskDO> {

    @NoSqlLog
    default List<OutTaskDO> selectListIdTimes(@Param(FlexConsts.QUERY) QueryWrapper queryWrapper) {
        return selectListByQuery(queryWrapper);
    }

}
