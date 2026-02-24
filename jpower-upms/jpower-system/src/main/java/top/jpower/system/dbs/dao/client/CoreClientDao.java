package top.jpower.system.dbs.dao.client;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.dao.client.mapper.CoreClientMapper;
import top.jpower.system.dbs.entity.client.CoreClient;

/**
 * 客户端数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreClientDao extends JpowerServiceImpl<CoreClientMapper, CoreClient> {

    /**
     * 通过CODE查询客户端ID
     *
     * @author mr.g
     * @param code 客户端编码
     * @return id
     **/
	public Long queryIdByCode(String code){
		return super.getObj(Condition.<CoreClient>getQueryWrapper().lambda().select(CoreClient::getId).eq(CoreClient::getClientCode, code), Fc::toLong);
	}

}
