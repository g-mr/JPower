package top.jpower.jpower.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mr.g
 * @date 2023/6/29 10:57 PM
 */
@Getter
@AllArgsConstructor
public enum TaskTypeEnum {

    DELAY(1,"延时任务");

    private final Integer code;
    private final String name;

}
