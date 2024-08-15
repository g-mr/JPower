package top.jpower.core.exception.config;

import top.jpower.core.exception.model.UserDto;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.jpower.module.dbs.config.LoginUserContext;

/**
 * 默认的返回用户信息
 *
 * @author mr.g
 */
public class DefaultUserConfig implements UserConfig{

    @Override
    public UserDto queryUser() {
         return BeanUtil.copyProperties(LoginUserContext.get(), UserDto.class);
    }
}
