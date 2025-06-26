package top.jpower.jpower.gateway.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.auth.utils.constant.ClientNameConstant;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.jpower.gateway.feign.RoleClient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author mr.g
 * @date 2022-08-01 23:09
 */
@Service
@EnableFeignClients(clients = RoleClient.class)
@AllArgsConstructor
public class RoleService {

    private RoleClient client;
    // private RestTemplate restTemplate;
    // private final WebClient webClient;

    /**
     * 根据角色ID查询功能
     *
     * @author mr.g
     * @param roleId 角色ID
     * @param clientCode
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @SneakyThrows({ExecutionException.class, InterruptedException.class})
    public List<String> queryUrlByRole(Long roleId, String clientCode){
        Future<ResponseData<List<String>>> future = ThreadUtil.execAsync(() -> {
            return client.getUrlsByRoleIds(ClientNameConstant.getInstance().getJpowerSystem(), roleId, clientCode);
            // return restTemplate.getForObject("http://"+ ClientNameConstant.getInstance().getJpowerSystem()+"/core/function/getUrlsByRoleIds?roleIds="+roleId+"&clientCode="+clientCode,ResponseData.class);
        });
        ResponseData<List<String>> responseData = future.get();
        return Fc.isNull(responseData) ? ListUtil.of() : responseData.getData();
    }


    /**
     * 根据菜单编码查询ID
     *
     * @author mr.g
     * @param code 菜单编码
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @SneakyThrows({ExecutionException.class, InterruptedException.class})
    public Long queryMenuIdByCode(String code){
        Future<ResponseData<Long>> future = ThreadUtil.execAsync(() -> {
            return null;
            // return restTemplate.getForObject("http://"+ ClientNameConstant.getInstance().getJpowerSystem()+"/core/menu/getIdByCode?code="+ code,ResponseData.class);
        });
        ResponseData<Long> responseData = future.get();
        return Fc.isNull(responseData) ? null : responseData.getData();
    }

    /**
     * 根据角色ID查询数据权限
     *
     * @author mr.g
     * @param roleIds 角色ID
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @SneakyThrows({ExecutionException.class, InterruptedException.class})
    public List<Map<String,Object>> queryDataScopeByRole(List<Long> roleIds, String clientCode){
        Future<ResponseData<List<Map<String,Object>>>> future = ThreadUtil.execAsync(() -> {
            return null;
            // return restTemplate.getForObject("http://"+ ClientNameConstant.getInstance().getJpowerSystem()+"/core/dataScope/getDataScopeByRole?roleIds=" + StringUtil.join(roleIds) + "&clientCode=" + clientCode,ResponseData.class);
        });
        ResponseData<List<Map<String,Object>>> responseData = future.get();
        return Fc.isNull(responseData) ? ListUtil.of() : responseData.getData();
    }


    /**
     * 根据角色ID查询数据权限
     *
     * @author mr.g
     * @param roleIds 角色ID
     * @param clientCode
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public String queryDataScopeByRoleAndMenu(List<Long> roleIds, String menuCode, String clientCode){
        List<Map<String,Object>> list = queryDataScopeByRole(roleIds,clientCode);

        if (Fc.isNotEmpty(list) && Fc.isNotBlank(menuCode)){
            Long menuId = queryMenuIdByCode(menuCode);
            if (Fc.notNull(menuId)){
                List<Map<String,Object>> listScope = list.stream()
                        .filter(m->Fc.equalsValue(MapUtil.getLong(m,"menuId"),menuId))
                        .sorted(Comparator.comparingInt(m->MapUtil.getInt(m,"allRole")))
                        .filter(m->{
                            if (Fc.equalsValue(MapUtil.getInt(m,"allRole"), YN01Enum.N.getValue())){
                                return true;
                            }
                            return list.stream().noneMatch(lm-> Fc.equalsValue(MapUtil.getInt(lm,"allRole"), YN01Enum.N.getValue()) &&
                                    Fc.equalsValue(MapUtil.getStr(lm,"scopeClass"), MapUtil.getStr(m,"scopeClass")));

                        }).collect(Collectors.toList());


                return JSON.toJSONString(listScope);
            }
        }

        return StringPool.EMPTY;
    }
}
