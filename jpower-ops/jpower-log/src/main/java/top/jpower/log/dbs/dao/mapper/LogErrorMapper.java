package top.jpower.log.dbs.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.jpower.core.dbs.config.annotation.NoSqlLog;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.log.dbs.entity.LogError;

/**
 * 错误日志
 *
 * @author mr.g
 */
@Mapper
public interface LogErrorMapper extends JpowerBaseMapper<LogError> {

	/**
	 * 插入一条记录
	 *
	 * @param entity 实体对象
	 * @return 插入数量
	 */
	@NoSqlLog
	int insert(LogError entity);
}
