package top.jpower.core.ai.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.lang.Nullable;
import top.jpower.core.ai.enums.ChatModelType;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ChatModel 注册表
 * <p>
 * 管理应用中所有可用的 ChatModel 实例，支持按模型类型获取。
 * 自动识别各 AI Starter 注册的 ChatModel Bean，并根据其类名推断模型类型。
 *
 * @author mr.g
 */
@Slf4j
public class ChatModelRegistry {

    private final Map<ChatModelType, ChatModel> models = new ConcurrentHashMap<>();
    private final Map<String, ChatModel> modelsByName = new ConcurrentHashMap<>();
    private volatile ChatModel primaryModel;
    private volatile ChatModelType primaryType;

    /**
     * 注册一个 ChatModel
     *
     * @param type  模型类型
     * @param model ChatModel 实例
     */
    public void register(ChatModelType type, ChatModel model) {
        models.put(type, model);
        modelsByName.put(type.getKey(), model);
        log.info("注册 ChatModel: {} -> {}", type.getKey(), model.getClass().getSimpleName());
    }

    /**
     * 设置主模型
     */
    public void setPrimary(ChatModel model) {
        this.primaryModel = model;
        this.primaryType = inferType(model);
    }

    /**
     * 获取主模型（默认模型）
     */
    public ChatModel getPrimary() {
        return primaryModel;
    }

    /**
     * 获取主模型的类型
     *
     * @return 主模型类型，如果未设置主模型则返回 null
     */
    @Nullable
    public ChatModelType getPrimaryType() {
        return primaryType;
    }

    /**
     * 根据模型类型获取 ChatModel
     *
     * @param type 模型类型
     * @return ChatModel，如果不存在返回 null
     */
    @Nullable
    public ChatModel getModel(ChatModelType type) {
        return models.get(type);
    }

    /**
     * 根据模型 key 获取 ChatModel
     *
     * @param key 模型标识 key
     * @return ChatModel，如果不存在返回 null
     */
    @Nullable
    public ChatModel getModel(String key) {
        return modelsByName.get(key);
    }

    /**
     * 获取所有已注册的模型类型
     */
    public Collection<ChatModelType> getRegisteredTypes() {
        return Collections.unmodifiableCollection(models.keySet());
    }

    /**
     * 获取已注册模型数量
     */
    public int size() {
        return models.size();
    }

    /**
     * 判断是否包含指定类型的模型
     */
    public boolean contains(ChatModelType type) {
        return models.containsKey(type);
    }

    /**
     * 根据 ChatModel 实现类名推断模型类型
     *
     * @param model ChatModel 实例
     * @return 推断出的模型类型，无法推断时返回 null
     */
    @Nullable
    public static ChatModelType inferType(ChatModel model) {
        String className = model.getClass().getName().toLowerCase();

        if (className.contains("deepseek")) {
            return ChatModelType.DEEPSEEK;
        } else if (className.contains("dashscope") || className.contains("alibaba") || className.contains("qwen")) {
            return ChatModelType.DASHSCOPE;
        } else if (className.contains("azure")) {
            return ChatModelType.AZURE_OPENAI;
        } else if (className.contains("openai")) {
            return ChatModelType.OPENAI;
        } else if (className.contains("ollama")) {
            return ChatModelType.OLLAMA;
        } else if (className.contains("zhipu")) {
            return ChatModelType.ZHIPU;
        }

        return null;
    }
}
