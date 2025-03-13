package top.jpower.jpower.module.config.user;

import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.user.model.UserDto;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.module.common.auth.RoleConstant;
import top.jpower.jpower.module.common.auth.UserInfo;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.dbs.config.LoginUserContext;

import java.util.Optional;

/**
 * 默认的返回用户信息
 *
 * @author mr.g
 */
public class DefaultUserConfig implements UserConfig {

    @Override
    public UserDto queryUser() {
        UserInfo userInfo = Optional.ofNullable(LoginUserContext.get()).orElse(new UserInfo());
        UserDto userDto = BeanUtil.copyProperties(userInfo, UserDto.class);
        userDto.setRoot(Fc.contains(userInfo.getRoleIds(), RoleConstant.ROOT_ID));
        userDto.setTenantCode(ShieldUtil.getTenantCode());
        return userDto;
    }

}
