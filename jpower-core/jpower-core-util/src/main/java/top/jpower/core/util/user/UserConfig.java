package top.jpower.core.util.user;


import top.jpower.core.util.user.model.UserDto;

/**
 * 错误日志和操作日志还有数据库记录需要记录用户信息则实现这个接口返回用户信息即可
 *
 * @author mr.g
 */
public interface UserConfig {

    UserDto queryUser();
}
