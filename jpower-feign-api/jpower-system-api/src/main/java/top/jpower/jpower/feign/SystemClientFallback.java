package top.jpower.jpower.feign;

import org.springframework.stereotype.Component;
import top.jpower.jpower.dbs.entity.city.TbCoreCity;
import top.jpower.jpower.dbs.entity.client.TbCoreClient;
import top.jpower.jpower.dbs.entity.function.TbCoreDataScope;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.dbs.entity.org.TbCoreOrg;
import top.jpower.jpower.dbs.entity.tenant.TbCoreTenant;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;

import java.util.List;

/**
 * @ClassName ParamsClientFallback
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-09-01 15:31
 * @Version 1.0
 */
@Component
public class SystemClientFallback implements SystemClient {

    @Override
    public ResponseData<List<Long>> queryChildOrgById(Long id) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<TbCoreOrg> queryOrgById(Long orgId) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<TbCoreClient> getClientByClientCode(String clientCode) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<List<String>> getUrlsByRoleIds(List<Long> roleIds, String clientCode) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<TbCoreTenant> getTenantByCode(String tenantCode) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<List<TbCoreFunction>> getMenuListByRole(List<Long> roleIds, String clientCode, Long topMenuId) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<List<TbCoreDataScope>> getAllRoleDataScope() {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<List<TbCoreDataScope>> getDataScopeByRole(List<Long> roleIds,String clientCode) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<List<String>> getRoleNameByIds(List<Long> roleIds) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<TbCoreCity> getCityByCode(String code) {
        return ReturnJsonUtil.fail("查询失败");
    }
}
