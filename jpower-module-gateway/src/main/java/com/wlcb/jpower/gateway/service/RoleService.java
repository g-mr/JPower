package com.wlcb.jpower.gateway.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.MapUtil;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

}
