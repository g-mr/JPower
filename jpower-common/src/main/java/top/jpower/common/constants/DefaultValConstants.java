package top.jpower.common.constants;

import top.jpower.common.enums.YN01Enum;

/**
 * 系统默认值常量
 *
 * @author mr.g
 */
public interface DefaultValConstants {

    /** 默认文件加密key **/
    String FILE_DES_KEY = "COREFILEENCRYPTKEY20200720";
    /** 用户默认密码。这里的优先级最低 **/
    String DEFAULT_USER_PASSWORD = "123456";
    /** 用户默认是否激活。这里的优先级最低 **/
    Integer DEFAULT_USER_ACTIVATION = YN01Enum.N.getValue();

}
