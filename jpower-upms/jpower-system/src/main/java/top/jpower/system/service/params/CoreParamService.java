package top.jpower.system.service.params;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import top.jpower.system.dbs.entity.params.CoreParam;

import java.util.List;
import java.util.Map;

/**
 * 参数服务接口
 * 
 * @author mr.g
 */
public interface CoreParamService extends BaseService<CoreParam> {

    /**
     * 通过code查询参数值
     * 
     * @author mr.g
     * @param code 参数编码
     * @return 参数值
     */
    String selectByCode(String code);

	/**
     * 批量删除参数
     *
     * @param ids 参数ID
     * @return 是否成功
     */
	boolean removeByIds(List<Long> ids);

	/**
     * 更新参数
     *
     * @param coreParam 参数
     * @return 是否成功
     */
	boolean updateById(CoreParam coreParam);

	/**
     * 保存参数
     *
     * @param coreParam 参数
     * @return 是否成功
     */
	Long create(CoreParam coreParam);

	/**
	 * 分页查询
	 *
	 * @param map 查询条件
	 * @return  数据
	 */
    Pg<CoreParam> pageByMap(Map<String, Object> map);
}
