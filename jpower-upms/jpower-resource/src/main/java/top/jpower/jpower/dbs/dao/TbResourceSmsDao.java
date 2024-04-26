package top.jpower.jpower.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.jpower.dbs.dao.mapper.TbResourceSmsMapper;
import top.jpower.jpower.dbs.entity.TbResourceSms;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;
import top.jpower.jpower.module.mp.support.Condition;

/**
* <p>
 * 短信配置表 数据类
 * </p>
*
* @author mr.g
* @since 2024-03-04
*/
@Repository
public class TbResourceSmsDao extends JpowerServiceImpl<TbResourceSmsMapper, TbResourceSms> {

    /**
     * 通过编号获取
     * @author mr.g
     * @param code 编号
     * @return 短信资源
     **/
    public TbResourceSms getByCode(String code) {
        return super.getOne(Condition.<TbResourceSms>getQueryWrapper().lambda().eq(TbResourceSms::getCode, code));
    }
}
