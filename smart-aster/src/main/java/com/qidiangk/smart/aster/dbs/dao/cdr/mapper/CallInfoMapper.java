package com.qidiangk.smart.aster.dbs.dao.cdr.mapper;


import com.mybatisflex.core.constant.FuncName;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.FunctionQueryColumn;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.CollectionUtil;
import com.mybatisflex.core.util.MapperUtil;
import org.apache.ibatis.annotations.Mapper;
import top.jpower.core.dbs.config.annotation.NoSqlLog;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallInfoDO;

import java.util.Collections;
import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.count;

@Mapper
public interface CallInfoMapper extends JpowerBaseMapper<CallInfoDO> {

    /**
     * 查询数据量。
     *
     * @param queryWrapper 条件
     * @return 数据量
     */
    @NoSqlLog
    default long selectCountByQuery(QueryWrapper queryWrapper) {
        return JpowerBaseMapper.super.selectCountByQuery(queryWrapper);
    }

}
