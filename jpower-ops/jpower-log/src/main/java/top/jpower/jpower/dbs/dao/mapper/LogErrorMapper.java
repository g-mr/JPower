package top.jpower.jpower.dbs.dao.mapper;

import org.springframework.stereotype.Component;
import top.jpower.jpower.dbs.entity.TbLogError;
import top.jpower.core.dbs.config.annotation.NoSqlLog;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;

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
