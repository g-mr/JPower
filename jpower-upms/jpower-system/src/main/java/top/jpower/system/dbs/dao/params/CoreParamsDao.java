package top.jpower.system.dbs.dao.params;

import org.springframework.stereotype.Repository;
import top.jpower.system.dbs.dao.params.mapper.CoreParamsMapper;
import top.jpower.system.dbs.entity.params.CoreParam;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;

/**
 * 参数数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreParamsDao extends JpowerServiceImpl<CoreParamsMapper, CoreParam> {


}
