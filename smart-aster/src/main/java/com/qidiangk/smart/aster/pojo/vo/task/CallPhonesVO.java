package com.qidiangk.smart.aster.pojo.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 */
@Data
public class CallPhonesVO implements Serializable {

    /**
     * 被叫手机号码。
     */
    @Schema(description = "被叫手机号码")
    @NotEmpty(message = "被叫手机号码不能为空")
    private List<String> phones;

    /**
     * 外呼时传递的初始参数，JSON 格式，用于机器人场景。
     */
    @Schema(description = "外呼时传递的初始参数")
    private Map<String, Object> params;

}
