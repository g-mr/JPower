package com.wlcb.jpower.auth.utils;

import com.wlcb.jpower.auth.AuthInfo;
import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.dbs.entity.function.TbCoreDataScope;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.datascope.DataScope;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author ding
 * @date 2020-11-05 16:45
 */
public class AuthInfoUtil {

    private static RedisUtil redisUtil;

    static {
        redisUtil = SpringUtil.getBean(RedisUtil.class);
    }

    public static void cacheAuth(AuthInfo authInfo) {


        List<Object> list = SystemCache.getUrlsByRoleIds(authInfo.getUser().getRoleIds());
        redisUtil.set(CacheNames.TOKEN_URL_KEY+authInfo.getAccessToken(),list , authInfo.getExpiresIn(), TimeUnit.SECONDS);

        List<TbCoreDataScope> dataScopeRoleList = SystemCache.getDataScopeByRole(authInfo.getUser().getRoleIds());
        List<TbCoreDataScope> dataScopeList = SystemCache.getAllRoleDataScope();
        List<TbCoreFunction> menuList = SystemCache.getMenuListByRole(authInfo.getUser().getRoleIds());

        Map<String,DataScope> map = ChainMap.newMap();
        dataScopeList.forEach(dataScope -> {
            String url = getUrl(menuList,dataScope.getMenuId());
            map.put(url,BeanUtil.copy(dataScope, DataScope.class));
        });


        dataScopeRoleList.forEach(dataScope -> {
            String url = getUrl(menuList,dataScope.getMenuId());
            map.put(url,BeanUtil.copy(dataScope, DataScope.class));
        });

        for (String key : map.keySet()) {
            DataScope dataScope = map.get(key);
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN.getValue())){
                dataScope.setIds(Collections.singletonList(authInfo.getUser().getUserId()));
            }



        }

    }

    private static String getUrl(List<TbCoreFunction> menuList,String menuId) {

        menuList = menuList.stream().filter(menu -> Fc.equals(menu.getId(),menuId)).collect(Collectors.toList());

        if (menuList.size() > 0){
            return menuList.get(0).getUrl();
        }
        return null;
    }
}
