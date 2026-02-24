package top.jpower.system.dbs.dao.dict;

import org.springframework.stereotype.Repository;
import top.jpower.system.dbs.dao.dict.mapper.CoreDictMapper;
import top.jpower.system.dbs.entity.dict.CoreDict;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;

/**
 * 字典数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreDictDao extends JpowerServiceImpl<CoreDictMapper, CoreDict> {


}
