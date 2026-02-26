package top.jpower.system.service.client;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import top.jpower.system.dbs.entity.client.CoreClient;
import top.jpower.system.vo.SelectVO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 客户端服务接口
 * 
 * @author mr.g
 */
public interface CoreClientService extends BaseService<CoreClient> {
    /**
     * 通过CODE查询客户端ID
     *
     * @author mr.g
     * @param clientCode 客户端编码
     * @return id
     **/
	Optional<Long> queryIdByCode(String clientCode);

	/**
	 * 创建或者更新
	 *
	 * @param coreClient 客户端信息
	 * @return  id
	 */
	Long createOrUpdate(CoreClient coreClient);

	/**
	 * 分页查询
	 *
	 * @param map 参数
	 * @return 分页结果
	 */
	Pg<CoreClient> page(Map<String, Object> map);

	/**
	 * 查询下拉列表
	 *
	 * @author mr.g
	 * @return  数据
	 **/
	List<SelectVO> select();
}
