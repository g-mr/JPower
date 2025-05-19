package top.jpower.jpower.dbs.dao.dict.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.jpower.dbs.entity.dict.TbCoreDict;
import top.jpower.jpower.vo.DictVo;

import java.util.List;

/**
 * @ClassName TbCoreParamsDao
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-03 13:30
 * @Version 1.0
 */
@Mapper
public interface TbCoreDictMapper extends JpowerBaseMapper<TbCoreDict> {

    /**
     * @author 郭丁志
     * @Description //TODO 查询字典列表包含是否存在下级列表
     * @date 22:15 2020/8/21 0021
     * @param dict
     * @return java.util.List<top.jpower.jpower.module.dbs.vo.DictVo>
     */
    List<DictVo> listByType(TbCoreDict dict);
}
