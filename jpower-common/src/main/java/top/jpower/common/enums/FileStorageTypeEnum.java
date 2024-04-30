package top.jpower.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件存储位置
 *
 * @author mr.g
 */
@AllArgsConstructor
@Getter
public enum FileStorageTypeEnum {

    /**
     * 服务器
     **/
    SERVER("SERVER", "服务器"),
    /**
     * fastdfs
     **/
    FASTDFS("FASTDFS", "fastdfs"),
    /**
     * 数据库
     **/
    DATABASE("DATABASE", "数据库");

    private final String value;
    private final String name;

    public static String getName(String value) {
        FileStorageTypeEnum[] businessModeEnums = values();
        for (FileStorageTypeEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

}
