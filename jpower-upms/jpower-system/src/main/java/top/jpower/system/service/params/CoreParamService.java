package top.jpower.system.service.params;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.dbs.entity.params.CoreParam;

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

}
