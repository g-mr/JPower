package top.jpower.system.api.feign;

import org.springframework.stereotype.Component;
import top.jpower.core.util.rsp.R;
import top.jpower.system.api.dto.*;

import java.util.List;

/**
 * @author mr.g
 */
@Component
public class SystemClientFallback implements SystemClient {

    @Override
    public R<List<Long>> queryChildOrgById(Long id) {
        return R.fail("查询失败");
    }

    @Override
    public R<OrgDTO> queryOrgById(Long orgId) {
        return R.fail("查询失败");
    }

    @Override
    public R<ClientDTO> getClientByClientCode(String clientCode) {
        return R.fail("查询失败");
    }

    @Override
    public R<List<String>> getUrlsByRoleIds(List<Long> roleIds, String clientCode) {
        return R.fail("查询失败");
    }

    @Override
    public R<TenantDTO> getTenantByCode(String tenantCode) {
        return R.fail("查询失败");
    }

    @Override
    public R<List<FunctionDTO>> getMenuListByRole(List<Long> roleIds, String clientCode, Long topMenuId) {
        return R.fail("查询失败");
    }

    @Override
    public R<List<DataScopeDTO>> getDataScopeByRole(List<Long> roleIds, String clientCode) {
        return R.fail("查询失败");
    }

    @Override
    public R<List<String>> getRoleNameByIds(List<Long> roleIds) {
        return R.fail("查询失败");
    }

    @Override
    public R<CityDTO> getCityByCode(String code) {
        return R.fail("查询失败");
    }
}
