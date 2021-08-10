package com.wlcb.jpower.utils;

import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.cache.UserCache;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.dbs.entity.function.TbCoreDataScope;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dto.AuthInfo;
import com.wlcb.jpower.feign.UserClient;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.datascope.DataScope;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author mr.g
 * @date 2020-11-05 16:45
 */
public class AuthUtil {

    private static RedisUtil redisUtil;
    private static UserClient userClient;

    static {
        redisUtil = SpringUtil.getBean(RedisUtil.class);
        userClient = SpringUtil.getBean(UserClient.class);
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
                    map.put(url,BeanUtil.copyProperties(dataScope, DataScope.class));
                }
            });
        }

        if (Fc.notNull(dataScopeRoleList)){
            dataScopeRoleList.forEach(dataScope -> {
                String url = getUrl(menuList,dataScope.getMenuId());
                if (Fc.isNotBlank(url)){
                    map.put(url,BeanUtil.copyProperties(dataScope, DataScope.class));
                }
            });
        }

        Map<String,String> redisMap = ChainMap.newMap();
        for (String key : map.keySet()) {
            DataScope dataScope = map.get(key);
            //本人可见
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN.getValue())){
                dataScope.setIds(Collections.singletonList  (authInfo.getUser().getUserId()));
            }
            //本级可见
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN_ORG.getValue())){
                dataScope.setIds(Collections.singletonList(authInfo.getUser().getOrgId()));
            }
            //本级以及子级可见
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN_ORG_CHILD.getValue())){
                List<String> listOrgId = SystemCache.getChildIdOrgById(authInfo.getUser().getOrgId());
                listOrgId = Fc.isNull(listOrgId)? new ArrayList<>() : listOrgId;
                if (Fc.isNotBlank(authInfo.getUser().getOrgId())){
                    listOrgId.add(authInfo.getUser().getOrgId());
                }
                dataScope.setIds(listOrgId);
            }
            //自定义
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.CUSTOM.getValue())){
                Map<String,Object> userMap = BeanUtil.getFieldValueMap(authInfo.getUser());
                userMap.put("roleIds", StringUtils.collectionToDelimitedString(authInfo.getUser().getRoleIds(), StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE));
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

    public static UserInfo toUserInfo(TbCoreUser result) {
        UserInfo userInfo = null;
        if(result != null){

            if (Fc.equals(result.getActivationStatus(), ConstantsEnum.YN01.N.getValue())){
                throw new BusinessException(TokenUtil.USER_NOT_ACTIVATION);
            }

            List<String> list  = UserCache.getRoleIds(result.getId());
            userInfo = new UserInfo();
            userInfo.setUserId(result.getId());
            userInfo.setIsSysUser(UserInfo.TBALE_USER_TYPE_CORE);
            userInfo.setAvatar(result.getAvatar());
            userInfo.setOrgId(result.getOrgId());
            userInfo.setOrgName(SystemCache.getOrgName(result.getOrgId()));
            userInfo.setUserType(result.getUserType());
            userInfo.setTelephone(result.getTelephone());
            userInfo.setLoginId(result.getLoginId());
            userInfo.setUserName(result.getUserName());
            userInfo.setNickName(result.getNickName());
//            userInfo.setOtherCode(result.getOtherCode());
            userInfo.setTenantCode(result.getTenantCode());
            userInfo.setRoleIds(list);
            userInfo.setPostCode(result.getPostCode());
            userInfo.setEmail(result.getEmail());
            userInfo.setAddress(result.getAddress());
            userInfo.setBirthday(result.getBirthday());
            userInfo.setIdType(result.getIdType());
            userInfo.setIdNo(result.getIdNo());
            userInfo.setLastLoginTime(result.getLastLoginTime());
            userInfo.setLoginCount(result.getLoginCount());
            // TODO: 2020-07-28 登录成功要刷新用户登录数据
            result.setLastLoginTime(new Date());
            result.setLoginCount((result.getLoginCount()==null?0:result.getLoginCount())+1);
            userClient.updateUserLoginInfo(result);
            CacheUtil.clear(CacheNames.USER_REDIS_CACHE);
        }
        return userInfo;
    }
}
