package top.jpower.jpower.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mr.g
 * @date 2023/6/29 10:57 PM
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum {

    FAIL(0,"执行失败"),
    EXECUTED(1,"待执行"),
    FINISH(10,"执行完成");

    private final Integer code;
    private final String name;

}
