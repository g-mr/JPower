package top.jpower.system.dbs.dao.client.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.system.dbs.entity.client.CoreClient;

/**
 * 客户端数据访问映射器
 * 
 * @author mr.g
 */
@Mapper
public interface CoreClientMapper extends JpowerBaseMapper<CoreClient> {
}
