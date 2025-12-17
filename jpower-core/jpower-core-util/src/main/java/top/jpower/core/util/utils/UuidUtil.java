package top.jpower.core.util.utils;


import java.util.UUID;

/**
 * UUID 工具
 *
 * @author mr.g
 **/
public class UuidUtil extends cn.hutool.core.util.IdUtil {

    /**
     * 获取UUID
     *
     * @author mr.g
     * @return java.lang.String
     **/
    public static String getUUID() {
        return fastUUID();
    }

    /**
     * 生成10位数字型UUId
     *
     * @author mr.g
     * @return java.lang.String
     **/
    public static String create10UUidNum() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        //有可能是负数
        if(hashCodeV < 0) {
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return String.format("%010d", hashCodeV);
    }
}
