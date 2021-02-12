package com.wlcb.jpower.wrapper;

import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.vo.UserVo;
import lombok.Builder;

import java.util.Objects;

/**
 * @ClassName UserWrapper
 * @Description TODO User转换
 * @Author 郭丁志
 * @Date 2020-10-16 15:39
 * @Version 1.0
 */
@Builder
public class UserWrapper extends BaseDictWrapper<TbCoreUser, UserVo> {
    @Override
    protected UserVo conver(TbCoreUser user) {
        UserVo userVo = Objects.requireNonNull(BeanUtil.copy(user, UserVo.class));
        userVo.setOrgName(SystemCache.getOrgName(userVo.getOrgId()));
        userVo.setRoleName(Fc.join(SystemCache.getRoleNameByIds(Fc.toStrList(user.getRoleIds()))," | "));
        return userVo;
    }
}
