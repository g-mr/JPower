package com.qidiangk.smart.aster.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.qidiangk.smart.aster.constants.MethodEnum;
import com.qidiangk.smart.aster.constants.ScriptEnum;
import com.qidiangk.smart.aster.constants.TypeEnum;
import com.qidiangk.smart.aster.handler.nodes.granter.*;
import com.qidiangk.smart.aster.tripartite.VoiceModelEnum;
import com.qidiangk.smart.common.validated.InEnum;
import com.qidiangk.smart.common.validated.SpEL;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class UserIntent implements Serializable {


    private List<Intent> intent;
    private Fallback fallback;

    @Data
    public static class Fallback implements Serializable {

        @NotEmpty(message = "意图未满足流程 不可为空")
        @Size(min = 1, message = "流程最少为一个")
        @Valid
        @Schema(description = "意图未满足流程")
        private List<? extends Node> behavior;
        @Schema(description = "意图流程结束以后的提示语")
        private String endMessage;
        @Schema(description = "不满足达标次数得提示语")
        private String unCountMessage;
        @Min(value = 0, message = "达标次数最小为0")
        @Schema(description = "达标次数")
        private int count = 0;
        @Schema(description = "意图未满足流程执行完是否结束")
        private boolean over = false;

    }

    @Data
    public static class Intent implements Serializable {
        private String code;
        private String description;
        private String quickMatchReg;
        private String condition;
        private List<? extends Node> nodeFlow;
        private String endMessage;
    }

    @Data
    @Schema(
            description = "节点",
            subTypes = {
                    Node.StartNode.class,
                    Node.AnswerNode.class,
                    Node.ConditionNode.class,
                    Node.ServiceNode.class,
                    Node.ReceivedNode.class,
                    Node.ScriptNode.class,
                    Node.TransferNode.class,
                    Node.MaxKBNode.class,
                    Node.GlobeValueNode.class,
                    Node.SayNode.class,
                    Node.SentimentNode.class,
                    Node.ChildNodes.class,
                    Node.IntentionNode.class,
                    Node.ExtractNode.class,
                    Node.HangupNode.class
            } // 列出所有子类
    )
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",  // 使用JSON中的type字段区分类型
            visible = true
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Node.StartNode.class, name = "start"),
            @JsonSubTypes.Type(value = Node.AnswerNode.class, name = AnswerGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.ConditionNode.class, name = ConditionGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.ServiceNode.class, name = ServiceGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.ReceivedNode.class, name = ReceivedGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.ScriptNode.class, name = ScriptGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.TransferNode.class, name = TransferGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.MaxKBNode.class, name = MaxKBGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.GlobeValueNode.class, name = GlobeValueGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.SayNode.class, name = SayGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.SentimentNode.class, name = SentimentGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.HangupNode.class, name = HangupGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.ChildNodes.class, name = ChildGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.IntentionNode.class, name = IntentionGranter.GRANT_TYPE),
            @JsonSubTypes.Type(value = Node.ExtractNode.class, name = ExtractGranter.GRANT_TYPE),
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class Node implements Serializable {

        @NotBlank(message = "名称 不可为空")
        @Schema(description = "名称")
        private String name;
        @NotBlank(message = "编码 不可为空")
        @Schema(description = "编码")
        private String code;
        @NotNull(message = "类型 不可为空")
        @InEnum(value = TypeEnum.class,message = "类型 的值必须为{value}")
        @Schema(description = "类型")
        private TypeEnum type;

        // 下面是前端需要的属性，不做任何校验
        private Double x;
        private Double y;
        private Integer width;
        private Integer height;
        private Boolean showNode;

        @Data
        @Schema(description = "开始节点")
        public static class StartNode extends Node {

            @NotNull(message = "ASR 不可为空")
            @Schema(description = "ASR")
            @InEnum(value = VoiceModelEnum.class,message = "类型 的值必须为{value}")
            private VoiceModelEnum asr;

            @NotNull(message = "TTS 不可为空")
            @Schema(description = "TTS")
            @InEnum(value = VoiceModelEnum.class,message = "类型 的值必须为{value}")
            private VoiceModelEnum tts;

            @Schema(description = "ASR配置")
            private JsonNode asrOption;
            @Schema(description = "TTS配置")
            private JsonNode ttsOption;




            @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
            @Schema(description = "下一流程CODE")
            private String nextNode;

            // 下面是前端需要的属性，不做任何校验
            private JsonNode edge;
        }

        @Data
        @Schema(description = "条件节点")
        public static class ConditionNode extends Node {

            @Valid
            @NotEmpty(message = "条件 不可为空")
            @Size(min = 2, message = "最少俩个 条件")
            @Schema(description = "条件")
            private List<Condition> conditions;

            @Data
            public static class Condition {
                @NotNull(message = "排序坐标 不可为空")
                @Schema(description = "排序坐标")
                private Integer index;
                @Schema(description = "返回结果，SPEL表达式")
                @SpEL(message = "返回结果 不是标准的SPEL表达式")
                private String result;
                @Schema(description = "条件表达式，SPEL表达式")
                @SpEL(message = "条件表达式 不是标准的SPEL表达式")
                private String condition;
                @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
                @Schema(description = "下一流程CODE")
                private String nextNode = "end";

                // 下面是前端需要的属性，不做任何校验
                private String id;
                private JsonNode ifs;
                private String type;
                private String concat;
                private Integer height;
                private JsonNode edge;

            }

        }

        @Data
        public static class ReceivedNode extends Node {

            @Schema(description = "播放文件是否保存")
            private Boolean isSave = false;

            @Schema(description = "收音语")
            @NotBlank(message = "收音语不可为空")
            @SpEL(message = "收音语 不是标准的SPEL表达式")
            private String jqrask;

            @Schema(description = "结束按键")
            @NotBlank(message = "结束按键不可为空")
            private String end = "#";

            @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
            @Schema(description = "下一流程CODE")
            private String nextNode;

            // 下面是前端需要的属性，不做任何校验
            private JsonNode edge;

        }

        @Data
        public static class AnswerNode extends Node {
            @Schema(description = "询问语 SPEL表达式")
            @SpEL(message = "询问语 不是标准的SPEL表达式")
            private String jqrask;
            @Schema(description = "播放文件是否保存")
            private Boolean isSave = false;
            @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
            @Schema(description = "下一流程CODE")
            private String nextNode;

            // 下面是前端需要的属性，不做任何校验
            private JsonNode edge;

        }

        @Data
        public static class SayNode extends Node {
            @Schema(description = "播放语 SPEL表达式")
            @NotBlank(message = "播放语 不可为空")
            @SpEL(message = "播放语 不是标准的SPEL表达式")
            private String play;

            @Schema(description = "是否允许按键打断")
            private Boolean interrupt = true;

            @Schema(description = "是否保存播放文件")
            private Boolean isSave = false;

            @Schema(description = "返回结果")
            @SpEL(message = "返回结果 不是标准的SPEL表达式")
            private String result;
            @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
            @Schema(description = "下一流程CODE")
            private String nextNode;

            // 下面是前端需要的属性，不做任何校验
            private JsonNode edge;
        }

        @Data
        public static class ServiceNode extends Node {
            @NotNull(message = "请求方式 不可为空")
            @Schema(description = "请求方式")
            @InEnum(MethodEnum.class)
            private String method;
            @Schema(description = "BODY")
            private JsonNode body;
            @Schema(description = "Header")
            private Map<String, String> headers;
            @NotBlank(message = "地址 不可为空")
            @Schema(description = "地址")
            private String url;
            @NotBlank(message = "键名 不可为空")
            @Schema(description = "接口返回结果所在键名")
            private String result;
            @Schema(description = "是否播放等待音乐")
            private Boolean playMusic = true;
            @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
            @Schema(description = "下一流程CODE")
            private String nextNode;

            // 下面是前端需要的属性，不做任何校验
            private JsonNode edge;
        }

        @Data
        public static class ScriptNode extends Node {

            @NotBlank(message = "脚本类型 不可为空")
            @Schema(description = "脚本类型")
            @InEnum(value = ScriptEnum.class, message = "脚本类型 的值必须为{value}")
            private String scriptType;
            @NotBlank(message = "脚本 不可为空")
            @Schema(description = "脚本")
            private String script;
            @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
            @Schema(description = "下一流程CODE")
            private String nextNode;

            @Schema(description = "是否播放等待音乐")
            private Boolean playMusic = false;

            // 下面是前端需要的属性，不做任何校验
            private JsonNode edge;

        }

        @Data
        public static class TransferNode extends Node {

            @NotBlank(message = "队列名称 不可为空")
            @Schema(description = "队列名称")
            private String queueName;
            @NotNull(message = "响铃超时时间 不可为空")
            @Schema(description = "响铃超时时长 单位：秒")
            private Integer ringTime = 10;

            @Schema(description = "开始转接话语")
            private String startAnswer;

            @Schema(description = "接通流程")
            @Valid
            private Through through = new Through();

            @Schema(description = "未接通流程")
            @Valid
            private UnThrough unThrough = new UnThrough();

            @Data
            public static class Through {
                @Schema(description = "返回结果")
                @SpEL(message = "返回结果 不是标准的SPEL表达式")
                private String result;
                @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
                @Schema(description = "下一流程CODE")
                private String nextNode;

                // 下面是前端需要的属性，不做任何校验
                private JsonNode edge;
            }

            @Data
            public static class UnThrough {
                @Schema(description = "返回结果")
                @SpEL(message = "返回结果 不是标准的SPEL表达式")
                private String result;
                @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
                @Schema(description = "下一流程CODE")
                private String nextNode;

                // 下面是前端需要的属性，不做任何校验
                private JsonNode edge;
            }

        }

        @Data
        public static class MaxKBNode extends Node {

            @Schema(description = "问题")
            @NotBlank(message = "问题 不可为空；可写变量")
            private String issue;
            @Schema(description = "是否播放等待音乐")
            private boolean waitMusic = true;
            @Schema(description = "是否重新聊")
            private boolean reChat = false;
            @Schema(description = "智能体ID")
            @NotBlank(message = "智能体ID不可为空")
            private String agent;
            @Schema(description = "失败结果")
            @NotBlank(message = "失败结果 不可为空")
            private String failResult;
            @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
            @Schema(description = "下一流程CODE")
            private String nextNode;

            // 下面是前端需要的属性，不做任何校验
            private JsonNode edge;

        }

        @Data
        public static class GlobeValueNode extends Node {
            @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
            @Schema(description = "下一流程CODE")
            private String nextNode;
            @Schema(description = "流程结束结果")
            @SpEL(message = "判断语句 不是标准的SPEL表达式")
            private String result;
            @Schema(description = "全局变量列表")
            @NotEmpty(message = "全局变量列表 不可为空")
            @Valid
            private List<Variable> variables;

            // 下面是前端需要的属性，不做任何校验
            private JsonNode edge;


            @Data
            @Builder
            @Jacksonized
            public static class Variable {
                @Schema(description = "全局变量名")
                @NotBlank(message = "全局变量名 不可为空")
                private String key;
                @NotBlank(message = "全局变量值 不可为空")
                @Schema(description = "全局变量的值")
                private String val;
            }
        }

        @Data
        public static class HangupNode extends Node{
            @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
            @Schema(description = "下一流程CODE")
            private String nextNode;
            @Schema(description = "挂机语")
            private String message;

            // 下面是前端需要的属性，不做任何校验
            private JsonNode edge;
        }

        @Data
        public static class SentimentNode extends Node{
            @NotBlank(message = "模型不可为空")
            @Schema(description = "模型")
            private String model;

            @NotBlank(message = "判断语句")
            @Schema(description = "判断语句 支持SPEL表达式")
            @SpEL(message = "判断语句 不是标准的SPEL表达式")
            private String message;

            @Schema(description = "情绪正面响应")
            @Valid
            private Front front;
            @Schema(description = "情绪负面响应")
            @Valid
            private Negative negative;
            @Schema(description = "情绪中性响应")
            @Valid
            private Neuter neuter;

            @Data
            public static class Front {
                @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
                @Schema(description = "下一流程CODE")
                private String nextNode;

                @Schema(description = "返回结果 支持SPEL表达式")
                @NotBlank(message = "返回结果 不可为空")
                @SpEL(message = "返回结果 不是标准的SPEL表达式")
                private String result;

                // 下面是前端需要的属性，不做任何校验
                private JsonNode edge;
            }

            @Data
            public static class Negative {
                @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
                @Schema(description = "下一流程CODE")
                private String nextNode;

                @Schema(description = "返回结果 支持SPEL表达式")
                @NotBlank(message = "返回结果 不可为空")
                @SpEL(message = "返回结果 不是标准的SPEL表达式")
                private String result;

                // 下面是前端需要的属性，不做任何校验
                private JsonNode edge;
            }

            @Data
            public static class Neuter {
                @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
                @Schema(description = "下一流程CODE")
                private String nextNode;

                @Schema(description = "返回结果 支持SPEL表达式")
                @NotBlank(message = "返回结果 不可为空")
                @SpEL(message = "返回结果 不是标准的SPEL表达式")
                private String result;

                // 下面是前端需要的属性，不做任何校验
                private JsonNode edge;
            }

        }

        @Data
        public static class ChildNodes extends Node {

            @Schema(description = "用户问题")
            @NotNull(message = "用户问题 不可为空")
            @SpEL(message = "用户问题 不是标准的SPEL表达式")
            private String issue;

            @Schema(description = "子流程ID")
            @NotNull(message = "子流程ID 不可为空")
            private Long intentId;

            @Schema(description = "子流程编码")
            @NotBlank(message = "子流程编码 不可为空")
            private String intentCode;

            @Schema(description = "子流程名称")
            @NotBlank(message = "子流程名称 不可为空")
            private String intentName;

            @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
            @Schema(description = "下一流程CODE")
            private String nextNode;

            // 下面是前端需要的属性，不做任何校验
            private JsonNode edge;
        }

        /**
         * 意图
         */
        @Data
        public static class IntentionNode extends Node {

            @Schema(description = "意图分类")
            @NotEmpty(message = "意图分类 不可为空")
            @Valid
            private List<Intention> intents;
            @Schema(description = "历史记录，一般情况放上一句客服的话语")
            private String history;
            @Schema(description = "用户问题")
            @NotBlank(message = "用户问题 不可为空；可写变量")
            @SpEL(message = "用户问题 不是标准的SPEL表达式")
            private String question;

            @Schema(description = "是否播放等待音乐")
            private Boolean playMusic = false;

            @Schema(description = "音乐提示语")
            private String musicPrompt;

            @Data
            @Builder
            @Jacksonized
            public static class Intention {

                @Schema(description = "意图分类编码")
                @NotBlank(message = "意图分类编码 不可为空")
                private String code;
                @Schema(description = "意图分类描述")
                @NotBlank(message = "意图分类描述 不可为空")
                private String description;
                @Schema(description = "快速匹配 正则表达式")
                private String quickMatchReg;

                @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
                @Schema(description = "下一流程CODE")
                private String nextNode;
                // 下面是前端需要的属性，不做任何校验
                private JsonNode edge;
                private Integer height;
                private Integer id;
                private Integer index;

            }
        }

        /**
         * 参数提取
         */
        @Data
        public static class ExtractNode extends Node {

            @Schema(description = "是否异步执行")
            private Boolean async = true;

            @Schema(description = "原始文本 SPEL表达式")
            @NotBlank(message = "原始文本 不可为空")
            @SpEL(message = "原始文本 不是标准的SPEL表达式")
            private String text;

            @Schema(description = "提取信息, KEY=提取参数名，value=参数说明")
            @NotEmpty(message = "提取信息 不可为空")
            private Map<String, String> keys;

            @NotBlank(message = "下一流程 不可为空;如果没用下一流程请填end")
            @Schema(description = "下一流程CODE")
            private String nextNode;

            // 下面是前端需要的属性，不做任何校验
            private JsonNode edge;

        }
    }

}

