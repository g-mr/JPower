package top.jpower.system.dbs.dao.client;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.system.dbs.dao.client.mapper.CoreClientMapper;
import top.jpower.system.dbs.entity.client.CoreClient;
import top.jpower.system.vo.SelectIdNameVO;

import java.util.List;
import java.util.Optional;

import static top.jpower.system.dbs.entity.client.table.CoreClientTableDef.CORE_CLIENT;

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
	public Optional<Long> queryIdByCode(String code){
		return super.getObjAsOpt(Wrappers.getQueryWrapper().select(CoreClient::getId).eq(CoreClient::getClientCode, code), Long.TYPE);
	}

	/**
	 * 查询下拉列表
	 *
	 * @author mr.g
	 * @return  数据
	 **/
	public List<SelectIdNameVO> select() {
		return super.listAs(Wrappers.getQueryWrapper()
				.select(CORE_CLIENT.ID.as(SelectIdNameVO::getId), CORE_CLIENT.NAME)
				.orderBy(CoreClient::getSortNum).asc(), SelectIdNameVO.class);
	}
}
