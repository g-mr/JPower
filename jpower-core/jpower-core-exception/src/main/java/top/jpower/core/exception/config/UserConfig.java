package top.jpower.core.exception.config;

import top.jpower.core.exception.model.UserDto;

/**
 * 错误日志和操作日志需要记录用户信息则实现这个接口返回用户信息即可
 *
 * @author mr.g
 */
public interface UserConfig {

    UserDto queryUser();
}
