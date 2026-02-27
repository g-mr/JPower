package top.jpower.system.dbs.dao.params.mapper;


import org.apache.ibatis.annotations.Mapper;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.system.dbs.entity.params.CoreParam;


/**
 * 参数数据访问映射器
 * 
 * @author mr.g
 */
@Mapper
public interface CoreParamsMapper extends JpowerBaseMapper<CoreParam> {}
