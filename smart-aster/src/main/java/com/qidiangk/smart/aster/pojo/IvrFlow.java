// IvrFlow.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.qidiangk.smart.aster.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import com.qidiangk.smart.common.validated.SpEL;

import java.io.Serializable;
import java.util.Map;


@Data
public class IvrFlow implements Serializable {
    /**
     * 用户意图
     */
    private UserIntent userIntent = new UserIntent();
    /**
     * 欢迎语
     */
    private Welcome welcome;
    /**
     * 数据查询
     */
    private GlobalServer globalServer;

    @Data
    public static class GlobalServer implements Serializable {
        @NotEmpty(message = "全局参数 不可为空")
        @Schema(description = "全局参数")
        private Map<String, String> gloabalParas;
        @Schema(description = "返回数据")
        private String result;
        @NotBlank(message = "全局请求地址 不可为空")
        @Schema(description = "全局请求地址")
        private String action;
    }

    @Data
    @Schema(description = "欢迎语")
    public static class Welcome implements Serializable {
        @Min(value = 1, message = "最小值为1")
        @Schema(description = "停留秒数")
        private Integer pause;
        @Schema(description = "请求地址")
        private String action;
        @Schema(description = "返回结果")
        @SpEL(message = "返回结果 不是标准的SPEL表达式")
        private String result;
        @Schema(description = "欢迎语")
        private String message;

    }
}