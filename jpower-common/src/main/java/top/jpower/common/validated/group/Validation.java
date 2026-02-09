package top.jpower.common.validated.group;

/**
 * 校验分组接口
 *
 * @author mr.g
 */
public interface Validation {
    /**
     * 创建校验组
     **/
    interface Create {}

    /**
     * 更新校验组
     **/
    interface Update {}

    /**
     * 查询校验组
     **/
    interface Query {}
}
