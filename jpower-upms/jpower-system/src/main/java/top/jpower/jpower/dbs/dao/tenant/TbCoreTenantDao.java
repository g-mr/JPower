package top.jpower.jpower.dbs.dao.tenant;

import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Repository;
import top.jpower.core.utils.utils.Fc;
import top.jpower.jpower.dbs.dao.tenant.mapper.TbCoreTenantMapper;
import top.jpower.jpower.dbs.entity.tenant.TbCoreTenant;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;
import top.jpower.jpower.module.mp.support.Condition;

import java.util.Map;

/**
 * @ClassName TbCoreTenantDao
 * @Description TODO 租户
 * @Author 郭丁志
 * @Date 2020-10-23 15:18
 * @Version 1.0
 */
@Repository
public class TbCoreTenantDao extends JpowerServiceImpl<TbCoreTenantMapper, TbCoreTenant> {

    /**
     * 查询租户得设置
     *
     * @author mr.g
     * @param id 租户ID
     * @return java.util.Map<java.lang.String,java.lang.String> 设置内容
     **/
    public Map<String, String> config(Long id) {
        return super.getObj(Condition.<TbCoreTenant>getQueryWrapper().lambda().select(TbCoreTenant::getConfig).eq(TbCoreTenant::getId, id), config-> JSON.parseObject(Fc.toStr(config, "{}"), Map.class));
    }

    /**
     * 租户设置
     *
     * @author mr.g
     * @param id 租户ID
     * @param config 设置内容
     * @return boolean 是否成功
     **/
    public boolean updateConfig(Long id, Map<String, String> config) {
        TbCoreTenant tenant = new TbCoreTenant();
        tenant.setId(id);
        tenant.setConfig(config);
        return super.updateById(tenant);
    }

}
