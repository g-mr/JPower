package com.wlcb.jpower.utils;

import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.cache.UserCache;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.dbs.entity.function.TbCoreDataScope;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dto.AuthInfo;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.datascope.DataScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author mr.g
 * @date 2020-11-05 16:45
 */
public class AuthUtil {

    private static RedisUtil redisUtil;

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
        List<TbCoreFunction> menuList = SystemCache.getMenuListByRole(authInfo.getUser().getRoleIds());

        Map<String,List<DataScope>> map = ChainMap.newMap();
        if (Fc.isNotEmpty(dataScopeRoleList)){
            dataScopeRoleList.forEach(dataScope -> {
                String code = getCode(menuList,dataScope.getMenuId());
                if (Fc.isNotBlank(code)){

                    boolean is = true;
                    //角色配置的数据权限比所有角色可执行的权限优先级要高，所以判断有自己的权限的时候就不要全角色执行的权限了
                    if (Fc.equalsValue(dataScope.getAllRole(), ConstantsEnum.YN01.Y.getValue())){
                        is = dataScopeRoleList.stream().noneMatch(scope-> Fc.equalsValue(scope.getAllRole(), ConstantsEnum.YN01.N.getValue()) && Fc.equalsValue(dataScope.getScopeClass(), scope.getScopeClass()));
                    }

                    if (is){
                        List<DataScope> dataScopeList = map.get(code);
                        if (Fc.isEmpty(dataScopeList)){
                            dataScopeList = new ArrayList<>();
                        }
                        dataScopeList.add(BeanUtil.copyProperties(dataScope, DataScope.class));

                        map.put(code,dataScopeList);
                    }

                }
            });
        }

        redisUtil.set(CacheNames.TOKEN_DATA_SCOPE_KEY+authInfo.getAccessToken(), map , authInfo.getExpiresIn(), TimeUnit.SECONDS);

        List<Object> list = SystemCache.getUrlsByRoleIds(authInfo.getUser().getRoleIds());
        redisUtil.set(CacheNames.TOKEN_URL_KEY+authInfo.getAccessToken(), list , authInfo.getExpiresIn(), TimeUnit.SECONDS);

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
    private static String getCode(List<TbCoreFunction> menuList,String menuId) {
        if (Fc.isNotEmpty(menuList)){
            return menuList.stream().filter(menu -> Fc.equals(menu.getId(),menuId)).map(TbCoreFunction::getCode).findFirst().orElse(null);
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
            userInfo.setOtherCode(result.getOtherCode());
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
            userInfo.setChildOrgId(SystemCache.getChildIdOrgById(result.getOrgId()));
            CacheUtil.clear(CacheNames.USER_REDIS_CACHE);
        }
        return userInfo;
    }
}
