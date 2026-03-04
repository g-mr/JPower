package top.jpower.system.dbs.dao.dict.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.system.dbs.entity.dict.CoreDict;
import top.jpower.system.vo.DictVO;

import java.util.List;

/**
 * 字典数据访问映射器
 * 
 * @author mr.g
 */
@Mapper
public interface CoreDictMapper extends JpowerBaseMapper<CoreDict> {

    /**
     * 查询字典列表包含是否存在下级列表
     * 
     * @author mr.g
     * @param dict 查询条件
     * @return java.util.List<top.jpower.system.vo.DictVo> 字典列表
     */
    List<DictVO> listByType(CoreDict dict);
}
