package top.jpower.jpower.dbs.dao.mapper;

import top.jpower.jpower.dbs.entity.TbLogError;
import top.jpower.jpower.module.annotation.NoSqlLog;
import top.jpower.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;
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
