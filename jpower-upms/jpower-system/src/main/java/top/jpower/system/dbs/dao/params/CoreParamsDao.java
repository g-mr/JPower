package top.jpower.system.dbs.dao.params;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.system.dbs.dao.params.mapper.CoreParamsMapper;
import top.jpower.system.dbs.entity.params.CoreParam;

/**
 * 参数数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreParamsDao extends JpowerServiceImpl<CoreParamsMapper, CoreParam> {

	/**
	 * 通过Code获取参数值
	 *
	 * @param code 参数编码
	 * @return 参数值
	 */
	public String selectValueByCode(String code) {
		return super.getObjAs(Wrappers.getQueryWrapper().select(CoreParam::getValue).eq(CoreParam::getCode, code), String.class);
	}
}
