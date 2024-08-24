package top.jpower.jpower.service.tenant;

import top.jpower.jpower.dbs.entity.tenant.TbCoreTenant;
import top.jpower.jpower.module.service.BaseService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mr.gmac
 */
public interface TenantService extends BaseService<TbCoreTenant> {

    /**
     * @author 郭丁志
     * @Description // 根据ID修改信息
     * @date 22:14 2020/10/24 0024
     */
    @Override
    boolean updateById(TbCoreTenant tenant);

    /**
     * @author 郭丁志
     * @Description // 新增信息
     * @date 22:14 2020/10/24 0024
     * @param tenant 租户信息
     * @param functionCodes 权限Code
     */
    boolean save(TbCoreTenant tenant, Set<String> functionCodes);

    /**
     * @author 郭丁志
     * @Description //TODO 租户授权配置
     * @date 13:34 2020/10/25 0025
     * @param ids 租户ID
     * @param accountNumber 额度
     * @param expireTime 过期时间
     */
    boolean setting(List<Long> ids, Integer accountNumber, Date expireTime);

    /**
     * 查询租户得设置
     *
     * @author mr.g
     * @param id
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    Map<String, String> config(Long id);

    /**
     * 租户设置
     *
     * @author mr.g
     * @param id 租户ID
     * @param config 设置内容
     * @return boolean 是否成功
     **/
    boolean updateConfig(Long id, Map<String, String> config);
}
