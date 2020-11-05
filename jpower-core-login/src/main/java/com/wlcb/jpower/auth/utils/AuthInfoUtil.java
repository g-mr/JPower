package com.wlcb.jpower.auth.utils;

import com.alibaba.fastjson.JSON;
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
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.datascope.DataScope;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ding
 * @date 2020-11-05 16:45
 */
public class AuthInfoUtil {

    private static RedisUtil redisUtil;

    private static final String REGEX = "\\$\\{(.*?)\\}";

    static {
        redisUtil = SpringUtil.getBean(RedisUtil.class);
    }

    /**
     * 缓存鉴权信息
     *
     * @author 郭丁志
     * @date 22:59 2020/11/5 0005
     */
    public static void cacheAuth(AuthInfo authInfo) {
        List<TbCoreDataScope> dataScopeRoleList = SystemCache.getDataScopeByRole(authInfo.getUser().getRoleIds());
        List<TbCoreDataScope> dataScopeList = SystemCache.getAllRoleDataScope();
        List<TbCoreFunction> menuList = SystemCache.getMenuListByRole(authInfo.getUser().getRoleIds());

        Map<String,DataScope> map = ChainMap.newMap();
        if (Fc.notNull(dataScopeList)){
            dataScopeList.forEach(dataScope -> {
                String url = getUrl(menuList,dataScope.getMenuId());
                if (Fc.isNotBlank(url)){
                    map.put(url,BeanUtil.copy(dataScope, DataScope.class));
                }
            });
        }

        if (Fc.notNull(dataScopeRoleList)){
            dataScopeRoleList.forEach(dataScope -> {
                String url = getUrl(menuList,dataScope.getMenuId());
                if (Fc.isNotBlank(url)){
                    map.put(url,BeanUtil.copy(dataScope, DataScope.class));
                }
            });
        }

        Map<String,String> redisMap = ChainMap.newMap();
        for (String key : map.keySet()) {
            DataScope dataScope = map.get(key);
            //本人可见
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN.getValue())){
                dataScope.setIds(Collections.singletonList(authInfo.getUser().getUserId()));
            }
            //本级可见
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN_ORG.getValue())){
                dataScope.setIds(Collections.singletonList(authInfo.getUser().getOrgId()));
            }
            //本级以及子级可见
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN_ORG_CHILD.getValue())){
                List<String> listOrgId = SystemCache.getChildIdOrgById(authInfo.getUser().getOrgId());
                listOrgId.add(authInfo.getUser().getOrgId());
                dataScope.setIds(listOrgId);
            }
            //自定义
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.CUSTOM.getValue())){
                Map<String,Object> userMap = BeanUtil.getFieldValueMap(authInfo.getUser());
                userMap.put("roleIds",StringUtil.collectionToDelimitedString(authInfo.getUser().getRoleIds(), StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE));
                dataScope.setScopeValue(StringUtil.format(dataScope.getScopeValue(),userMap));
            }

            redisMap.put(key, JSON.toJSONString(dataScope));
        }

        redisUtil.set(CacheNames.TOKEN_DATA_SCOPE_KEY+authInfo.getAccessToken(),redisMap , authInfo.getExpiresIn(), TimeUnit.SECONDS);

        List<Object> list = SystemCache.getUrlsByRoleIds(authInfo.getUser().getRoleIds());
        redisUtil.set(CacheNames.TOKEN_URL_KEY+authInfo.getAccessToken(),list , authInfo.getExpiresIn(), TimeUnit.SECONDS);

    }

    /**
     * 通过ID获取URL
     *
     * @author 郭丁志
     * @date 22:59 2020/11/5 0005
     * @param menuList 全部菜单
     * @param menuId 要获取得菜单ID
     * @return java.lang.String
     */
    private static String getUrl(List<TbCoreFunction> menuList,String menuId) {
        if (Fc.notNull(menuList)){
            menuList = menuList.stream().filter(menu -> Fc.equals(menu.getId(),menuId)).collect(Collectors.toList());
            if (menuList.size() > 0){
                return menuList.get(0).getUrl();
            }
        }
        return null;
    }
}
