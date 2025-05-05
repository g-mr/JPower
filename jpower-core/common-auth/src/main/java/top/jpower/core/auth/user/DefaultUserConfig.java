package top.jpower.core.auth.user;

import top.jpower.core.auth.utils.constant.RoleConstant;
import top.jpower.core.auth.dto.UserInfo;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.auth.utils.LoginUserContext;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.user.model.UserDto;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;

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
