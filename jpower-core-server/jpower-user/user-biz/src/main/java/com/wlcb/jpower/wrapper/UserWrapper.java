package com.wlcb.jpower.wrapper;

import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.mp.support.BaseWrapper;
import com.wlcb.jpower.vo.UserVo;

import java.util.Objects;

/**
 * @ClassName UserWrapper
 * @Description TODO User转换
 * @Author 郭丁志
 * @Date 2020-10-16 15:39
 * @Version 1.0
 */
public class UserWrapper extends BaseWrapper<TbCoreUser, UserVo> {

    public static UserWrapper builder(){
        return new UserWrapper();
    }

    @Override
    protected UserVo conver(TbCoreUser user) {
        UserVo userVo = Objects.requireNonNull(BeanUtil.copyProperties(user, UserVo.class));
        userVo.setOrgName(SystemCache.getOrgName(userVo.getOrgId()));
        userVo.setRoleName(Fc.join(SystemCache.getRoleNameByIds(Fc.toStrList(user.getRoleIds()))," | "));
        return userVo;
    }
}
