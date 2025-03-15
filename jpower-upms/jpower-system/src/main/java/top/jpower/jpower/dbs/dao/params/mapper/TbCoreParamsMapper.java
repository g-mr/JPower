package top.jpower.jpower.dbs.dao.params.mapper;


import org.springframework.stereotype.Component;
import top.jpower.jpower.dbs.entity.params.TbCoreParam;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;

import java.util.List;


/**
 * @author mr.gmac
 */
@Component("tbCoreParamsMapper")
public interface TbCoreParamsMapper extends JpowerBaseMapper<TbCoreParam> {

    String selectByCode(String code);

    List<TbCoreParam> listAll(TbCoreParam coreParam);

    Integer updateByPrimaryKeySelective(TbCoreParam coreParam);
}
