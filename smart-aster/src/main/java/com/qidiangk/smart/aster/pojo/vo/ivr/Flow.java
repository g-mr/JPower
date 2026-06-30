package com.qidiangk.smart.aster.pojo.vo.ivr;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.qidiangk.smart.aster.pojo.IvrFlow;

import java.io.Serializable;
import java.util.List;

@Data
public class Flow  implements Serializable {
    /**
     * 欢迎语
     */
    @Schema(description = "欢迎语设置", contentSchema = IvrFlow.Welcome.class)
    private IvrFlow.Welcome welcome;

    /**
     * 数据查询
     */
    @Valid
    @Schema(description = "全局查询设置", implementation = IvrFlow.GlobalServer.class)
    private IvrFlow.GlobalServer globalServer;

    /**
     * 用户意图
     */
    @Valid
    @Schema(description = "用户意图")
    @NotNull(message = "用户意图 不可为空")
    private UserIntent userIntent;

    @Data
    public static class UserIntent implements Serializable {

        @Schema(description = "意图ID")
        @NotEmpty(message = "意图ID 不可为空")
        private List<Long> intent;

        @Schema(description = "意图未匹配流程")
        @NotNull(message = "意图未匹配流程 不可为空")
        @Valid
        private com.qidiangk.smart.aster.pojo.UserIntent.Fallback fallback;
    }

}
