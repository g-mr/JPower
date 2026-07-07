package com.qidiangk.smart.aster.dbs.dao.ivr.mapper;

import com.mybatisflex.core.FlexConsts;
import com.mybatisflex.core.provider.EntitySqlProvider;
import com.mybatisflex.core.query.QueryWrapper;
import com.qidiangk.smart.aster.dbs.entity.ivr.OutTaskDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import top.jpower.core.dbs.config.annotation.NoSqlLog;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;

import java.util.List;

/**
 * @author mr.g
 */
@Mapper
public interface OutTaskMapper extends JpowerBaseMapper<OutTaskDO> {

    @NoSqlLog
    @SelectProvider(type = EntitySqlProvider.class, method = "selectListByQuery")
    List<OutTaskDO> selectListIdTimes(@Param(FlexConsts.QUERY) QueryWrapper queryWrapper);

}
