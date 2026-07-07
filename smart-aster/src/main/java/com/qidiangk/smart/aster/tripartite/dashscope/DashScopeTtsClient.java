package com.qidiangk.smart.aster.tripartite.dashscope;

import cn.hutool.core.bean.copier.CopyOptions;
import com.qidiangk.smart.aster.tripartite.property.DashScopeProperty;
import com.qidiangk.smart.aster.tripartite.property.DashScopeTtsOption;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.asterisk.audio.TtsClient;
import top.jpower.core.asterisk.audio.TtsResult;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.util.utils.StringUtil;

import java.io.OutputStream;

/**
 * DashScope 实时 TTS 代理客户端
 * <br/>
 * 根据配置的模型名称自动选择底层实现：
 * <ul>
 *   <li>模型名包含 "cosyvoice" → {@link DashScopeCosyVoiceTtsClient}（CosyVoice 系列模型）</li>
 *   <li>其他（qwen3-tts 等）→ {@link DashScopeQwenTtsClient}（Qwen-TTS Realtime 系列模型）</li>
 * </ul>
 * <br/>
 * 配置合并逻辑与 {@link DashScopeAsrClient} 一致：页面传入的 ttsOption 中非 null、非空白的字段会覆盖配置文件的对应字段。
 *
 * @author mr.g
 */
@Slf4j
public class DashScopeTtsClient implements TtsClient {

    /**
     * CosyVoice模型标识
     */
    public static final String MODEL_COSYVOICE = "cosy";

    private final TtsClient ttsClient;

    /**
     * 合并配置：将 Spring Bean（配置文件值）与页面传入的 ttsOption（动态覆盖值）合并。
     * ttsOption 中非 null 的字段会覆盖配置文件的对应字段。
     */
    private static DashScopeProperty merged(DashScopeTtsOption ttsOption) {
        // 复制 Bean 到新对象，避免污染全局配置
        DashScopeProperty target = BeanUtil.copyProperties(
                SpringUtil.getBean(DashScopeProperty.class), DashScopeProperty.class);
        // 深拷贝嵌套 ttsOption，切断与全局 Bean 的引用，防止后续写操作污染全局配置
        target.setTtsOption(BeanUtil.copyProperties(
                SpringUtil.getBean(DashScopeProperty.class).getTtsOption(), DashScopeProperty.TtsOption.class));

        // 顶层字段覆盖（apiKey、websocketUrl），忽略 null、空字符串及纯空白字符串
        BeanUtil.copyProperties(ttsOption, target, CopyOptions.create()
                .ignoreNullValue()
                .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));
        // 嵌套 ttsOption 字段覆盖（model、voice、format、sampleRate 等），忽略 null、空字符串及纯空白字符串
        BeanUtil.copyProperties(ttsOption, target.getTtsOption(), CopyOptions.create()
                .ignoreNullValue()
                .setPropertiesFilter((field, value) -> !(value instanceof String) || StringUtil.isNotBlank((String) value)));

        // 检查音色并给默认值
        target.getTtsOption().setVoice(defaultVoice(target.getTtsOption()));
        return target;
    }

    public static String defaultVoice(DashScopeProperty.TtsOption ttsOption) {
        if (Fc.isBlank(ttsOption.getVoice())) {
            if (StringUtil.containsAnyIgnoreCase(ttsOption.getModel(), MODEL_COSYVOICE)) {
                return "longanyang";
            } else {
                return "Cherry";
            }
        }
        return ttsOption.getVoice();
    }

    public DashScopeTtsClient(DashScopeTtsOption ttsOption) {
        DashScopeProperty property = merged(ttsOption);
        if (StringUtil.containsAnyIgnoreCase(property.getTtsOption().getModel(), MODEL_COSYVOICE)) {
            ttsClient = new DashScopeCosyVoiceTtsClient(property);
        } else {
            ttsClient = new DashScopeQwenTtsClient(property);
        }
    }

    public DashScopeTtsClient() {
        DashScopeProperty property = SpringUtil.getBean(DashScopeProperty.class);
        property.getTtsOption().setVoice(defaultVoice(property.getTtsOption()));
        if (StringUtil.containsAnyIgnoreCase(property.getTtsOption().getModel(), MODEL_COSYVOICE)) {
            ttsClient = new DashScopeCosyVoiceTtsClient(property);
        } else {
            ttsClient = new DashScopeQwenTtsClient(property);
        }
    }

    @Override
    public TtsResult process(String say, OutputStream audioOutput) {
        return ttsClient.process(say, audioOutput);
    }

    @Override
    public void close() {
        ttsClient.close();
    }

    @Override
    public int getSampleRate() {
        return ttsClient.getSampleRate();
    }
}
