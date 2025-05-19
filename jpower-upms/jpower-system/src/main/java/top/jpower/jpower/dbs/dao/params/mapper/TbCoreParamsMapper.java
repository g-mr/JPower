package top.jpower.jpower.dbs.dao.params.mapper;


import org.apache.ibatis.annotations.Mapper;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.jpower.dbs.entity.params.TbCoreParam;

import java.util.List;


/**
 * @author mr.gmac
 */
@Mapper
public interface TbCoreParamsMapper extends JpowerBaseMapper<TbCoreParam> {

    String selectByCode(String code);

    List<TbCoreParam> listAll(TbCoreParam coreParam);

    Integer updateByPrimaryKeySelective(TbCoreParam coreParam);
}
