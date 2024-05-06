package top.jpower.jpower.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.jpower.dbs.entity.city.TbCoreCity;
import top.jpower.jpower.dbs.entity.client.TbCoreClient;
import top.jpower.jpower.dbs.entity.function.TbCoreDataScope;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.dbs.entity.org.TbCoreOrg;
import top.jpower.jpower.dbs.entity.tenant.TbCoreTenant;

import java.util.List;

/**
 * @author mr.gmac
 */
@FeignClient(value = AppConstant.JPOWER_SYSTEM, fallback = SystemClientFallback.class, path = "/core/")
public interface SystemClient {

    /**
     * @author null
     * @Description //TODO 查询部门所有下级部门id
     * @date 0:42 2020/9/3 0003
     * @param id
     * @return top.jpower.jpower.module.base.vo.ResponseData<java.lang.String>
     */
    @GetMapping("/org/queryChildById")
    ResponseData<List<Long>> queryChildOrgById(@RequestParam("id") Long id);

    @GetMapping("/org/queryOrgById")
    ResponseData<TbCoreOrg> queryOrgById(@RequestParam("orgId") Long orgId);

    @GetMapping("/client/getClientByClientCode")
    ResponseData<TbCoreClient> getClientByClientCode(@RequestParam("clientCode") String clientCode);

    @GetMapping("/function/getUrlsByRoleIds")
    ResponseData<List<String>> getUrlsByRoleIds(@RequestParam("roleIds") List<Long> roleIds, @RequestParam("clientCode") String clientCode);

    @GetMapping("/tenant/getTenantByCode")
    ResponseData<TbCoreTenant> getTenantByCode(@RequestParam("tenantCode") String tenantCode);

    @GetMapping("/function/getMenuListByRole")
    ResponseData<List<TbCoreFunction>> getMenuListByRole(@RequestParam("roleIds") List<Long> roleIds, @RequestParam("clientCode") String clientCode, @RequestParam("topMenuId") Long topMenuId);

    @GetMapping("/dataScope/getAllRoleDataScope")
    ResponseData<List<TbCoreDataScope>> getAllRoleDataScope();

    @GetMapping("/dataScope/getDataScopeByRole")
    ResponseData<List<TbCoreDataScope>> getDataScopeByRole(@RequestParam("roleIds") List<Long> roleIds, @RequestParam("clientCode") String clientCode);

    @GetMapping("/role/getRoleNameByIds")
    ResponseData<List<String>> getRoleNameByIds(@RequestParam("roleIds") List<Long> roleIds);

    @GetMapping("/city/getCityByCode")
    ResponseData<TbCoreCity> getCityByCode(@RequestParam("code") String code);
}
