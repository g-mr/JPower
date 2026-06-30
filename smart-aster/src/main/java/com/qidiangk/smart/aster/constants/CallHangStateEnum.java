package com.qidiangk.smart.aster.constants;

import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局呼叫类型枚举
 */
@AllArgsConstructor
@Getter
public enum CallHangStateEnum {

    NORMALLY(1000, "正常挂断"),
    EMPTY(1001, "空号"),
    UN_ANSWER(1002, "拒接/关机/未接听"),
    BUSY(1017, "忙"),
    NO_ANSWER(1018, "未应答"),
    REJECTED(1021, "拒接"),
    AGENT_FULL(9998, "坐席已满"),
    UNUSUAL(9999, "异常挂断"),
    NOT_EXIST(9990, "线路不存在"),
    NULL(-1000, "空标识");

    /**
     * 类型
     */
    @EnumValue
    @JsonValue
    private final Integer value;
    /**
     * 类型名
     */
    private final String name;

    public static CallHangStateEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getValue().equals(value), CallHangStateEnum.values());
    }

    /**
     * 通过asterisk cause获取枚举
     * <br/>
     * 16 - AST_CAUSE_NORMAL_CLEARING：正常挂断
     * 31 - Normal, unspecified - 正常，未指定
     * 17 - AST_CAUSE_USER_BUSY：用户忙/占线
     * 18 - AST_CAUSE_NO_USER_RESPONSE：无应答
     * 19 - AST_CAUSE_NO_ANSWER：无应答 (另一种)
     * 3  - 无路由/空号
     * 20 - AST_CAUSE_SUBSCRIBER_ABSENT：用户缺席（可能为空号）
     * 21 - AST_CAUSE_CALL_REJECTED：呼叫被拒绝
     * 27 - Destination out of order - 目的地故障
     * 28 - AST_CAUSE_INVALID_NUMBER_FORMAT：无效号码格式
     * 29 - Facility rejected - 设施被拒绝
     * 1: 是我通过asterisk配置后观察拿到的，拒绝原因写的是Unallocated (unassigned) number；不知道有没有其他含义，暂且归为空号处理
     *
     * @param cause cause
     * @return 枚举
     */
    public static CallHangStateEnum getByAsteriskCause(Integer cause) {
        return switch (cause) {
            case 16, 31 -> CallHangStateEnum.NORMALLY;
            case 17, 18, 19, 21 -> CallHangStateEnum.UN_ANSWER;
            case 1, 3, 20, 28 -> CallHangStateEnum.EMPTY;
            default -> CallHangStateEnum.UNUSUAL;
        };
    }

    /**
     * 通过asterisk cause获取枚举
     * @param cause
     * @return
     */
    public static CallHangStateEnum getBySourceAsteriskCause(Integer cause) {
        return switch (cause) {
            case 16, 31 -> CallHangStateEnum.NORMALLY;
            case 1, 3, 20, 28 -> CallHangStateEnum.EMPTY;
            case 17 -> CallHangStateEnum.BUSY;
            case 18, 19 -> CallHangStateEnum.NO_ANSWER;
            case 21 -> CallHangStateEnum.REJECTED;
            case 0 -> CallHangStateEnum.UN_ANSWER;
            default -> CallHangStateEnum.UNUSUAL;
        };
    }

}
