package top.jpower.jpower.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.jpower.dbs.dao.mapper.TbResourceOssMapper;
import top.jpower.jpower.dbs.entity.TbResourceOss;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.mp.support.Condition;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2024/4/23 10:14 AM
 */
@Repository
public class TbResourceOssDao extends JpowerServiceImpl<TbResourceOssMapper, TbResourceOss> {

    /**
     * 获取资源详情
     *
     * @author mr.g
     * @param code 编码
     * @return 资源详情
     **/
    public TbResourceOss getByCode(String code) {
        return super.getOne(Condition.<TbResourceOss>getQueryWrapper()
                .lambda().eq(TbResourceOss::getCode, code));
    }

    /**
     * 查询选择列表
     *
     * @author mr.g
     * @param
     * @return
     **/
    public List<Map<String, Object>> listCodeName() {
        return super.listMaps(Condition.<TbResourceOss>getQueryWrapper()
                .lambda().select(TbResourceOss::getCode, TbResourceOss::getName));
    }
}
