package top.jpower.jpower.service.params;

import top.jpower.jpower.dbs.entity.params.TbCoreParam;
import top.jpower.jpower.module.service.BaseService;

/**
 * @author mr.gmac
 */
public interface CoreParamService extends BaseService<TbCoreParam> {

    /**
     * @Author 郭丁志
     * @Description //TODO 通过code查询参数值
     * @Date 16:09 2020-05-06
     * @Param [key]
     * @return java.lang.String
     **/
    String selectByCode(String code);

}
