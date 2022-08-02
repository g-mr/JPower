package com.wlcb.jpower.gateway.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.MapUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
@AllArgsConstructor
public class RoleService {

    private RestTemplate restTemplate;

    /**
     * 根据角色ID查询功能
     *
     * @author mr.g
     * @param roleId 角色ID
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @SneakyThrows({ExecutionException.class, InterruptedException.class})
    public List<Map<String,Object>> queryFunctionByRole(String roleId){
        Future<ResponseData<List<Map<String,Object>>>> future = ThreadUtil.execAsync(() -> restTemplate.getForObject("http://"+ AppConstant.JPOWER_SYSTEM+"/core/role/roleFunction?roleId="+roleId,ResponseData.class));
        ResponseData<List<Map<String,Object>>> responseData = future.get();
        return Fc.isNull(responseData) ? ListUtil.of() : responseData.getData();
    }

    /**
     * 根据角色ID查询接口
     *
     * @author mr.g
     * @param roleId 角色ID
     * @return java.util.List<java.lang.String>
     **/
    public List<String> queryUrlByRole(String roleId){
        List<Map<String,Object>> functionList = queryFunctionByRole(roleId);
        if (Fc.isEmpty(functionList)){
            return ListUtil.of();
        }
        return functionList.stream().map(m-> MapUtil.getStr(m,"url")).distinct().collect(Collectors.toList());
    }


    /**
     * 根据角色ID查询数据权限
     *
     * @author mr.g
     * @param code 菜单编码
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @SneakyThrows({ExecutionException.class, InterruptedException.class})
    public String queryMenuIdByCode(String code){
        Future<ResponseData<String>> future = ThreadUtil.execAsync(() -> restTemplate.getForObject("http://"+ AppConstant.JPOWER_SYSTEM+"/core/menu/getIdByCode?code="+ code,ResponseData.class));
        ResponseData<String> responseData = future.get();
        return Fc.isNull(responseData) ? StringPool.EMPTY : responseData.getData();
    }

    /**
     * 根据角色ID查询数据权限
     *
     * @author mr.g
     * @param roleIds 角色ID
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @SneakyThrows({ExecutionException.class, InterruptedException.class})
    public List<Map<String,Object>> queryDataScopeByRole(List<String> roleIds){
        Future<ResponseData<List<Map<String,Object>>>> future = ThreadUtil.execAsync(() -> restTemplate.getForObject("http://"+ AppConstant.JPOWER_SYSTEM+"/core/dataScope/getDataScopeByRole?roleIds="+ StringUtil.join(roleIds),ResponseData.class));
        ResponseData<List<Map<String,Object>>> responseData = future.get();
        return Fc.isNull(responseData) ? ListUtil.of() : responseData.getData();
    }


    /**
     * 根据角色ID查询数据权限
     *
     * @author mr.g
     * @param roleIds 角色ID
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public String queryDataScopeByRoleAndMenu(List<String> roleIds,String menuCode){
        List<Map<String,Object>> list = queryDataScopeByRole(roleIds);

        if (Fc.isNotEmpty(list) && Fc.isNotBlank(menuCode)){
            String menuId = queryMenuIdByCode(menuCode);
            if (Fc.isNotBlank(menuId)){
                List<Map<String,Object>> listScope = list.stream()
                        .filter(m->Fc.equalsValue(MapUtil.getStr(m,"menuId"),menuId))
                        .sorted(Comparator.comparingInt(m->MapUtil.getInt(m,"allRole")))
                        .filter(m->{
                            if (Fc.equalsValue(MapUtil.getInt(m,"allRole"), ConstantsEnum.YN01.N.getValue())){
                                return true;
                            }
                            return list.stream().noneMatch(lm-> Fc.equalsValue(MapUtil.getInt(lm,"allRole"), ConstantsEnum.YN01.N.getValue()) &&
                                    Fc.equalsValue(MapUtil.getStr(lm,"scopeClass"), MapUtil.getStr(m,"scopeClass")));

                        }).collect(Collectors.toList());


                return JSON.toJSONString(listScope);
            }
        }

        return StringPool.EMPTY;
    }
}
