package com.qidiangk.smart.aster.tripartite.dashscope;

import cn.hutool.core.bean.copier.CopyOptions;
import com.qidiangk.smart.aster.tripartite.property.DashScopeAsrOption;
import com.qidiangk.smart.aster.tripartite.property.DashScopeProperty;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.AsrResult;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.util.utils.StringUtil;

import java.io.PipedInputStream;

/**
 * DashScope 实时 ASR 代理客户端
 *
 * @author mr.g
 */
@Slf4j
public class DashScopeAsrClient implements AsrClient {

    /**
     * FUN模型标识
     */
    private static final String MODEL_FUN = "fun";
    /**
     * Paraformer模型标识
     */
    private static final String MODEL_PARAFORMER = "paraformer";

    private final AsrClient asrClient;

    /**
     * 合并配置：将 Spring Bean（配置文件值）与页面传入的 asrOption（动态覆盖值）合并。
     * asrOption 中非 null 的字段会覆盖配置文件的对应字段。
     */
    private static DashScopeProperty merged(DashScopeAsrOption asrOption) {
        // 复制 Bean 到新对象，避免污染全局配置
        DashScopeProperty target = BeanUtil.copyProperties(
                SpringUtil.getBean(DashScopeProperty.class), DashScopeProperty.class);
        // 深拷贝嵌套 asrOption，切断与全局 Bean 的引用，防止后续写操作污染全局配置
        target.setAsrOption(BeanUtil.copyProperties(
                SpringUtil.getBean(DashScopeProperty.class).getAsrOption(), DashScopeProperty.AsrOption.class));

        // 顶层字段覆盖（apiKey、websocketUrl），忽略 null、空字符串及纯空白字符串
        BeanUtil.copyProperties(asrOption, target, CopyOptions.create()
                .ignoreNullValue()
                .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));
        // 嵌套 asrOption 字段覆盖（model、language、VAD 等），忽略 null、空字符串及纯空白字符串
        BeanUtil.copyProperties(asrOption, target.getAsrOption(), CopyOptions.create()
                .ignoreNullValue()
                .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));
        return target;
    }

    public DashScopeAsrClient(DashScopeAsrOption asrOption) {
        DashScopeProperty property = merged(asrOption);
        if (StringUtil.containsAnyIgnoreCase(property.getAsrOption().getModel(), MODEL_FUN, MODEL_PARAFORMER)) {
            asrClient = new DashScopeFunAsrClient(property);
        } else {
            asrClient = new DashScopeQwenAsrClient(property);
        }
    }

    public DashScopeAsrClient() {
        DashScopeProperty property = SpringUtil.getBean(DashScopeProperty.class);
        if (StringUtil.containsAnyIgnoreCase(property.getAsrOption().getModel(), MODEL_FUN, MODEL_PARAFORMER)) {
            asrClient = new DashScopeFunAsrClient(property);
        } else {
            asrClient = new DashScopeQwenAsrClient(property);
        }
    }

    @Override
    public AsrResult process(PipedInputStream pipedInput) throws InterruptedException {
        return asrClient.process(pipedInput);
    }

    @Override
    public void close() {
        asrClient.close();
    }

}
